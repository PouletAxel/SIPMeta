package multiProcessing;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
		File file = new File(outdir);
		if (file.exists()==false) file.mkdir();
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nbCPU);
		Iterator<String> chrName = chrSize.keySet().iterator();
		while(chrName.hasNext()){
			String chr = chrName.next();
			DumpData dumpData = new DumpData(juiceBoxTools, hicFile, normJuiceBox, resolution);
			int step = sizeImage/2;
			RunnableDumpData task =  new RunnableDumpData(outdir, chr, chrSize.get(chr), dumpData, resolution, sizeImage, step);
			executor.execute(task);
		}
		
		executor.shutdown();
		int nb = 0;
		
		if(gui){
			_p = new Progress("Loop Detection step",chrSize.size()+1);
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
}