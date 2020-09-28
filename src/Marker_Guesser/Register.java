//this class places markers in the rest of the images other than the first

package Marker_Guesser;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.Macro_Runner;
import ij.plugin.PlugIn;

public class Register implements PlugIn {
	public String dir_path;
	public String[] marker_names;
	public int numImages;
	public int dilate_iter;

	public void run(String arg) {
		IJ.error("This part has not been finished yet");
		String macro = 	
				"out = exec(\"cmd\", \"/c\", \"dir\", \"c:\\\\program files\");"
				+ "showMessage(getString(\"enter\", \"d\"));";
		Macro_Runner mr1 = new Macro_Runner();
		mr1.runMacro(macro, "");
	}
}
