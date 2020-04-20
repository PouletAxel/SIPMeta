package multiProcessing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import utils.CoolerDumpData;
import utils.CoolerExpected;

/**
 * 
 * @author axel poulet
 *
 */
public class RunnableDumpDataCooler extends Thread implements Runnable{
	/**String: path where save the dump data  */
	private String _outdir ="";
	/**String: name of the chr*/
	private String _chrName = "";
	/**int: chr size */
	private int _chrSize = 0;
	/** DumpData object run juicertoolbox.jar*/
	private CoolerDumpData _coolerDumpData;
	/**int: bin resolution*/
	private int _res = 0;
	/**int: image Size */
	private int _matrixSize = 0;
	/**int: size of the step to run a chr */
	private int _step = 0;
	
	
	/**
	 * Constructor, initialize the variables of interest
	 *  
	 * @param outdir
	 * @param chrName
	 * @param chrSize
	 * @param dumpData
	 * @param res
	 * @param matrixSize
	 * @param step
	 */
	public RunnableDumpDataCooler (String outdir, String chrName, int chrSize, CoolerDumpData dumpData,int res, int matrixSize, int step){
		this._outdir = outdir;
		this._chrName = chrName;
		this._chrSize = chrSize;
		this._res = res;
		this._matrixSize = matrixSize;
		this._step = step;
		this._coolerDumpData = dumpData;
	}

	/**
	 * Dump teh data by chr
	 */
	public void run(){
		boolean coolerTools;
		String nameRes = String.valueOf(_res);
		nameRes = nameRes.replace("000", "");
		nameRes = nameRes+"kb"; 
		String expectedFile = this._outdir+File.separator+nameRes+".expected";
		System.out.println(expectedFile);
		CoolerExpected expected = new CoolerExpected(expectedFile, _matrixSize);
		try {
			expected.parseExpectedFile();
			ArrayList<Double> lExpected = expected.getExpected(_chrName);
			_coolerDumpData.setExpected(lExpected);
			String outdir = this._outdir+File.separator+nameRes+File.separator+this._chrName+File.separator;
			File file = new File(outdir);
			if (file.exists()==false) file.mkdirs();
			int j = _matrixSize*_res;
			int step = _step*_res;
			String name = outdir+this._chrName+"_0_"+j+".txt";
			if (file.exists()==false) file.mkdir();
			System.out.println("start dump "+this._chrName+" size "+this._chrSize+" res "+ nameRes);
			if(j > this._chrSize) j = this._chrSize;
			for(int i = 0 ; j-1 <= this._chrSize; i+=step,j+=step){
				int end =j-1;
				String dump = this._chrName+":"+i+"-"+end;
				name = outdir+this._chrName+"_"+i+"_"+end+".txt";
				System.out.println("start dump "+this._chrName+" size "+this._chrSize+" dump "+dump+" res "+ nameRes);
				coolerTools = this._coolerDumpData.dumpObservedMExpected(dump,name,_res);
				if (coolerTools == false){
					System.out.print(dump+" "+"\n"+coolerTools+"\n");
					System.exit(0);
				}		
				if(j+step > this._chrSize && j < this._chrSize){
					j= this._chrSize;
					i+=step;
					dump = this._chrName+":"+i+"-"+j;
					name = outdir+this._chrName+"_"+i+"_"+j+".txt";
					System.out.println("start dump "+this._chrName+" size "+this._chrSize+" dump "+dump+" res "+ nameRes);
					coolerTools = this._coolerDumpData.dumpObservedMExpected(dump,name,_res);
					if (coolerTools == false){
						System.out.print(dump+" "+"\n"+coolerTools+"\n");
						System.exit(0);
					}
				}
			}
		} catch (IOException e) {	e.printStackTrace();}
		System.out.println("##### End dump "+this._chrName+" "+nameRes);
		System.gc();
	}

}
