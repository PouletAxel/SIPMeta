package multiProcessing;

import java.io.File;
import java.util.ArrayList;
import gui.Progress;

public class ProcessMakeImage {
	static int _nbLance = 0;
	static boolean _continuer;
	static int _indice = 0;
	/** */
	private Progress _p;

	/**
	 * 
	 */
	public ProcessMakeImage(){ }

	/**
	 * 
	 * @param hicFile
	 * @param sip
	 * @param chrSize
	 * @param juiceBoxTools
	 * @param normJuiceBox
	 * @param nbCPU
	 * @throws InterruptedException
	 */
	public void go(String SIPDir,ArrayList<String> chr, int nbCPU, boolean gui, int resolution, int ratio, int imageSize) throws InterruptedException{
		if(gui){
			_p = new Progress("tif disatnce normalized",chr.size()-1);
			_p.bar.setValue(0);
		}
		_nbLance = 0;
		ArrayList<Thread> arrayListImageThread = new ArrayList<Thread>() ;
		for(int i = 0; i < chr.size(); ++i){
			File a = new File(SIPDir+File.separator+chr.get(i));
			File[] listOfFile = a.listFiles();
			if(testTiff(listOfFile,resolution,ratio) == false){
				arrayListImageThread.add(new RunnableMakeImage(	listOfFile, resolution, imageSize));
				arrayListImageThread.get(i).start();	
				while (_continuer == false)
					Thread.sleep(10);
				while (_nbLance > nbCPU)
					Thread.sleep(10);
				}
			if(gui)
				_p.bar.setValue(i);
		}
		for (int i = 0; i < arrayListImageThread.size(); ++i)
			while(arrayListImageThread.get(i).isAlive())
				Thread.sleep(10);
		if(gui)
			_p.dispose();
	}
	

	
	/**
	 * 
	 * @param listOfFile
	 * @param min
	 * @return
	 */
	private boolean testTiff(File[] listOfFile,int resolution, int ratio){
		boolean tif = false;
		for(int j = 0; j < listOfFile.length; ++j){
			if(listOfFile[j].toString().contains("_"+ratio+"_N.tif")){
				tif = true;
				return tif;
			}
		}
		return tif;
	}

}
