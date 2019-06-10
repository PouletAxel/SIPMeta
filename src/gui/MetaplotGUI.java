package gui;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;



/**
 * Class to construct graphical interface for the chromocenter analysis pipeline in batch
 * 
 * @author poulet axel
 *
 */

public class MetaplotGUI extends JFrame{
	/** */
	private static final long serialVersionUID = 1L;
	/** */
	private Container _container;
	private ButtonGroup _bGroupInputData = new ButtonGroup();
	/** */
	private JRadioButton _jrSIP = new JRadioButton("SIP files");
	/** */
	private JRadioButton _jrHicFile = new JRadioButton(".hic file");
    /** */
    private JTextField _jtfBoxTools = new JTextField();
    /** */
    private JButton _jbBoxTools = new JButton("JuiceBox Tools");
    /** */
    private JLabel _jlNorm;
    /** */
    private ButtonGroup _bNorm = new ButtonGroup();
    /** */
    private JRadioButton _jrbNone = new JRadioButton("NONE");
    /** */
    private JRadioButton _jrbKR = new JRadioButton("KR");
    /** */
    private JRadioButton _jrbVC = new JRadioButton("VC");
    /** */
    private JRadioButton _jrbVC_sqrt = new JRadioButton("VC SQRT");
	/** */
	private JButton _jbChrSize = new JButton("Chr size file");
	/** */
	private JTextField _jtfChrSize  =  new JTextField();
	/** */
	private JButton _jbRawData = new JButton("Raw data");
	/** */
	private JTextField _jtfRawData = new JTextField();
	
	/** */
	private JButton _jbRawData2 = new JButton("Raw data 2");
	/** */
	private JTextField _jtfOutput = new JTextField();
	/** */
	private JButton _jbOutput = new JButton("output 1st .hic");
	/** */
	private JTextField _jtfOutput2 = new JTextField();
	/** */
	private JButton _jbOutput2 = new JButton("output 2nd .hic");
	/** */
	private JTextField _jtfRawData2 = new JTextField();
	/** */
	private JTextField _jtfLoopsFile = new JTextField();
	/** */
	private JButton _jbLoopsFile = new JButton("loops file");

	/** */
	private JTextField _jtfScript = new JTextField();
	
	/** */
	private JTextField _jtfPrefix = new JTextField();
	/** */
	private JButton _jbScriptFile = new JButton("Script");
	
	private JButton _jbStart = new JButton("Start");
	/** */
	private JButton _jbQuit = new JButton("Quit");
	/** */
	private boolean _start = false;
    /** */
    private JFormattedTextField _cpu = new JFormattedTextField(Number.class);
	/** */
	private ButtonGroup _bGroupMetaplotype = new ButtonGroup();
	/** */
	private JRadioButton _jrSimple = new JRadioButton("Simple");
	/** */
	private JRadioButton _jrSubstraction = new JRadioButton("Substraction");

	/** */
	private ButtonGroup _bGroupMaxRes = new ButtonGroup();
	/** */
	private JRadioButton _jrTrue = new JRadioButton("True");
	/** */
	private JRadioButton _jrFalse = new JRadioButton("False");
	/**
	 *  To help, I created a command line version of the bullseye plot. It takes the matrix.tab output from your metaplotter and creates both bullseye and normal heatmaps. 
	 * We should replace the Rscript with this python script. Note that it has several options that would be nice for users to have.  Here are the options and how 
	 * I think they should be in the gui:
	 * -c in the python script should be an optional text entry in the gui (or dropdown menu if possible) for any colorscheme in matplotlib. If not set in python script it defaults to Red.
	 * -z should be a checkbox in the gui (default is no check). In the python script, the -z option calculates the zscores of each ring in the bullseye.
	 *  We don’t need to worry about the -s option in the bullseye script. 
	 *  -l and -u should be optional integers. If no input it uses the default scale from matplotlib. If set, -l and -u correspond to the color range in the heatmap.
	 *  You  can pass the matrix to -i in the python script.
	 *  -o in the python script is the prefix for output png files. It appends “_bullseye.png” and “_normal.png” to the prefix.
	 *  -s   Trim edges to make a square bullseye plot
	 *  
	 */
	
