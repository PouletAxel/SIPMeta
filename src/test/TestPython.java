package test;
import java.io.IOException;
import utils.Script;



public class TestPython {
	public static void main(String[] args) throws IOException, InterruptedException {
		String matrix = "/home/plop/Bureau/SIPpaper/SIPTest/loops_matrix.tab";
		String output =  "/home/plop/Bureau/SIPpaper/SIPTest/test";
		String script = "/home/plop/Bureau/bullseye.py";
		//String script, String color,boolean zscore, boolean square, String input, String output, double min, double max
		Script python = new Script(script, "Blues",false, false, matrix, output,2.0,3.0);  
		python.runPythonScript();
		System.out.println(output);
	}

}
