package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import utils.PythonScript;
import utils.Rscript;



public class TestPython {
	public static void main(String[] args) throws IOException, InterruptedException {
		String loopsFile = "/home/plop/Bureau/SIPpaper/SIPTest/loops_matrix.tab";
		String input ="/home/plop/Bureau/SIPpaper/SIPTest/";
		String output =  "/home/plop/Bureau/SIPpaper/SIPTest/test";
		String output2 =  "/home/plop/Bureau/SIPpaper/SIPTest/testZ";
		String script = "/home/plop/Bureau/bullseye.py";
		int imageSize =2000;
		int step = 0;
		int metaSize = 21;
		int resolution = 0;
		int minRes = 10000;
		int ratio = 2;
		boolean resMax = true;
		String type = "simple";
		PythonScript python = new PythonScript(script, "Blues",false, loopsFile, output);  
		python.runRscript();
		//PythonScript python2 = new PythonScript(script, "Blues",true, loopsFile, output2);  
		//python2.runRscript();
		System.out.println(output);
	}

}