	/** */
	private JRadioButton _jrZscore = new JRadioButton("Bullseye with Zscore");
	/** */
	private JRadioButton _jrSquare = new JRadioButton("Trim edges to make a square bullseye plot");
	
	
	/** Accent, Accent_r, Blues, Blues_r, BrBG, BrBG_r, BuGn, BuGn_r, BuPu, BuPu_r, CMRmap, CMRmap_r, Dark2, Dark2_r, GnBu, GnBu_r, Greens, Greens_r, Greys, Greys_r, OrRd, 
	 * OrRd_r, Oranges, Oranges_r, PRGn, PRGn_r, Paired, Paired_r, Pastel1, Pastel1_r, Pastel2, Pastel2_r, PiYG, PiYG_r, PuBu, PuBuGn, PuBuGn_r, PuBu_r, PuOr, PuOr_r, PuRd,
	 *  PuRd_r, Purples, Purples_r, RdBu, RdBu_r, RdGy, RdGy_r, RdPu, RdPu_r, RdYlBu, RdYlBu_r, RdYlGn, RdYlGn_r, Reds, Reds_r, Set1, Set1_r, Set2, Set2_r, Set3, Set3_r, 
	 *  Spectral, Spectral_r, Vega10, Vega10_r, Vega20, Vega20_r, Vega20b, Vega20b_r, Vega20c, Vega20c_r, Wistia, Wistia_r, YlGn, YlGnBu, YlGnBu_r, YlGn_r, YlOrBr, YlOrBr_r, 
	 *  YlOrRd, YlOrRd_r, afmhot, afmhot_r, autumn, autumn_r, binary, binary_r, bone, bone_r, brg, brg_r, bwr, bwr_r, cool, cool_r, coolwarm, coolwarm_r, copper, copper_r, 
	 *  cubehelix, cubehelix_r, flag, flag_r, gist_earth, gist_earth_r, gist_gray, gist_gray_r, gist_heat, gist_heat_r, gist_ncar, gist_ncar_r, gist_rainbow, gist_rainbow_r, 
	 *  gist_stern, gist_stern_r, gist_yarg, gist_yarg_r, gnuplot, gnuplot2, gnuplot2_r, gnuplot_r, gray, gray_r, hot, hot_r, hsv, hsv_r, inferno, inferno_r, jet, jet_r, magma,
	 *   magma_r, nipy_spectral, nipy_spectral_r, ocean, ocean_r, pink, pink_r, plasma, plasma_r, prism, prism_r, rainbow, rainbow_r, seismic, seismic_r, spectral, spectral_r,
	 *    spring, spring_r, summer, summer_r, tab10, tab10_r, tab20, tab20_r, tab20b, tab20b_r, tab20c, tab20c_r, terrain, terrain_r, viridis, viridis_r, winter, winter_r
	 */
	
	// define items in a String array:
	String[] colors = new String[] {"Reds", "BuGn", "Greens", "Purples", "Blues", "coolwarm", "magma", "inferno", "spectral", "viridis"};
	
	private JComboBox<String> _comboColor = new JComboBox<String>(colors);
	
	/** */
    private JFormattedTextField _matrixSize =  new JFormattedTextField(Number.class);
	
    /** */
    private JFormattedTextField _sipImageSize =  new JFormattedTextField(Number.class);
    /** */
    private JFormattedTextField _minValue =  new JFormattedTextField(Number.class);
	
    /** */
    private JFormattedTextField _maxValue =  new JFormattedTextField(Number.class);
    /** */
    private JFormattedTextField _threshold =  new JFormattedTextField(Number.class);
    /**
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		MetaplotGUI gui = new MetaplotGUI();
		gui.setLocationRelativeTo(null);
	} 
		
	    
	/**
	 * Architecture of the graphical windows
	 *
	 */
	
