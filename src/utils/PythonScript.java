package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import utils.Rscript.ReturnFlux;

/**
 * To help, I created a command line version of the bullseye plot. It takes the matrix.tab output from your metaplotter and creates both bullseye and normal heatmaps. 
 * We should replace the Rscript with this python script. Note that it has several options that would be nice for users to have.  Here are the options and how 
 * I think they should be in the gui:
 * -c in the python script should be an optional text entry in the gui (or dropdown menu if possible) for any colorscheme in matplotlib. If not set in python script it defaults to Red.
 * -z should be a checkbox in the gui (default is no check). In the python script, the -z option calculates the zscores of each ring in the bullseye.
 *  We don’t need to worry about the -s option in the bullseye script. 
 *  -l and -u should be optional integers. If no input it uses the default scale from matplotlib. If set, -l and -u correspond to the color range in the heatmap.
 *  You  can pass the matrix to -i in the python script.
 *  -o in the python script is the prefix for output png files. It appends “_bullseye.png” and “_normal.png” to the prefix.
 *  
 *  
 */

public class PythonScript {
	String _logError = "";
	String _input ="";
	String _output ="";
	String _scriptPath = "";
	String _color = "";
	boolean _zscore = false;
	int _min;
	int _max;
	
	/**
	 * Constructor of this class to iniatilise the different variables
	 * 
	 * @param juiceboxTools: String: path of juicertoolsBox
	 * @param hicFile: String: path to the HiC file
	 * @param norm: String: type of normalisation
	 * @param resolution: int: resolution of the bins 
	 */
	public PythonScript(String script, String color,boolean zscore, String input, String output){
		_input = input;
		_output= output;
		_scriptPath = script;
		_color = color;
		_zscore = zscore;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean runRscript() throws IOException{
		int exitValue=1;
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process;
			if(this._zscore) process= runtime.exec("python3"+" " +_scriptPath+" -i "+_input+" -o "+_output+" -c "+_color+" -z -c "+_color);
			else process= runtime.exec("python"+" " +_scriptPath+" -i "+_input+" -o "+_output+" -c "+_color+" -c "+_color);
			new ReturnFlux(process.getInputStream()).start();
			new ReturnFlux(process.getErrorStream()).start();
			exitValue=process.waitFor();
		}
		catch (IOException e) {	e.printStackTrace();}
		catch (InterruptedException e) {e.printStackTrace();}
		System.out.print(_logError);
		return exitValue==0;
	}
	
	
	/**
	 * 
	 * @author axel poulet
	 *
	 */
	public class ReturnFlux extends Thread {  

		/**  Le flux à rediriger  */
		private InputStream flux;

		/**
		 * <b>Constructeur de RecuperationSorties</b>
		 * @param flux
		 *  Le flux à rediriger
		 */
		public ReturnFlux(InputStream flux){
			this.flux = flux;
		}
		
		/**
		 * 
		 */
		public void run(){
			try {    
				InputStreamReader reader = new InputStreamReader(flux);
				BufferedReader br = new BufferedReader(reader);
				String line=null;
				
				while ( (line = br.readLine()) != null) _logError = _logError+line+"\n";
			}
			catch (IOException ioe){
				ioe.printStackTrace();
			}
		}
		
	}
}
