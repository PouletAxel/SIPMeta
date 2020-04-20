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
	private int _res = 0;
	/**int: image size */
	private int _matrixSize = 0;
	/**file[]: with the txt file used to creat .tif file */
	File[] _listOfFile;
	/** if value > threshold don't use it to make the image*/
	double _threshold = -1;
	
	/**
	 * Constructor 
	 * 
	 * @param listOfFile
	 * @param resMin
	 * @param imageSize
	 * @param threshold
	 */
	public RunnableMakeImage (File[] listOfFile, int resMin, int imageSize, double threshold){
		this._listOfFile = listOfFile;
		this._res = resMin;
		this._matrixSize = imageSize;
		this._threshold = threshold;
	}
	
	/**
	 * 
	 */
	public void run(){
		for(int j = 0; j < _listOfFile.length; ++j){
			if(this._listOfFile[j].toString().contains(".txt")){
				ProcessTuplesFile ptf;
				try {
					ptf = new ProcessTuplesFile(this._listOfFile[j].toString(), this._res, this._matrixSize ,this._threshold);
					ptf.readTupleFile(this._res);
				} catch (IOException e) {e.printStackTrace();}
			}
		}
		System.gc();
	}
}