	public MetaplotGUI(){
		///////////////////////////////////////////// Global parameter of the JFram and def of the gridBaglayout
		this.setTitle("SIPMeta");
		this.setSize(550, 780);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setBackground(Color.LIGHT_GRAY);
		_container = getContentPane();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.1};
		gridBagLayout.rowHeights = new int[] {17, 260, 124, 7};
		gridBagLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.1};
		gridBagLayout.columnWidths = new int[] {250, 120, 72, 20};
		_container.setLayout (gridBagLayout);
		
				
	   	JLabel label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",1,12));
	   	label.setText("Input file Metaplot choice: SIP output or .hic file.");
	   	_container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0
		 ));   	
		
		this._bGroupInputData.add(this._jrSIP);
	   	_bGroupInputData.add(this._jrHicFile);
	 	
	   	_jrSIP.setFont(new java.awt.Font("arial",2,11));
	   	_jrHicFile.setFont(new java.awt.Font("arial",2,11));
		_container.add(_jrSIP,new GridBagConstraints(
			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE, new Insets(20, 20, 0, 0), 0, 0
		));
		
		_container.add(_jrHicFile,new GridBagConstraints(
			0, 1, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE,new Insets(20, 150, 0, 0), 0, 0
		));
		
		_jrSIP.setSelected(true);
		
	   	
		label = new JLabel();
	   	label.setText("Metaplot choice Simple (one data set) or Substarction (two data sets):");
	   	_container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(40, 10, 0, 0), 0, 0
		 ));   	
		//// Comapre or not 
		
		_bGroupMetaplotype.add(_jrSubstraction);
	 	_bGroupMetaplotype.add(_jrSimple);
	 	
		_jrSimple.setFont(new java.awt.Font("arial",2,11));
		_jrSubstraction.setFont(new java.awt.Font("arial",2,11));
		_container.add(_jrSimple,new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(60, 20, 0, 0), 0, 0
		));
		_container.add(_jrSubstraction,new GridBagConstraints(
				0, 1, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE,new Insets(60, 150, 0, 0), 0, 0
		));
		
		_jrSimple.setSelected(true);
		_jtfRawData2.setEditable(false);
		_jbRawData2.setEnabled(false);
				  
	   	///////////////////// Rawdata and work dir button and text.	   	
	   	   	
		label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",1,12));
	   	label.setText("Input and script choices:");
	   	_container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(90, 10, 0, 0), 0, 0
		 ));
	   	
	   	_jbLoopsFile.setPreferredSize(new java.awt.Dimension(100, 21));
	   	_jbLoopsFile.setFont(new java.awt.Font("arial",2,11));
	   	_container.add ( _jbLoopsFile, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(110, 10, 0, 0), 0, 0
	   	));
	   	
	   	this._jtfLoopsFile.setPreferredSize(new java.awt.Dimension(280, 21));
	   	_jtfLoopsFile.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfLoopsFile, new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(110, 160, 0, 0),0, 0
		));
		
	   	_jbRawData.setPreferredSize(new java.awt.Dimension(100, 21));
	   	_jbRawData.setFont(new java.awt.Font("arial",2,10));
	   	_container.add ( _jbRawData, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(140, 10, 0, 0), 0, 0
	   	));
	   	
	   	_jtfRawData.setPreferredSize(new java.awt.Dimension(280, 21));
		_jtfRawData.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfRawData, new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(140, 160, 0, 0),0, 0
		));
		
	   	_jbRawData2.setPreferredSize(new java.awt.Dimension(100, 21));
	   	_jbRawData2.setFont(new java.awt.Font("arial",2,10));
	   	_container.add ( _jbRawData2, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(170, 10, 0, 0), 0, 0
	   	));
	   	
	   	_jtfRawData2.setPreferredSize(new java.awt.Dimension(280, 21));
		_jtfRawData2.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfRawData2, new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(170, 160, 0, 0),0, 0
		));
	   	label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	label.setText("Script path (path to bullseye plot (bullseye.py))");
	   	_container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
	   			GridBagConstraints.NONE, new Insets(200, 20, 0, 0),0, 0
	   	));
		_jbScriptFile.setPreferredSize(new java.awt.Dimension(100, 21));
	   	_jbScriptFile.setFont(new java.awt.Font("arial",2,11));
	   	_container.add ( _jbScriptFile, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(230, 10, 0, 0), 0, 0
	   	));
	   	
	   	this._jtfScript.setPreferredSize(new java.awt.Dimension(280, 21));
	   	_jtfScript.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfScript, new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(230, 160, 0, 0),0, 0
		));
		
	   	label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	label.setText("Parameters and files for .hic file processing.");
	   	_container.add(label, new GridBagConstraints(
	   			0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
	   			GridBagConstraints.NONE, new Insets(0, 20, 0, 0),0, 0
	   	));
	   	
	   	this._jtfOutput.setEditable(false);
		this._jbOutput.setEnabled(false);
		this._jtfOutput2.setEditable(false);
		this._jbOutput2.setEnabled(false);
	    _jtfBoxTools.setEnabled(false);
	    _jbBoxTools.setEnabled(false);
		_jbChrSize.setEnabled(false);
		_jtfChrSize.setEnabled(false);
		_jrbNone.setEnabled(false);
		_jrbKR.setEnabled(false);
		_jrbVC.setEnabled(false);
		 _jrbVC_sqrt.setEnabled(false);

		_jbOutput.setPreferredSize(new java.awt.Dimension(120, 21));
		_jbOutput.setFont(new java.awt.Font("arial",2,10));
	   	_container.add ( _jbOutput, new GridBagConstraints(
	   			0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(30, 10, 0, 0), 0, 0
	   	));
	   	
	   	_jtfOutput.setPreferredSize(new java.awt.Dimension(280, 21));
	   	_jtfOutput.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfOutput, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(30, 160, 0, 0),0, 0
		));
		
		_jbOutput2.setPreferredSize(new java.awt.Dimension(120, 21));
		_jbOutput2.setFont(new java.awt.Font("arial",2,10));
	   	_container.add (_jbOutput2, new GridBagConstraints(
	   			0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(60, 10, 0, 0), 0, 0
	   	));
	   	
	   	_jtfOutput2.setPreferredSize(new java.awt.Dimension(280, 21));
	   	_jtfOutput2.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfOutput2, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(60, 160, 0, 0),0, 0
		));
	   	
		this._jbChrSize.setPreferredSize(new java.awt.Dimension(120, 21));
		_jbChrSize.setFont(new java.awt.Font("arial",2,10));
	   	_container.add (_jbChrSize, new GridBagConstraints(
	   			0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(90, 10, 0, 0), 0, 0
	   	));
	   	
	   	this._jtfChrSize.setPreferredSize(new java.awt.Dimension(280, 21));
	   	_jtfChrSize.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfChrSize, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(90, 160, 0, 0),0, 0
		));

		this._jbBoxTools.setPreferredSize(new java.awt.Dimension(120, 21));
		_jbBoxTools.setFont(new java.awt.Font("arial",2,10));
	   	_container.add (_jbBoxTools, new GridBagConstraints(
	   			0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(120, 10, 0, 0), 0, 0
	   	));
	   	
	   	this._jtfBoxTools.setPreferredSize(new java.awt.Dimension(280, 21));
	   	_jtfBoxTools.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfBoxTools, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(120, 160, 0, 0),0, 0
		));
		
		_jlNorm = new JLabel();
		_jlNorm.setText("Normalization scheme (prefers KR):");
		_jlNorm.setFont(new java.awt.Font("arial",2,11));
	   	_container.add(_jlNorm, new GridBagConstraints(
	   		0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
	   		GridBagConstraints.NONE, new Insets(153, 20, 0, 0), 0, 0
	   	));
	   	
	   	_bNorm.add(_jrbNone);
	   	_bNorm.add(_jrbKR);
	   	_bNorm.add(_jrbVC);
	   	_bNorm.add(_jrbVC_sqrt);
	   	
	   	_jrbNone.setFont(new java.awt.Font("arial",2,11));
	   	_jrbKR.setFont(new java.awt.Font("arial",2,11));
	   	_jrbVC.setFont(new java.awt.Font("arial",2,11));
	   	_jrbVC_sqrt.setFont(new java.awt.Font("arial",2,11));
		
	   	_container.add(_jrbNone,new GridBagConstraints(
	   		0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE, new Insets(150, 220, 0, 0), 0, 0
		));
		
	   	_container.add(_jrbKR,new GridBagConstraints(
	   		0, 2, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE,new Insets(150, 290, 0, 0), 0, 0
		));
		
	   	_container.add(_jrbVC, new GridBagConstraints(
			0,2, 0, 0, 0.0, 0.0,GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE, new Insets(150, 340, 0, 0), 0, 0
		));
	   	
		_container.add(_jrbVC_sqrt, new GridBagConstraints(
			0,2, 0, 0, 0.0, 0.0,GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE, new Insets(150, 390, 0, 0), 0, 0
		));
	   	///////////////////// Parameters for the metaplot
		/*   sMetaPlot: size of the metaplot (default 20 bins)
		 *   sImg: size of the image analysed by SIP (default 2000 bins)
		 *   -resMax: default true, if false take the samller resolution
		 *   -min: default min value detected in the matrix results
		 *   -max: default max value detected in the matrix results
		 */ 
		label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",1,12));
	   	label.setText("Metaplot parameters:");
	   	_container.add(label, new GridBagConstraints(
	   			0, 3, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(55, 10, 0, 0), 0, 0
		 ));
		
	   	label = new JLabel();
	   	label.setText("metaplot size:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(85, 20, 0, 0), 0, 0
		));
			
		this._matrixSize.setText("21");
		_matrixSize.setPreferredSize(new java.awt.Dimension(60, 21));
		_matrixSize.setFont(new java.awt.Font("arial",2,11));
		_container.add( _matrixSize, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(83, 130, 0, 0), 0, 0
		));
		
		label = new JLabel();
	   	label.setText("SIP image size in bins:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(85, 230, 0, 0), 0, 0
		));
			
		this._sipImageSize.setText("2000");
		_sipImageSize.setPreferredSize(new java.awt.Dimension(60, 21));
		_sipImageSize.setFont(new java.awt.Font("arial",2,11));
		_container.add( _sipImageSize, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(83, 400, 0, 0), 0, 0
		));
	
		
		
		label = new JLabel();
	   	label.setText("Used the resolution max detected in the loops file ?:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(110, 20, 0, 0), 0, 0
		));
		
		_bGroupMaxRes.add(_jrTrue);
	 	_bGroupMaxRes.add(_jrFalse);
	 	
		_jrTrue.setFont(new java.awt.Font("arial",2,11));
		_jrFalse.setFont(new java.awt.Font("arial",2,11));
		_container.add(_jrTrue,new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(107, 320, 0, 0), 0, 0
		));
		_container.add(_jrFalse,new GridBagConstraints(
				0, 3, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE,new Insets(107, 370, 0, 0), 0, 0
		));
		_jrTrue.setSelected(true);
		
	   	label = new JLabel();
	   	label.setText("Heatmap color for Manhattan disatnce option:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(140, 20, 0, 0), 0, 0
		));
		
		_comboColor.setFont(new java.awt.Font("arial",0,10));
		_container.add( _comboColor, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(137, 300, 0, 0), 0, 0
		));
		
	   	label = new JLabel();
	   	label.setText("Min value:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(170, 20, 0, 0), 0, 0
		));
			
		this._minValue.setText("-1");
		_minValue.setPreferredSize(new java.awt.Dimension(30, 21));
		_minValue.setFont(new java.awt.Font("arial",2,11));
		_container.add( _minValue, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(167, 80, 0, 0), 0, 0
		));
		
		label = new JLabel();
	   	label.setText("Max value:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(170, 125, 0, 0), 0, 0
		));
			
		this._maxValue.setText("-1");
		_maxValue.setPreferredSize(new java.awt.Dimension(30, 21));
		_maxValue.setFont(new java.awt.Font("arial",2,11));
		_container.add(_maxValue, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(167, 190, 0, 0), 0, 0
		));
		
		label = new JLabel();
	   	label.setText("Nb CPU:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(170, 235, 0, 0), 0, 0
		));
			
		this._cpu.setText("1");
		_cpu.setPreferredSize(new java.awt.Dimension(30, 21));
		_cpu.setFont(new java.awt.Font("arial",2,11));
		_container.add(_cpu, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(167, 283, 0, 0), 0, 0
		));
		
		label = new JLabel();
	   	label.setText("Threshold:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(170, 320, 0, 0), 0, 0
		));
			
		this._threshold.setText("-1");
		_threshold.setPreferredSize(new java.awt.Dimension(30, 21));
		_threshold.setFont(new java.awt.Font("arial",2,11));
		_container.add(_threshold, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(167, 385, 0, 0), 0, 0
		));
		
		
		_jrZscore.setFont(new java.awt.Font("arial",2,11));
		_container.add(_jrZscore, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(200, 20, 0, 0), 0, 0
		));
		_jrZscore.setSelected(false);
		
		_jrSquare.setFont(new java.awt.Font("arial",2,11));
		_container.add(_jrSquare, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(200, 200, 0, 0), 0, 0
		));
		_jrSquare.setSelected(false);

		
		label = new JLabel();
	   	label.setText("Prefixe for SIPMeta results file");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(230, 20, 0, 0), 0, 0
		));
		this._jtfPrefix.setText("SIPMeta");
		_jtfPrefix.setPreferredSize(new java.awt.Dimension(200, 21));
		_jtfPrefix.setFont(new java.awt.Font("arial",2,11));
		_container.add(_jtfPrefix, new GridBagConstraints(
				0, 3, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(227, 200, 0, 0), 0, 0
		));
		////////////////////////// start and quit button
		_jbStart.setPreferredSize(new java.awt.Dimension(120, 21));
	   	_container.add(_jbStart, new GridBagConstraints(
	   			0, 3, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
	   			GridBagConstraints.NONE, new Insets(260, 140, 0,0), 0, 0
	   	));
	   	
	   	
	   	_jbQuit.setPreferredSize(new java.awt.Dimension(120, 21));
		_container.add(_jbQuit,new GridBagConstraints(
				0, 3, 0, 0,  0.0, 0.0,GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE,new Insets(260, 10, 0, 0), 0, 0
		));
		
		RBHicListener plop = new RBHicListener(this);
		this._jrSIP.addActionListener(plop);
		this._jrHicFile.addActionListener(plop);
		this._jrSimple.addActionListener(plop);
	  	this._jrSubstraction.addActionListener(plop);
		
	  	FileListener rFile = new FileListener(_jtfScript);
		_jbScriptFile.addActionListener(rFile);
		FileListener loopsFile = new FileListener(_jtfLoopsFile);
		_jbLoopsFile.addActionListener(loopsFile);	 
		FileListener chrFile = new FileListener(this._jtfChrSize);
		_jbChrSize.addActionListener(chrFile);
		
		FileListener juiceBox = new FileListener(this._jtfBoxTools);
		_jbBoxTools.addActionListener(juiceBox);
		
		InputDirectoryListener rawDataListener = new InputDirectoryListener(this,_jtfRawData);
	  	_jbRawData.addActionListener(rawDataListener);
	  	InputDirectoryListener rawData2Listener = new InputDirectoryListener(this,_jtfRawData2);
	  	_jbRawData2.addActionListener(rawData2Listener);
	  	
	  	
	  	DataDirectoryListener outputDir1 = new DataDirectoryListener(this,_jtfOutput);
	  	_jbOutput.addActionListener(outputDir1);
	  	DataDirectoryListener outputDir2 = new DataDirectoryListener(this,_jtfOutput2);
	  	_jbOutput2.addActionListener(outputDir2);
		
		QuitListener quitListener = new QuitListener(this);
		_jbQuit.addActionListener(quitListener);
		StartListener startListener = new StartListener(this);
		_jbStart.addActionListener(startListener);	  
		this.setVisible(true);
	}
				
	/**
	 * 
	 * @return
	 */
	public String getRawDataDir(){ return _jtfRawData.getText(); }

	/**
	 * 
	 * @return
	 */
	public String getRawDataDir2(){ return _jtfRawData2.getText(); }

	/**
	 * 
	 * @return
	 */
	public String getOutDir(){ return this._jtfOutput.getText(); }

	/**
	 * 
	 * @return
	 */
	public String getOutDir2(){ return this._jtfOutput2.getText(); }
	
	/**
	 * 
	 * @return
	 */
	public String getChrSizeFile(){ return this._jtfChrSize.getText(); }
	
	/**
	 * 
	 * @return
	 */
	public String getPrefix(){ return this._jtfPrefix.getText(); }
	
	/**
	 * 
	 * @return
	 */
	public String getScript(){ return this._jtfScript.getText(); }
	
	
	/**
	 * 
	 * @return
	 */
	public String getLoopFile(){ return this._jtfLoopsFile.getText(); }
	
	/**
	 * 
	 * @return
	 */
	public boolean isStart(){	return _start;}
	
	/**
	 * 
	 * @return
	 */
	public boolean isOneData(){	return _jrSimple.isSelected(); }
	
	/**
	 * 
	 * @return
	 */
	public boolean isZscore(){	return _jrZscore.isSelected(); }
	
	/**
	 * 
	 * @return
	 */
	public boolean isSquareManha(){	return _jrSquare.isSelected(); }	
	
	/**
	 * 
	 * @return
	 */
	public String getColor(){	return (String) _comboColor.getSelectedItem(); }
	/**
	 * 
	 * @return
	 */
	public boolean isCompare(){ return this._jrSubstraction.isSelected(); }
	
	/**
	 * 
	 * @return
	 */
	public boolean isMaxRes(){ return this._jrTrue.isSelected(); }
	/**
	 * 
	 * @return
	 */
	public double getMinValue(){
		String x = _minValue.getText();
		return Double.parseDouble(x.replaceAll(",", "."));
	}
	
	/**
	 * 
	 * @return
	 */
	public double getMaxValue(){
		String x = _maxValue.getText();
		return Double.parseDouble(x.replaceAll(",", "."));
	}
	
	/**
	 * 
	 * @return
	 */
	public double getThreshold(){
		String x = _threshold.getText();
		return Double.parseDouble(x.replaceAll(",", "."));
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMatrixSize(){
		String x = this._matrixSize.getText();
		return Integer.parseInt(x.replaceAll(",", "."));
	}
	/**
	 * 
	 * @return
	 */
	public int getNbCpu(){
		String x = this._cpu.getText();
		return Integer.parseInt(x.replaceAll(",", "."));
	}

	/**
	 * 
	 * @return
	 */
	public int getSipImageSize(){
		String x = this._sipImageSize.getText();
		return Integer.parseInt(x.replaceAll(",", "."));
	}
	
	/**
	 * 
	 * @return
	 */
	public String getJuiceBox(){ return _jtfBoxTools.getText();}
	/**
	 * 
	 * @return
	 */
	public boolean isVC_SQRT(){	return _jrbVC_sqrt.isSelected();}
	
	/**
	 * 
	 * @return
	 */
	public boolean isVC(){ return _jrbVC.isSelected(); }
	
	/**
	 * 
	 * @return
	 */
	public boolean isNONE(){ return _jrbNone.isSelected(); }
	
	/**
	 * 
	 * @return
	 */
	public boolean isKR(){	return _jrbKR.isSelected();}
	
	/**
	 * 
	 * @return
	 */
	public boolean isSIP(){	return this._jrSIP.isSelected();}


	/********************************************************************************************************************************************
	 * 	Classes listener to interact with the several element of the window
	 */
	/********************************************************************************************************************************************
	/********************************************************************************************************************************************
	/********************************************************************************************************************************************
	/********************************************************************************************************************************************/
				
	/**
	 * 
	 * @author plop
	 *
	 */
	class StartListener implements ActionListener {
		/** */
		MetaplotGUI _gui;
		/**
		 * 
		 * @param gui
		 */
		public  StartListener (MetaplotGUI gui){
			_gui = gui;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent actionEvent){
			if(_jtfRawData.getText().equals("") || _gui._jtfLoopsFile.getText().equals("")|| _gui._jtfScript.getText().equals("")){
				JOptionPane.showMessageDialog(
				null, "Add the path of Raw  data directory and/or loops file and/or script",
				"Error", JOptionPane.ERROR_MESSAGE);	
			}else if(_gui.getScript().contains("bullseye.py") == false){
				JOptionPane.showMessageDialog(
					null, "The script choose for the bullseye metaplot is not the good one. give the path of bullseye.py",
					"Error",JOptionPane.ERROR_MESSAGE);	
			}else if(_gui.isCompare() && _jtfRawData2.getText().equals("")){
				JOptionPane.showMessageDialog(
					null, "Add path of Raw data2 directory",
					"Error", JOptionPane.ERROR_MESSAGE);
			}else if(_gui.getNbCpu() > Runtime.getRuntime().availableProcessors() || _gui.getNbCpu()<=0 ){
				JOptionPane.showMessageDialog(
						null, "The number of CPU chose is superior to the number of computer's CPU",
						"Error", JOptionPane.ERROR_MESSAGE
					);	
			}else if(_gui.isSIP() == false && _gui.isCompare()){
					if( _jtfRawData.getText().contains(".hic") == false || _jtfRawData2.getText().contains(".hic") == false){
						JOptionPane.showMessageDialog(
								null, "Missing hic file",
								"Error", JOptionPane.ERROR_MESSAGE);
					}else if (_jtfBoxTools.getText().equals("") || _jtfChrSize.getText().equals("") || _jtfRawData2.getText().equals("") 
							|| _jtfRawData.getText().equals("") || _jtfOutput.getText().equals("") || _jtfOutput2.getText().equals("")){
						JOptionPane.showMessageDialog(
							null, "Missing some file path",
							"Error", JOptionPane.ERROR_MESSAGE);
					}else{
						_start=true;
						_gui.dispose();
					}
			}else if(_gui.isSIP() == false && _gui.isCompare()==false){
				if( _jtfRawData.getText().contains(".hic") == false){
					JOptionPane.showMessageDialog(
							null, "Missing hic file",
							"Error", JOptionPane.ERROR_MESSAGE);
				}else if (_jtfBoxTools.getText().equals("") || _jtfChrSize.getText().equals("") || _jtfRawData.getText().equals("") || _jtfOutput.getText().equals("")){
					JOptionPane.showMessageDialog(
						null, "Missing some file path",
						"Error", JOptionPane.ERROR_MESSAGE);
				}else{
					_start=true;
					_gui.dispose();
				}
			}else{
				_start=true;
				_gui.dispose();
			}
		}
	}
		
	/**
	 * 
	 * @author plop
	 *
	 */
	class QuitListener implements ActionListener {
		/** */
		MetaplotGUI _gui;	
		/**
		 * 
		 * @param gui
		 */
		public  QuitListener (MetaplotGUI gui){
			_gui = gui;
		}
			
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent actionEvent){
				_gui.dispose();
				System.exit(0);
			}
		}
		
	/**
	 * 
	 * @author plop
	 *
	 */
	class FileListener implements ActionListener
	{
		/** */
		JTextField m_jtf;
		/**
		 * 
		 * @param jtf
		 */
		public FileListener(JTextField jtf){
			m_jtf = jtf;
		}
			
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent actionEvent){
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnValue = jFileChooser.showOpenDialog(getParent());
			if(returnValue == JFileChooser.APPROVE_OPTION){
				@SuppressWarnings("unused")
				String run = jFileChooser.getSelectedFile().getName();
				String chrSize = jFileChooser.getSelectedFile().getAbsolutePath();
				m_jtf.setText(chrSize);
			}
			setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}	
	}
	
	/**
	 * 
	 * @author plop
	 *
	 */
	class RBHicListener implements ActionListener{
		/** */
		MetaplotGUI _gui;
		/**
		 * 
		 * @param gui
		 */
		public  RBHicListener (MetaplotGUI gui){
			_gui = gui;
		}
		
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent actionEvent){
			if (_gui.isSIP()){
				if(_gui.isCompare()){
					_gui._jtfRawData2.setEditable(true);
					_gui._jbRawData2.setEnabled(true);
				}else if(_gui.isOneData()){
					_gui._jtfRawData2.setEditable(false);
					_gui._jbRawData2.setEnabled(false);
				}
			   	_jtfOutput.setEditable(false);
				_jbOutput.setEnabled(false);
				_jtfOutput2.setEditable(false);
				_jbOutput2.setEnabled(false);
			    _jtfBoxTools.setEnabled(false);
			    _jbBoxTools.setEnabled(false);
				_jbChrSize.setEnabled(false);
				_jtfChrSize.setEnabled(false);
				_jrbNone.setEnabled(false);
				_jrbKR.setEnabled(false);
				_jrbVC.setEnabled(false);
				_jrbVC_sqrt.setEnabled(false);
				_jrbKR.setSelected(false);
				_jrbVC.setSelected(false);
				_jrbVC_sqrt.setSelected(false);
				_jrbNone.setSelected(false);

			}else if (_gui.isSIP() == false){
				if(_gui.isCompare()){
					_gui._jtfRawData2.setEditable(true);
					_gui._jbRawData2.setEnabled(true);
					_jtfOutput2.setEditable(true);
					_jbOutput2.setEnabled(true);
				}else if(_gui.isOneData()){
					_gui._jtfRawData2.setEditable(false);
					_gui._jbRawData2.setEnabled(false);
					_jtfOutput2.setEditable(false);
					_jbOutput2.setEnabled(false);
				}
				_jtfOutput.setEditable(true);
				_jbOutput.setEnabled(true);
			    _jtfBoxTools.setEnabled(true);
			    _jbBoxTools.setEnabled(true);
				_jbChrSize.setEnabled(true);
				_jtfChrSize.setEnabled(true);
				_jrbNone.setEnabled(true);
				_jrbKR.setEnabled(true);
				_jrbKR.setSelected(true);
				_jrbVC.setEnabled(true);
				_jrbVC_sqrt.setEnabled(true);
				
			}
		}
	}	
	
	/**
	 * 
	 * @author plop
	 *
	 */
	class DataDirectoryListener implements ActionListener{
		/** */
		MetaplotGUI _gui;
		/** */
		JTextField _jtf;
		/**
		 * 
		 * @param gui
		 * @param jtf
		 */
		public DataDirectoryListener(MetaplotGUI gui,JTextField jtf){
			_gui = gui;
			_jtf = jtf;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent actionEvent ){
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnValue = jFileChooser.showOpenDialog(getParent());
			if(returnValue == JFileChooser.APPROVE_OPTION){
				@SuppressWarnings("unused")
				String run = jFileChooser.getSelectedFile().getName();
				String text= jFileChooser.getSelectedFile().getAbsolutePath();
				_jtf.setText(text);
			 }
			 setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		 }	
	 }
	
	/**
	 * 
	 * @author axel poulet
	 *
	 */
	class InputDirectoryListener implements ActionListener{
		/** */
		MetaplotGUI _gui;
		/** */
		JTextField _jtf;
		/**
		 * 
		 * @param gui
		 * @param jtf
		 */
		public InputDirectoryListener(MetaplotGUI gui,JTextField jtf){
			_gui = gui;
			_jtf = jtf;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent actionEvent ){
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if(_gui.isSIP()== false)
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnValue = jFileChooser.showOpenDialog(getParent());
			if(returnValue == JFileChooser.APPROVE_OPTION){
				@SuppressWarnings("unused")
				String run = jFileChooser.getSelectedFile().getName();
				String text= jFileChooser.getSelectedFile().getAbsolutePath();
				_jtf.setText(text);
			 }
			 setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		 }	
	 }
}

