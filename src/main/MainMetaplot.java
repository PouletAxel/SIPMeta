package main;

import java.io.IOException;

import gui.MetaplotGUI;
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
	static String _type = "";
	/** */
	static boolean _zScore = false;
	/** */
	static boolean _square = false;
	/** */
	static String _color = "Reds";
	/** */
	static boolean _gui = false;
	static boolean _simple = true;
	
	static String[] _colors = new String[] {"Reds", "BuGn", "Greens", "Purples", "Blues", "coolwarm", "magma", "inferno", "spectral", "viridis"};
	
	private static String _doc = ("metaplot Version 0.0.1 run with java 8\n"
			+"Usage:\n\n"
			+"simple  <loopsFile> <RawData> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-c COLORSCHEME] \n"
			+"substraction <loopsFile> <RawData1> <RawData2> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-c COLORSCHEME]\n\n"
			+"sMetaPlot: size of the metaplot (default 20 bins)\n"
			+"sImg: size of the image analysed by SIP (default 2000 bins)\n"
			+"-resMax TRUE or FALSE: default true, if false take the samller resolution\n"
			+"-c COLORSCHEME  matplotlib_colors (Blues, BuGn, Greens, Purples, Reds, coolwarm, magma, inferno, spectral, viridis) default Reds\n"
			+"-z znorm each ring\n"
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
			_type= args[0];
			_loopsFile = args[1];
			_input = args[2];
			if(_type.equals("simple") && args[3].contains("bullseye.py")==false){
				System.out.println("error bullseye option need bullseye.py script!!!!!!!\n\n"+_doc);
				System.exit(0);
			}
			if(_type.equals("substraction") && args[4].contains("bullseye.py")==false){
				System.out.println("error bullseye option need bullseye.py script!!!!!!!\n\n"+_doc);
				System.exit(0);
			}
				
			if(_type.matches("simple")){
				readOption(args,6);
				_script = args[3];
				try{_metaSize =Integer.parseInt(args[4]);}
				catch(NumberFormatException e){ returnError("sMetaPlot",args[4],"int");} 
				try{_imageSize =Integer.parseInt(args[5]);}
				catch(NumberFormatException e){ returnError("sImg",args[5],"int");}				
				
			}else if(_type.matches("substraction")){
				_input2 = args[3];
				_script = args[4];
				try{_metaSize =Integer.parseInt(args[5]);}
				catch(NumberFormatException e){ returnError("sMetaPlot",args[5],"int");} 
				try{_imageSize =Integer.parseInt(args[6]);}
				catch(NumberFormatException e){ returnError("sImg",args[6],"int");}
				readOption(args,8);
				_simple = false;
			}else{
				System.out.println("error in simple or substraction choice !!!!\n\n"+_doc);
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
				if(gui.isCompare()){
					_type = "substraction";
					_simple = false;
				}
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
			}
		}
		
		_step = _imageSize/2;
		try {	
			SIPMeta sip ;
			if(_type.matches("simple"))   sip = new SIPMeta(_input,_loopsFile,_gui,_resMax,_nbCpu-1,_imageSize,_metaSize);
			else			sip = new SIPMeta(_input,_input2,_loopsFile,_gui,_resMax,_nbCpu-1,_imageSize,_metaSize);
			sip.run(_script,_square,_simple,_zScore,_color,_min,_max);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
}

