package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import multiProcessing.ProcessMakeImage;

/**
 * 
 * @author axel poulet
 *
 */
public class SIPMeta {
	/** */
	private String _loopsFile = "";
	/** */
	private String _input2 ="";
	/** */
	private String _input ="";
	/** */
	private int _imageSize =2000;
	/** */
	private int _step = 0;
	/** */
	private int _metaSize = 21;
	/** */
	private int _resolution = 0;
	/** */
	private int _minRes = 10000000;
	/** */
	private int _ratio = 1;
	/** */
	private boolean _resMax = true;
	/** */
	private boolean _gui = false;
	/** */
	private ArrayList<String> _chr = new ArrayList<String>(); 
	/** */
	private int _nbLine = 0;
	/** */
	private int _cpu = 0;
	
	
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
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	
	public void run(String script, boolean squarre, boolean simple, boolean zscore, String color, double min, double max) throws IOException, InterruptedException{
		String pathFileMatrix = _loopsFile.replace(".txt", "_matrix.tab");
		String output = _loopsFile.replace(".txt", "");
		FileToMatrix ftm = new FileToMatrix();
		System.out.println("Check existing images if not existing the program creating the image");
		makeTif(_input);
		if (simple){
			ftm = new FileToMatrix(_input, _loopsFile, _resolution, _metaSize);
		}else{
			makeTif(_input2);
			ftm = new FileToMatrix(_input,_input2, _loopsFile, _resolution, _metaSize);
		}
		System.out.println("##### End of creating the image\n Start matrix for metaplot");
		ftm.creatMatrix(_step, _ratio, _gui,_nbLine);
		ftm.getAPA();
		ftm.writeStrengthFile();
		System.out.println("##### End of matrix (file: )"+pathFileMatrix+" \n Start python script");
		Script python = new Script(script, color, zscore, squarre, pathFileMatrix, output, min,max);  
		python.runPythonScript();
		System.out.println("End of SIPMeta");
	}

	
	/**
	 * 
	 * @param imgDir
	 * @param resMin
	 * @param imageSize
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void makeTif(String imgDir) throws IOException, InterruptedException{
		ProcessMakeImage process = new ProcessMakeImage();
		process.go(imgDir, _chr,_cpu, _gui, _resolution, _ratio,_imageSize );
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
	private int readLoopFile() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(_loopsFile));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		//double sum = 0;
		int nbLine = 0;
		//ArrayList<Double> value =  new ArrayList<Double> (); 
		while (line != null){
			if(nbLine > 0){
				sb.append(line);
				String[] parts = line.split("\\t");				
				String chr = parts[0]; 
				if (_chr.contains(chr)==false)
					_chr.add(chr);
				
				int size = Integer.parseInt(parts[2])-Integer.parseInt(parts[1]);
				if(size > _resolution)
					_resolution = size;
				if(size < _minRes)
					_minRes = size;
				//sum +=  Double.parseDouble(parts[13]);
				//value.add(Double.parseDouble(parts[13]));
			}
			++nbLine;
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
		if(_resMax)
			_ratio = _resolution/_minRes;
		
		return nbLine;
	} 
		
	
	/**
	 * 
	 * @param mean
	 * @param img
	 * @return
	 */
	/*private double std(ArrayList<Double> value){
		double semc = 0;
		for(int i = 0; i < value.size(); ++i)
			semc += (value.get(i)-_avgValue)*(value.get(i)-_avgValue);
		semc = Math.sqrt(semc/value.size());
		return semc;
	}*/
		
		
	
}
