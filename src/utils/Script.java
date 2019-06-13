package utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * run python script
 * @author axel poulet
 *
 */
public class Script {
	/** String log error => error */
	private String _logError = "";
	/** String path to the input matrix*/
	private String _input ="";
	/** Path where save the results*/
	private String _output ="";
	/** path to the bullseye.py*/
	private String _scriptPath = "";
	/** String color, define the color of the metaplot*/
	private String _color = "";
	/** double min minimum value for the key*/
	private double _min;
	/** double max minimum value for the key*/
	private double _max;
	/** boolean if true compute the distance manhattan*/
	private boolean _square = false;
	/**boolean if true compute the zscore */
	private boolean _zscore = false;
	
	
	/**
	 * Constructor of this class to iniatilise the different variables
	 * 
	 * @param juiceboxTools: String: path of juicertoolsBox
	 * @param hicFile: String: path to the HiC file
	 * @param norm: String: type of normalisation
	 * @param resolution: int: resolution of the bins 
	 */
	public Script(String script, String color,boolean zscore, boolean square, String input, String output, double min, double max ){
		this._input = input;
		this._output= output;
		this._scriptPath = script;
		this._color = color;
		this._zscore = zscore;
		this._square = square;
		this._min = min;
		this._max = max;
	}
	

	
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
	 * bullseye.py [-h] -i INPUT_FILE -o OUTPUT_FILE [-c COLORSCHEME] [-z] [-s] [-l LOWER] [-u UPPER]
	 * @return
	 * @throws IOException
	 */
	public boolean runPythonScript() throws IOException{
		int exitValue=1;
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process;
			if(this._min == -1 && this._max == -1){
				if(this._zscore){
					if(this._square)	process= runtime.exec("python"+" " +_scriptPath+" -i "+_input+" -o "+_output+" -c "+_color+" -z -s");
					else				process= runtime.exec("python"+" " +_scriptPath+" -i "+_input+" -o "+_output+" -c "+_color+" -z");	
				}else process= runtime.exec("python"+" " +this._scriptPath+" -i "+this._input+" -o "+this._output+" -c "+this._color+" -c "+this._color);
			}else{
				if(this._zscore){
					if(this._square) process= runtime.exec("python3"+" " +this._scriptPath+" -i "+this._input+" -o "+this._output+" -c "+this._color+" -z -s  -l "+(int)this._min+" -u "+(int)this._max);
					else             process= runtime.exec("python3"+" " +this._scriptPath+" -i "+this._input+" -o "+this._output+" -c "+this._color+" -z -c -l "+(int)this._min+" -u "+(int)this._max);
				}else process= runtime.exec("python"+" " +this._scriptPath+" -i "+this._input+" -o "+this._output+" -c "+this._color+" -c "+this._color+" -l "+(int)this._min+" -u "+(int)this._max);
			}
			new ReturnFlux(process.getInputStream()).start();
			new ReturnFlux(process.getErrorStream()).start();
			exitValue=process.waitFor();
		}
		catch (IOException e) {	e.printStackTrace();}
		catch (InterruptedException e) {e.printStackTrace();}
		System.out.print(this._logError);
		return exitValue==0;
	}
	
	/**
	 * 
	 * @author axel poulet
	 *
	 */
	public class ReturnFlux extends Thread {  
		/**  Le flux à rediriger  */
		private InputStream _flux;

		/**
		 * <b>Constructeur de RecuperationSorties</b>
		 * @param flux
		 *  Le flux à rediriger
		 */
		public ReturnFlux(InputStream flux){this._flux = flux;}
		/**
		 * 
		 */
		public void run(){
			try {    
				InputStreamReader reader = new InputStreamReader(this._flux);
				BufferedReader br = new BufferedReader(reader);
				String line=null;
				while ( (line = br.readLine()) != null) _logError = _logError+line+"\n";
			}catch (IOException ioe){ ioe.printStackTrace();}
		}	
	}
}
