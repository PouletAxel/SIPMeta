package plop.multiProcessing;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import plop.gui.Progress;
import plop.utils.DumpData;


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
	 * @param hicFile String path to the hic file
	 * @param outdir	String path where dump the data set
	 * @param chrSize	Hashmap chrname: size
	 * @param juiceBoxTools	String path to juicer tools
	 * @param normJuiceBox	String norm to use
	 * @param nbCPU	int: nc of processors use
	 * @param resolution int resolution of the bin
	 * @param sizeImage int size of the image
	 * @param gui boolean: is main.java.plop.gui or not
	 * @throws InterruptedException exception
	 */
	public void go(String hicFile, String outdir, HashMap<String,Integer> chrSize, String juiceBoxTools, String normJuiceBox,int nbCPU, int resolution, int sizeImage, boolean gui) throws InterruptedException{
		File file = new File(outdir);
		if (!file.exists())
			file.mkdir();
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
			_p = new Progress("Dump data set from "+hicFile,chrSize.size()+1);
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