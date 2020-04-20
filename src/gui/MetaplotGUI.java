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
 * GUI architecture 
 * 
 * @author poulet axel
 *
 */
public class MetaplotGUI extends JFrame{
	/** */
	private static final long serialVersionUID = 1L;
	/** Container object to architecture the size of the different GUI cell*/
	private Container _container;
	/** ButtonGroup which contain the choice or SIP or hic input*/
	private ButtonGroup _bGroupInputData = new ButtonGroup();
	/** SIP radioButton */
	private JRadioButton _jrSIP = new JRadioButton("SIP files");
	/** Hic file radioButton*/
	private JRadioButton _jrHic = new JRadioButton(".hic file");
	/** mcool file radioButton*/
	private JRadioButton _jrCool = new JRadioButton(".mcool file");
    /** textfiled for the juicerboxtools.jar*/
    private JTextField _jtfBoxTools = new JTextField();
    /** Button to select the juicer jar*/
    private JButton _jbBoxTools = new JButton("Juicer Tools");
    /** textfiled for the juicerboxtools.jar*/
    private JTextField _jtfCooler = new JTextField();
    /** Button to select the juicer jar*/
    private JButton _jbCooler = new JButton("cooler");
    /** textfiled for the juicerboxtools.jar*/
    private JTextField _jtfCoolTools = new JTextField();
    /** Button to select the juicer jar*/
    private JButton _jbCoolTools = new JButton("cooltools");
    /** ButtonGroup which contain the choice of dumpData normalization (NONE,KR, VC or VC_SQRT)*/
    private ButtonGroup _bNorm = new ButtonGroup();
    /** radioButton for NONE normalization*/
    private JRadioButton _jrbNone = new JRadioButton("NONE");
    /** radioButton for KR normalization*/
    private JRadioButton _jrbKR = new JRadioButton("KR");
    /** radioButton for VC normalization*/
    private JRadioButton _jrbVC = new JRadioButton("VC");
    /** radioButton for VC_SQRT normalization*/
    private JRadioButton _jrbVC_sqrt = new JRadioButton("VC SQRT");
	/** */
	private JButton _jbChrSize = new JButton("Chr size file");
	/** textfiled for chr size file */
	private JTextField _jtfChrSize  =  new JTextField();
	/** */
	private JButton _jbRawData = new JButton("SIP data, .hic or .mcool");
	/** textfiled for raw data (SIP or .hic file)*/
	private JTextField _jtfRawData = new JTextField();
	/** */
	private JButton _jbRawData2 = new JButton("2nd SIP data, .hic or .mcool");
	/** textfiled for raw data 2 (SIP or .hic file) when substraction is selected */
	private JTextField _jtfRawData2 = new JTextField();
	/** textfiled for output dumpa data when SIP is selected */
	private JTextField _jtfOutput = new JTextField();
	/** */
	private JButton _jbOutput = new JButton("Folder for dumping ");
	/** textfiled for output2 dumpa data when SIP is selected */
	private JTextField _jtfOutput2 = new JTextField();
	/** */
	private JButton _jbOutput2 = new JButton("2nd Folder for dumping");
	/** textfiled for loops file path */
	private JTextField _jtfLoopsFile = new JTextField();
	/** */
	private JButton _jbLoopsFile = new JButton("loops file");
	/** textfiled for sciprt file*/
	private JTextField _jtfScript = new JTextField();
	/** textfiled for teh prefix output of metaplot */
	private JTextField _jtfPrefix = new JTextField();
	/** */
	private JButton _jbScriptFile = new JButton("bullseye.py");
	/** */
	private JButton _jbStart = new JButton("Start");
	/** */
	private JButton _jbQuit = new JButton("Quit");
	/** */
	private boolean _start = false;
    /** textfiled  number formated stock the number of cpu*/
    private JFormattedTextField _cpu = new JFormattedTextField(Number.class);
    /** ButtonGroup which contain the choice simple or substraction*/
	private ButtonGroup _bGroupMetaplotype = new ButtonGroup();
	/** */
	private JRadioButton _jrSimple = new JRadioButton("Simple");
	/** */
	private JRadioButton _jrSubtraction = new JRadioButton("Subtraction");
	/** */
	private JFormattedTextField _jtfResolution = new JFormattedTextField(Number.class);
	/** */
	private JRadioButton _jrZscore = new JRadioButton("Bullseye with Zscore");
	/** */
	private JRadioButton _jrSquare = new JRadioButton("Trim edges to make a square bullseye plot");
	/**	 String array stock a list of colors */
	String[] colors = new String[] {"Reds", "BuGn", "Greens", "Purples", "Blues", "coolwarm", "magma", "inferno", "spectral", "viridis"};
	/** comboBox object to stock and select the color in the metaplot*/
	private JComboBox<String> _comboColor = new JComboBox<String>(colors);
	/** textfiled  number formated stock the matrix size*/
    private JFormattedTextField _jtfMatrixSize =  new JFormattedTextField(Number.class);
    /** textfiled  number formated stcok the image size of SIP ouput*/
    private JFormattedTextField _jtfSipImageSize =  new JFormattedTextField(Number.class);
    /** textfiled  number formated stock the min value for the metaplot key*/
    private JFormattedTextField _jtfMinValue =  new JFormattedTextField(Number.class);
    /** textfiled  number formated stock the max value for the metaplot key*/
    private JFormattedTextField _jtfMaxValue =  new JFormattedTextField(Number.class);
    /** textfiled  number formated stock the threshold value to filter extrema value during the metaplot matrix processing*/
    private JFormattedTextField _jtfThreshold =  new JFormattedTextField(Number.class);
   
    
    
