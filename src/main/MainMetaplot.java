package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.swing.JOptionPane;

import gui.MetaplotGUI;
import multiProcessing.ProcessCoolerDumpData;
import multiProcessing.ProcessDumpData;
import utils.SIPMeta;


/**
 * This main class obtain all the parameter by the gui or the command line to then run SIPMetaplot with the good parameters gave by the the user
 * 
 * 	We are using juicerboxTools.jar for the hic option to dump the data of the coodinate of interest.
 * Neva C. Durand, Muhammad S. Shamim, Ido Machol, Suhas S. P. Rao, Miriam H. Huntley, Eric S. Lander, and Erez Lieberman Aiden. "Juicer provides a 
 * one-click system for analyzing loop-resolution Hi-C experiments." Cell Systems 3(1), 2016.
 * 
 * We are also using imageJ to create the image and run them.
 *  Schneider, C. A.; Rasband, W. S. & Eliceiri, K. W. (2012), "NIH Image to ImageJ: 25 years of image analysis", Nature methods 9(7): 671-675, PMID 22930834
 *  Rueden, C. T.; Schindelin, J. & Hiner, M. C. et al. (2017), "ImageJ2: ImageJ for the next generation of scientific image data", BMC Bioinformatics 18:529, PMID 29187165, doi:10.1186/s12859-017-1934-z
 * 
 * @author axel poulet and m. jordan rowley
 *
 */
