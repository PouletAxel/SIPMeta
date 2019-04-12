package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import gui.MetaplotGUI;
import gui.Progress;
import utils.FileToMatrix;
import utils.ProcessTuplesFile;
import utils.Script;

/**
 * 
 * @author axel poulet
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
	static String _plot = "";
	/** */
	static boolean _zScore = false;
	/** */
	static boolean _square = false;
	/** */
	static String _color = "Reds"; 
	/** */
	static boolean _gui = false;
	static String[] _colors = new String[] {"Reds", "BuGn", "Greens", "Purples", "Blues", "coolwarm", "magma", "inferno", "spectral", "viridis"};
	
	private static String _doc = ("metaplot Version 0.0.1 run with java 8\n"
			+"Usage:\n\n"
			+"simple <bullseye or classic> <loopsFile> <RawData> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-c COLORSCHEME] \n"
			+"substraction <bullseye or classic> <loopsFile> <RawData1> <RawData2> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-c COLORSCHEME]\n\n"
			+"sMetaPlot: size of the metaplot (default 20 bins)\n"
			+"sImg: size of the image analysed by SIP (default 2000 bins)\n"
			+"-resMax TRUE or FALSE: default true, if false take the samller resolution (only for classic metaplot)\n"
			+"-c COLORSCHEME  matplotlib_colors (Blues, BuGn, Greens, Purples, Reds, coolwarm, magma, inferno, spectral, viridis) default Reds (option for bullseye)\n"
			+"-z znorm each ring in bullseye plot\n"
			+"-s Trim edges to make a square bullseye plot\n"
			+"-min MIN minvalue for color scale\n"
			+"-max Max maxvalue for color scale\n"
			+"-h, --help print help\n");
	
	/** hash map stocking in key the name of the chr and in value the size*/
	private static HashMap<String,Integer> _chrSize =  new HashMap<String,Integer>();
	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		if((args.length >= 1 && args.length <= 6)){
			System.out.println("miss some arguments!!!!!!\n\n"+_doc);
			System.exit(0);
		}else if(args.length > 6){
			_type =args[0];if(_type.equals("-h")){
				System.out.println(_doc);
				System.exit(0);
			}else {
				_plot = args[1];
				_loopsFile = args[2];
				_input = args[3];
				if(_type.equals("simple") && _plot.equals("bullseye") && args[4].contains("bullseye.py")==false){
					System.out.println("error bullseye option need bullseye.py script!!!!!!!\n\n"+_doc);
					System.exit(0);
				}
				if(_type.equals("substraction") && _plot.equals("bullseye") && args[5].contains("bullseye.py")==false){
					System.out.println("error bullseye option need bullseye.py script!!!!!!!\n\n"+_doc);
					System.exit(0);
				}
				if(_type.equals("simple") && _plot.equals("classic") && args[4].contains("heatmap.R")==false){
					System.out.println("error classic option need heatmap.R script!!!!!\n\n"+_doc);
					System.exit(0);
				}
				if(_type.equals("substraction") && _plot.equals("classic") && args[5].contains("heatmap.R")==false){
					System.out.println("error classic option need heatmap.R script!!!!!!!\n\n"+_doc);
					System.exit(0);
				}
				if(_type.matches("simple")){
					readOption(args,7);
					_script = args[4];
					try{_metaSize =Integer.parseInt(args[5]);}
					catch(NumberFormatException e){ returnError("sMetaPlot",args[5],"int");} 
					try{_imageSize =Integer.parseInt(args[6]);}
					catch(NumberFormatException e){ returnError("sImg",args[6],"int");}				
				
				}else if(_type.matches("substraction")){
					_input2 = args[4];
					_script = args[5];
					try{_metaSize =Integer.parseInt(args[6]);}
					catch(NumberFormatException e){ returnError("sMetaPlot",args[6],"int");} 
					try{_imageSize =Integer.parseInt(args[7]);}
					catch(NumberFormatException e){ returnError("sImg",args[7],"int");}
					readOption(args,8);
				}else{
					
					System.out.println("error in classic or substraction choice !!!!\n\n"+_doc);
					System.exit(0);
				}
			
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
				if(gui.isOneData())	_type = "simple";
				else if(gui.isCompare()) _type = "substraction";
				
				if(gui.isManhattan()) _plot ="bullseye";
				else if(gui.isClassic()) _plot ="classic";
				_input2 = gui.getRawDataDir2();
				_min = gui.getMinValue();
				_max = gui.getMaxValue();
				_metaSize = gui.getMatrixSize();
				_imageSize = gui.getSipImageSize();
				_zScore = gui.isZscore();
				_square = gui.isSquareManha();
				_gui = true;
				_color = gui.getColor();
			}
		}
		
		_step = _imageSize/2;
		int nbLine = readLoopFile();
		FileToMatrix ftm = new FileToMatrix();
		String pathFileMatrix = _loopsFile.replace(".txt", "_matrix.tab");
		String output = pathFileMatrix.replace("_matrix.tab", ".pdf");
		if(_plot.equals("bullseye")) output = _loopsFile.replace(".txt", "");
				
		if(_type.matches("simple")){
			makeTif(_input,_minRes,_imageSize);
			ftm = new FileToMatrix(_input, _loopsFile, _resolution, _metaSize);		
			int step = (_imageSize/_ratio)/2;
			ftm.creatMatrix(step, _ratio, _gui,nbLine);
			ftm.getAPA();
			ftm.writeStrengthFile();
		}else if(_type.matches("substraction")){
			makeTif(_input,_minRes,_imageSize);
			makeTif(_input2,_minRes,_imageSize);
			ftm = new FileToMatrix(_input,_input2, _loopsFile, _resolution, _metaSize);
			ftm.creatMatrixSubstarction(_step, _ratio, _gui, nbLine);
			ftm.getAPA();
			ftm.writeStrengthFile();
		}else{
			System.out.println("plopi!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n"+_doc);
			System.exit(0);
		}
		
		if(ftm.isTest() && _plot.matches("classic")){
				if(_min == -1) _min = ftm.getMinMatrix();
				if(_max == -1) _max = ftm.getMaxMatrix();
				Script r = new Script(_script, pathFileMatrix,output,"false",_min,_max); 
				r.runRscript();
				System.out.println(output);
		}else if(ftm.isTest() && _plot.matches("bullseye")){
			Script python = new Script(_script, _color, _zScore, _square, pathFileMatrix, output, _min, _max);  
			python.runPythonScript();
		}else{
			System.out.println(_doc);
			System.exit(0);
		}
	}
	

	/**
	 * 0 chromossome1
	 * 1	x1
	 * 2	x2	
	 * 3	chromosome2
	 * 4	y1
	 * 5	y2
	 * 6	color
	 * 7	APScoreAvg
	 * 8	RegAPScoreAvg
	 * 9	Avg_diffMaxNeihgboor_1
	 * 9	Avg_diffMaxNeihgboor_2
	 * 11	avg
	 * 12	std
	 * 13	value
	 * @param chrSizeFile
	 * @throws IOException
	 */
	private static int readLoopFile() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(_loopsFile));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		double sum = 0;
		int nbLine = 0;
		ArrayList<Double> value =  new ArrayList<Double> (); 
		while (line != null){
			if(nbLine > 0){
				sb.append(line);
				String[] parts = line.split("\\t");				
				String chr = parts[0]; 
				int size = Integer.parseInt(parts[2])-Integer.parseInt(parts[1]);
				if(size > _resolution)
					_resolution = size;
				if(size < _minRes)
					_minRes = size;
				sum +=  Double.parseDouble(parts[13]);
				value.add(Double.parseDouble(parts[13]));
				_chrSize.put(chr, size);
			}
			++nbLine;
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
		_avgValue = sum/(nbLine-1);
		_stdValue = std(value);
		if(_resMax)
			_ratio = _resolution/_minRes;
		else{
			_resolution = _minRes;
			_ratio = _resolution/_minRes;
		}
		return nbLine;
	} 
	
	
	/**
	 * 
	 * @param mean
	 * @param img
	 * @return
	 */
	private static double std(ArrayList<Double> value){
		double semc = 0;
		for(int i = 0; i < value.size(); ++i)
			semc += (value.get(i)-_avgValue)*(value.get(i)-_avgValue);
		
		semc = Math.sqrt(semc/value.size());
		
		return semc;
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
					if(_plot.equals("classic")){
						System.out.println("-z doesn't exist for classic plot!!!!!!!!!\n\n"+_doc);
						System.exit(0);
					}else{
						try{_zScore = true;}
						catch(NumberFormatException e){ returnError(args[i],args[i+1],"int");}
						i--;
					}
				}else if(args[i].equals("-s")){
					if(_plot.equals("classic")){
						System.out.println("-s doesn't exist for classic plot!!!!!\n\n"+_doc);
						System.exit(0);
					}else{
						try{_square = true;}
						catch(NumberFormatException e){ returnError(args[i],args[i+1],"int");}
						i--;
					}
				}else if(args[i].equals("-c")){
					if(_plot.equals("classic")){
						System.out.println("-c doesn't exist for classic plot!!!!!!\n\n"+_doc);
						System.exit(0);
					}else{					
						if(_colors.equals(args[i+1]) == false)
							_color = args[i+1];
						else{
							System.out.println("color choose doesn't exist !!!!!!!\n\n"+_doc);
							System.exit(0);
						}
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
	 * 
	 * @param imgDir
	 * @param resMin
	 * @param imageSize
	 * @throws IOException
	 */
	public static void makeTif(String imgDir, int resMin, int imageSize) throws IOException{
		File folder = new File(imgDir);
		File[] listOfFolder = folder.listFiles();
		Progress plopi = new Progress();
		if(_gui){
			plopi = new Progress("tif disatnce normalized",listOfFolder.length-1);
			plopi.bar.setValue(0);
		}
		for(int i = 0; i < listOfFolder.length;++i){
			if(!(listOfFolder[i].toString().contains("normVector")) && listOfFolder[i].isDirectory()){
				if(_gui)
					plopi.bar.setValue(i);
				File[] listOfFile = listOfFolder[i].listFiles();
				if(testTiff(listOfFile, resMin) == false){
					for(int j = 0; j < listOfFile.length; ++j){
						if(listOfFile[j].toString().contains(".txt")){
							ProcessTuplesFile ptf = new ProcessTuplesFile(listOfFile[j].toString(), resMin, imageSize);
							ptf.readTupleFile(_resolution);
						}
					}
				}				
			}
		}
		plopi.dispose();
	}
	
	
	
	/**
	 * 
	 * @param listOfFile
	 * @param min
	 * @return
	 */
	public static boolean testTiff(File[] listOfFile, int min){
		boolean tif = false;
		int ratio = _resolution/min;
		for(int j = 0; j < listOfFile.length; ++j){
			if(listOfFile[j].toString().contains("_"+ratio+"_N.tif")){
				tif = true;
				return tif;
			}
		}
		return tif;
	}
}

