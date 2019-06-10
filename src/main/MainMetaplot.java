package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import gui.MetaplotGUI;
import multiProcessing.ProcessDumpData;
import utils.SIPMeta;


/**
 * 
 * @author axel poulet and m. jordan rowley
 *
 * metaplot Version 0.0.1 run with java
 * Usage:
 *	 simple <loopsFile> <RawData> <Rscript> <sMetaPlot> <sImg> [option]
 *   substraction <loopsFile> <RawData1> <RawData2> <Rscript> <sMetaPlot> <sImg> [option]
 *   		
 *   sMetaPlot: size of the metaplot (default 20 bins)
 *   sImg: size of the image analysed by SIP (default 2000 bins)
 *   -resMax: default true, if false take the samller resolution
 *   -min: default min value detected in the matrix results
 *   -max: default max value detected in the matrix results
 *   -h, --help print help
 *
 *
 */
public class MainMetaplot{
	/** */
	static String _loopsFile = "";
	/** */
	static String _input2 ="";
	/** */
	static String _input ="";
	static String _outDir2 ="";
	/** */
	static String _outDir ="";
	/** */
	static String _script = "";
	/** */
	static int _imageSize =2000;
	/** */
	static int _step = 0;
	/** */
	static int _metaSize = 21;
	/** */
	static int _resolution = 0;
	/** */
	static int _minRes = 10000;
	/** */
	static int _ratio = 2;
	/** */
	static int _nbCpu = 1;
	/** */
	static double _avgValue = 0;
	/** */
	static double _stdValue = 0;
	/** */
	static boolean _resMax = true;
	/** */
	static double _min = -1;
	/** */
	static double _max = -1;
	/** */
	static boolean _zScore = false;
	/** */
	static boolean _square = false;
	/** */
	static boolean _hic = false;
	/** */
	static String _color = "Reds";
	/** */
	static boolean _gui = false;
	static boolean _simple = true;
	/** hash map stocking in key the name of the chr and in value the size*/
	private static HashMap<String,Integer> _chrSize =  new HashMap<String,Integer>();
	

	private static double _threshold = -1;
	/** Path to the jucier_tools_box to dump the data not necessary for Processed and dumped method */
	private static String _juiceBoxTools = "";
	/**Normalisation method to dump the the data with hic method (KR,NONE.VC,VC_SQRT)*/
	private static String _juiceBoXNormalisation = "KR";
	private static String _prefix = "SIPMeta";
	static String[] _colors = new String[] {"Reds", "BuGn", "Greens", "Purples", "Blues", "coolwarm", "magma", "inferno", "spectral", "viridis"};
	
	private static String _doc = ("metaplot Version 0.0.1 run with java 8\n"
			+"Usage:\n\n"
			+"simple  <loopsFile> <RawData> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-t T] [-prefix PREFIX] [-c COLORSCHEME] \n"
			+"substraction <loopsFile> <RawData1> <RawData2> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-t T] [-prefix PREFIX] [-c COLORSCHEME]\n\n"
			+"hic simple  <loopsFile> <hicFile1> <outdir> <chrSizeFile> <JuicerBoxTools.jar> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s][-t T] [-prefix PREFIX] [-c COLORSCHEME] \n"
			+"hic substraction <loopsFile> <hicFile1> <hicFile2> <outdir1> <outdir2> <chrSizeFile> <JuicerBoxTools.jar> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-t T] [-prefix PREFIX] [-resMax TRUE/FALSE][-z] [-s] [-c COLORSCHEME]\n\n"
			+"sMetaPlot: size of the metaplot (default 21 bins)\n"
			+"sImg: size of the image analysed by SIP (default 2000 bins)\n"
			+"chrSizeFile: path to the chr size file, with the same name of the chr as in the hic file\n"
			+ "-norm: <NONE/VC/VC_SQRT/KR> only for hic option (default KR)\n"
			+"-resMax TRUE or FALSE: default true, if false take the samller resolution\n"
			+"-c COLORSCHEME  matplotlib_colors (Blues, BuGn, Greens, Purples, Reds, coolwarm, magma, inferno, spectral, viridis) default Reds\n"
			+"-z znorm each ring\n"
			+"-t T threshold value tests the distance normalized value, all the value > T will be replace by zero\n"
			+"-prefix PREFIX name of the output file\n"
			+"-s Trim edges to make a square\n"
			+"-min MIN minvalue for color scale\n"
			+"-max Max maxvalue for color scale\n"
			+"-cpu: Number of CPU used for SIP processing (default 1)\n"
			+"-h, --help print help\n");
	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		if((args.length >= 1 && args.length < 5)){
			System.out.println("miss some arguments!!!!!!\n\n"+_doc);
			System.exit(0);
		}else if(args.length >= 5 ){
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
		}else{
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
			}
			else{
				System.out.println(_doc);
				System.exit(0);
			}
		}
		
		_step = _imageSize/2;
		try {	
			SIPMeta sip = new SIPMeta(_input,_loopsFile,_gui,_resMax,_nbCpu-1,_imageSize,_metaSize);
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
			
			System.out.println("prefix "+_prefix+"\n"
					+ "input "+_input+"\n"
					+ "input "+_input2+"\n"
					);
			sip.setPrefix(_prefix);
			sip.run(_script,_square,_simple,_zScore,_color,_min,_max,_threshold);
			
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
					if(args[i+1].equals("false") || args[i+1].equals("FALSE"))
						_resMax = false;
					else if(args[i+1].equals("true") || args[i+1].equals("TRUE"))
						_resMax = true;
					else
						returnError(args[i+1],args[i+1],"String");
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

