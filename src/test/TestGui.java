package test;


import java.io.IOException;

import gui.MetaplotGUI;

/**
 * test of the GUI
 * 
 * @author Axel Poulet
 *
 */
public class TestGui {
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException{
		MetaplotGUI gui = new MetaplotGUI();
		while( gui.isShowing()){
			try {Thread.sleep(1);}
			catch (InterruptedException e) {e.printStackTrace();}
	    }	
		if (gui.isStart()){
			System.out.println("test");
			String raw = gui.getRawDataDir();
			String raw2 = gui.getRawDataDir2();
			String loop = gui.getLoopFile();
			String rScript = gui.getScript();
			String plopi ="";
			boolean maxRes = gui.isMaxRes();
			if(gui.isOneData()){	plopi ="simple";}
			else{	plopi = "subtraction";}
			double min = gui.getMinValue();
			double max = gui.getMaxValue();
			String plot = "classic";
			//if(gui.isManhattan()) plot = "bullseye";
			boolean zScore = gui.isZscore();
			boolean square = gui.isSquareManha();
			String color = gui.getColor();
			int metaSize = gui.getMatrixSize();
			int sizeSipImage = gui.getSipImageSize();
			System.out.println("metaplot mode: "+plopi
					+ "\nraw: "+raw
					+ "\nraw2: "+raw2
					+ "\nloop: "+loop
					+ "\nrScript: "+rScript
					+"\nmin: "+ min
					+"\nmax: "+max
					+ "\nsize meta: "+metaSize
					+"\nsizeSipImage: "+sizeSipImage
					+"\nmax res: "+maxRes+"\n"
					+ "color "+color
					+"\nplot "+plot
					+"\ntype "+plopi
					+"\nzScore "+zScore
					+"\nsquare "+square);
		}
		else {
			System.out.println("program NO Name closed: if you want the help: -h");
			System.exit(0);
		}
	}
}