package multiProcessing;
import java.io.File;
import java.io.IOException;
import utils.ProcessTuplesFile;

/**
 * RunnableMakeImage call the processTupleFile to create the image 
 * 
 * @author axel poulet
 *
 */
public class RunnableMakeImage extends Thread implements Runnable{
	/**int:  bins resolution*/
	private int _resolution;
	/**int: image size */
	private int _imageSize;
	/**file[]: with the txt file used to creat .tif file */
	private File[] _listOfFile;
	/** if value > threshold don't use it to make the image*/
	private double _threshold ;
	
	/**
	 * Constructor 
	 * 
	 * @param listOfFile file[]: with the txt file used to creat .tif file
	 * @param resolution int:  bins resolution
	 * @param imageSize int: image size
	 * @param threshold if value > threshold don't use it to make the image
	 */
	public RunnableMakeImage (File[] listOfFile, int resolution, int imageSize, double threshold){
		this._listOfFile = listOfFile;
		this._resolution = resolution;
		this._imageSize = imageSize;
		this._threshold = threshold;
	}
	
	/**
	 * 
	 */
	public void run(){
		for (File file : _listOfFile) {
			if (file.toString().contains(".txt")) {
				ProcessTuplesFile ptf;
				try {
					ptf = new ProcessTuplesFile(file.toString(), this._resolution, this._imageSize, this._threshold);
					ptf.readTupleFile(this._resolution);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.gc();
	}
}
