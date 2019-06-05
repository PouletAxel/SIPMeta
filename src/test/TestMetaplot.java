package test;

import java.io.IOException;


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
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		//String output= "/home/plop/Bureau/SIPpaper/Droso/SIPresuPlop";
		//output= "/home/plop/Bureau/SIPpaper/chr1/testNewNew";
		
		String input = "/home/plop/Bureau/SIPout3/";
		String loopsFile =  "/home/plop/Bureau/SIPout3/loops.txt"; //"https://hicfiles.s3.amazonaws.com/hiseq/gm12878/in-situ/combined_30.hic"; //";
		boolean z = true;		
		boolean squarre = true;
		boolean simple = true;
		String python = "/home/plop/Bureau/SIPpaper/bullseye.py";
		int matrixSize = 500;
		int sizeMeta = 21;
		double min = 0;
		double max = 20;
		String color = "inferno";
		
		System.out.println("input "+input+"\n"
				+ "loops file "+loopsFile+"\n"
				+ "python "+python+"\n"
				+ "min "+min+"\n"
				+ "max "+max+"\n"
				+ "matrix size "+matrixSize+"\n");
			
		//String script, boolean squarre, boolean simple, boolean zscore, String color, double min, double max
		//String input, String input2, String loopsFile, boolean gui, int res, boolean resMax, int cpu,int imageSize
		SIPMeta sip = new SIPMeta(input,loopsFile,false,true,2,matrixSize,sizeMeta);
		sip.run(python,squarre,simple,z,color,min,max);
	}

}
