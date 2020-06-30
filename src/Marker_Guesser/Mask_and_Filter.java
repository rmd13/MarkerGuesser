package Marker_Guesser;

import ij.IJ;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import ij.ImagePlus;
import ij.CompositeImage;
import ij.io.Opener;
import ij.io.OpenDialog;
import ij.ImageJ;
import ij.*;
import ij.Macro;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;

import ij.ImageStack;
import ij.Prefs;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

import java.awt.image.ColorModel;



public class Mask_and_Filter{
	private String m_dir_path;//the path for the project folder
	private String type; //scene or model
	private int dilate_iter = 20;
	private ImagePlus targ_img;
	private String[] results_dir = new String[10]; //results directory is an array of strings - each string in the array is the directory for each channel results file - max 10 channels
	
	public Mask_and_Filter(ImagePlus atarget, int a_dilate_iter) {
		targ_img = atarget;
		dilate_iter = a_dilate_iter;
	}
	
	public ImagePlus run(String ptype) {//the functino run returns the masked imagePlus//set type is scene or model
		type = ptype;
		
		ImagePlus img = createMask();//working
		
		
		
		ImagePlus targ_img = mask(img);
		
		targ_img = filter(targ_img, img);
		
		return targ_img;
	}
	
	private ImagePlus filter(ImagePlus targ_img, ImagePlus img) {
		ImagePlus[] channels = ChannelSplitter.split(targ_img);
		
		//bandpass filter, autothresholding, and 3d obj counting for 2nd and 3rd channel of target image
		IJ.run("Set 3D Measurements", "nb_of_obj._voxels centroid dots_size=10 font_size=15 store_results_within_a_table_named_after_the_image_(macro_friendly) redirect_to=none");
		//pass in the correct number of channels
		for(int i = 1; i < 3; i ++) {
			//IJ.run(channels[i], "Bandpass Filter...", "filter_large=12 filter_small=2 suppress=None tolerance=5 process");//this might not be necessary
			Prefs.blackBackground = true;
			
			//IJ.run(channels[i], "Convert to Mask", "method=Triangle background=Dark calculate black");
			
			IJ.run(channels[i], "3D object counter...", "threshold=2 slice=35 min.=10 max.=200000 exclude_objects_on_edges statistics");
			String chan = "";
			if(i==1) {
				chan = "C2";
				results_dir[0] = m_dir_path+chan+"_"+type+".csv";
				IJ.saveAs("Results", results_dir[0]);
			}else if(i==2) {
				chan = "C3";
				results_dir[1] = m_dir_path+chan+"_"+type+".csv";
				IJ.saveAs("Results", results_dir[1]);
			}
			
		}

		RGBStackMerge merger = new RGBStackMerge();	//merges the channels back together
		targ_img = merger.mergeHyperstacks(channels, true);
		targ_img.show();
		
		for(int i = 0; i < 3; i ++) { //closes the individual channel imgs
			channels[i].changes = false;
			channels[i].close();
		}
		
		img.changes = false;//says no changes have been made so it can close quietly
		img.close();

		return targ_img;
	}
	
	private ImagePlus createMask() {
		//TRACE FILE
		//img is the trace file
		ImagePlus img = open("Open trace file", false); //this returns an ImagePlus
		IJ.run(img, "8-bit", "");
		Dilator dilator1 = new Dilator();
		for(int d = 0; d < dilate_iter; d ++) {
			img = dilator1.dilate(img, 20, false);//dilates the trace image making it thicker
		}
		img.show();
		IJ.run(img, "8-bit", "");
		return img;
	}
	
	private ImagePlus mask(ImagePlus img) {
		//TARGET FILE
		//ImagePlus targ_img = open("Open target file", false);//you could use CompositeImage in the future
		IJ.run(targ_img, "8-bit", "");
		targ_img.show();
		
		IJ.error("applying mask and filtering - may take a while");
		
		Mask mask1 = new Mask(img, targ_img);//applies the mask to target image - leaves first channel unchanged
		mask1.mask();
		return targ_img;
	}
	
	
	private ImagePlus open(String text, boolean composite) {
		IJ.error(text);
		OpenDialog od = new OpenDialog(text, "");	
		m_dir_path = od.getDirectory();
		String path = m_dir_path + od.getFileName();
		
		if (od.getFileName() == null) {	
			return null;
		}
		
		ImagePlus img = IJ.openImage(path); //this returns an ImagePlus
		if(composite==true) {
			CompositeImage c_img = new CompositeImage(img);
			return c_img;
		}else {
			return img;
		}
	}
	
	public String getDirPath() {
		return m_dir_path;
	}
	
	public String[] getResultsDir() {
		return results_dir;
	}
}



