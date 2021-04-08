package plop.multiProcessing;

import plop.utils.DumpData;

import java.io.File;
import java.io.IOException;

/**
 * Runnable class to dummp the data
 * 
 * @author axel poulet
 *
 */
public class RunnableDumpData extends Thread implements Runnable{

	/**String: path where save the dump data  */
	private String _outdir;
	/**String: name of the chr*/
	private String _chrName;
	/**int: chr size */
	private int _chrSize;
	/** DumpData object run juicertoolbox.jar*/
	private DumpData _dumpData;
	/**int: bin resolution*/
	private int _resolution;
	/**int: image Size */
	private int _imageSize;
	/**int: size of the step to run a chr */
	private int _step;
	
	/**
	 * Constructor
	 * 
	 * @param outdir String: path where save the dump data
	 * @param chrName String: name of the chr
	 * @param chrSize int: chr size
	 * @param dumpData  DumpData object run juicertoolbox.jar
	 * @param res int: bin resolution
	 * @param matrixSize int: image Size
	 * @param step int: size of the step to run a chr
	 */
	
	public RunnableDumpData (String outdir, String chrName, int chrSize, DumpData dumpData,int res, int matrixSize, int step){
		this._outdir = outdir;
		this._chrName = chrName;
		this._chrSize = chrSize;
		this._resolution = res;
		this._imageSize = matrixSize;
		this._step = step;
		this._dumpData = dumpData;
	}
	
	/**
	 * create the name file, and call the dumpData function
	 * 
	 */
	public void run(){
		boolean juicerTools;
		String expected ="";
		String nameRes = String.valueOf(_resolution);
		nameRes = nameRes.replace("000", "");
		nameRes = nameRes+"kb"; 
		String outdir = this._outdir+File.separator+nameRes+File.separator+this._chrName+File.separator;
		File file = new File(outdir);
		if (!file.exists())
			file.mkdirs();
		int step = this._step*this._resolution;
		int j = this._imageSize *this._resolution;
		if (j > _chrSize) {j = _chrSize; }
		String test = this._chrName+":0:"+j;
		String name = outdir+this._chrName+"_0_"+j+".txt";
		this._dumpData.getExpected(test,name);
		String normOutput = this._outdir+File.separator+nameRes+File.separator+"normVector";
		file = new File(normOutput);
		if (!file.exists())
			file.mkdir();
		try {
			this._dumpData.getNormVector(this._chrName,normOutput+File.separator+this._chrName+".norm");
			System.out.println("start dump "+this._chrName+" size "+this._chrSize);
			if(j > this._chrSize) j = this._chrSize;
			for(int i = 0 ; j-1 <= this._chrSize; i+=step,j+=step){
				int end =j-1;
				String dump = this._chrName+":"+i+":"+end;
				name = outdir+this._chrName+"_"+i+"_"+end+".txt";
				System.out.println("start dump "+this._chrName+" size "+this._chrSize+" dump "+dump);
				System.out.println(expected);
				juicerTools = this._dumpData.dumpObservedMExpected(dump,name);
				if (!juicerTools){
					System.out.print(dump+" "+"\n"+juicerTools+"\n");
					System.exit(0);
				}
				if(j+step > this._chrSize && j < this._chrSize){
					j= this._chrSize;
					i+=step;
					dump = this._chrName+":"+i+":"+j;
					name = outdir+this._chrName+"_"+i+"_"+j+".txt";
					System.out.println("start dump "+this._chrName+" size "+this._chrSize+" dump "+dump);
					juicerTools = this._dumpData.dumpObservedMExpected(dump,name);
					if (!juicerTools){
						System.out.print(dump+" "+"\n"+juicerTools+"\n");
						System.exit(0);
					}
				}
			}
			System.out.println("##### End dump "+this._chrName);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		System.gc();
	}
}