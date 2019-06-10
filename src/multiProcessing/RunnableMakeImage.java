package multiProcessing;

import java.io.File;
import java.io.IOException;
import utils.ProcessTuplesFile;

public class RunnableMakeImage extends Thread implements Runnable{
	private int _res = 0;
	private int _matrixSize = 0;
	File[] _listOfFile;
	double _threshold = -1;
	
	/**
	 * 
	 */
	public RunnableMakeImage (File[] listOfFile, int resMin, int imageSize, double threshold){
		_listOfFile = listOfFile;
		_res = resMin;
		_matrixSize = imageSize;
		_threshold = threshold;
	}
	
	/**
	 * 
	 */
	public void run(){
		ProcessMakeImage._nbLance++;
		ProcessMakeImage._continuer = true;
		for(int j = 0; j < _listOfFile.length; ++j){
					if(_listOfFile[j].toString().contains(".txt")){
						ProcessTuplesFile ptf;
						try {
							ptf = new ProcessTuplesFile(_listOfFile[j].toString(), _res, _matrixSize ,_threshold);
							ptf.readTupleFile(_res);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
		}
		System.gc();
		ProcessMakeImage._nbLance--;
	}
	
}
