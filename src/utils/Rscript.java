package utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class Rscript {
	String _logError = "";
	String _input ="";
	String _output ="";
	String _scriptPath = "";
	String _color = "";
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
	public Rscript(String script, String input,String pdfOutput, String color, int min, int max){
		_input = input;
		_output= pdfOutput;
		_scriptPath = script;
		_color = color;
		_min = min;
		_max = max;
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
			Process process = runtime.exec("Rscript"+" " +_scriptPath+" "+_input+" "+_output+" "+_color+" "+_min+" "+_max+" ");
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