public class MainMetaplot{
	/** String: stock the loops file path */
	static String _loopsFile = "";
	/** String: stock the input2 path (hic file or directory dependent of the SIP value)*/
	static String _input2 ="";
	/** String: stock the input path (hic file or directory dependent of the SIP value)*/
	static String _input ="";
	/** String: stock the output2 path if SIP false, path for the dump dat of the input2*/
	static String _outDir2 ="";
	/** String: stock the output path if SIP false, path for the dump dat of the input*/
	static String _outDir ="";
	/** String: stock the script file path have to be bullseye.py */
	static String _script = "";
	/** String: stock the script file path have to be bullseye.py */
	static String _logError = "";
	/** int: image size of SIP */
	static int _imageSize =2000;
	/** int: sixe step to run a chr */
	static int _step = _imageSize/2;
	/** int: sixe of the metaplot*/
	static int _metaSize = 21;
	/**int: loops res*/
	static int _resolution = 5000;
	/** ratio between minRes and resolution*/
	static int _ratio = 2;
	/** int: nb cpu used*/
	static int _nbCpu = 1;
	/** double: min value for the key of the metaplot*/
	static double _min = -1;
	/** double: max value for the key of the metaplot*/
	static double _max = -1;
	/**boolean: if true compute the zscore in bullseye.py*/
	static boolean _zScore = false;
	/**boolean: if true compute manhattan distance in bullseye.py*/
	static boolean _square = false;
	/**boolean: if true run SIPMeta with the hic option if false input is SIP output*/
	static boolean _isHic = false;
	/** */
	static boolean _isCool = false;
	static boolean _isSip = true;
	/** String: colors of the metaplot*/
	static String _color = "Reds";
	/**boolean: if true SIPMeta is run with a gui */
	static boolean _gui = false;
	/**boolean: if true run SIPMeta simple else run SIPMeta substraction */
	static boolean _simple = true;
	/** hash map stocking in key the name of the chr and in value the size*/
	private static HashMap<String,Integer> _chrSize =  new HashMap<String,Integer>();
	/**double: threshold value for the matrix computation */
	private static double _threshold = -1;
	/** Path to the jucier_tools_box to dump the data not necessary for Processed and dumped method */
	private static String _juiceBoxTools = "";
	private static String _cooler = "";
	private static String _cooltools = "";
	/**Normalisation method to dump the the data with hic method (KR,NONE.VC,VC_SQRT)*/
	private static String _juiceBoXNormalisation = "KR";
	/**String: prefix for metaplot output */
	private static String _prefix = "SIPMeta";
	/**Array String: list of colors available for the metaplot*/
	static String[] _colors = new String[] {"Reds", "BuGn", "Greens", "Purples", "Blues", "coolwarm", "magma", "inferno", "spectral", "viridis"};
	/** String: doc of SIPMeta*/
	private static String _doc = ("SIPMeta run with java 8\n"
			+ "Usage:\n"
			+ "\twith SIP output\n"
			+ "\t\tsimple  <loopsFile> <RawData> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-res RES][-z] [-s] [-t T] [-prefix PREFIX] [-c COLORSCHEME]\n"
			+ "\t\tsubtraction <loopsFile> <RawData1> <RawData2> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-res RES][-z] [-s] [-t T] [-prefix PREFIX] [-c COLORSCHEME]\n"
			+ "\n\twith .hic file\n"
			+ "\t\thic simple  <loopsFile> <hicFile1> <outdir> <chrSizeFile> <JuicerBoxTools.jar> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-res RES][-z] [-s][-t T] [-prefix PREFIX] [-c COLORSCHEME]\n"
			+ "\t\thic subtraction <loopsFile> <hicFile1> <hicFile2> <outdir1> <outdir2> <chrSizeFile> <JuicerBoxTools.jar> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-t T] [-prefix PREFIX] [-res RES][-z] [-s] [-c COLORSCHEME]\n"
			+ "\n\twith .mcool file\n"
			+ "\t\tcool simple  <loopsFile> <hicFile1> <outdir> <chrSizeFile> <cooler> <cooltools> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-res RES][-z] [-s][-t T] [-prefix PREFIX] [-c COLORSCHEME]\n"
			+ "\t\tcool subtraction <loopsFile> <hicFile1> <hicFile2> <outdir1> <outdir2> <chrSizeFile> <cooler> <cooltools> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-t T] [-prefix PREFIX] [-res RES][-z] [-s] [-c COLORSCHEME]\n"
			+ "\nParameters:\n"
			+ "\tsMetaPlot: size of the desired metaplot (default 21 bins). Must be an odd number.\n"
			+ "\tsImg: size of the image analyzed by SIP. Corresponds to –mat option in SIP (default 2000 bins).\n"
			+ "\tchrSizeFile: path to the chr size file, with the same name of the chr as in the hic file (i.e. chr1 does not match Chr1 or 1).\n"
			+ "\t-res: resolution in bp (default 5000 bp)\n"
			+ "\t-norm: <NONE/VC/VC_SQRT/KR> only for hic option (default KR).\n"
			+ "\t-c COLORSCHEME  matplotlib_colors (Blues, BuGn, Greens, Purples, Reds, coolwarm, magma, inferno, spectral, viridis) default Reds. Can be any matplotlib color gradient.\n"
			+ "\t-z Set this option to znorm each ring.\n"
			+ "\t-t Threshold value tests the distance normalized value, all the value > T will be replaced by zero. Set high to avoid outliers skewing the average.\n"
			+ "\t-prefix Prefix desired when naming the output files.\n"
			+ "\t-s Set this option to trim edges to make a square plot but with Manhattan distances. (Not recommended as normal square plots are already created).\n"
			+ "\t-min Min minimum value for color scale.\n"
			+ "\t-max Max maximum value for color scale.\n"
			+ "\t-cpu: Number of CPU used for processing (default 1).\n"
			+ "\t-h, --help print help."
			+ "\nAuthors:\n"
			+ "Axel Poulet\n"
			+ "\tDepartment of Molecular, Cellular  and Developmental Biology Yale University 165 Prospect St\n"
			+ "\tNew Haven, CT 06511, USA\n"
			+ "M. Jordan Rowley\n"
			+ "\tDepartment of Genetics, Cell Biology and Anatomy, University of Nebraska Medical Center Omaha,NE 68198-5805\n"
			+ "\nContact: pouletaxel@gmail.com OR jordan.rowley@unmc.edu");
	/**
	 * Obtain the parameter from the gui or the command line.
	 * and then run the program on function the paramters
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		if((args.length >= 1 && args.length < 5)){
			System.out.println("missing some arguments !!!!!!\n\n"+_doc);
			System.exit(0);
		}else if(args.length >= 5 ){ ////////////////////////////////////////////////////////////////////// Here command line parameters
			/// if hic paramater
			if(args[0].equals("hic")){ 
				_isHic = true;
				_loopsFile = args[2];
				_input = args[3];
				if(args[1].equals("simple")){
					_outDir = args[4];
					_script = args[7];
					readChrSizeFile(args[5]);
					_juiceBoxTools = args[6];
					try{_metaSize =Integer.parseInt(args[8]);}
					catch(NumberFormatException e){ returnError("sMetaPlot",args[8],"int");} 
					try{_imageSize =Integer.parseInt(args[9]);}
					catch(NumberFormatException e){ returnError("sImg",args[9],"int");}				
					readOption(args,10);
				}else if(args[1].equals("subtraction")){
					_simple = false;
					_input2 = args[4];
					_outDir = args[5];
					_outDir2 = args[6];
					readChrSizeFile( args[7]);
					_juiceBoxTools = args[8];
					_script = args[9];					
					try{_metaSize =Integer.parseInt(args[10]);}
					catch(NumberFormatException e){ returnError("sMetaPlot",args[10],"int");} 
					try{_imageSize =Integer.parseInt(args[11]);}
					catch(NumberFormatException e){ returnError("sImg",args[11],"int");}				
					readOption(args,12);
				}
			}else if(args[0].equals("cool")){ 
				_isCool = true;
				_loopsFile = args[2];
				_input = args[3];
				if(args[1].equals("simple")){
					_outDir = args[4];
					readChrSizeFile(args[5]);
					_cooler = args[6];
					_cooltools = args[7];
					_script = args[8];
					try{_metaSize =Integer.parseInt(args[9]);}
					catch(NumberFormatException e){ returnError("sMetaPlot",args[9],"int");} 
					try{_imageSize =Integer.parseInt(args[10]);}
					catch(NumberFormatException e){ returnError("sImg",args[10],"int");}				
					readOption(args,11);
				}else if(args[1].equals("subtraction")){
					_simple = false;
					_input2 = args[4];
					_outDir = args[5];
					_outDir2 = args[6];
					readChrSizeFile( args[7]);
					_cooler = args[8];
					_cooltools= args[9];
					_script = args[10];					
					try{_metaSize =Integer.parseInt(args[11]);}
					catch(NumberFormatException e){ returnError("sMetaPlot",args[11],"int");} 
					try{_imageSize =Integer.parseInt(args[12]);}
					catch(NumberFormatException e){ returnError("sImg",args[12],"int");}				
					readOption(args,13);
				}
			}else if(args[0].equals("simple") || args[0].equals("subtraction")){
				_loopsFile = args[1];
				_input = args[2];
				if(args[0].equals("subtraction")){
					_simple=false;
					_script = args[4];
					_input2 = args[3];
					try{_metaSize =Integer.parseInt(args[5]);}
					catch(NumberFormatException e){ returnError("sMetaPlot",args[5],"int");} 
					try{_imageSize =Integer.parseInt(args[6]);}
					catch(NumberFormatException e){ returnError("sImg",args[6],"int");}
					readOption(args,7);
				}else if(args[0].equals("simple")){
					_script = args[3];
					try{_metaSize =Integer.parseInt(args[4]);}
					catch(NumberFormatException e){ returnError("sMetaPlot",args[4],"int");} 
					try{_imageSize =Integer.parseInt(args[5]);}
					catch(NumberFormatException e){ returnError("sImg",args[5],"int");}				
					readOption(args,6);
				}
			}else{
				System.out.println(args[0]+" doesn't existed\n\n");
				System.out.println(_doc);
				return;
			}
		}else{ ////////////////// If gui 
			MetaplotGUI gui = new MetaplotGUI();
			while( gui.isShowing()){
				 try {Thread.sleep(1);}
				catch (InterruptedException e) {e.printStackTrace();}
		    }	
			if (gui.isStart()){
				_input = gui.getRawDataDir();
				_loopsFile = gui.getLoopFile();
				_script = gui.getScript();
				
				if(gui.isCompare())	_simple = false;
				if(gui.isHic()){
					_isHic = true;
					_isSip = false;
					_isCool = false;
					readChrSizeFile(gui.getChrSizeFile());
				}
				if(gui.isCool()){
					_isCool = true;
					_isHic = false;
					_isSip = false;
					readChrSizeFile(gui.getChrSizeFile());
				}
				_outDir = gui.getOutDir();
				_outDir2 = gui.getOutDir2();
				_juiceBoxTools = gui.getJuiceBox();
				_cooler = gui.getCooler();
				_cooltools = gui.getCoolTools();
				if(gui.isNONE()) _juiceBoXNormalisation = "NONE";
				else if (gui.isVC()) _juiceBoXNormalisation = "VC";
				else if (gui.isVC_SQRT()) _juiceBoXNormalisation = "VC_SQRT";
				_input2 = gui.getRawDataDir2();
				_min = gui.getMinValue();
				_max = gui.getMaxValue();
				_resolution = gui.getResolution();
				_metaSize = gui.getMatrixSize();
				_imageSize = gui.getSipImageSize();
				_zScore = gui.isZscore();
				_square = gui.isSquareManha();
				_gui = true;
				_color = gui.getColor();
				_nbCpu = gui.getNbCpu();
				_prefix = gui.getPrefix();
			}else{
				System.out.println(_doc);
				return;
			}
		}

		////Test dir and file
		
		File f = new File(_loopsFile);
		if(f.exists()==false){
			System.out.println(_loopsFile+" doesn't existed !!! \n\n");
			System.out.println(_doc);
			return;
		}
		
		if(_script.contains("bullseye.py")==false){
			System.out.println("error bullseye option need bullseye.py script!!!!!!!\n\n"+_doc);
			return;
		}
		f = new File(_script);
		if(f.exists()==false){
			System.out.println(_script+" doesn't existed !!! \n\n");
			System.out.println(_doc);
			return;
		}
		
		f = new File(_input);
		if(f.exists()==false && _input.startsWith("https")==false){
				System.out.println(_input+" doesn't existed !!! \n\n");
				System.out.println(_doc);
				return;
		}
		
		if(_simple == false){
			f = new File(_input2);
			if(f.exists()==false){
					System.out.println(_input2+" doesn't existed !!! \n\n");
					System.out.println(_doc);
					return;
			}
		}
		
		///////////////////// SIPMeta process.
		_step = _imageSize/2;
		try {	
			SIPMeta sip = new SIPMeta(_input,_loopsFile,_gui,_resolution,_nbCpu,_imageSize,_metaSize);
			if ((_metaSize % 2) == 0) {
				System.out.println("Error: bullseye requires a central point in the matrix, therefore metaplot size must be odd\n");
				return;
			}
			if(_isHic){
				if(_outDir.endsWith(File.separator) == false) 
					_outDir = _outDir+File.separator;
				System.out.println("start dump data "+_input);
				ProcessDumpData processDumpData = new ProcessDumpData();
				processDumpData.go(_input, _outDir, _chrSize, _juiceBoxTools, _juiceBoXNormalisation, _nbCpu, _resolution, _imageSize,_gui);
				sip.setInput(_outDir);
				System.out.println("######## End dump data "+_input);
				if(_simple==false){
					if(_outDir2.endsWith(File.separator) == false) 
						_outDir2 = _outDir2+File.separator;
					System.out.println("start dump data "+_input2);
					processDumpData.go(_input2, _outDir2, _chrSize, _juiceBoxTools, _juiceBoXNormalisation, _nbCpu, _resolution, _imageSize, _gui);
					sip = new SIPMeta(_outDir,_outDir2,_loopsFile,_gui,_resolution,_nbCpu,_imageSize,_metaSize);
					System.out.println("######## End dump data "+_input2);
				}
			}else if(_isCool) {
				if( testTools(_cooltools,0,3,0) == false || testTools(_cooler,0,8,6) == false) {
					System.out.println( _cooltools +" or" + _cooler+" is not the good version for SIP (it needs cooltools version >= 0.3.0 and cooler version >= 0.8.6) !!! \n\n");
					System.out.println(_doc);
					if(_gui){
						JOptionPane.showMessageDialog(null, "Error SIP program", _cooltools +" or" + _cooler+" is not the good version for SIP (it needs cooltools version >= 0.3.0 and cooler version >= 0.8.6) !!!"
								 , JOptionPane.ERROR_MESSAGE);
					}
					return;
				}
				System.out.println("start dump data "+_input);
				if(_outDir.endsWith(File.separator) == false) 
					_outDir = _outDir+File.separator;
				ProcessCoolerDumpData dumpData = new ProcessCoolerDumpData();
				dumpData.go(_input, _outDir, _chrSize, _cooltools,_cooler, _nbCpu, _resolution, _imageSize,_gui);
				sip.setInput(_outDir);
				System.out.println("######## End dump data "+_input);
				if(_simple==false){
					if(_outDir2.endsWith(File.separator) == false) 
						_outDir2 = _outDir2+File.separator;
					System.out.println("start dump data "+_input2);
					dumpData.go(_input2, _outDir2, _chrSize, _cooltools,_cooler, _nbCpu, _resolution, _imageSize,_gui);
					sip = new SIPMeta(_outDir,_outDir2,_loopsFile,_gui,_resolution,_nbCpu,_imageSize,_metaSize);
					System.out.println("######## End dump data "+_input2);
				}
				
			}else if(_isHic == false && _simple == false){
				sip = new SIPMeta(_input,_input2,_loopsFile,_gui,_resolution,_nbCpu-1,_imageSize,_metaSize);
			}
			sip.setPrefix(_prefix);
			try {
				System.out.println();
				sip.run(_script,_square,_simple,_zScore,_color,_min,_max,_threshold);
			} catch (NullPointerException w) {
				System.out.println("\nError! This is usually because the chromosome names don't match up between the loops file and the raw data files."
						+ " For example Chr1 (capitalized) vs chr1 (no caps) vs 1 (no chr).\n Change your loops file to stay consistent with the 2-D input data.");	}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	
	/**
	 * 
	 * @param args table of String stocking the arguments for the program
	 * @param index table index where start to read the arguments
	 * @throws IOException if some parameters don't exist
	 */
	private static void readOption(String args[], int index) throws IOException{
		if(index < args.length){
			for(int i = index; i < args.length;i+=2){
				if(args[i].equals("-res")){
					try{_resolution =Integer.parseInt(args[i+1]);}
					catch(NumberFormatException e){ returnError("-res",args[i+1],"int");} 
				}else if(args[i].equals("-prefix")){
					_prefix = args[i+1];
				}else if(args[i].equals("-t")){
					try{_threshold = Double.parseDouble(args[i+1]);}
					catch(NumberFormatException e){ returnError(args[i],args[i+1],"double");}
				}else if(args[i].equals("-min")){
						try{_min =Integer.parseInt(args[i+1]);}
						catch(NumberFormatException e){ returnError(args[i],args[i+1],"int");} 
				}else if(args[i].equals("-max")){
					try{_max =Integer.parseInt(args[i+1]);}
					catch(NumberFormatException e){ returnError(args[i],args[i+1],"int");} 
				}else if(args[i].equals("-z")){
					try{_zScore = true;}
					catch(NumberFormatException e){ returnError(args[i],args[i+1],"int");}
					i--;
				}else if(args[i].equals("-s")){
					try{_square = true;}
					catch(NumberFormatException e){ returnError(args[i],args[i+1],"int");}
					i--;
				}else if(args[i].equals("-c")){
					if(_colors.equals(args[i+1]) == false)
						_color = args[i+1];
					else{
						System.out.println("color choose doesn't exist !!!!!!!\n\n"+_doc);
						System.exit(0);
					}
				}else if(args[i].equals("-cpu")){
					try{_nbCpu =Integer.parseInt(args[i+1]);}
					catch(NumberFormatException e){ returnError("-cpu",args[i+1],"int");}
					if(_nbCpu > Runtime.getRuntime().availableProcessors() || _nbCpu <= 0){
						System.out.println("the number of CPU "+ _nbCpu+" is superior of the server/computer' cpu "+Runtime.getRuntime().availableProcessors()+"\n");
						System.out.println(_doc);
						System.exit(0);
					}
				}else if(args[i].equals("-norm")){
					if(args[i+1].equals("NONE") || args[i+1].equals("VC") 
							|| args[i+1].equals("VC_SQRT") || args[i+1].equals("KR")){
						_juiceBoXNormalisation = args[i+1];
					}else{
						System.out.println("-norm = "+args[i+1]+", not defined\n");
						System.out.println(_doc);
						System.exit(0);
					}
				}else{
					System.out.println(args[i]+" doesn't existed\n\n");
					System.out.println(_doc);
					System.exit(0);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param param
	 * @param value
	 * @param type
	 */
	private static void returnError(String param, String value, String type){
		System.out.println(param+" has to be an integer "+value+" can't be convert in "+type+"\n\n");
		System.out.println(_doc);
		System.exit(0);
	}
	
	
	/**
	 * Run the input file and stock the info of name chr and their size in hashmap
	 * @param chrSizeFile path chr size file
	 * @throws IOException if file does't exist
	 */
	private static void readChrSizeFile( String chrSizeFile) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(chrSizeFile));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null){
			sb.append(line);
			String[] parts = line.split("\\t");				
			String chr = parts[0]; 
			int size = Integer.parseInt(parts[1]);
			_chrSize.put(chr, size);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
	}
	
	public static boolean testTools(String pathTools, int first, int second, int third) {
		
		Runtime runtime = Runtime.getRuntime();
		String cmd = pathTools+" --version";
		//System.out.println(cmd);
		Process process;
		try {
			process = runtime.exec(cmd);
	
		new ReturnFlux(process.getInputStream()).start();
		new ReturnFlux(process.getErrorStream()).start();
		process.waitFor();
		
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String [] tline = _logError.split(" ");
		System.out.println("log!!!! "+_logError);
		_logError = "";
		if(tline.length > 0){
			tline = tline[tline.length-1].split("\\.");
			tline[2] = tline[2].replace("\n", "");
			if(Integer.parseInt(tline[0]) >= first && Integer.parseInt(tline[1]) >= second) //&& Integer.parseInt(tline[2]) >= third)
				return true;
			else
				return false;
		}else
			return false;
	}
	
	public static class ReturnFlux extends Thread {  

		/**  Flux to redirect  */
		private InputStream _flux;

		/**
		 * <b>Constructor of ReturnFlux</b>
		 * @param flux
		 *  flux to redirect
		 */
		public ReturnFlux(InputStream flux){this._flux = flux; }
		
		/**
		 * 
		 */
		public void run(){
			try {    
				InputStreamReader reader = new InputStreamReader(this._flux);
				BufferedReader br = new BufferedReader(reader);
				String line=null;
				while ( (line = br.readLine()) != null) {
					if(line.contains("WARN")== false) _logError = _logError+line+"\n";
				}
			}
			catch (IOException ioe){
				ioe.printStackTrace();
			}
		}		
	}
	
}

