package test;
import java.io.IOException;
import utils.Script;



public class TestPython {
	public static void main(String[] args) throws IOException, InterruptedException {
		String loopsFile = "/home/plop/Bureau/SIPpaper/SIPTest/loops_matrix.tab";
		String output =  "/home/plop/Bureau/SIPpaper/SIPTest/test";
		String script = "/home/plop/Bureau/bullseye.py";
		Script python = new Script(script, "Blues",false, loopsFile, output);  
		python.runPythonScript();
		System.out.println(output);
	}

}
