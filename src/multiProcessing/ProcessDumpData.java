package multiProcessing;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import gui.Progress;
import utils.DumpData;


/**
 * multi thread class dumping the data via juicertoolsbox.jar 
 *  and make file step by step
 * bed file: start1 start2 obs-expected distanceNormalizedValue
 * 
 * @author axel poulet
 *
 */
public class ProcessDumpData{
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
	public ProcessDumpData(){ }

	/**
	 * run the processing on different cpu, if all cpu are running, take break else run a new one.
	 * 
	 * @param hicFile
	 * @param outdir
	 * @param chrSize
	 * @param juiceBoxTools
	 * @param normJuiceBox
	 * @param nbCPU
	 * @param resolution
	 * @param sizeImage
	 * @param gui
	 * @throws InterruptedException
	 */
	public void go(String hicFile, String outdir, HashMap<String,Integer> chrSize, String juiceBoxTools, String normJuiceBox,int nbCPU, int resolution, int sizeImage, boolean gui) throws InterruptedException{
		int nb = 0;
		File file = new File(outdir);
		if (file.exists()==false) file.mkdir();
		if(gui){
			_p = new Progress("Dump data step",chrSize.size());
			_p._bar.setValue(nb);
		}
		_nbLance = 0;
		ArrayList<Thread> arrayListImageThread = new ArrayList<Thread>() ;
		int j = 0; 
		Iterator<String> chrName = chrSize.keySet().iterator();
		while(chrName.hasNext()){
			String chr = chrName.next();
			DumpData dumpData = new DumpData(juiceBoxTools, hicFile, normJuiceBox, resolution);
			int step = sizeImage/2;
			arrayListImageThread.add( new RunnableDumpData(outdir, chr, chrSize.get(chr), dumpData, resolution, sizeImage, step));
			arrayListImageThread.get(j).start();
			
			while (_continuer == false) Thread.sleep(10);
			while (_nbLance > nbCPU) Thread.sleep(10);
			++j;
			if(gui)	_p._bar.setValue(nb);
			nb++;
		}
		for (int i = 0; i < arrayListImageThread.size(); ++i)
			while(arrayListImageThread.get(i).isAlive())
				Thread.sleep(10);
		if(gui) _p.dispose();
	}
}