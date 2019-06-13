package utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

/**
 * Process dump file data and make images (.tif file)
 * 
 * @author axel poulet
 *
 */
public class ProcessTuplesFile{
	/** String path to the input file*/
	private String _file;
	/** int size of the step */
	private int _step = 0;
	/** image sixe*/
	private int _matrixSize = 0;
	/** image resolution*/
	private int _res = 0;
	/** threshold, if value superior to threshold remove the value of the image*/
	private double _threshold = 0;
	
	/**
	 * Constructor
	 * 
	 * @param inputFile String
	 * @param resMin int
	 * @param matrixSize int image size
	 * @param threshold double threshold removed value > at this threshold
	 * @throws IOException
	 */
	public ProcessTuplesFile(String inputFile, int resMin, int matrixSize, double threshold) throws IOException{
		this._file = inputFile;
		this._step = matrixSize/2;
		this._matrixSize = matrixSize;
		this._res = resMin;
		this._threshold = threshold;
		System.out.println(_file+"\t"+_step+"\t"+_res+"\t"+_matrixSize);
	}
		
	/**
	 * read the tuple file and make the tif file
	 * 
	 * @param resMax resolution
	 */
	public void readTupleFile(int resMax){
		BufferedReader br;
		FloatProcessor p = new FloatProcessor(this._matrixSize,this._matrixSize);
		String[] tfile = this._file.split("_");
		ImagePlus img = new ImagePlus();
		int numImage = Integer.parseInt(tfile[tfile.length-2])/(this._step*this._res);
		try {
			p.abs();
			br = new BufferedReader(new FileReader(this._file));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null){
				sb.append(line);
				String[] parts = line.split("\\t");
				float a = 0;
				if(!(parts[3].equals("NAN")))	a +=Float.parseFloat(parts[3]);
				int correction = numImage*this._step*this._res;
				int i = (Integer.parseInt(parts[0]) - correction)/this._res; 
				int j = (Integer.parseInt(parts[1]) - correction)/this._res;
				if(i < this._matrixSize && j < this._matrixSize){
					if(this._threshold > -1){
						if(a < this._threshold){
							p.setf(i, j, a);
							p.setf(j, i, a);
						}
					}else{
						p.setf(i, j, a);
						p.setf(j, i, a);
					}
				}
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) { e.printStackTrace();}
		img.setProcessor(p);
		int ratio = resMax/this._res;
		if (ratio != 1) 	img = changeRes(img,ratio);
		FileSaver fileSaver = new FileSaver(img);
		String newName  = "_N.tif";
		if(ratio > 1)	newName = "_"+ratio+"_N.tif";
	    fileSaver.saveAsTiff(this._file.replace(".txt", newName));
	}
	
	/**
	 * Change the resolution if needed
	 * 
	 * @param img ImagePlus object
	 * @param factor int define the increase of resolution
	 * @return
	 */
	public ImagePlus changeRes(ImagePlus img, int factor){
		FloatProcessor p = new FloatProcessor(img.getWidth()/factor,img.getWidth()/factor);
		ImageProcessor ip =  img.getProcessor();
		for(int i = 0; i <= img.getWidth()-1; i+=factor){
			for(int j = 0; j <= img.getWidth()-1; j+=factor){
				float sum = 0;
				for(int ii = i; ii <= i+factor-1; ++ii)
					for(int jj = j ; jj <= j+factor-1; ++jj){
						sum+= ip.getf(ii, jj);
					}
				p.setf(i/factor,j/factor,sum);
			}
		}
		ImagePlus imgResu = new ImagePlus();
		imgResu.setProcessor(p);
		return imgResu;
	}
}