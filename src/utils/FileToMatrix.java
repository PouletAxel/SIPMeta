package utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import gui.Progress;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

/**
 * 
 * @author axel poulet
 *
 */

public class FileToMatrix {
	/** */
	private int _resolution = -1;
	/** */
	private String _imgDir = "";
	/** */
	private String _imgDir2 = "";
	/** */
	private String _loopsFile = "";
	/** */
	float[][] _resu;
	/** */
	private int _metaSize = -1;
	/** */
	private boolean _test = false;
	/** */
	private double _scoreAPA = 0;
	/** */
	private String _loopsStrength  = "id\tstrength\tdistance";
	/** */
	private int _min = 1000000000;
	/** */
	private int _max = -100000000;
	/** */
	private Progress _plopi;	
	
	/**
	 * 
	 * @param imgDir
	 * @param loopsFile
	 * @param res
	 * @param meta
	 */
	public FileToMatrix(String imgDir, String loopsFile, int res, int meta){
		_resolution = res;
		_imgDir = imgDir;
		_loopsFile = loopsFile;
		_resu = new float[meta][meta];
		_metaSize = meta  ;
	}
	
	/**
	 * 
	 */
	public FileToMatrix(){ }

	/**
	 * 
	 * @param imgDir
	 * @param loopsFile
	 * @param res
	 * @param meta
	 */
	public FileToMatrix(String imgDir1,String imgDir2, String loopsFile, int res, int meta){
		_resolution = res;
		_imgDir = imgDir1;
		_imgDir2 = imgDir2;
		_loopsFile = loopsFile;
		_resu = new float[meta][meta];
		_metaSize = meta  ;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public String creatMatrix(int step, int ratio,boolean gui, int nbloop) throws IOException{
		int prout = 0; 
		if(gui){
			_plopi = new Progress("loops file processing",nbloop);
			_plopi.bar.setValue(prout);
		}
		BufferedReader br = new BufferedReader(new FileReader(_loopsFile));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		String resu = "";
		int nbLine = 0;
		
		while (line != null){
			sb.append(line);
			if(prout>0){
				String[] parts = line.split("\\t");
					String chr = parts[0];
					String dir = _imgDir+File.separator+chr;
					File folder = new File(dir);
					File[] listOfFiles = folder.listFiles();
					for (int i = 0; i < listOfFiles.length; ++i){
						String test = "_N.tif";
						if (ratio > 1) test = ratio+"_N.tif";
						
						if(listOfFiles[i].toString().contains("tif") && listOfFiles[i].toString().contains(test)){
							String [] plop = listOfFiles[i].toString().split("/");
							String coord = plop[plop.length-1].replaceAll("_"+ratio+"_N.tif", "");
							String [] tcoord = coord.split("_");
							int a = Integer.parseInt(parts[1]);
							int a_end = Integer.parseInt(parts[4]);
							int b;
							int b_end;
							//System.out.println(a+" "+a_end);
							if(chr.contains("_")){
								String[] testName = chr.split("_");
								b = Integer.parseInt(tcoord[testName.length]);
								b_end = Integer.parseInt(tcoord[testName.length+1]);
							}else{
								b= Integer.parseInt(tcoord[1]);
								b_end = Integer.parseInt(tcoord[2]);
							}
							
							if(a >= b && a_end <= b_end){
								int numImage = b/(step*_resolution);
								int correction = numImage*step*_resolution;
								double j = (a - correction)/_resolution; 
								double k = (a_end - correction)/_resolution;									
								ImagePlus img = new ImagePlus();
								img = IJ.openImage(listOfFiles[i].toString());
								_loopsStrength = _loopsStrength+"\n"+nbLine+"\t"+line+"\t";
								runImage((int)j,(int)k, img);
								nbLine++;
								img.close();
								break;	
							}
						}
				}
			}
			prout++;
			if(gui)
				_plopi.bar.setValue(prout);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
		_test = true;
		writeMatrix(nbLine);
		return resu;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public String creatMatrixSubstarction(int step, int ratio, boolean gui, int nbLoops) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(_loopsFile));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		String resu = "";
		int nbLine = 0;
		int prout = 0;  
		if(gui){
			_plopi = new Progress("loops file processing",nbLoops);
			_plopi.bar.setValue(prout);
		}
		while (line != null){
			sb.append(line);
			if(prout > 0){
				String[] parts = line.split("\\t");
				String chr = parts[0];
				String dir = _imgDir+File.separator+chr;
				String dir2 = _imgDir2+File.separator+chr;
				File folder = new File(dir);
				File[] listOfFiles = folder.listFiles();
				for (int i = 0; i < listOfFiles.length; ++i){
					if(listOfFiles[i].toString().contains(ratio+"_N.tif")){
						String[] testTable = listOfFiles[i].toString().split("/");
						String fileName2 = dir2+File.separator+testTable[testTable.length-1];
						String [] plop = listOfFiles[i].toString().split("/");
						String coord = plop[plop.length-1].replaceAll("_"+ratio+"_N.tif", "");
						String [] tcoord = coord.split("_");
						int a = Integer.parseInt(parts[1]);
						int a_end = Integer.parseInt(parts[4]);
						int b, b_end;
						if(chr.contains("_")){
							String[] testName = chr.split("\\t");
							b = Integer.parseInt(tcoord[testName.length]);
							b_end = Integer.parseInt(tcoord[testName.length+1]);
						}else{
							b = Integer.parseInt(tcoord[1]);
							b_end = Integer.parseInt(tcoord[2]);
						}
						
						if(a >= b && a_end <= b_end){
							int numImage = b/(step*_resolution);
							int correction = numImage*step*_resolution;
							double j = (a - correction)/_resolution; 
							double k = (a_end - correction)/_resolution;									
							ImagePlus img = new ImagePlus();
							ImagePlus img2 = new ImagePlus();
								
							img = IJ.openImage(listOfFiles[i].toString());
							img2 = IJ.openImage(fileName2);
							_loopsStrength = _loopsStrength+"\n"+nbLine+"\t"+line+"\t";
							
							runImage((int)j,(int)k, img, img2);
							nbLine++;
							img.close();
							break;	
						}
					}
				}
			}
			prout++;
			if(gui)
				_plopi.bar.setValue(prout);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
		_test = true;
		writeMatrix(nbLine);
		return resu;
	}
	
	
	private void writeMatrix(int nbLine) throws IOException{
		String pathFile = _loopsFile;
		pathFile = pathFile.replace(".txt", "_matrix.tab");
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathFile)));
		for(int x = 0 ; x < _resu.length; x++){
			String l ="";
			for(int y = 0 ; y < _resu[x].length; y++){
				float avg = _resu[x][y]/nbLine;
				_resu[x][y]=avg;
				if(avg < this._min)
					this._min = (int) avg; 
				else if(avg > this._max)
					this._max = (int)avg; 
				if(y==0)
					l= avg+"";
				else 
					l= l+"\t"+avg;
			}
			writer.write(l+"\n");
		}
		writer.close();
	}
	
	
	/**
	 * 
	 * @param nbLine
	 * @throws IOException
	 */
	public void writeStrengthFile( ) throws IOException{
		String pathFile = _loopsFile;
		pathFile = pathFile.replace(".txt", "_strength.tab");
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathFile)));
		writer.write(this._loopsStrength+"\n");
		writer.close();
		
	}
	public double getAPA() throws IOException{
		double avg = (process3By3Square(1,1)+process3By3Square(1,_metaSize-2)+process3By3Square(_metaSize-2,1)+process3By3Square(_metaSize-2,_metaSize-2))/4; 
		double val = _resu[_metaSize/2][_metaSize/2];
		String pathFile = _loopsFile;
		pathFile = pathFile.replace(".txt", "_APA.tab");
		_scoreAPA = val/avg;
		String line = "valueCenter\tcorner avg\tAPA\n"+val+"\t"+avg+"\t"+_scoreAPA;
		System.out.println(line);
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathFile)));
		writer.write(line);
		writer.close();
		return _scoreAPA;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private double process3By3Square(int x, int y){
		double sum = 0;
		for(int i = x-1; i <= x+1; ++i){
			for(int j = y-1; j <= y+1; ++j){
				sum += _resu[i][j];
			}
		}
		return sum/9;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @param img
	 */
	private void runImage (int a, int b, ImagePlus img){
		ImageProcessor ip = img.getProcessor();
		int xx = 0;
		int dist =  (b-a)*this._resolution;
		if(a>=0 && b>=0 && a<ip.getWidth() && b<ip.getHeight()){
			_loopsStrength = _loopsStrength+ip.getf(a,b)+"\t"+dist;
			for(int x = a-(int)_metaSize/2; x <= a+(int)_metaSize/2; x++,xx++){
				int yy = 0;
				for(int y = b-(int)_metaSize/2 ; y <= b+(int)_metaSize/2; y++,yy++){
					if(y >= 0 && x >= 0   && y < ip.getHeight() &&  x<ip.getWidth()){
						float value = ip.getf(x, y);
						_resu[xx][yy] += value;
					}
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @param img
	 */
	private void runImage (int a, int b, ImagePlus img, ImagePlus img2){
		ImageProcessor ip = img.getProcessor();
		ImageProcessor ip2 = img2.getProcessor();
		int xx = 0;
		int dist =  (b-a)*this._resolution;
		if(a>=0 && b>=0 && a<ip.getWidth() && b<ip.getHeight()){
			_loopsStrength = _loopsStrength+ip.getf(a,b)+"\t"+dist;
			for(int x = a-(int)_metaSize/2; x <= a+(int)_metaSize/2; x++,xx++){
				int yy = 0;
				for(int y = b-(int)_metaSize/2 ; y <= b+(int)_metaSize/2; y++,yy++){
					if(y >= 0 && x >= 0   && y < ip.getHeight() &&  x<ip.getWidth()){
						float value = ip.getf(x, y);
						float value2 = ip2.getf(x, y);
						_resu[xx][yy] += (value-value2);
					}
				}
			}
		}
	}
	/**
	 * 
	 * @return
	 */
	public boolean isTest(){return this._test;}
	
	/**
	 * 
	 * @return
	 */
	public int getMinMatrix(){
		if (this._min == 0)
			return this._min;
		else
			return this._min-1;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxMatrix(){ return this._max+1;}
	
	
	
}
