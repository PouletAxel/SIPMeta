package utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import multiProcessing.ProcessMakeImage;

/**
 * SIPMeta object, with dumps data and a loops file, with the different method, the matrix for the metaplot will be obtain.
 * The APA score is also comput. And a strength file is also obtain where the strength of each loop is write in tabulated file
 *  
 * @author axel poulet
 *
 */

public class SIPMeta {
	/**String: prefix for the output files */
	private String _prefix = "";
	/** String: path to the loops file */
	private String _loopsFile = "";
	/**String: input2 path to the dumpdData used only when simple== false*/
	private String _input2 ="";
	/** String: input path to the dumpdData */
	private String _input ="";
	/**int: image size */
	private int _imageSize =2000;
	/** int step size*/
	private int _step = _imageSize/2;
	/** int metaplot matrix size*/
	private int _metaSize = 21;
	/** int bin resolution*/
	private int _resolution = 5000;
	/**boolean is true used teh max resolution to do the metaplot */
	private boolean _gui = false;
	/** array list of string with the name of the chr*/
	private ArrayList<String> _chr = new ArrayList<String>(); 
	/** nb of cpu used in the nalaysis*/
	private int _cpu = 0;
	
	
	
	/**
	 * Constructor for simple processing, initialised all the value
	 * @param input
	 * @param loopsFile
	 * @param gui
	 * @param resMax
	 * @param cpu
	 * @param imageSize
	 * @param metaSize
	 * @throws IOException
	 */
	public SIPMeta(String input, String loopsFile, boolean gui, int resolution, int cpu, int imageSize, int metaSize ) throws IOException{
		this._input = input;
		this._gui = gui;
		this._imageSize = imageSize;
		this._loopsFile = loopsFile;
		this._metaSize = metaSize;
		this._step = _imageSize/2;
		this._cpu=cpu;
		this._resolution = resolution;
	}
	
	/**
	 * Constructor for the substraction processing, initialised all the value
	 *  
	 * @param input
	 * @param input2
	 * @param loopsFile
	 * @param gui
	 * @param resMax
	 * @param cpu
	 * @param imageSize
	 * @param metaSize
	 * @throws IOException
	 */
	public SIPMeta(String input, String input2, String loopsFile, boolean gui, int res , int cpu,int imageSize, int metaSize)throws IOException{
		this._input = input;
		this._input2 = input2;
		this._gui = gui;
		this._resolution = res;
		this._imageSize = imageSize;
		this._loopsFile = loopsFile;
		this._step = _imageSize/2;
		this._cpu=cpu;
		this._metaSize=metaSize;
	}
	
	/**
	 * run the metaSIP with the paramter in input
	 * 
	 * @param script
	 * @param squarre
	 * @param simple
	 * @param zscore
	 * @param color
	 * @param min
	 * @param max
	 * @param threshold
	 * @throws IOException
	 * @throws InterruptedException
	 */
	
	public void run(String script, boolean squarre, boolean simple, boolean zscore, String color, double min, double max, double threshold) throws IOException, InterruptedException{
		String pathFileMatrix = "";
		String[] tmpPath = this._loopsFile.split("\\/");
		String output = this._loopsFile.replaceAll(tmpPath[tmpPath.length-1], this._prefix);
		if(tmpPath[tmpPath.length-1].contains(".")){
			String[] tmp = tmpPath[tmpPath.length-1].split("\\.");
			pathFileMatrix = output+"_"+tmp[0]+"_matrix.tab";
			output = output+"_"+tmp[0];
		}else{		
			output = output+"_"+tmpPath[tmpPath.length-1];
			pathFileMatrix = output+"_matrix.tab";
		}
		FileToMatrix ftm = new FileToMatrix();
				
		String nameRes = String.valueOf(_resolution);
		nameRes = nameRes.replace("000", "");
		nameRes = nameRes+"kb"; 
		String input = this._input+nameRes+File.separator;
		File a = new File(input);
		System.out.println(input);
		if(a.exists()==false) {
			if(_gui){
				JOptionPane.showMessageDialog(null,this._resolution+" is not present in the dumped file: "+this._input+" run the program with the hic or cool option to dump the dataset" , "End of SIP program", JOptionPane.ERROR_MESSAGE);
			}
			System.out.println(this._resolution+" is not present in the dumped file: \"+this._input+\" run the program with the hic or cool option to dump the dataset !!!!\n");
			return;
		}
			
		System.out.println("Check existing images if not existing the program creating the image");
		a = new File(input);
		File[] listOfFile = a.listFiles();
		for(int i = 0; i < listOfFile.length; ++i) {
			if(listOfFile[i].isDirectory()) {
				String [] b = listOfFile[i].toString().split(File.separator);
				this._chr.add(b[b.length-1]);
			}
		}
		System.out.println(input);
		makeTif(input,threshold);
		if (simple){
			ftm = new FileToMatrix(input, this._loopsFile,pathFileMatrix, this._resolution, this._metaSize);
			ftm.creatMatrix(this._step,this. _gui);
		}else{
			makeTif(this._input2,threshold);
			ftm = new FileToMatrix(this._input,this._input2,this._loopsFile,pathFileMatrix, this._resolution, this._metaSize);
			ftm.creatMatrixSubstarction(this._step, this._gui);
		}
		System.out.println("##### End of creating the image\n Start matrix for metaplot");
		ftm.getAPA();
		ftm.writeStrengthFile();
		System.out.println("##### End of matrix (file: "+pathFileMatrix+") \n Start python script");
		Script python = new Script(script, color, zscore, squarre, pathFileMatrix, output, min,max);  
		python.runPythonScript();
		System.out.println("End of SIPMeta");
	}

	
	/**
	 * 
	 * @param imgDir
	 * @param threshold
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void makeTif(String imgDir,double threshold) throws IOException, InterruptedException{
		ProcessMakeImage process = new ProcessMakeImage();
		process.go(imgDir, this._chr,this._cpu, this._gui, this._resolution, this._imageSize, threshold);
	}
	
		
	/**
	 * getter of the image resolution
	 * @return resolution detected in the loops file
	 */
	public int getResolution(){return this._resolution;}
	/**
	 * getter of step size
	 * @return int step size
	 */
	public int getStep(){return this._step;}
	
	/**
	 *	input setter to change the input if needed 
	 * @param input
	 */
	public void setInput(String input){this._input = input;}
	
	/**
	 * prefix setter 
	 * @param String prefix
	 */	
	public void setPrefix(String input){this._prefix = input;}		
}