    /**
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		MetaplotGUI gui = new MetaplotGUI();
		gui.setLocationRelativeTo(null);
	} 
		   
	/**
	 * Architecture of the graphical windows for SIPMetaplot.
	 * 
	 *
	 */
	public MetaplotGUI(){
		///////////////////////////////////////////// Global parameter of the JFram and def of the gridBaglayout
		this.setTitle("SIPMeta");
		this.setSize(550, 790);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setBackground(Color.LIGHT_GRAY);
		this._container = getContentPane();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.1};
		gridBagLayout.rowHeights = new int[] {17, 350, 124, 7};
		gridBagLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.1};
		gridBagLayout.columnWidths = new int[] {250, 120, 72, 20};
		this._container.setLayout (gridBagLayout);
	   	JLabel label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",1,12));
	   	label.setText("Input file format: SIP output or .hic file.");
	   	this._container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0
		 ));   	
		
		this._bGroupInputData.add(this._jrSIP);
		this._bGroupInputData.add(this._jrHic);
		this._bGroupInputData.add(this._jrCool);
	 	
		this._jrSIP.setFont(new java.awt.Font("arial",2,11));
		this._jrHic.setFont(new java.awt.Font("arial",2,11));
		this._jrCool.setFont(new java.awt.Font("arial",2,11));
		this._container.add(this._jrSIP,new GridBagConstraints(
			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE, new Insets(20, 20, 0, 0), 0, 0
		));
		
		this._container.add(this._jrHic,new GridBagConstraints(
			0, 1, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE,new Insets(20, 150, 0, 0), 0, 0
		));
		this._container.add(this._jrCool,new GridBagConstraints(
				0, 1, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE,new Insets(20, 280, 0, 0), 0, 0
			));
		
		this._jrSIP.setSelected(true);
		
	   	
		label = new JLabel();
	   	label.setText("Metaplot choice Simple (one data set) or Subtraction (two data sets):");
	   	this._container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(40, 10, 0, 0), 0, 0
		 ));   	
		//// Comapre or not 
		
	   	this._bGroupMetaplotype.add(this._jrSubtraction);
	   	this._bGroupMetaplotype.add(this._jrSimple);
	 	
	   	this._jrSimple.setFont(new java.awt.Font("arial",2,11));
	   	this._jrSubtraction.setFont(new java.awt.Font("arial",2,11));
	   	this._container.add(_jrSimple,new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(60, 20, 0, 0), 0, 0
		));
	   	this._container.add(this._jrSubtraction,new GridBagConstraints(
				0, 1, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE,new Insets(60, 150, 0, 0), 0, 0
		));
		
	   	this._jrSimple.setSelected(true);
	   	this._jtfRawData2.setEditable(false);
	   	this._jbRawData2.setEnabled(false);
				  
	   	///////////////////// Rawdata and work dir button and text.	   	
		label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",1,12));
	   	label.setText("Input and script choices:");
	   	this._container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(90, 10, 0, 0), 0, 0
		 ));
	   	
	   	this._jbLoopsFile.setPreferredSize(new java.awt.Dimension(170, 21));
	   	this._jbLoopsFile.setFont(new java.awt.Font("arial",2,11));
	   	this._container.add ( this._jbLoopsFile, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(110, 10, 0, 0), 0, 0
	   	));
	   	
	   	this._jtfLoopsFile.setPreferredSize(new java.awt.Dimension(280, 21));
	   	this._jtfLoopsFile.setFont(new java.awt.Font("arial",2,10));
	   	this._container.add(this._jtfLoopsFile, new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(110, 200, 0, 0),0, 0
		));
		
	   	this._jbRawData.setPreferredSize(new java.awt.Dimension(170, 21));
	   	this._jbRawData.setFont(new java.awt.Font("arial",2,10));
	   	this._container.add ( this._jbRawData, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(140, 10, 0, 0), 0, 0
	   	));
	   	
	   	this._jtfRawData.setPreferredSize(new java.awt.Dimension(280, 21));
	   	this._jtfRawData.setFont(new java.awt.Font("arial",2,10));
	   	this._container.add(this._jtfRawData, new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(140, 200, 0, 0),0, 0
		));
		
	   	this._jbRawData2.setPreferredSize(new java.awt.Dimension(170, 21));
	   	this._jbRawData2.setFont(new java.awt.Font("arial",2,10));
	   	this._container.add ( this._jbRawData2, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(170, 10, 0, 0), 0, 0
	   	));
	   	
	   	this._jtfRawData2.setPreferredSize(new java.awt.Dimension(280, 21));
	   	this._jtfRawData2.setFont(new java.awt.Font("arial",2,10));
	   	this._container.add(this._jtfRawData2, new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(200, 160, 0, 0),0, 0
		));
	   	label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	label.setText("Location of downloaded bullseye.py script");
	   	this._container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
	   			GridBagConstraints.NONE, new Insets(200, 20, 0, 0),0, 0
	   	));
	   	this._jbScriptFile.setPreferredSize(new java.awt.Dimension(130, 21));
	   	this._jbScriptFile.setFont(new java.awt.Font("arial",2,11));
	   	this._container.add ( this._jbScriptFile, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(220, 10, 0, 0), 0, 0
	   	));
	   	
	   	this._jtfScript.setPreferredSize(new java.awt.Dimension(280, 21));
	   	this._jtfScript.setFont(new java.awt.Font("arial",2,10));
	   	this._container.add(this._jtfScript, new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(220, 160, 0, 0),0, 0
		));
		
	   	label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	label.setText("Parameters and files for .hic or .mcool file processing.");
	   	this._container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
	   			GridBagConstraints.NONE, new Insets(250, 20, 0, 0),0, 0
	   	));
	   	
	   	this._jtfOutput.setEditable(false);
		this._jbOutput.setEnabled(false);
		this._jtfOutput2.setEditable(false);
		this._jbOutput2.setEnabled(false);
		this._jtfBoxTools.setEditable(false);
		this._jbBoxTools.setEnabled(false);
		this._jbChrSize.setEnabled(false);
		this._jtfChrSize.setEditable(false);
		this._jrbNone.setEnabled(false);
		this._jrbKR.setEnabled(false);
		this._jrbVC.setEnabled(false);
		this._jrbVC_sqrt.setEnabled(false);
		
		this._jtfCoolTools.setEditable(false);
		this._jbCoolTools.setEnabled(false);
		this._jtfCooler.setEditable(false);
		this._jbCooler.setEnabled(false);

		this._jbOutput.setPreferredSize(new java.awt.Dimension(170, 21));
		this._jbOutput.setFont(new java.awt.Font("arial",2,10));
		this._container.add ( this._jbOutput, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(270, 10, 0, 0), 0, 0
	   	));
	   	
		this._jtfOutput.setPreferredSize(new java.awt.Dimension(280, 21));
		this._jtfOutput.setFont(new java.awt.Font("arial",2,10));
		this._container.add(this._jtfOutput, new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(270, 200, 0, 0),0, 0
		));
		
		this._jbOutput2.setPreferredSize(new java.awt.Dimension(170, 21));
		this._jbOutput2.setFont(new java.awt.Font("arial",2,10));
		this._container.add (this._jbOutput2, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(300, 10, 0, 0), 0, 0
	   	));
	   	
		this._jtfOutput2.setPreferredSize(new java.awt.Dimension(280, 21));
		this._jtfOutput2.setFont(new java.awt.Font("arial",2,10));
		this._container.add(this._jtfOutput2, new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(300, 200, 0, 0),0, 0
		));
	   	
		this._jbChrSize.setPreferredSize(new java.awt.Dimension(170, 21));
		this._jbChrSize.setFont(new java.awt.Font("arial",2,10));
		this._container.add (this._jbChrSize, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(330, 10, 0, 0), 0, 0
	   	));
	   	
	   	this._jtfChrSize.setPreferredSize(new java.awt.Dimension(280, 21));
	   	this._jtfChrSize.setFont(new java.awt.Font("arial",2,10));
	   	this._container.add(this._jtfChrSize, new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(330, 200, 0, 0),0, 0
		));
	   	
	   	label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	label.setText("Juicer tool file and paramters.");
	   	this._container.add(label, new GridBagConstraints(
	   			0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
	   			GridBagConstraints.NONE, new Insets(10, 20, 0, 0),0, 0
	   	));
	   	
		this._jbBoxTools.setPreferredSize(new java.awt.Dimension(170, 21));
		this._jbBoxTools.setFont(new java.awt.Font("arial",2,10));
	   	this._container.add (this._jbBoxTools, new GridBagConstraints(
	   			0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(30, 10, 0, 0), 0, 0
	   	));
	   	
	   	this._jtfBoxTools.setPreferredSize(new java.awt.Dimension(280, 21));
	   	this._jtfBoxTools.setFont(new java.awt.Font("arial",2,10));
	   	this._container.add(this._jtfBoxTools, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(30, 200, 0, 0),0, 0
		));
		
		label = new JLabel();
		label.setText("Normalization scheme (prefers KR):");
		label.setFont(new java.awt.Font("arial",2,11));
		this._container.add(label, new GridBagConstraints(
	   		0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
	   		GridBagConstraints.NONE, new Insets(60, 20, 0, 0), 0, 0
	   	));
	   	
		this._bNorm.add(this._jrbNone);
		this._bNorm.add(this._jrbKR);
		this._bNorm.add(this._jrbVC);
		this._bNorm.add(this._jrbVC_sqrt);
	   	
		this._jrbNone.setFont(new java.awt.Font("arial",2,11));
		this._jrbKR.setFont(new java.awt.Font("arial",2,11));
		this._jrbVC.setFont(new java.awt.Font("arial",2,11));
		this._jrbVC_sqrt.setFont(new java.awt.Font("arial",2,11));
		
		this._container.add(this._jrbNone,new GridBagConstraints(
	   		0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE, new Insets(57, 220, 0, 0), 0, 0
		));
		
		this._container.add(this._jrbKR,new GridBagConstraints(
	   		0, 2, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE,new Insets(57, 290, 0, 0), 0, 0
		));
		
		this._container.add(this._jrbVC, new GridBagConstraints(
			0,2, 0, 0, 0.0, 0.0,GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE, new Insets(57, 340, 0, 0), 0, 0
		));
	   	
		this._container.add(this._jrbVC_sqrt, new GridBagConstraints(
			0,2, 0, 0, 0.0, 0.0,GridBagConstraints.NORTHWEST,
			GridBagConstraints.NONE, new Insets(57, 390, 0, 0), 0, 0
		));
		
		label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	label.setText("cooler and cooltools file.");
	   	this._container.add(label, new GridBagConstraints(
	   			0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
	   			GridBagConstraints.NONE, new Insets(80, 20, 0, 0),0, 0
	   	));
		
		this._jbCooler.setPreferredSize(new java.awt.Dimension(170, 21));
		this._jbCooler.setFont(new java.awt.Font("arial",2,10));
	   	this._container.add (this._jbCooler, new GridBagConstraints(
	   			0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(100, 10, 0, 0), 0, 0
	   	));
	   	
	   	this._jtfCooler.setPreferredSize(new java.awt.Dimension(280, 21));
	   	this._jtfCooler.setFont(new java.awt.Font("arial",2,10));
	   	this._container.add(this._jtfCooler, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(100, 200, 0, 0),0, 0
		));


		this._jbCoolTools.setPreferredSize(new java.awt.Dimension(170, 21));
		this._jbCoolTools.setFont(new java.awt.Font("arial",2,10));
	   	this._container.add (this._jbCoolTools, new GridBagConstraints(
	   			0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   			GridBagConstraints.NONE, new Insets(130, 10, 0, 0), 0, 0
	   	));
	   	
	   	this._jtfCoolTools.setPreferredSize(new java.awt.Dimension(280, 21));
	   	this._jtfCoolTools.setFont(new java.awt.Font("arial",2,10));
	   	this._container.add(this._jtfCoolTools, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(130, 200, 0, 0),0, 0
		));
	   	
	   	///////////////////// Parameters for the metaplot

		label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",1,12));
	   	label.setText("Metaplot parameters:");
	   	this._container.add(label, new GridBagConstraints(
	   			0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(160, 10, 0, 0), 0, 0
		 ));
		
	   	label = new JLabel();
	   	label.setText("Metaplot size (odd number):");
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	this._container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(185, 20, 0, 0), 0, 0
		));
			
		this._jtfMatrixSize.setText("21");
		this._jtfMatrixSize.setPreferredSize(new java.awt.Dimension(60, 21));
		this._jtfMatrixSize.setFont(new java.awt.Font("arial",2,11));
		this._container.add( this._jtfMatrixSize, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(183, 180, 0, 0), 0, 0
		));
		
		label = new JLabel();
	   	label.setText("SIP image size in bins:");
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	this._container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(185, 260, 0, 0), 0, 0
		));
			
		this._jtfSipImageSize.setText("2000");
		this._jtfSipImageSize.setPreferredSize(new java.awt.Dimension(60, 21));
		this._jtfSipImageSize.setFont(new java.awt.Font("arial",2,11));
		this._container.add( this._jtfSipImageSize, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(183, 400, 0, 0), 0, 0
		));
	
		
		label = new JLabel();
	   	label.setText("Resolution:");
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	this._container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(215, 20, 0, 0), 0, 0
		));
		
		this._jtfResolution.setText("5000");
		this._jtfResolution.setPreferredSize(new java.awt.Dimension(60, 21));
		this._jtfResolution.setFont(new java.awt.Font("arial",2,11));
		this._container.add( this._jtfResolution, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(213, 180, 0, 0), 0, 0
		));
	 	
	   
		
	   	label = new JLabel();
	   	label.setText("Heatmap color for Manhattan distance option:");
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	this._container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(240, 20, 0, 0), 0, 0
		));
		
	   	this._comboColor.setFont(new java.awt.Font("arial",0,10));
	   	this._container.add( this._comboColor, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(237, 250, 0, 0), 0, 0
		));
		
	   	label = new JLabel();
	   	label.setText("Min value:");
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	this._container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(270, 20, 0, 0), 0, 0
		));
			
		this._jtfMinValue.setText("-1");
		this._jtfMinValue.setPreferredSize(new java.awt.Dimension(30, 21));
		this._jtfMinValue.setFont(new java.awt.Font("arial",2,11));
		this._container.add( this._jtfMinValue, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(267, 80, 0, 0), 0, 0
		));
		
		label = new JLabel();
	   	label.setText("Max value:");
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	this._container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(270, 125, 0, 0), 0, 0
		));
			
		this._jtfMaxValue.setText("-1");
		this._jtfMaxValue.setPreferredSize(new java.awt.Dimension(30, 21));
		this._jtfMaxValue.setFont(new java.awt.Font("arial",2,11));
		this._container.add(this._jtfMaxValue, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(267, 190, 0, 0), 0, 0
		));
		
		label = new JLabel();
	   	label.setText("Nb CPU:");
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	this._container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(270, 235, 0, 0), 0, 0
		));
			
		this._cpu.setText("1");
		this._cpu.setPreferredSize(new java.awt.Dimension(30, 21));
		this._cpu.setFont(new java.awt.Font("arial",2,11));
		this._container.add(this._cpu, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(267, 283, 0, 0), 0, 0
		));
		
		label = new JLabel();
	   	label.setText("Threshold:");
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	this._container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(270, 320, 0, 0), 0, 0
		));
			
		this._jtfThreshold.setText("-1");
		this._jtfThreshold.setPreferredSize(new java.awt.Dimension(30, 21));
		this._jtfThreshold.setFont(new java.awt.Font("arial",2,11));
		this._container.add(this._jtfThreshold, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(267, 385, 0, 0), 0, 0
		));
		
		
		this._jrZscore.setFont(new java.awt.Font("arial",2,11));
		this._container.add(this._jrZscore, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(290, 20, 0, 0), 0, 0
		));
		this._jrZscore.setSelected(false);
		
		this._jrSquare.setFont(new java.awt.Font("arial",2,11));
		this._container.add(this._jrSquare, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(290, 200, 0, 0), 0, 0
		));
		this._jrSquare.setSelected(false);

		
		label = new JLabel();
	   	label.setText("Prefix for SIPMeta results file");
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	this._container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(320, 20, 0, 0), 0, 0
		));
		this._jtfPrefix.setText("SIPMeta");
		this._jtfPrefix.setPreferredSize(new java.awt.Dimension(200, 21));
		this._jtfPrefix.setFont(new java.awt.Font("arial",2,11));
		this._container.add(this._jtfPrefix, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(317, 200, 0, 0), 0, 0
		));
		
		////////////////////////// start and quit button
		this._jbStart.setPreferredSize(new java.awt.Dimension(120, 21));
		this._container.add(this._jbStart, new GridBagConstraints(
	   			0, 2, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
	   			GridBagConstraints.NONE, new Insets(360, 140, 0,0), 0, 0
	   	));
	   	
		this._jbQuit.setPreferredSize(new java.awt.Dimension(120, 21));
		this._container.add(this._jbQuit,new GridBagConstraints(
				0, 2, 0, 0,  0.0, 0.0,GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE,new Insets(360, 10, 0, 0), 0, 0
		));
		
		RBHicListener listener = new RBHicListener(this);
		this._jrSIP.addActionListener(listener);
		this._jrHic.addActionListener(listener);
		this._jrCool.addActionListener(listener);
		this._jrSimple.addActionListener(listener);
	  	this._jrSubtraction.addActionListener(listener);
		
	  	FileListener rFile = new FileListener(this._jtfScript);
	  	this._jbScriptFile.addActionListener(rFile);
		FileListener loopsFile = new FileListener(this._jtfLoopsFile);
		this._jbLoopsFile.addActionListener(loopsFile);	 
		FileListener chrFile = new FileListener(this._jtfChrSize);
		this._jbChrSize.addActionListener(chrFile);
		
		FileListener juiceBox = new FileListener(this._jtfBoxTools);
		this._jbBoxTools.addActionListener(juiceBox);
		FileListener cooler = new FileListener(this._jtfCooler);
		this._jbCooler.addActionListener(cooler);
		FileListener cooltools = new FileListener(this._jtfCoolTools);
		this._jbCoolTools.addActionListener(cooltools);
		
		InputDirectoryListener rawDataListener = new InputDirectoryListener(this,this._jtfRawData);
		this._jbRawData.addActionListener(rawDataListener);
	  	InputDirectoryListener rawData2Listener = new InputDirectoryListener(this,this._jtfRawData2);
	  	this._jbRawData2.addActionListener(rawData2Listener);
	  	
	  	
	  	DataDirectoryListener outputDir1 = new DataDirectoryListener(this,this._jtfOutput);
	  	this._jbOutput.addActionListener(outputDir1);
	  	DataDirectoryListener outputDir2 = new DataDirectoryListener(this,this._jtfOutput2);
	  	this._jbOutput2.addActionListener(outputDir2);
		
		QuitListener quitListener = new QuitListener(this);
		this._jbQuit.addActionListener(quitListener);
		StartListener startListener = new StartListener(this);
		this._jbStart.addActionListener(startListener);	  
		this.setVisible(true);
	}
	
	/**
	 * getter  for the raw hic file or the raw directory (dependent of the hic option)
	 * @return String path to the file or to the directory
	 */
	public String getRawDataDir(){ return this._jtfRawData.getText(); }

	/**
	 * getter  for the second raw hic file or the raw directory (dependent of the hic option) when the substraction is true
	 * @return String path to the file or to the directory
	 */
	public String getRawDataDir2(){ return this._jtfRawData2.getText(); }

	/**
	 * getter  for the output directory path for hic dumpdata only with hic == true
	 * @return String path to the output directory
	 */
	public String getOutDir(){ return this._jtfOutput.getText(); }

	/**
	 * getter for the 2nd output directory path for hic dumpdata only with hic == true and substraction == true
	 * @return String path to the output directory
	 */
	public String getOutDir2(){ return this._jtfOutput2.getText(); }
	
	/**
	 * getter for the chr size file path only with hic == true
	 * @return String path to chr size file
	 */
	public String getChrSizeFile(){ return this._jtfChrSize.getText(); }
	
	/**
	 * getter for the prefix fo the file name of the meatplot results
	 * @return String prefix
	 */
	public String getPrefix(){ return this._jtfPrefix.getText(); }
	
	/**
	 * getter for the script path
	 * @return String script path
	 */
	public String getScript(){ return this._jtfScript.getText(); }
	
	
	/**
	 * getter for path of the loops file 
	 * @return String loops file path
	 */
	public String getLoopFile(){ return this._jtfLoopsFile.getText(); }
	
	/**
	 *  getter is Start 
	 * @return boolean start true if the user push start
	 */
	public boolean isStart(){	return this._start;}
	
	/**
	 * getter if simple analysis true else false
	 * @return boolean
	 */
	public boolean isOneData(){	return this._jrSimple.isSelected(); }
	
	/**
	 * getter is true compute the zscore
	 * @return boolean
	 */
	public boolean isZscore(){	return this._jrZscore.isSelected(); }
	
	/**
	 * getter is true comput the manhattan distance
	 * @return boolean
	 */
	public boolean isSquareManha(){	return this._jrSquare.isSelected(); }	
	
	/**
	 * getter of the heatmap color
	 * @return String color
	 */
	public String getColor(){	return (String) this._comboColor.getSelectedItem(); }
	
	/**
	 * getter if is true => substraction analysis run 
	 * @return String color
	 */
	public boolean isCompare(){ return this._jrSubtraction.isSelected(); }
	
	/**
	 * getter min value for the heatmap key
	 * @return double min
	 */
	public double getMinValue(){
		String x = this._jtfMinValue.getText();
		return Double.parseDouble(x.replaceAll(",", "."));
	}
	
	/**
	 * getter max value for the heatmap key
	 * @return double max
	 */
	public double getMaxValue(){
		String x = this._jtfMaxValue.getText();
		return Double.parseDouble(x.replaceAll(",", "."));
	}
	
	/**
	 * getter threshold value for the matrix, take only value < tor this threshold
	 * @return double threshold
	 */
	public double getThreshold(){
		String x = this._jtfThreshold.getText();
		return Double.parseDouble(x.replaceAll(",", "."));
	}
	
	/**
	 * getter of the matrix size for te metaplot
	 * @return int size
	 */
	public int getMatrixSize(){
		String x = this._jtfMatrixSize.getText();
		return Integer.parseInt(x.replaceAll(",", "."));
	}
	
	/**
	 * getter of the matrix size for te metaplot
	 * @return int size
	 */
	public int getResolution(){
		String x = this._jtfResolution.getText();
		return Integer.parseInt(x.replaceAll(",", "."));
	}
	/**
	 * nb of cpu used for the analysis
	 * @return int cpu
	 */
	public int getNbCpu(){
		String x = this._cpu.getText();
		return Integer.parseInt(x.replaceAll(",", "."));
	}

	/**
	 * getter of the iimage size 
	 * @return int image size
	 */
	public int getSipImageSize(){
		String x = this._jtfSipImageSize.getText();
		return Integer.parseInt(x.replaceAll(",", "."));
	}
	
	/**
	 * path to juicer tool box
	 * @return String path to jar file
	 */
	public String getCooler(){ return this._jtfCooler.getText();}
	
	/**
	 * path to juicer tool box
	 * @return String path to jar file
	 */
	public String getCoolTools(){ return this._jtfCoolTools.getText();}
	
	/**
	 * path to juicer tool box
	 * @return String path to jar file
	 */
	public String getJuiceBox(){ return this._jtfBoxTools.getText();}
	
	/**
	 * normalization for dumpdata 
	 * @return boolean
	 */
	public boolean isVC_SQRT(){	return this._jrbVC_sqrt.isSelected();}
	
	/**
	 * normalization for dumpdata 
	 * @return boolean
	 */
	public boolean isVC(){ return this._jrbVC.isSelected(); }
	
	/**
	 * normalization for dumpdata 
	 * @return boolean
	 */
	public boolean isNONE(){ return this._jrbNone.isSelected(); }
	
	/**
	 * normalization for dumpdata 
	 * @return boolean
	 */
	public boolean isKR(){	return this._jrbKR.isSelected();}
	
	/**
	 * SIP or hic input
	 * @return boolean
	 */
	public boolean isSIP(){	return this._jrSIP.isSelected();}
	/**
	 * SIP or hic input
	 * @return boolean
	 */
	public boolean isHic(){	return this._jrHic.isSelected();}
	/**
	 * SIP or hic input
	 * @return boolean
	 */
	public boolean isCool(){	return this._jrCool.isSelected();}


	/********************************************************************************************************************************************
	 * 	Classes listener to interact with the several element of the window
	 */
	/********************************************************************************************************************************************
	/********************************************************************************************************************************************
	/********************************************************************************************************************************************
	/********************************************************************************************************************************************/
				
	/**
	 * 
	 * @author axel poulet
	 * Listener for the start button 
	 *
	 */
	class StartListener implements ActionListener {
		/** */
		MetaplotGUI _gui;
		/**
		 * construtor 
		 * @param gui
		 */
		public  StartListener (MetaplotGUI gui){	_gui = gui; }
		
		/**
		 * Test all the box, condition etc before to allow the program to run and dispose the gui
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
	 * Quit button listener
	 * 
	 * @author axel poulet
	 *
	 */
	class QuitListener implements ActionListener {
		/** */
		MetaplotGUI _gui;	
		/**
		 *	 Constructor
		 * @param gui
		 */
		public  QuitListener (MetaplotGUI gui){ _gui = gui; }
		/**
		 * dipose the gui and quit the program 
		 */
		public void actionPerformed(ActionEvent actionEvent){
			_gui.dispose();
			System.exit(0);
		}
	}
		
	/**
	 * 
	 * Listener for the button selecting a file, so allow only to select file
	 * 
	 * @author axel poulet
	 *
	 */
	class FileListener implements ActionListener
	{
		/** */
		JTextField m_jtf;
		/**
		 * Constructor 
		 * @param jtf
		 */
		public FileListener(JTextField jtf){ m_jtf = jtf; }
			
		/**
		 * allow only to select a file in the computer arborescence 
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
	 * Radio button listener, manage teh access of the different button box etc on function of the parameters choose
	 * 
	 * @author axel poulet
	 *
	 */
	class RBHicListener implements ActionListener{
		/** */
		MetaplotGUI _gui;
		/**
		 * Constructor   
		 * @param gui
		 */
		public  RBHicListener (MetaplotGUI gui){	_gui = gui;}
		
		/**
		 * manage the access of the different gui element on function of the paramter choose
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
				_gui._jtfOutput.setEditable(false);
				_gui._jbOutput.setEnabled(false);
				_gui._jtfOutput2.setEditable(false);
				_gui._jbOutput2.setEnabled(false);
				_gui._jtfBoxTools.setEditable(false);
				_gui._jbBoxTools.setEnabled(false);
				_gui._jbChrSize.setEnabled(false);
				_gui._jtfChrSize.setEditable(false);
				_gui._jrbNone.setEnabled(false);
				_gui._jrbKR.setEnabled(false);
				_gui._jrbVC.setEnabled(false);
				_gui._jrbVC_sqrt.setEnabled(false);
				_gui._jrbKR.setSelected(false);
				_gui._jrbVC.setSelected(false);
				_gui._jrbVC_sqrt.setSelected(false);
				_gui._jrbNone.setSelected(false);
				_gui._jtfCoolTools.setEditable(false);
				_gui._jbCoolTools.setEnabled(false);
				_gui._jtfCooler.setEditable(false);
				_gui._jbCooler.setEnabled(false);
			}else if (_gui.isHic()){
				if(_gui.isCompare()){
					_gui._jtfRawData2.setEditable(true);
					_gui._jbRawData2.setEnabled(true);
					_gui._jtfOutput2.setEditable(true);
					_gui._jbOutput2.setEnabled(true);
				}else if(_gui.isOneData()){
					_gui._jtfRawData2.setEditable(false);
					_gui._jbRawData2.setEnabled(false);
					_gui._jtfOutput2.setEditable(false);
					_gui._jbOutput2.setEnabled(false);
				}
				_gui._jtfOutput.setEditable(true);
				_gui._jbOutput.setEnabled(true);
				_gui._jtfBoxTools.setEditable(true);
				_gui._jbBoxTools.setEnabled(true);
				_gui._jbChrSize.setEnabled(true);
				_gui._jtfChrSize.setEditable(true);
				_gui._jrbNone.setEnabled(true);
				_gui._jrbKR.setEnabled(true);
				_gui._jrbKR.setSelected(true);
				_gui._jrbVC.setEnabled(true);
				_gui._jrbVC_sqrt.setEnabled(true);
				_gui._jtfCoolTools.setEditable(false);
				_gui._jbCoolTools.setEnabled(false);
				_gui._jtfCooler.setEditable(false);
				_gui._jbCooler.setEnabled(false);
			}else if (_gui.isCool()){
				if(_gui.isCompare()){
					_gui._jtfRawData2.setEditable(true);
					_gui._jbRawData2.setEnabled(true);
					_gui._jtfOutput2.setEditable(true);
					_gui._jbOutput2.setEnabled(true);
				}else if(_gui.isOneData()){
					_gui._jtfRawData2.setEditable(false);
					_gui._jbRawData2.setEnabled(false);
					_gui._jtfOutput2.setEditable(false);
					_gui._jbOutput2.setEnabled(false);
				}
				_gui._jtfOutput.setEditable(true);
				_gui._jbOutput.setEnabled(true);
				_gui._jtfBoxTools.setEditable(false);
				_gui._jbBoxTools.setEnabled(false);
				_gui._jbChrSize.setEnabled(true);
				_gui._jtfChrSize.setEditable(true);
				_gui._jrbNone.setEnabled(false);
				_gui._jrbKR.setEnabled(false);
				_gui._jrbKR.setSelected(false);
				_gui._jrbVC.setEnabled(false);
				_gui._jrbVC_sqrt.setEnabled(false);
				_gui._jtfCoolTools.setEditable(true);
				_gui._jbCoolTools.setEnabled(true);
				_gui._jtfCooler.setEditable(true);
				_gui._jbCooler.setEnabled(true);
			}
		}
	}	
	
	/**
	 * Directory button 
	 * 
	 * @author axel Poulet
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
	 * Directory or file button selection
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
		 * Choose teh directory or file on function of the paramter choose before
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