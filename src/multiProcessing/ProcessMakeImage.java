package multiProcessing;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import gui.Progress;

/**
 * Create the image via imageJ, 
 * 
 * @author axel poulet
 *
 */
public class ProcessMakeImage {
	/** progress bar if gui is true*/
	private Progress _p;

	/**
	 * 
	 */
	public ProcessMakeImage(){ }

	/**
	 * 
	 * make the image for the chr directories, if those one are not present.
	 * 
	 * @param SIPDir
	 * @param chr
	 * @param nbCPU
	 * @param gui
	 * @param resolution
	 * @param ratio
	 * @param imageSize
	 * @param threshold
	 * @throws InterruptedException
	 */

	public void go(String SIPDir,ArrayList<String> chr, int nbCPU, boolean gui, int resolution, int imageSize,double threshold) throws InterruptedException{
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nbCPU);
		for(int i = 0; i < chr.size(); ++i){
			File a = new File(SIPDir+chr.get(i));
			File[] listOfFile = a.listFiles();
			if(testTiff(listOfFile,resolution) == false){
				RunnableMakeImage task =  new  RunnableMakeImage(listOfFile, resolution, imageSize,threshold);
				executor.execute(task);	
			}
		}
		executor.shutdown();
		int nb = 0;
			
		if(gui){
			_p = new Progress("Loop Detection step",chr.size()+1);
			_p._bar.setValue(nb);
		}
		while (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
			if (nb != executor.getCompletedTaskCount()) {
				nb = (int) executor.getCompletedTaskCount();
				if(gui) _p._bar.setValue(nb);
			}
		}
		if(gui)	_p.dispose();
	}
	

	
	/**
	 * if tif with good name present in the directory return true else return false
	 * @param listOfFile
	 * @param min
	 * @return
	 */
	private boolean testTiff(File[] listOfFile,int resolution){
		boolean tif = false;
		for(int j = 0; j < listOfFile.length; ++j){
			if(listOfFile[j].toString().contains("_N.tif")){
				tif = true;
				return tif;
			}
		}
		return tif;
	}

}
