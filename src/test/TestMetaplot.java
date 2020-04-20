package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import multiProcessing.ProcessCoolerDumpData;
import multiProcessing.ProcessDumpData;
import utils.SIPMeta;




/**
 * Test loops calling on Hic file
 * 
 * @author Axel Poulet
 *
 */
public class TestMetaplot{
	/**
	 *
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	/*private static String _doc = ("metaplot Version 0.0.1 run with java 8\n"
			+"Usage:\n\n"
			+"simple  <loopsFile> <RawData> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-c COLORSCHEME] \n"
			+"substraction <loopsFile> <RawData1> <RawData2> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-c COLORSCHEME]\n\n"
			+"sMetaPlot: size of the metaplot (default 20 bins)\n"
			+"sImg: size of the image analysed by SIP (default 2000 bins)\n"
			+"-resMax TRUE or FALSE: default true, if false take the samller resolution\n"
			+"-c COLORSCHEME  matplotlib_colors (Blues, BuGn, Greens, Purples, Reds, coolwarm, magma, inferno, spectral, viridis) default Reds\n"
			+"-z znorm each ring\n"
			+"-s Trim edges to make a square\n"
			+"-min MIN minvalue for color scale\n"
			+"-max Max maxvalue for color scale\n"
			+"-h, --help print help\n");*/
	
	static HashMap<String,Integer> _chrSize = new HashMap<String,Integer>();
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String loopsFile =  "/home/plop/Desktop/CoolTest21/5kbLoops.txt"; 
		//String input =  "/home/plop/Desktop/SIP/testCooler/GM12878_4DNFIXP4QG5B.mcool";
		//String input =  "/home/plop/Desktop/SIP/testCooler/4DNFI1UEG1HD.hic";
		String input =  "/home/plop/Desktop/CoolTest21/";
		boolean z = true;		
		boolean squarre = true;
		boolean simple = true;
		String python = "/home/plop/Desktop/SIP/bullseye.py";
		int matrixSize = 2000;
		int res = 5000;
		int sizeMeta = 21;
		double min = 0;
		double max = 20;
		String juicer = "/home/plop/Tools/juicer_tools_1.13.02.jar";
		String cooler = "/home/plop/anaconda3/bin/cooler";
		String cooltools = "/home/plop/anaconda3/bin/cooltools";
		int nbCpu = 3;
		
		String color = "inferno";
		double threshold = -1;
		String outdir =  "/home/plop/Desktop/Juicer21/";
		String prefix = "cool";
		String pathFileMatrix = "";
		String[] tmpPath = loopsFile.split("\\/");
		String output = loopsFile.replaceAll(tmpPath[tmpPath.length-1], prefix);
		System.out.println(output);
		if(tmpPath[tmpPath.length-1].contains(".")){
			String[] tmp = tmpPath[tmpPath.length-1].split("\\.");
			pathFileMatrix = output+"_"+tmp[0]+"_matrix.tab";
			output = output+"_"+tmp[0];
		}else{		
			output = output+"_"+tmpPath[tmpPath.length-1];
			pathFileMatrix = output+"_matrix.tab";
		}
			System.out.println(pathFileMatrix+" "+output);
		
		readChrSizeFile("/home/plop/Desktop/w_hg19.sizes");
		System.out.println("input "+input+"\n"
				+ "loops file "+loopsFile+"\n"
				+ "python "+python+"\n"
				+ "min "+min+"\n"
				+ "max "+max+"\n"
				+ "matrix size "+matrixSize+"\n");
		
		//ProcessCoolerDumpData dumpData = new ProcessCoolerDumpData();
		//go( String coolFile, String output, HashMap<String,Integer> chrSize,int nbCPU, int resolution, int matrixSize, boolean gui)
		//dumpData.go(input, outdir, _chrSize, cooltools,cooler, nbCpu, res, matrixSize,false);
		
		//ProcessDumpData processDumpData = new ProcessDumpData();
		//processDumpData.go(input, outdir, _chrSize, juicer, "KR", nbCpu, res, matrixSize,false);
				
		SIPMeta sip = new SIPMeta(input,loopsFile, true,res,nbCpu,matrixSize,sizeMeta); 
		sip.setInput(outdir);
		sip.setPrefix(prefix);
		sip.run(python,squarre,simple,z,color,min,max,threshold);
	}
	
	/**
	 * Run the input file and stock the info of name chr and their size in hashmap
	 * @param chrSizeFile path chr size file
	 * @throws IOException if file does't exist
	 */
	@SuppressWarnings("unused")
	private static void readChrSizeFile( String chrSizeFile) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(chrSizeFile));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null){
			sb.append(line);
			String[] parts = line.split("\\t");				
			String chr = parts[0]; 
			int size = Integer.parseInt(parts[1]);
			_chrSize.put(chr, size);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
	}

}
