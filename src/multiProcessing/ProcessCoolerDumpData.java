package multiProcessing;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import gui.Progress;
import utils.CoolerDumpData;
import utils.CoolerExpected;

/**
 * multi thread class dumping the data via juicertoolsbox.jar 
 *  and make file step by step
 * bed file: start1 start2 obs-expected distanceNormalizedValue
 * 
 * @author axel poulet
 */
public class ProcessCoolerDumpData {
	
	/** progress bar if gui is true*/
	private Progress _p;
	
	/**
	 * 
	 */
	public ProcessCoolerDumpData(){ }
		

	/**
	 * 
	 * @param coolFile
	 * @param output
	 * @param chrSize
	 * @param coolTools
	 * @param cooler
	 * @param nbCPU
	 * @param resolution
	 * @param matrixSize
	 * @param gui
	 * @throws InterruptedException
	 */
	public void go( String coolFile, String output, HashMap<String,Integer> chrSize, String coolTools, String cooler,int nbCPU, int resolution, int matrixSize, boolean gui) throws InterruptedException {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nbCPU);
		Iterator<String> chrName = chrSize.keySet().iterator();
		File outDir = new File(output);
		if (outDir.exists()==false) outDir.mkdir();
			
		CoolerExpected expected = new CoolerExpected(coolTools,coolFile, resolution, matrixSize, nbCPU);
		String nameRes = String.valueOf(resolution);
		nameRes = nameRes.replace("000", "");
		nameRes = nameRes+"kb"; 
		String expectedFile = outDir+File.separator+nameRes+".expected";
		System.out.println("start "+expectedFile);
		expected.dumpExpected(expectedFile);
		System.out.println("!!!!!!! End "+expectedFile);
		
		while(chrName.hasNext()){
			String chr = chrName.next();
			CoolerDumpData dumpData = new CoolerDumpData(cooler, coolFile);
			RunnableDumpDataCooler task =  new RunnableDumpDataCooler(output, chr, chrSize.get(chr), dumpData, resolution, matrixSize,matrixSize/2);
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
		File folder = new File(output);
		File[] listOfFiles = folder.listFiles();
		for(int i = 0; i < listOfFiles.length;++i) {
				String name = listOfFiles[i].toString();
				if(name.contains(".expected"))  listOfFiles[i].delete();
		}
		if(gui)	_p.dispose();
		
	}
}
