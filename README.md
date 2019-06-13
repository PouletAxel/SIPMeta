# SIPMeta1.0 version

	Usage:
		
		with SIP output
		
			simple  <loopsFile> <RawData> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-t T] [-prefix PREFIX] [-c COLORSCHEME]
			substraction <loopsFile> <RawData1> <RawData2> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-t T] [-prefix PREFIX] [-c COLORSCHEME]
		
		with .hic file
			
				hic simple  <loopsFile> <hicFile1> <outdir> <chrSizeFile> <JuicerBoxTools.jar> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s][-t T] [-prefix PREFIX] [-c COLORSCHEME]
				hic substraction <loopsFile> <hicFile1> <hicFile2> <outdir1> <outdir2> <chrSizeFile> <JuicerBoxTools.jar> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-t T] [-prefix PREFIX] [-resMax TRUE/FALSE][-z] [-s] [-c COLORSCHEME]
				
		Parameters:
		
			sMetaPlot: size of the metaplot (default 21 bins)
			sImg: size of the image analysed by SIP (default 2000 bins)
			chrSizeFile: path to the chr size file, with the same name of the chr as in the hic file
			-resMax TRUE or FALSE: default true, if false take the samller resolution
			-c COLORSCHEME  matplotlib_colors (Blues, BuGn, Greens, Purples, Reds, coolwarm, magma, inferno, spectral, viridis) default Reds
			-z znorm each ring in bullseye
			-s Trim edges to make a square
			-min MIN minvalue for color scale
			-max Max maxvalue for color scale
			-cpu number of cpu uses
			-t T threshold value tests the distance normalized value, all the value > T will be replace by zero
			-prefix PREFIX name of the output file
			-h, --help print help
	
	Authors:
		
		Axel Poulet
		Department of Biology, Emory University, 1510 Clifton Rd. NE, Atlanta, GA 30322, USA.
		Department of Molecular, Cellular  and Developmental Biology Yale University 165 Prospect St New Haven, CT 06511, USA
		
		M. Jordan Rowley
		Department of Biology, Emory University, 1510 Clifton Rd. NE, Atlanta, GA 30322, USA.
		
		Contact: pouletaxel@gmail.com OR michael.j.rowley@emory.edu