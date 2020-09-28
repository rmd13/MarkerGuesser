package Marker_Guesser;


import ij.plugin.Duplicator;
import ij.plugin.Macro_Runner;
import ij.plugin.PlugIn;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.Macro;
import ij.Prefs;
import ij.gui.*;





public class MarkerGuesser implements PlugIn {
	public String dir_path;
	public String[] marker_names;
	public int numImages;
	public int dilate_iter;
	public void run(String arg) {
		Macro_Runner m1 = new Macro_Runner();
		m1.runMacro("ojShowImage(1);run(\"Duplicate...\", \"duplicate\");", "");
		ImagePlus imageStack1 = WindowManager.getCurrentImage(); //you might want to change the imageStacks to an array so you don't only have the ability to use 2


		
		
		numImages = Options.getNImages();
		marker_names = Options.getMarkersforChannels(imageStack1);

		
		
		
		dilate_iter = Options.getDilateIter();
		Mask_and_Filter filter_model = new Mask_and_Filter(imageStack1, dilate_iter);
		
		imageStack1 = filter_model.run("model", marker_names);
		//future - Add warning if the directory path is different for second imageStack
		dir_path = filter_model.getDirPath();	
		
		
		
		//temporary
		double[][] transform = {
				{1,0,0,0},
				{0,1,0,0},
				{0,0,1,0},
				{0,0,0,1}
		};

		
		
		String results_path;
		ObjJ_Markers om_model;
		int results_index = 0;
		
		
		
		for(int i = 0; i < marker_names.length; i ++) {	
			if(!marker_names[i].contentEquals("cellFill")) {	
				results_path = filter_model.getResultsDir()[results_index];
				om_model = new ObjJ_Markers(transform, results_path, marker_names[i], i+1);
				om_model.run();
				results_index++;
			}
		}
		
		new Macro_Runner().runMacro("ojShowImage(1);\n" + 
				"for(i = ojFirstObject(1); i <= ojLastObject(1); i ++){\n" + 
				"	ojQualify(i, false);\n" + 
				"}", "");
		
		
	}
}
