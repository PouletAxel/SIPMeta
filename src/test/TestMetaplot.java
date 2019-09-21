package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;




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
		
		String loopsFile =  "/home/plop/Bureau/SIPpaper/Droso/SIPresuPlop/loops"; //"https://hicfiles.s3.amazonaws.com/hiseq/gm12878/in-situ/combined_30.hic"; //";
		//String output= "/home/plop/Bureau/SIPpaper/Droso/SIPresuPlop";
		//output= "/home/plop/Bureau/SIPpaper/chr1/testNewNew";
		
		/*String input =  "/home/plop/Bureau/SIPpaper/Droso/HiC_rep12.hic";
		String input2 =  "/home/plop/Bureau/SIPpaper/Droso/HiC_rep12.hic";
		
		//String output = "/home/plop/Bureau/plopi/";
		boolean z = true;		
		boolean squarre = true;
		
		boolean simple = false;
		
		String python = "/home/plop/Bureau/SIPpaper/bullseye.py";
		int matrixSize = 2000;
		int sizeMeta = 21;
		double min = 0;
		double max = 20;
		String inputType = "hic";
		String color = "inferno";
		double threshold = -1;
		String outdir =  "/home/plop/Bureau/plop/plopi/";
		String outdir2 =  "/home/plop/Bureau/plop/plopBis/";
		String juiceBoxTools = "/home/plop/Tools/juicer_tools.1.8.9_jcuda.0.8.jar";
		String juiceBoXNormalisation = "KR";
		int cpu = 2;*/
		String prefix = "plopi";
		
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
		
		/*readChrSizeFile("/home/plop/Bureau/SIPpaper/Droso/armsizes.txt");
		System.out.println("input "+input+"\n"
				+ "loops file "+loopsFile+"\n"
				+ "python "+python+"\n"
				+ "min "+min+"\n"
				+ "max "+max+"\n"
				+ "matrix size "+matrixSize+"\n");
		
		SIPMeta sip = new SIPMeta(input,loopsFile,false,true,2,matrixSize,sizeMeta);
		if(inputType.equals("hic") ){
			System.out.println("start 1");
			ProcessDumpData processDumpData = new ProcessDumpData();
			processDumpData.go(input, outdir, _chrSize, juiceBoxTools, juiceBoXNormalisation, cpu-1, sip.getResolution(), matrixSize,false);
			sip.setInput(outdir);
			System.out.println("#######end 1");
			if(simple==false){
				System.out.println("start 2");
				processDumpData.go(input2, outdir2, _chrSize, juiceBoxTools, juiceBoXNormalisation, cpu-1, sip.getResolution(), matrixSize,false);
				sip = new SIPMeta(outdir,outdir2,loopsFile,false,true,cpu-1,matrixSize,sizeMeta);
				System.out.println("end 2");
			}
		}
		
		//String script, boolean squarre, boolean simple, boolean zscore, String color, double min, double max
		//String input, String input2, String loopsFile, boolean gui, int res, boolean resMax, int cpu,int imageSize
		sip.setPrefix(prefix);
		sip.run(python,squarre,simple,z,color,min,max,threshold);*/
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
