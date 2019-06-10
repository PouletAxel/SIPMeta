package utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

/**
 * 
 * @author axel poulet
 *
 */
public class ProcessTuplesFile{
	/** */
	private String _file;
	/** */
	private int _step = 0;
	/** */
	private int _matrixSize = 0;
	/** */
	private int _res = 0;
	/** */
	private double _threshold = 0;
	
	/**
	 * 
	 * @param inputFile
	 * @param resMin
	 * @param matrixSize
	 * @throws IOException
	 */
	
	public ProcessTuplesFile(String inputFile, int resMin, int matrixSize, double threshold) throws IOException{
		_file = inputFile;
		_step = matrixSize/2;
		_matrixSize = matrixSize;
		_res = resMin;
		_threshold = threshold;
		System.out.println(_file+"\t"+_step+"\t"+_res+"\t"+_matrixSize);
	}
		
	/**
	 * 
	 * @param resMax
	 */
	public void readTupleFile(int resMax){
		BufferedReader br;
		FloatProcessor p = new FloatProcessor(_matrixSize,_matrixSize);
		String[] tfile = _file.split("_");
		ImagePlus img = new ImagePlus();
		int numImage = Integer.parseInt(tfile[tfile.length-2])/(_step*_res);
		try {
			p.abs();
			br = new BufferedReader(new FileReader(_file));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null){
				sb.append(line);
				String[] parts = line.split("\\t");
				float a = 0;
				if(!(parts[3].equals("NAN")))
					a +=Float.parseFloat(parts[3]);
				int correction = numImage*_step*_res;
				int i = (Integer.parseInt(parts[0]) - correction)/_res; 
				int j = (Integer.parseInt(parts[1]) - correction)/_res;
				
				if(i < _matrixSize && j < _matrixSize){
					if(_threshold > -1){
						if(a < _threshold){
							p.setf(i, j, a);
							p.setf(j, i, a);
						}
					}else if(_threshold == -1){
						p.setf(i, j, a);
						p.setf(j, i, a);
					}
				}
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) { e.printStackTrace();}
			
		img.setProcessor(p);
		int ratio = resMax/_res;
		if (ratio != 1)
			img = changeRes(img,ratio);
		
		FileSaver fileSaver = new FileSaver(img);
		String newName  = "_N.tif";
		if(ratio > 1)	newName = "_"+ratio+"_N.tif";
	    fileSaver.saveAsTiff(_file.replace(".txt", newName));
	}
	
	/**
	 * 
	 * @param img
	 * @param factor
	 * @return
	 */
	public ImagePlus changeRes(ImagePlus img, int factor){
		FloatProcessor p = new FloatProcessor(img.getWidth()/factor,img.getWidth()/factor);
		ImageProcessor ip =  img.getProcessor();
		for(int i = 0; i <= img.getWidth()-1; i+=factor){
			for(int j = 0; j <= img.getWidth()-1; j+=factor){
				float sum = 0;
				for(int ii = i; ii <= i+factor-1; ++ii)
					for(int jj = j ; jj <= j+factor-1; ++jj){
						sum+= ip.getf(ii, jj);
					}
				p.setf(i/factor,j/factor,sum);
			}
		}
		ImagePlus imgResu = new ImagePlus();
		imgResu.setProcessor(p);
		return imgResu;
	}
}