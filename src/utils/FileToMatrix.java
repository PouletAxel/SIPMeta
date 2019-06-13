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
 * Create the metaplot matrix with the tif file
 * 
 * @author axel poulet
 *
 */

public class FileToMatrix {
	/** int: resolution fot the image*/
	private int _resolution = -1;
	/** String: path for image directory */
	private String _imgDir = "";
	/** String: path for image directory2 only for substraction */
	private String _imgDir2 = "";
	/** String: path the loops file*/
	private String _loopsFile = "";
	/** 2D array of flaot to stock metaplot matrix*/
	float[][] _resu;
	/** size of the matrix for the metaplot*/
	private int _metaSize = -1;
	/**boolean:  */
	private boolean _test = false;
	/** double: APA score */
	private double _scoreAPA = 0;
	/** String: stock the loops strength*/
	private String _loopsStrength  = "id\tstrength\tdistance";
	/** min for the metaplot key*/
	private int _min = 1000000000;
	/** max for the metaplot key*/
	private int _max = -100000000;
	/** progress bar if gui is used*/
	private Progress _progress;	
	/** path for the metaplot output*/
	private String _matrixPathFile;
	
	/**
	 * Constructor, initialized the parameters of interest used this of for simple analysis
	 * 
	 * @param imgDir
	 * @param loopsFile
	 * @param res
	 * @param meta
	 */
	public FileToMatrix(String imgDir, String loopsFile, String matrixPathFile, int res, int meta){
		this._resolution = res;
		this._imgDir = imgDir;
		this._loopsFile = loopsFile;
		this._resu = new float[meta][meta];
		this._metaSize = meta ;
		this._matrixPathFile = matrixPathFile;
	}
	
	/**
	 * 
	 */
	public FileToMatrix(){ }

