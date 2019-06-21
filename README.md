# SIPMeta1.0 version
			
	Usage:
		with SIP output
			
			simple  <loopsFile> <RawData> <script> <sMetaPlot> <sImg> [options]
			subtraction <loopsFile> <RawData1> <RawData2> <script> <sMetaPlot> <sImg> [options]
			
		with .hic file
			
			hic simple  <loopsFile> <hicFile1> <outdir> <chrSizeFile> <JuicerBoxTools.jar> <script> <sMetaPlot> 
				<sImg> [options]
			hic subtraction <loopsFile> <hicFile1> <hicFile2> <outdir1> <outdir2> <chrSizeFile> <JuicerBoxTools.jar>
				<script> <sMetaPlot> <sImg> [options]
				
	Parameters:
	
		sMetaPlot: size of the metaplot (default 21 bins). Must be an odd number
		sImg: size of the image analysed by SIP (default 2000 bins)
		chrSizeFile: chrSizeFile: path to the chr size file, with the same name of the chr as 
		in the hic file
		-norm: <NONE/VC/VC_SQRT/KR> only for hic option (default KR)
		-resMax TRUE or FALSE: default true, if false take the smaller resolution
		-c COLORSCHEME  matplotlib_colors (Blues, BuGn, Greens, Purples, Reds, coolwarm, magma,
		inferno, spectral,
		viridis) default Reds
		-z znorm each ring
		-t T threshold value tests the distance normalized value, all the value > T will be replaced
		by zero
		-prefix Prefix name of the output file
		-s Trim edges to make a square plot but with Manhattan distances
		-min Min minvalue for color scale
		-max Max maxvalue for color scale
		-cpu: Number of CPU used for processing (default 1)
		-h, --help print help
	
	Authors:
		
		Axel Poulet
		Department of Biology, Emory University, 1510 Clifton Rd. NE, Atlanta, GA 30322, USA.
		Department of Molecular, Cellular  and Developmental Biology Yale University 165
		Prospect St New Haven, CT 06511, USA
		
		M. Jordan Rowley
		Department of Biology, Emory University, 1510 Clifton Rd. NE, Atlanta, GA 30322, USA.
		
		Contact: pouletaxel@gmail.com OR michael.j.rowley@emory.edu
