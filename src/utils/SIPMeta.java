package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
	private int _step = 0;
	/** int metaplot matrix size*/
	private int _metaSize = 21;
	/** int bin resolution*/
	private int _resolution = 0;
	/** int min resolution*/
	private int _minRes = 10000000;
	/** int ratio = res/minRes*/
	private int _ratio = 1;
	/**boolean is true used teh max resolution to do the metaplot */
	private boolean _resMax = true;
	/** boolean if true used the progress bar*/
	private boolean _gui = false;
	/** array list of string with the name of the chr*/
	private ArrayList<String> _chr = new ArrayList<String>(); 
	/** nb of loops*/
	private int _nbLine = 0;
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
	public SIPMeta(String input, String loopsFile, boolean gui, boolean resMax, int cpu, int imageSize, int metaSize ) throws IOException{
		this._input = input;
		this._gui = gui;
		this._resMax = resMax;
		this._imageSize = imageSize;
		this._loopsFile = loopsFile;
		this._nbLine = readLoopFile();
		this._metaSize=metaSize;
		this._step = (_imageSize/_ratio)/2;
		this._cpu=cpu;
		 
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
	public SIPMeta(String input, String input2, String loopsFile, boolean gui, boolean resMax, int cpu,int imageSize, int metaSize)throws IOException{
		this._input = input;
		this._input2 = input2;
		this._gui = gui;
		this._resMax = resMax;
		this._imageSize = imageSize;
		this._loopsFile = loopsFile;
		this._nbLine = readLoopFile();
		this._step = (_imageSize/_ratio)/2;
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
		String pathFileMatrix = this._loopsFile.replace(".txt", "_matrix.tab");
		String pattern = Pattern.quote(System.getProperty("file.separator"));
		String [] tab = pathFileMatrix.split(pattern);
		String newName = this._prefix+"_"+tab[tab.length-1];
		pathFileMatrix = pathFileMatrix.replace(tab[tab.length-1], newName);
		String output = pathFileMatrix.replace(".txt", "");
		FileToMatrix ftm = new FileToMatrix();
		System.out.println("Check existing images if not existing the program creating the image");
		makeTif(this._input,threshold);
		if (simple){
			ftm = new FileToMatrix(this._input, this._loopsFile,pathFileMatrix, this._resolution, this._metaSize);
			ftm.creatMatrix(this._step, this._ratio,this. _gui,this._nbLine);
		}else{
			makeTif(this._input2,threshold);
			ftm = new FileToMatrix(this._input,this._input2,this._loopsFile,pathFileMatrix, this._resolution, this._metaSize);
			ftm.creatMatrixSubstarction(this._step, this._ratio, this._gui,this._nbLine);
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
		process.go(imgDir, this._chr,this._cpu, this._gui, this._resolution, this._ratio, this._imageSize, threshold);
	}
	
	
	/**
	 * read the loop file and initialized the arrayList chr, minRes, res and ratio
	 * @return the nb of loops
	 * @throws IOException
	 */
	 
	private int readLoopFile() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(this._loopsFile));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		int nbLine = 0;
		while (line != null){
			if(nbLine > 0){
				sb.append(line);
				String[] parts = line.split("\\t");				
				String chr = parts[0]; 
				if (this._chr.contains(chr)==false)	this._chr.add(chr);
				int size = Integer.parseInt(parts[2])-Integer.parseInt(parts[1]);
				if(size > this._resolution) this._resolution = size;
				if(size < this._minRes) this._minRes = size;
			}
			++nbLine;
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
		if(this._resMax) this._ratio = this._resolution/this._minRes;
		return nbLine;
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
