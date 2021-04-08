package plop.utils;

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
	private int _step ;
	/** image size*/
	private int _imageSize;
	/** image resolution*/
	private int _resolution;
	/** threshold, if value superior to threshold remove the value of the image*/
	private double _threshold;
	
	/**
	 * Constructor
	 * 
	 * @param inputFile String
	 * @param resMin int
	 * @param matrixSize int image size
	 * @param threshold double threshold removed value > at this threshold
	 * @throws IOException exception
	 */
	public ProcessTuplesFile(String inputFile, int resMin, int matrixSize, double threshold) throws IOException{
		this._file = inputFile;
		this._step = matrixSize/2;
		this._imageSize = matrixSize;
		this._resolution = resMin;
		this._threshold = threshold;
		System.out.println(_file+"\t"+_step+"\t"+ _resolution +"\t"+ _imageSize);
	}
		
	/**
	 * read the tuple file and make the tif file
	 * 
	 * @param resMax resolution
	 */
	public void readTupleFile(int resMax){
		BufferedReader br;
		FloatProcessor p = new FloatProcessor(this._imageSize,this._imageSize);
		String[] tfile = this._file.split("_");
		ImagePlus img = new ImagePlus();
		int numImage = Integer.parseInt(tfile[tfile.length-2])/(this._step*this._resolution);
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
				int correction = numImage*this._step*this._resolution;
				int i = (Integer.parseInt(parts[0]) - correction)/this._resolution;
				int j = (Integer.parseInt(parts[1]) - correction)/this._resolution;
				if(i < this._imageSize && j < this._imageSize){
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
		int ratio = resMax/this._resolution;
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
	 * @return ImagePLus
	 */
	private ImagePlus changeRes(ImagePlus img, int factor){
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