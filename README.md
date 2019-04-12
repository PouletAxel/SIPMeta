# SIP_metaploter beta version

	Usage:
	
		simple <bullseye or classic> <loopsFile> <RawData> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-c COLORSCHEME]
		substraction <bullseye or classic> <loopsFile> <RawData1> <RawData2> <script> <sMetaPlot> <sImg> [-min MIN] [-max MAX] [-resMax TRUE/FALSE][-z] [-s] [-c COLORSCHEME]
		
		Param:
			sMetaPlot: size of the metaplot (default 20 bins)
			sImg: size of the image analysed by SIP (default 2000 bins)
			-resMax TRUE or FALSE: default true, if false take the samller resolution (only for classic metaplot)
			-c COLORSCHEME  matplotlib_colors (Blues, BuGn, Greens, Purples, Reds, coolwarm, magma, inferno, spectral, viridis) default Reds (option for bullseye)
			-z znorm each ring in bullseye plot
			-s Trim edges to make a square bullseye plot
			-min MIN minvalue for color scale
			-max Max maxvalue for color scale
			-h, --help print help
