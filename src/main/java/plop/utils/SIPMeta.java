package plop.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import plop.multiProcessing.ProcessMakeImage;

/**
 * SIPMeta object, with dumps data and a loops file, with the different method, the matrix for the metaplot will be obtain.
 * The APA score is also comput. And a strength file is also obtain where the strength of each loop is write in tabulated file
 *  
 * @author axel poulet
 *
 */

public class SIPMeta {
	/**String: prefix for the output files */
	private String _prefix;
	/** String: path to the loops file */
	private String _loopsFile;
	/**String: input2 path to the dumpdData used only when simple== false*/
	private String _input2="";
	/** String: input path to the dumpdData */
	private String _input;
	/**int: image size */
	private int _imageSize;
	/** int step size*/
	private int _step;
	/** int metaplot matrix size*/
	private int _metaSize;
	/** int bin resolution*/
	private int _resolution;
	/**boolean is true used teh max resolution to do the metaplot */
	private boolean _gui;
	/** array list of string with the name of the chr*/
	private ArrayList<String> _chr = new ArrayList<>();
	/** nb of cpu used in the nalaysis*/
	private int _cpu;
	
	
	
	/**
	 * Constructor for simple processing, initialised all the value
	 *
	 * @param input	String: path of SIP input
	 * @param loopsFile	String: path loops file
	 * @param gui	boolean: is main.java.plop.gui or not
	 * @param resolution int: resolution
	 * @param cpu	int: nb of processor
	 * @param imageSize int: imageSize
	 * @param metaSize	int: metaplot size
	 * @throws IOException exception
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
	 * Constructor for the subtraction processing, initialised all the value
	 *
	 *
	 * @param input	path for the data set with SIP data
	 * @param input2 path for the data set with SIP data
	 * @param loopsFile path to the loops file
	 * @param gui	boolean isGui
	 * @param resolution	int resolution of the SIP data
	 * @param cpu	int nb of processor used
	 * @param imageSize	int img size
	 * @param metaSize int metaplot size
	 */
	public SIPMeta(String input, String input2, String loopsFile, boolean gui, int resolution , int cpu,int imageSize, int metaSize){
		this._input = input;
		this._input2 = input2;
		this._gui = gui;
		this._resolution = resolution;
		this._imageSize = imageSize;
		this._loopsFile = loopsFile;
		this._step = _imageSize/2;
		this._cpu=cpu;
		this._metaSize=metaSize;
	}
	
	/**
	 * run the metaSIP with the paramter in input
	 * 
	 * @param script	path to buleye.py
	 * @param square	boolean metaplot square
	 * @param simple boolean simple if yes simple if no subtraction
	 * @param zscore boolean for the zscore computation
	 * @param color	String color for the plot
	 * @param min	double min for the metaplot scale
	 * @param max	double max for the metaplot scale
	 * @param threshold double threshold
	 * @throws IOException	exception
	 * @throws InterruptedException exception
	 */
	
	public void run(String script, boolean square, boolean simple, boolean zscore, String color, double min, double max, double threshold) throws IOException, InterruptedException{
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
		FileToMatrix ftm ;
		System.out.println(_input+"\n"+_input2);
		String nameRes = String.valueOf(_resolution);
		nameRes = nameRes.replace("000", "");
		nameRes = nameRes+"kb";
		String input = this._input+nameRes+File.separator;
		String input2 = this._input2+nameRes+File.separator;
		File a = new File(input);
		File a2 = new File(input2);
		if(simple && !a.exists()) {
			if(_gui)
				JOptionPane.showMessageDialog(null,this._resolution+" is not present in the dumped file: "+input+" run the program with the hic or cool option to dump the dataset" , "End of SIP program", JOptionPane.ERROR_MESSAGE);
			System.out.println(this._resolution+" is not present in the dumped file: \"+input+\" run the program with the hic or cool option to dump the dataset !!!!\n");
			return;
		}else if(!simple && (!a.exists() || !a2.exists())){
			if(_gui)
				JOptionPane.showMessageDialog(null,this._resolution+" is not present in the dumped file: "+input+" or "+input2+" run the program with the hic or cool option to dump the dataset" , "End of SIP program", JOptionPane.ERROR_MESSAGE);
			System.out.println(this._resolution+" is not present in the dumped file: \"+input+\" or \"+input2+\" run the program with the hic or cool option to dump the dataset !!!!\n");
			return;
		}

		System.out.println("Check existing images if not existing the program creating the image");

		File[] listOfFile = a.listFiles();
		for (File file : listOfFile) {
			if (file.isDirectory()) {
				String[] b = file.toString().split(File.separator);
				this._chr.add(b[b.length - 1]);
			}
		}
		makeTif(input,threshold);
		if (simple){
			ftm = new FileToMatrix(input, this._loopsFile,pathFileMatrix, this._resolution, this._metaSize);
			ftm.creatMatrix(this._step,this. _gui);
		}else{
			makeTif(input2,threshold);
			ftm = new FileToMatrix(input,input2,this._loopsFile,pathFileMatrix, this._resolution, this._metaSize);
			ftm.creatMatrixSubstarction(this._step, this._gui);
		}
		System.out.println("##### End of creating the image\n Start matrix for metaplot");
		ftm.getAPA();
		ftm.writeStrengthFile();
		System.out.println("##### End of matrix (file: "+pathFileMatrix+") \n Start python script");
		Script python = new Script(script, color, zscore, square, pathFileMatrix, output, min,max);
		python.runPythonScript();
		System.out.println("End of SIPMeta");
	}


	/**
	 * Create tif file
	 *
	 * @param imgDir: inoutDir with SIP file
	 * @param threshold double threshold
 	 * @throws InterruptedException Exception
	 */
	private void makeTif(String imgDir,double threshold) throws InterruptedException{
		ProcessMakeImage process = new ProcessMakeImage();
		process.go(imgDir, this._chr,this._cpu, this._gui, this._resolution, this._imageSize, threshold);
	}
	
		
	/**
	 * getter of the image resolution
	 * @return resolution detected in the loops file
	 */
	public int getResolution(){return this._resolution;}

	/**
	 *	input setter to change the input if needed 
	 * @param input  path of the input
	 */
	public void setInput(String input){this._input = input;}



	/**
	 *setter of the prefix parameter
	 * @param input String prefix for the output
	 */
	public void setPrefix(String input){this._prefix = input;}

}
