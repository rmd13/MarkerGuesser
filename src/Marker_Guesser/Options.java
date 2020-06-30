package Marker_Guesser;

import ij.plugin.Duplicator;
import ij.plugin.Macro_Runner;
import ij.plugin.PlugIn;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.Macro;
import ij.gui.*;

public class Options {
	public static int getNImages() {	
		String macro = 	"num = ojNImages();\n	print(num);\n	text = \"\"+num;\n	return text;";
		Macro_Runner mr1 = new Macro_Runner();
		String numImages = mr1.runMacro(macro, "");
		int nImages = Integer.parseInt(numImages);
		return nImages;
	}
	
	public static int getDilateIter() {
		String macro = "Dialog.create(\"Dilate Option\");\r\n" + 
				"radius = Dialog.getNumber();\r\n" + 
				"Dialog.addNumber(\"Enter number of pixels to dilate the path as radius\", 15);\r\n" + 
				"Dialog.show;\r\n" + 
				"string_out = \"\"+radius;\r\n" + 
				"return string_out;";
		Macro_Runner mr1 = new Macro_Runner();
		String dilateIter_string = mr1.runMacro(macro, "");
		int dilateIter = Integer.parseInt(dilateIter_string);
		return dilateIter;
	}
	
	public static String[] getMarkersforChannels(ImagePlus img) {
		int numChannels = img.getNChannels();
		String[] marker_names = new String[numChannels];
		
		for(int i = 1; i < numChannels; i ++) {
			String macro = 	"names = ojGetItemNames();\n	"
					+ "indv_names = split(names, \" \");\n"
					+ "Dialog.create(\"Channel to marker assignment\");\r\n" + 
					"  Dialog.addRadioButtonGroup(\"Choose a marker for channel \"+"+i+", indv_names, 3, 1, indv_names[0]);\r\n" + 
					"  Dialog.show;\r\n" + 
					"  return Dialog.getRadioButton;";
			Macro_Runner mr1 = new Macro_Runner();
			marker_names[i] = mr1.runMacro(macro, "");
		}
		return marker_names;
	}
}