	/**
	 * Constructor, initialized the parameters of interest used this of for substraction analysis 
	 * @param imgDir1
	 * @param imgDir2
	 * @param loopsFile
	 * @param matrixPathFile
	 * @param res
	 * @param meta
	 */
	public FileToMatrix(String imgDir1,String imgDir2, String loopsFile, String matrixPathFile, int res, int meta){
		this._resolution = res;
		this._imgDir = imgDir1;
		this._imgDir2 = imgDir2;
		this._loopsFile = loopsFile;
		this._resu = new float[meta][meta];
		this._metaSize = meta;
		this._matrixPathFile = matrixPathFile;
	}
	
	
	/**
	 * Process the image and compute the final metaplot matrix for simple processing
	 * @param step
	 * @param ratio
	 * @param gui
	 * @param nbloop
	 * @return
	 * @throws IOException
	 */
	public String creatMatrix(int step, int ratio,boolean gui, int nbloop) throws IOException{
		int nbLine = 0; 
		if(gui){
			this._progress = new Progress("loops file processing",nbloop);
			this._progress._bar.setValue(nbLine);
		}
		BufferedReader br = new BufferedReader(new FileReader(this._loopsFile));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		String resu = "";
		
		
		while (line != null){
			sb.append(line);
			String[] parts = line.split("\\t");
			
			if((nbLine == 0 && (!(parts[1].equals("x1")))) || nbLine > 0){
				String chr = parts[0];
				String dir = this._imgDir+File.separator+chr;
				File folder = new File(dir);
				File[] listOfFiles = folder.listFiles();
				for (int i = 0; i < listOfFiles.length; ++i){
					String test = "_N.tif";
					if (ratio > 1) test = ratio+"_N.tif";
					if(listOfFiles[i].toString().contains("tif") && listOfFiles[i].toString().contains(test)){
						String [] tTemp = listOfFiles[i].toString().split("/");
						String coord = tTemp[tTemp.length-1].replaceAll("_"+ratio+"_N.tif", "");
						String [] tcoord = coord.split("_");
						int a = Integer.parseInt(parts[1]);
						int a_end = Integer.parseInt(parts[4]);
						int b;
						int b_end;
						// if chr has composed name
						if(chr.contains("_")){
							String[] testName = chr.split("_");
							b = Integer.parseInt(tcoord[testName.length]);
							b_end = Integer.parseInt(tcoord[testName.length+1]);
						}else{
							b= Integer.parseInt(tcoord[1]);
							b_end = Integer.parseInt(tcoord[2]);
						}
							
						if(a >= b && a_end <= b_end){
							int numImage = b/(step*this._resolution);
							int correction = numImage*step*this._resolution;
							double j = (a - correction)/this._resolution; 
							double k = (a_end - correction)/this._resolution;									
							ImagePlus img = new ImagePlus();
							img = IJ.openImage(listOfFiles[i].toString());
							this._loopsStrength = this._loopsStrength+"\n"+nbLine+"\t"+line+"\t";
							runImage((int)j,(int)k, img);
							nbLine++;
							img.close();
							break;	
						}
					}
				}
			}
			if(gui) this._progress._bar.setValue(nbLine);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
		if(gui) this._progress.dispose();
		this._test = true;
		writeMatrix(nbLine);
		return resu;
	}
	
	/**
	 * Process the image and compute the final metaplot matrix for substraction processing 
	 * @param step
	 * @param ratio
	 * @param gui
	 * @param nbLoops
	 * @return
	 * @throws IOException
	 */
	public String creatMatrixSubstarction(int step, int ratio, boolean gui, int nbLoops) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(this._loopsFile));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		String resu = "";
		int nbLine = 0;
		int prout = 0;  
		if(gui){
			this._progress = new Progress("loops file processing",nbLoops);
			this._progress._bar.setValue(prout);
		}
		while (line != null){
			sb.append(line);
			if(prout > 0){
				String[] parts = line.split("\\t");
				String chr = parts[0];
				String dir = this._imgDir+File.separator+chr;
				String dir2 = this._imgDir2+File.separator+chr;
				File folder = new File(dir);
				File[] listOfFiles = folder.listFiles();
				for (int i = 0; i < listOfFiles.length; ++i){
					if(listOfFiles[i].toString().contains(ratio+"_N.tif")){
						String[] testTable = listOfFiles[i].toString().split("/");
						String fileName2 = dir2+File.separator+testTable[testTable.length-1];
						String [] tTemp = listOfFiles[i].toString().split("/");
						String coord = tTemp[tTemp.length-1].replaceAll("_"+ratio+"_N.tif", "");
						String [] tcoord = coord.split("_");
						int a = Integer.parseInt(parts[1]);
						int a_end = Integer.parseInt(parts[4]);
						int b, b_end;
						//if chr has composed name
						if(chr.contains("_")){
							String[] testName = chr.split("_");
							b = Integer.parseInt(tcoord[testName.length]);
							b_end = Integer.parseInt(tcoord[testName.length+1]);
						}else{
							b= Integer.parseInt(tcoord[1]);
							b_end = Integer.parseInt(tcoord[2]);
						}
						
						if(a >= b && a_end <= b_end){
							int numImage = b/(step*this._resolution);
							int correction = numImage*step*this._resolution;
							double j = (a - correction)/this._resolution; 
							double k = (a_end - correction)/this._resolution;									
							ImagePlus img = new ImagePlus();
							ImagePlus img2 = new ImagePlus();
								
							img = IJ.openImage(listOfFiles[i].toString());
							img2 = IJ.openImage(fileName2);
							this._loopsStrength = this._loopsStrength+"\n"+nbLine+"\t"+line+"\t";
							
							runImage((int)j,(int)k, img, img2);
							nbLine++;
							img.close();
							break;	
						}
					}
				}
			}
			prout++;
			if(gui) this._progress._bar.setValue(prout);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
		this._test = true;
		writeMatrix(nbLine);
		if(gui)this._progress.dispose();
		return resu;
	}
	
