package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import gui.MetaplotGUI;
import multiProcessing.ProcessDumpData;
import utils.SIPMeta;


/**
 * This main class obtain all the parameter by the gui or the command line to then run SIPMetaplot with the good parameters gave by the the user
 * 
 * Usage:
 * with SIP output
 * simple  <loopsFile> <RawData> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-t T] [-prefix PREFIX] [-c COLORSCHEME]
 * substraction <loopsFile> <RawData1> <RawData2> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-t T] [-prefix PREFIX] [-c COLORSCHEME]
 * 
 * with .hic file
 * hic simple  <loopsFile> <hicFile1> <outdir> <chrSizeFile> <JuicerBoxTools.jar> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s][-t T] [-prefix PREFIX] [-c COLORSCHEME]
 * hic substraction <loopsFile> <hicFile1> <hicFile2> <outdir1> <outdir2> <chrSizeFile> <JuicerBoxTools.jar> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-t T] [-prefix PREFIX] [-resMax TRUE/FALSE][-z] [-s] [-c COLORSCHEME]
 * 
 * 	Param:
 * 	sMetaPlot: size of the metaplot (default 20 bins)
 * sImg: size of the image analysed by SIP (default 2000 bins)
 * chrSizeFile: path to the chr size file, with the same name of the chr as in the hic file
 * -resMax TRUE or FALSE: default true, if false take the samller resolution
 * -c COLORSCHEME  matplotlib_colors (Blues, BuGn, Greens, Purples, Reds, coolwarm, magma, inferno, spectral, viridis) default Reds
 * -z znorm each ring in bullseye
 * -s Trim edges to make a square
 * -min MIN minvalue for color scale
 * -max Max maxvalue for color scale
 * -cpu number of cpu uses
 * -t T threshold value tests the distance normalized value, all the value > T will be replace by zero
 * -prefix PREFIX name of the output file
 * -h, --help print help
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
	/** int: image size of SIP */
	static int _imageSize =2000;
	/** int: sixe step to run a chr */
	static int _step = 0;
	/** int: sixe of the metaplot*/
	static int _metaSize = 21;
	/**int: loops res*/
	static int _resolution = 0;
	/** int: min resolution to do the metaplot*/
	static int _minRes = 1000000;
	/** ratio between minRes and resolution*/
	static int _ratio = 2;
	/** int: nb cpu used*/
	static int _nbCpu = 1;
	/** boolean if true do the analysis a the max resolution found in the loops file*/
	static boolean _resMax = true;
	/** double: min value for the key of the metaplot*/
	static double _min = -1;
	/** double: max value for the key of the metaplot*/
	static double _max = -1;
	/**boolean: if true compute the zscore in bullseye.py*/
	static boolean _zScore = false;
	/**boolean: if true compute manhattan distance in bullseye.py*/
	static boolean _square = false;
	/**boolean: if true run SIPMeta with the hic option if false input is SIP output*/
	static boolean _hic = false;
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
	/**Normalisation method to dump the the data with hic method (KR,NONE.VC,VC_SQRT)*/
	private static String _juiceBoXNormalisation = "KR";
	/**String: prefix for metaplot output */
	private static String _prefix = "SIPMeta";
	/**Array String: list of colors available for the metaplot*/
	static String[] _colors = new String[] {"Reds", "BuGn", "Greens", "Purples", "Blues", "coolwarm", "magma", "inferno", "spectral", "viridis"};
	/** String: doc of SIPMeta*/
	private static String _doc = ("SIPMeta1.0 Version  run with java 8\n"
			+ "Usage:\n"
			+ "with SIP output\n"
			+ "\tsimple  <loopsFile> <RawData> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-t T] [-prefix PREFIX] [-c COLORSCHEME]\n"
			+ "\tsubstraction <loopsFile> <RawData1> <RawData2> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-t T] [-prefix PREFIX] [-c COLORSCHEME]\n"
			+ "\nwith .hic file\n"
			+ "\thic simple  <loopsFile> <hicFile1> <outdir> <chrSizeFile> <JuicerBoxTools.jar> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s][-t T] [-prefix PREFIX] [-c COLORSCHEME]\n"
			+ "\thic substraction <loopsFile> <hicFile1> <hicFile2> <outdir1> <outdir2> <chrSizeFile> <JuicerBoxTools.jar> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-t T] [-prefix PREFIX] [-resMax TRUE/FALSE][-z] [-s] [-c COLORSCHEME]\n"
			+ "\nParameters:\n"
			+ "\tsMetaPlot: size of the metaplot (default 21 bins)\n"
			+ "\tsImg: size of the image analysed by SIP (default 2000 bins)\n"
			+ "\tchrSizeFile: path to the chr size file, with the same name of the chr as in the hic file\n"
			+ "\t-resMax TRUE or FALSE: default true, if false take the samller resolution\n"
			+ "\t-c COLORSCHEME  matplotlib_colors (Blues, BuGn, Greens, Purples, Reds, coolwarm, magma, inferno, spectral, viridis) default Reds\n"
			+ "\t-z znorm each ring in bullseye\n"
			+ "\t-s Trim edges to make a square\n"
			+ "\t-min MIN minvalue for color scale\n"
			+ "\t-max Max maxvalue for color scale\n"
			+ "\t-cpu number of cpu uses\n"
			+ "\t-t T threshold value tests the distance normalized value, all the value > T will be replace by zero\n"
			+ "\t-prefix PREFIX name of the output file\n"
			+ "\t-h, --help print help\n");
	
	
	/**
	 * Obtain the parameter from the gui or the command line.
	 * and then run the program on function the paramters
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		if((args.length >= 1 && args.length < 5)){
			System.out.println("miss some arguments!!!!!!\n\n"+_doc);
			System.exit(0);
		}else if(args.length >= 5 ){ ////////////////////////////////////////////////////////////////////// Here command line parameters
			/// if hic paramater
			if(args[0].equals("hic")){ 
				_hic=true;
				_loopsFile = args[2];
				_input = args[3];
				if(args[1].equals("simple")){
					if(args[7].contains("bullseye.py")==false){
						System.out.println("error bullseye option need bullseye.py script!!!!!!!\n\n"+_doc);
						System.exit(0);
					}
					_outDir = args[4];
					_script = args[7];
					readChrSizeFile(args[5]);
					_juiceBoxTools = args[6];
					try{_metaSize =Integer.parseInt(args[8]);}
					catch(NumberFormatException e){ returnError("sMetaPlot",args[8],"int");} 
					try{_imageSize =Integer.parseInt(args[9]);}
					catch(NumberFormatException e){ returnError("sImg",args[9],"int");}				
					readOption(args,10);
				}else if(args[1].equals("substraction")){
					_simple = false;
					if(args[9].contains("bullseye.py")==false){
						System.out.println("error bullseye option need bullseye.py script!!!!!!!\n\n"+_doc);
						System.exit(0);
					}
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
			}else if(args[0].equals("simple") || args[0].equals("substarction")){
				///if SIP output
				_loopsFile = args[1];
				_input = args[2];
				if(args[0].equals("substarction")){
					if(args[4].contains("bullseye.py")==false){
						System.out.println("error bullseye option need bullseye.py script!!!!!!!\n\n"+_doc);
						System.exit(0);
					}
					_simple=false;
					_script = args[4];
					_input2 = args[3];
					try{_metaSize =Integer.parseInt(args[5]);}
					catch(NumberFormatException e){ returnError("sMetaPlot",args[5],"int");} 
					try{_imageSize =Integer.parseInt(args[6]);}
					catch(NumberFormatException e){ returnError("sImg",args[6],"int");}
					readOption(args,7);
				}else if(args[0].equals("simple")){
					if(args[3].contains("bullseye.py")==false){
						System.out.println("error bullseye option need bullseye.py script!!!!!!!\n\n"+_doc);
						System.exit(0);
					}
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
				System.exit(0);
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
				_resMax = gui.isMaxRes();
				
				if(gui.isCompare())	_simple = false;
				if(gui.isSIP()==false){
					_hic = true;		
					readChrSizeFile(gui.getChrSizeFile());
				}	
				_outDir = gui.getOutDir();
				_outDir2 = gui.getOutDir2();
				_juiceBoxTools = gui.getJuiceBox();
				if(gui.isNONE()) _juiceBoXNormalisation = "NONE";
				else if (gui.isVC()) _juiceBoXNormalisation = "VC";
				else if (gui.isVC_SQRT()) _juiceBoXNormalisation = "VC_SQRT";
				_input2 = gui.getRawDataDir2();
				_min = gui.getMinValue();
				_max = gui.getMaxValue();
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
				System.exit(0);
			}
		}
		///////////////////// SIPMeta process.
		_step = _imageSize/2;
		try {	
			SIPMeta sip = new SIPMeta(_input,_loopsFile,_gui,_resMax,_nbCpu-1,_imageSize,_metaSize);
			if ((_metaSize % 2) == 0) {
				System.out.println("Error: bullseye requires a central point in the matrix, therefore metaplot size must be odd\n");
				System.exit(0);
			}
			if(_hic){
				System.out.println("start dump data "+_input);
				ProcessDumpData processDumpData = new ProcessDumpData();
				processDumpData.go(_input, _outDir, _chrSize, _juiceBoxTools, _juiceBoXNormalisation, _nbCpu-1, sip.getResolution(), _imageSize,_gui);
				sip.setInput(_outDir);
				System.out.println("######## End dump data "+_input);
				if(_simple==false){
					System.out.println("start dump data "+_input2);
					processDumpData.go(_input2, _outDir2, _chrSize, _juiceBoxTools, _juiceBoXNormalisation, _nbCpu-1, sip.getResolution(), _imageSize, _gui);
					sip = new SIPMeta(_outDir,_outDir2,_loopsFile,_gui,_resMax,_nbCpu-1,_imageSize,_metaSize);
					System.out.println("######## End dump data "+_input2);
				}
			}else if(_hic == false && _simple == false){
				System.out.println("plopi");
				sip = new SIPMeta(_input,_input2,_loopsFile,_gui,_resMax,_nbCpu-1,_imageSize,_metaSize);
			}
			sip.setPrefix(_prefix);
			try {
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
				if(args[i].equals("-resMax")){
					if(args[i+1].equals("false") || args[i+1].equals("FALSE")) _resMax = false;
					else if(args[i+1].equals("true") || args[i+1].equals("TRUE")) _resMax = true;
					else returnError(args[i+1],args[i+1],"String");
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
}

