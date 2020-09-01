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
	 * Constructor
	 */
	public ProcessMakeImage(){ }

	/**
	 * 
	 * make the image for the chr directories, if those one are not present.
	 *
	 *
	 * @param SIPDir String path with SIP data set
	 * @param chr  Hashmap chrname: size
	 * @param nbCPU int: nb of processor
	 * @param gui boolean: is gui or not
	 * @param resolution int resolution of the bi
	 * @param imageSize int size of the imag
	 * @param threshold double threshold
	 * @throws InterruptedException exception
	 */
	public void go(String SIPDir,ArrayList<String> chr, int nbCPU, boolean gui, int resolution, int imageSize,double threshold) throws InterruptedException{
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nbCPU);
		for (String s : chr) {
			File a = new File(SIPDir + s);
			File[] listOfFile = a.listFiles();
			if (!testTiff(listOfFile, resolution)) {
				RunnableMakeImage task = new RunnableMakeImage(listOfFile, resolution, imageSize, threshold);
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
	 *
	 * @param listOfFile array of File
	 * @param resolution int resolution
	 * @return boolean
	 */
	private boolean testTiff(File[] listOfFile,int resolution){
		boolean tif = false;
		for (File file : listOfFile) {
			if (file.toString().contains("_N.tif")) {
				tif = true;
				return tif;
			}
		}
		return tif;
	}

}
