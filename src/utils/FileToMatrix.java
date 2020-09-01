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
	private int _resolution;
	/** String: path for image directory */
	private String _imgDir;
	/** String: path for image directory2 only for substraction */
	private String _imgDir2;
	/** String: path the loops file*/
	private String _loopsFile;
	/** 2D array of flaot to stock metaplot matrix*/
	private float[][] _resu;
	/** size of the matrix for the metaplot*/
	private int _metaSize = -1;
	/**boolean:  */
	private boolean _test = false;
	/** String: stock the loops strength*/
	private String _loopsStrength  = "id\tstrength\tdistance";
	/** min for the metaplot key*/
	private int _min;
	/** max for the metaplot key*/
	private int _max;
	/** progress bar if gui is used*/
	private Progress _progress;	
	/** path for the metaplot output*/
	private String _matrixPathFile;
	
	/**
	 * Constructor, initialized the parameters of interest used this of for simple analysis
	 * 
	 * @param imgDir path to the dir
	 * @param loopsFile path to the loop file
	 * @param res resolution of the loops
	 * @param meta size of the metaplot
	 * @param matrixPathFile path to the output matrix

	 */
	public FileToMatrix(String imgDir, String loopsFile, String matrixPathFile, int res, int meta){
		this._resolution = res;
		this._imgDir = imgDir;
		System.out.println(imgDir);
		this._loopsFile = loopsFile;
		this._resu = new float[meta][meta];
		this._metaSize = meta ;
		this._matrixPathFile = matrixPathFile;
		_min = 1000000000;
		_max = -100000000;
		_imgDir2 ="";
	}
	

	/**
	 * Constructor, initialized the parameters of interest used this of for substraction analysis 
	 * @param imgDir1 path to the dir of the first SIP data
	 * @param imgDir2 path to the dir of the second SIP data
	 * @param loopsFile path to the loops file
	 * @param matrixPathFile path to the output matrix
	 * @param res resolution of the loops
	 * @param meta size of the metaplot
	 */
	public FileToMatrix(String imgDir1,String imgDir2, String loopsFile, String matrixPathFile, int res, int meta){
		this._resolution = res;
		this._imgDir = imgDir1;
		this._imgDir2 = imgDir2;
		System.out.println(imgDir1+"\n"+imgDir2);
		this._loopsFile = loopsFile;
		this._resu = new float[meta][meta];
		this._metaSize = meta;
		this._matrixPathFile = matrixPathFile;
		_min = 1000000000;
		_max = -100000000;
	}
	
	
	/**
	 * Process the image and compute the final metaplot matrix for simple processing
	 *
	 * @param step int step between 2 following images
	 * @param gui	boolean is gui
	 * @throws IOException exception
	 */
	public void creatMatrix(int step, boolean gui) throws IOException{
		int nbLine = 0; 
		if(gui){
			this._progress = new Progress("loops file processing",10000);
			this._progress._bar.setValue(nbLine);
		}
		BufferedReader br = new BufferedReader(new FileReader(this._loopsFile));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();

		
		while (line != null){
			sb.append(line);
			String[] parts = line.split("\\t");
			
			if((nbLine == 0 && (!(parts[1].equals("x1")))) || nbLine > 0){
				String chr = parts[0];
				String dir = this._imgDir+File.separator+chr;
				File folder = new File(dir);
				File[] listOfFiles = folder.listFiles();
				for (File listOfFile : listOfFiles) {
					if (listOfFile.toString().contains("tif") && listOfFile.toString().contains("_N.tif")) {
						String[] tTemp = listOfFile.toString().split("/");
						String coord = tTemp[tTemp.length - 1].replaceAll("_N.tif", "");
						String[] tcoord = coord.split("_");
						int a = Integer.parseInt(parts[1]);
						int a_end = Integer.parseInt(parts[4]);
						int b;
						int b_end;
						if (chr.contains("_")) {
							String[] testName = chr.split("_");
							b = Integer.parseInt(tcoord[testName.length]);
							b_end = Integer.parseInt(tcoord[testName.length + 1]);
						} else {
							b = Integer.parseInt(tcoord[1]);
							b_end = Integer.parseInt(tcoord[2]);
						}

						if (a >= b && a_end <= b_end) {
							int numImage = b / (step * this._resolution);
							int correction = numImage * step * this._resolution;
							double j = (a - correction) / this._resolution;
							double k = (a_end - correction) / this._resolution;
							System.out.println(listOfFile.toString());
							ImagePlus img = IJ.openImage(listOfFile.toString());
							this._loopsStrength = this._loopsStrength + "\n" + nbLine + "\t" + line + "\t";
							runImage((int) j, (int) k, img);
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
	}
	
	/**
	 * Process the image and compute the final metaplot matrix for substraction processing 
	 *
	 * @param step int step
	 * @param gui boolean gui
	 * @throws IOException exception
	 */
	public void creatMatrixSubstarction(int step, boolean gui) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(this._loopsFile));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		int nbLine = 0;
		int cmp = 0;  
		if(gui){
			this._progress = new Progress("loops file processing",10000);
			this._progress._bar.setValue(cmp);
		}
		while (line != null){
			sb.append(line);
			if(cmp > 0){
				String[] parts = line.split("\\t");
				String chr = parts[0];
				String dir = this._imgDir+chr;
				String dir2 = this._imgDir2+chr;
				File folder = new File(dir);
				System.out.println(dir);
				File[] listOfFiles = folder.listFiles();
				for (File listOfFile : listOfFiles) {
					if (listOfFile.toString().contains("_N.tif")) {
						String[] testTable = listOfFile.toString().split("/");
						String fileName2 = dir2 + File.separator + testTable[testTable.length - 1];
						String[] tTemp = listOfFile.toString().split("/");
						String coord = tTemp[tTemp.length - 1].replaceAll("_N.tif", "");
						String[] tcoord = coord.split("_");
						int a = Integer.parseInt(parts[1]);
						int a_end = Integer.parseInt(parts[4]);
						int b, b_end;
						//if chr has composed name
						if (chr.contains("_")) {
							String[] testName = chr.split("_");
							b = Integer.parseInt(tcoord[testName.length]);
							b_end = Integer.parseInt(tcoord[testName.length + 1]);
						} else {
							b = Integer.parseInt(tcoord[1]);
							b_end = Integer.parseInt(tcoord[2]);
						}
						if (a >= b && a_end <= b_end) {
							int numImage = b / (step * this._resolution);
							int correction = numImage * step * this._resolution;
							double j = (a - correction) / this._resolution;
							double k = (a_end - correction) / this._resolution;
							ImagePlus img = IJ.openImage(listOfFile.toString());
							ImagePlus img2 = IJ.openImage(fileName2);
							this._loopsStrength = this._loopsStrength + "\n" + nbLine + "\t" + line + "\t";

							runImage((int) j, (int) k, img, img2);
							nbLine++;
							img.close();
							break;
						}
					}
				}
			}
			cmp++;
			if(gui) this._progress._bar.setValue(cmp);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
		this._test = true;
		writeMatrix(nbLine);
		if(gui)this._progress.dispose();
	}
	
	/**
	 * write matrix in a file
	 * @param nbLine int nb of loops in the loops file
	 * @throws IOException exception
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
	 * write strength file in the output
	 * @throws IOException exception
	 */

	void writeStrengthFile() throws IOException{
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
	 * @return double APA score
	 * @throws IOException exception
	 */
	public double getAPA() throws IOException{
		double avg = (process3By3Square(1,1)+process3By3Square(1,this._metaSize-2)+process3By3Square(this._metaSize-2,1)+process3By3Square(this._metaSize-2,this._metaSize-2))/4; 
		double val = this._resu[this._metaSize/2][this._metaSize/2];
		String pathFile = this._matrixPathFile;
		pathFile = pathFile.replace("_matrix.tab", "_APA.tab");
		System.out.println("strength file: "+pathFile);
		double scoreAPA = val / avg;
		String line = "valueCenter\tcorner avg\tAPA\n"+val+"\t"+avg+"\t"+ scoreAPA +"\n";
		System.out.println(line);
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathFile)));
		writer.write(line);
		writer.write("\n");
		writer.close();
		return scoreAPA;
	}
	
	/**
	 * compute the average a 3*3square 
	 * @param x coordinate of center pixel
	 * @param y coordinate of center pixel
	 * @return average of 3 by 3 square
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
	 * @param a int x coordinate of the loop
	 * @param b int y coordinate of the loop
	 * @param img ImagePlus input image
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
	 * used for subtraction analysis
	 * @param a int x coordinate of the loop
	 * @param b int y coordinate of the loop
	 * @param img ImagePlus input image
	 * @param img2 ImagePlus input image 2
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
	

}
