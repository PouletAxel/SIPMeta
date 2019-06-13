package multiProcessing;

import java.io.File;
import java.util.ArrayList;
import gui.Progress;

/**
 * Create the image via imageJ, 
 * 
 * @author axel poulet
 *
 */
public class ProcessMakeImage {
	/**int: number of processus*/
	static int _nbLance = 0;
	/** boolean: if true continue the process else take a break*/
	static boolean _continuer;
	/** */
	static int _indice = 0;
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
	public void go(String SIPDir,ArrayList<String> chr, int nbCPU, boolean gui, int resolution, int ratio, int imageSize,double threshold) throws InterruptedException{
		if(gui){
			_p = new Progress("tif disatnce normalized",chr.size()-1);
			_p._bar.setValue(0);
		}
		_nbLance = 0;
		ArrayList<Thread> arrayListImageThread = new ArrayList<Thread>() ;
		for(int i = 0; i < chr.size(); ++i){
			File a = new File(SIPDir+File.separator+chr.get(i));
			File[] listOfFile = a.listFiles();
			if(testTiff(listOfFile,resolution,ratio) == false){
				arrayListImageThread.add(new RunnableMakeImage(	listOfFile, resolution, imageSize,threshold));
				arrayListImageThread.get(i).start();	
				while (_continuer == false)	Thread.sleep(10);
				while (_nbLance > nbCPU) Thread.sleep(10);
				}
			if(gui)	_p._bar.setValue(i);
		}
		for (int i = 0; i < arrayListImageThread.size(); ++i)
			while(arrayListImageThread.get(i).isAlive())
				Thread.sleep(10);
		if(gui)	_p.dispose();
	}
	

	
	/**
	 * if tif with good name present in the directory return true else return false
	 * @param listOfFile
	 * @param min
	 * @return
	 */
	private boolean testTiff(File[] listOfFile,int resolution, int ratio){
		boolean tif = false;
		for(int j = 0; j < listOfFile.length; ++j){
			if(ratio == 1 && listOfFile[j].toString().contains("_N.tif")){
				tif = true;
				return tif;
			}
			
			if(listOfFile[j].toString().contains("_"+ratio+"_N.tif")){
				tif = true;
				return tif;
			}
		}
		return tif;
	}

}