	/**
	 * 
	 * @param nbLine
	 * @throws IOException
	 */
	private void writeMatrix(int nbLine) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(this._matrixPathFile)));
		for(int x = 0 ; x < this._resu.length; x++){
			String l ="";
			for(int y = 0 ; y < this._resu[x].length; y++){
				float avg = this._resu[x][y]/nbLine;
				this._resu[x][y]=avg;
				if(avg < this._min)		this._min = (int) avg; 
				else if(avg > this._max) this._max = (int)avg; 
				if(y==0) l= avg+"";
				else l= l+"\t"+avg;
			}
			writer.write(l+"\n");
		}
		writer.close();
	}
	
	
	/**
	 * 
	 * @throws IOException
	 */
	
	public void writeStrengthFile( ) throws IOException{
		String pathFile = this._matrixPathFile;
		pathFile = pathFile.replace("_matrix.tab", "_strength.tab");
		System.out.println("strength file: "+pathFile);
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathFile)));
		writer.write(this._loopsStrength+"\n");
		writer.close();
		
	}
	
	/**
	 * Compute the APA score for the set of loops present in loops file, save the reuslt in a file
	 * 
	 * @return
	 * @throws IOException
	 */
	public double getAPA() throws IOException{
		double avg = (process3By3Square(1,1)+process3By3Square(1,this._metaSize-2)+process3By3Square(this._metaSize-2,1)+process3By3Square(this._metaSize-2,this._metaSize-2))/4; 
		double val = this._resu[this._metaSize/2][this._metaSize/2];
		String pathFile = this._matrixPathFile;
		pathFile = pathFile.replace("_matrix.tab", "_APA.tab");
		System.out.println("strength file: "+pathFile);
		this._scoreAPA = val/avg;
		String line = "valueCenter\tcorner avg\tAPA\n"+val+"\t"+avg+"\t"+_scoreAPA;
		System.out.println(line);
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathFile)));
		writer.write(line);
		writer.close();
		return this._scoreAPA;
	}
	
	/**
	 * compute the average a 3*3square 
	 * @param x
	 * @param y
	 * @return
	 */
	private double process3By3Square(int x, int y){
		double sum = 0;
		for(int i = x-1; i <= x+1; ++i){
			for(int j = y-1; j <= y+1; ++j) 
				sum += this._resu[i][j];
		}
		return sum/9;
	}
	
	/**
	 * run the image and stock the sub matrix for the specific loops with a and b coordinate
	 * used for simple analysis
	 * @param a
	 * @param b
	 * @param img
	 */
	private void runImage (int a, int b, ImagePlus img){
		ImageProcessor ip = img.getProcessor();
		int xx = 0;
		int dist =  (b-a)*this._resolution;
		if(a>=0 && b>=0 && a<ip.getWidth() && b<ip.getHeight()){
			this._loopsStrength = this._loopsStrength+ip.getf(a,b)+"\t"+dist;
			for(int x = a-(int)this._metaSize/2; x <= a+(int)this._metaSize/2; x++,xx++){
				int yy = 0;
				for(int y = b-(int)_metaSize/2 ; y <= b+(int)_metaSize/2; y++,yy++){
					if(y >= 0 && x >= 0   && y < ip.getHeight() &&  x<ip.getWidth()){
						float value = ip.getf(x, y);
						this._resu[xx][yy] += value;
					}
				}
			}
		}
	}
	
	
	/**
	 * run the image and stock the sub matrix for the specific loops with a and b coordinate
	 * used for substraction analysis
	 * @param a
	 * @param b
	 * @param img
	 * @param img2
	 */
	private void runImage (int a, int b, ImagePlus img, ImagePlus img2){
		ImageProcessor ip = img.getProcessor();
		ImageProcessor ip2 = img2.getProcessor();
		int xx = 0;
		int dist =  (b-a)*this._resolution;
		if(a>=0 && b>=0 && a<ip.getWidth() && b<ip.getHeight()){
			this._loopsStrength = this._loopsStrength+ip.getf(a,b)+"\t"+dist;
			for(int x = a-(int)this._metaSize/2; x <= a+(int)this._metaSize/2; x++,xx++){
				int yy = 0;
				for(int y = b-(int)this._metaSize/2 ; y <= b+(int)this._metaSize/2; y++,yy++){
					if(y >= 0 && x >= 0   && y < ip.getHeight() &&  x<ip.getWidth()){
						float value = ip.getf(x, y);
						float value2 = ip2.getf(x, y);
						this._resu[xx][yy] += (value-value2);
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
	 * return the min of the matrix
	 * @return
	 */
	public int getMinMatrix(){
		if (this._min == 0)	return this._min;
		else return this._min-1;
	}
	
	/**
	 * return the max of the matrix
	 * @return
	 */
	public int getMaxMatrix(){ return this._max+1;}
}
