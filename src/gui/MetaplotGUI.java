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
	
	/** */
	private JButton _jbRawData = new JButton("Raw data");
	/** */
	private JTextField _jtfRawData = new JTextField();
	
	/** */
	private JButton _jbRawData2 = new JButton("Raw data 2");
	/** */
	private JTextField _jtfRawData2 = new JTextField();
	/** */
	private JTextField _jtfLoopsFile = new JTextField();
	/** */
	private JButton _jbLoopsFile = new JButton("loops file");

	/** */
	private JTextField _jtfScript = new JTextField();
	/** */
	private JButton _jbScriptFile = new JButton("Script");
	
	private JButton _jbStart = new JButton("Start");
	/** */
	private JButton _jbQuit = new JButton("Quit");
	/** */
	private boolean _start = false;
	
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
	private ButtonGroup _bGroupMetaType = new ButtonGroup();
	/** */
	private JRadioButton _jrManha = new JRadioButton("Manhattan distance (bullseye plot)");
	/** */
	private JRadioButton _jrClassic = new JRadioButton("Classic");
	
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
		this.setTitle("Metaploter");
		this.setSize(550, 600);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setBackground(Color.LIGHT_GRAY);
		_container = getContentPane();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.1};
		gridBagLayout.rowHeights = new int[] {17, 280, 124, 7};
		gridBagLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.1};
		gridBagLayout.columnWidths = new int[] {260, 120, 72, 20};
		_container.setLayout (gridBagLayout);
		
		JLabel label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",1,12));
	   	label.setText("Type of Metaplot:");
	   	_container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0
		 ));   	
		
	   	_bGroupMetaType.add(_jrClassic);
	   	_bGroupMetaType.add(_jrManha);
	 	
	   	_jrClassic.setFont(new java.awt.Font("arial",2,11));
	   	_jrManha.setFont(new java.awt.Font("arial",2,11));
		_container.add(_jrClassic,new GridBagConstraints(
				0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(30, 20, 0, 0), 0, 0
		));
		_container.add(_jrManha,new GridBagConstraints(
				0, 1, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE,new Insets(30, 150, 0, 0), 0, 0
		));
		_jrClassic.setSelected(true);
		
	   	label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",1,12));
	   	label.setText("Metaplot choice Simple (one data set) or Substarction (two data sets):");
	   	_container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(60, 10, 0, 0), 0, 0
		 ));   	
		
		//// Comapre or not 
		
		_bGroupMetaplotype.add(_jrSubstraction);
	 	_bGroupMetaplotype.add(_jrSimple);
	 	
		_jrSimple.setFont(new java.awt.Font("arial",2,11));
		_jrSubstraction.setFont(new java.awt.Font("arial",2,11));
		_container.add(_jrSimple,new GridBagConstraints
				(
						0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(80, 20, 0, 0), 0, 0
				)
		);
		_container.add(_jrSubstraction,new GridBagConstraints
				(
						0, 1, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE,new Insets(80, 150, 0, 0), 0, 0
				)
		);
		
		_jrSimple.setSelected(true);
		_jtfRawData2.setEditable(false);
		_jbRawData2.setEnabled(false);
	   	///////////////////// Rawdata and work dir button and text.	   	
	   	   	
		label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",1,12));
	   	label.setText("Input and script choices:");
	   	_container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(110, 10, 0, 0), 0, 0
		 ));
	   	
	   	_jbLoopsFile.setPreferredSize(new java.awt.Dimension(100, 21));
	   	_jbLoopsFile.setFont(new java.awt.Font("arial",2,11));
	   	_container.add ( _jbLoopsFile, new GridBagConstraints
	   			(
	   					0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   					GridBagConstraints.NONE, new Insets(130, 10, 0, 0), 0, 0
	   			)
	   	);
	   	
	   	this._jtfLoopsFile.setPreferredSize(new java.awt.Dimension(280, 21));
	   	_jtfLoopsFile.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfLoopsFile, new GridBagConstraints
				(
						0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(130, 160, 0, 0),0, 0
				)
		);
		
	   	_jbRawData.setPreferredSize(new java.awt.Dimension(100, 21));
	   	_jbRawData.setFont(new java.awt.Font("arial",2,10));
	   	_container.add ( _jbRawData, new GridBagConstraints
	   			(
	   					0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   					GridBagConstraints.NONE, new Insets(160, 10, 0, 0), 0, 0
	   			)
	   	);
	   	
	   	_jtfRawData.setPreferredSize(new java.awt.Dimension(280, 21));
		_jtfRawData.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfRawData, new GridBagConstraints
				(
						0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(160, 160, 0, 0),0, 0
				)
		);
		
	   	_jbRawData2.setPreferredSize(new java.awt.Dimension(100, 21));
	   	_jbRawData2.setFont(new java.awt.Font("arial",2,10));
	   	_container.add ( _jbRawData2, new GridBagConstraints
	   			(
	   					0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   					GridBagConstraints.NONE, new Insets(190, 10, 0, 0), 0, 0
	   			)
	   	);
	   	
	   	_jtfRawData2.setPreferredSize(new java.awt.Dimension(280, 21));
		_jtfRawData2.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfRawData2, new GridBagConstraints
				(
						0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(190, 160, 0, 0),0, 0
				)
		);
	   	label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",2,11));
	   	label.setText("Script path (Classic metaplot (heatmap.R) bullseye plot (bullseye.py))");
	   	_container.add(label, new GridBagConstraints
				(
						0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(220, 20, 0, 0),0, 0
				)
		);
		_jbScriptFile.setPreferredSize(new java.awt.Dimension(100, 21));
	   	_jbScriptFile.setFont(new java.awt.Font("arial",2,11));
	   	_container.add ( _jbScriptFile, new GridBagConstraints
	   			(
	   					0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   					GridBagConstraints.NONE, new Insets(240, 10, 0, 0), 0, 0
	   			)
	   	);
	   	
	   	this._jtfScript.setPreferredSize(new java.awt.Dimension(280, 21));
	   	_jtfScript.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfScript, new GridBagConstraints
				(
						0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(240, 160, 0, 0),0, 0
				)
		);
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
	   			0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0
		 ));
		
	   	label = new JLabel();
	   	label.setText("metaplot size:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(30, 20, 0, 0), 0, 0
		));
			
		this._matrixSize.setText("21");
		_matrixSize.setPreferredSize(new java.awt.Dimension(60, 21));
		_matrixSize.setFont(new java.awt.Font("arial",2,11));
		_container.add( _matrixSize, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(27, 130, 0, 0), 0, 0
		));
		
		label = new JLabel();
	   	label.setText("SIP image size in bins:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(30, 230, 0, 0), 0, 0
		));
			
		this._sipImageSize.setText("2000");
		_sipImageSize.setPreferredSize(new java.awt.Dimension(60, 21));
		_sipImageSize.setFont(new java.awt.Font("arial",2,11));
		_container.add( _sipImageSize, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(27, 400, 0, 0), 0, 0
		));
	
		
		
		label = new JLabel();
	   	label.setText("Used the resolution max detected in the loops file ?:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(60, 20, 0, 0), 0, 0
		));
		
		_bGroupMaxRes.add(_jrTrue);
	 	_bGroupMaxRes.add(_jrFalse);
	 	
		_jrTrue.setFont(new java.awt.Font("arial",2,11));
		_jrFalse.setFont(new java.awt.Font("arial",2,11));
		_container.add(_jrTrue,new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(57, 320, 0, 0), 0, 0
		));
		_container.add(_jrFalse,new GridBagConstraints(
				0, 2, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE,new Insets(57, 370, 0, 0), 0, 0
		));
		_jrTrue.setSelected(true);
		
	   	label = new JLabel();
	   	label.setText("Heatmap color for Manhattan disatnce option:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(90, 20, 0, 0), 0, 0
		));
		
		_comboColor.setFont(new java.awt.Font("arial",0,10));
		_container.add( _comboColor, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(87, 300, 0, 0), 0, 0
		));
		_comboColor.setEnabled(false);
		
	   	label = new JLabel();
	   	label.setText("Min value:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(120, 20, 0, 0), 0, 0
		));
			
		this._minValue.setText("-1");
		_minValue.setPreferredSize(new java.awt.Dimension(60, 21));
		_minValue.setFont(new java.awt.Font("arial",2,11));
		_container.add( _minValue, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(117, 90, 0, 0), 0, 0
		));
		
		label = new JLabel();
	   	label.setText("Max value:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(120, 200, 0, 0), 0, 0
		));
			
		this._maxValue.setText("-1");
		_maxValue.setPreferredSize(new java.awt.Dimension(60, 21));
		_maxValue.setFont(new java.awt.Font("arial",2,11));
		_container.add(_maxValue, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(117, 300, 0, 0), 0, 0
		));
		
		_jrZscore.setFont(new java.awt.Font("arial",2,11));
		_container.add(_jrZscore, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(150, 20, 0, 0), 0, 0
		));
		_jrZscore.setSelected(false);
		_jrZscore.setEnabled(false);
		
		_jrSquare.setFont(new java.awt.Font("arial",2,11));
		_container.add(_jrSquare, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(150, 200, 0, 0), 0, 0
		));
		_jrSquare.setSelected(false);
		_jrSquare.setEnabled(false);

		
		////////////////////////// start and quit button
		_jbStart.setPreferredSize(new java.awt.Dimension(120, 21));
	   	_container.add(_jbStart, new GridBagConstraints(
	   			0, 2, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
	   			GridBagConstraints.NONE, new Insets(190, 140, 0,0), 0, 0
	   	));
	   	
	   	
	   	_jbQuit.setPreferredSize(new java.awt.Dimension(120, 21));
		_container.add(_jbQuit,new GridBagConstraints(
				0, 2, 0, 0,  0.0, 0.0,GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE,new Insets(190, 10, 0, 0), 0, 0
		));
		
		RBHicListener plop = new RBHicListener(this);
		this._jrClassic.addActionListener(plop);
	  	this._jrManha.addActionListener(plop);
		this._jrSimple.addActionListener(plop);
	  	this._jrSubstraction.addActionListener(plop);
		FileListener rFile = new FileListener(_jtfScript);
		_jbScriptFile.addActionListener(rFile);
		FileListener loopsFile = new FileListener(_jtfLoopsFile);
		_jbLoopsFile.addActionListener(loopsFile);	 
		
		
		DataDirectoryListener rawDataListener = new DataDirectoryListener(this,_jtfRawData);
	  	_jbRawData.addActionListener(rawDataListener);
	  	
	  	DataDirectoryListener rawData2Listener = new DataDirectoryListener(this,_jtfRawData2);
	  	_jbRawData2.addActionListener(rawData2Listener);
		
		
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
	public boolean isClassic(){ return this._jrClassic.isSelected();}
	
	/**
	 * 
	 * @return
	 */
	public boolean isManhattan(){ return this._jrManha.isSelected();}
	
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
	public int getMatrixSize(){
		String x = this._matrixSize.getText();
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
			}else if(_gui.isClassic()){
				if(_gui.getScript().contains("heatmap.R")){
					_start=true;
					_gui.dispose();
				}else{
					JOptionPane.showMessageDialog(
						null, "The script choose for the classic metaplot is not the good one. give the path of heatmap.R",
						"Error", JOptionPane.ERROR_MESSAGE);	
				}
			}else if(_gui.isManhattan()){
				if(_gui.getScript().contains("bullseye.py")){
					_start=true;
					_gui.dispose();
				}else{
					JOptionPane.showMessageDialog
					(
						null,
						"The script choose for the bullseye metaplot is not the good one. give the path of bullseye.py",
						"Error",
						JOptionPane.ERROR_MESSAGE
					);	
				}
			}
			if(_gui.isCompare() && _jtfRawData2.getText().equals("")){
				JOptionPane.showMessageDialog(
						null, "Add path of Raw data2 directory",
						"Error", JOptionPane.ERROR_MESSAGE);
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
			if(_gui.isClassic()){
				if(_gui.isCompare()){
					_gui._jtfRawData2.setEditable(true);
					_gui._jbRawData2.setEnabled(true);
				}else if(_gui.isOneData()){
					_gui._jtfRawData2.setEditable(false);
					_gui._jbRawData2.setEnabled(false);
				}
				_jrSquare.setEnabled(false);
				_jrZscore.setEnabled(false);
				_comboColor.setEnabled(false);
				_jrSubstraction.setEnabled(true);
			 	_jrSimple.setEnabled(true);	
			}else if(_gui.isManhattan()){
				if(_gui.isCompare()){
					_gui._jtfRawData2.setEditable(true);
					_gui._jbRawData2.setEnabled(true);
				}else if(_gui.isOneData()){
					_gui._jtfRawData2.setEditable(false);
					_gui._jbRawData2.setEnabled(false);
				}
				_jrSquare.setEnabled(true);
				_jrZscore.setEnabled(true);
				_comboColor.setEnabled(true);
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
}

