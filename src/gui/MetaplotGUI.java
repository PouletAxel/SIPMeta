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
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	private JTextField _jtfRFile = new JTextField();
	/** */
	private JButton _jbRFile = new JButton("R script");
	
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
		this.setSize(550, 450);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setBackground(Color.LIGHT_GRAY);
		_container = getContentPane();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.1};
		gridBagLayout.rowHeights = new int[] {17, 200, 124, 7};
		gridBagLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.1};
		gridBagLayout.columnWidths = new int[] {200, 120, 72, 20};
		_container.setLayout (gridBagLayout);
		
	   	JLabel label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",1,12));
	   	label.setText("Metaplot choice Simple (one data set) or Substarction (two data sets):");
	   	_container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0
		 ));   	
		
		//// Comapre or not 
		
		_bGroupMetaplotype.add(_jrSubstraction);
	 	_bGroupMetaplotype.add(_jrSimple);
	 	
		_jrSimple.setFont(new java.awt.Font("arial",2,11));
		_jrSubstraction.setFont(new java.awt.Font("arial",2,11));
		_container.add(_jrSimple,new GridBagConstraints
				(
						0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(20, 20, 0, 0), 0, 0
				)
		);
		_container.add(_jrSubstraction,new GridBagConstraints
				(
						0, 1, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE,new Insets(20, 150, 0, 0), 0, 0
				)
		);
		
		_jrSimple.setSelected(true);
		_jtfRawData2.setEditable(false);
		_jbRawData2.setEnabled(false);
	   	///////////////////// Rawdata and work dir button and text.	   	
	   	   	
		label = new JLabel();
	   	label.setFont(new java.awt.Font("arial",1,12));
	   	label.setText("Input and Rscript choices:");
	   	_container.add(label, new GridBagConstraints(
	   			0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
		   		GridBagConstraints.NONE, new Insets(50, 10, 0, 0), 0, 0
		 ));
	   	
	   	_jbLoopsFile.setPreferredSize(new java.awt.Dimension(100, 21));
	   	_jbLoopsFile.setFont(new java.awt.Font("arial",2,11));
	   	_container.add ( _jbLoopsFile, new GridBagConstraints
	   			(
	   					0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   					GridBagConstraints.NONE, new Insets(80, 10, 0, 0), 0, 0
	   			)
	   	);
	   	
	   	this._jtfLoopsFile.setPreferredSize(new java.awt.Dimension(280, 21));
	   	_jtfLoopsFile.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfLoopsFile, new GridBagConstraints
				(
						0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(80, 160, 0, 0),0, 0
				)
		);
		
	   	_jbRawData.setPreferredSize(new java.awt.Dimension(100, 21));
	   	_jbRawData.setFont(new java.awt.Font("arial",2,10));
	   	_container.add ( _jbRawData, new GridBagConstraints
	   			(
	   					0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   					GridBagConstraints.NONE, new Insets(110, 10, 0, 0), 0, 0
	   			)
	   	);
	   	
	   	_jtfRawData.setPreferredSize(new java.awt.Dimension(280, 21));
		_jtfRawData.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfRawData, new GridBagConstraints
				(
						0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(110, 160, 0, 0),0, 0
				)
		);
		
	   	_jbRawData2.setPreferredSize(new java.awt.Dimension(100, 21));
	   	_jbRawData2.setFont(new java.awt.Font("arial",2,10));
	   	_container.add ( _jbRawData2, new GridBagConstraints
	   			(
	   					0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   					GridBagConstraints.NONE, new Insets(140, 10, 0, 0), 0, 0
	   			)
	   	);
	   	
	   	_jtfRawData2.setPreferredSize(new java.awt.Dimension(280, 21));
		_jtfRawData2.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfRawData2, new GridBagConstraints
				(
						0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(140, 160, 0, 0),0, 0
				)
		);
		
		_jbRFile.setPreferredSize(new java.awt.Dimension(100, 21));
	   	_jbRFile.setFont(new java.awt.Font("arial",2,11));
	   	_container.add ( _jbRFile, new GridBagConstraints
	   			(
	   					0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST, 
	   					GridBagConstraints.NONE, new Insets(170, 10, 0, 0), 0, 0
	   			)
	   	);
	   	
	   	this._jtfRFile.setPreferredSize(new java.awt.Dimension(280, 21));
	   	_jtfRFile.setFont(new java.awt.Font("arial",2,10));
		_container.add(_jtfRFile, new GridBagConstraints
				(
						0, 1, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.NONE, new Insets(170, 160, 0, 0),0, 0
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
		
		////////////////// heatmap min and max parameters
		
	   	label = new JLabel();
	   	label.setText("Heatmap parameters for the key values:");
	   	label.setFont(new java.awt.Font("arial",1,12));
		_container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(90, 20, 0, 0), 0, 0
		));
			
	   	label = new JLabel();
	   	label.setText("Min value:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(120, 20, 0, 0), 0, 0
		));
			
		this._minValue.setText("0");
		_minValue.setPreferredSize(new java.awt.Dimension(60, 21));
		_minValue.setFont(new java.awt.Font("arial",2,11));
		_container.add( _minValue, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(120, 130, 0, 0), 0, 0
		));
		
		label = new JLabel();
	   	label.setText("max value:");
	   	label.setFont(new java.awt.Font("arial",2,11));
		_container.add(label, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(120, 230, 0, 0), 0, 0
		));
			
		this._maxValue.setText("20");
		_maxValue.setPreferredSize(new java.awt.Dimension(60, 21));
		_maxValue.setFont(new java.awt.Font("arial",2,11));
		_container.add(_maxValue, new GridBagConstraints(
				0, 2, 0, 0, 0.0, 0.0,  GridBagConstraints.NORTHWEST, 
				GridBagConstraints.NONE, new Insets(120, 400, 0, 0), 0, 0
		));
		
		////////////////////////// start and quit button
		_jbStart.setPreferredSize(new java.awt.Dimension(120, 21));
	   	_container.add(_jbStart, new GridBagConstraints(
	   			0, 2, 0, 0,  0.0, 0.0, GridBagConstraints.NORTHWEST,
	   			GridBagConstraints.NONE, new Insets(160, 140, 0,0), 0, 0
	   	));
	   	
	   	
	   	_jbQuit.setPreferredSize(new java.awt.Dimension(120, 21));
		_container.add(_jbQuit,new GridBagConstraints(
				0, 2, 0, 0,  0.0, 0.0,GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE,new Insets(160, 10, 0, 0), 0, 0
		));
		
		RBHicListener plop = new RBHicListener(this);
	  	this._jrSimple.addActionListener(plop);
	  	this._jrSubstraction.addActionListener(plop);
		FileListener rFile = new FileListener(_jtfRFile);
		_jbRFile.addActionListener(rFile);
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
	public String getRFile(){ return this._jtfRFile.getText(); }
	
	
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
			_start=true;
			_gui.dispose();
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
			if(_gui.isCompare()){
        		_gui._jtfRawData2.setEditable(true);
        		_gui._jbRawData2.setEnabled(true);
        	}else if(_gui.isOneData()){
        		_gui._jtfRawData2.setEditable(false);
        		_gui._jbRawData2.setEnabled(false);
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

