# SIPMeta

<img src="https://github.com/PouletAxel/SIPImage/blob/master/meta.jpeg" width="1000">

# What is SIPMeta

SIPMeta is a tool to create both **normal** and **bullseye** transformed average **metaplots** of 2-D matrices, specifically Hi-C, HiChIP, or ChIA-PET data. This program is written in java with a helper python script and can be run on Linux, Windows, or MAC systems and includes either command line options or a graphical user interface.

Follow these links to get started.
* [Installation & Quick Start](https://github.com/PouletAxel/SIPMeta/wiki/Installation-and-Quick-Start)

* [Parameter Guide](https://github.com/PouletAxel/SIPMeta/wiki/Explanations-of-Parameters)

##  If you use SIP or SIPMeta, please cite us.

* [View the Paper](https://genome.cshlp.org/content/early/2020/03/03/gr.257832.119.long)

Rowley MJ, Poulet A, Nichols MH, Bixler BJ, Sanborn AL, Brouhard EA, Hermetz K, Linsenbaum H, Csankovszki G, Lieberman Aiden E, Corces G. Analysis of Hi-C data using SIP effectively identifies loops in organisms from C. elegans to mammals. Genome Research 2020.

# SIPMeta help menu:
	
1. with SIP output

`java -jar SIPMeta.jar simple  <loopsFile> <RawData> <script> <sMetaPlot> <sImg> [options]`

`java -jar SIPMeta.jar subtraction <loopsFile> <RawData1> <RawData2> <script> <sMetaPlot> <sImg> [options]`

1. with .hic file

`java -jar SIPMeta.jar hic simple  <loopsFile> <hicFile1> <outdir> <chrSizeFile> <JuicerBoxTools.jar> <script>  <sMetaPlot> <sImg> [options]`

`java -jar SIPMeta.jar hic subtraction <loopsFile> <hicFile1> <hicFile2> <outdir1> <outdir2> <chrSizeFile>  <JuicerBoxTools.jar> <script> <sMetaPlot> <sImg> [options]`      

1. with .mcool file
		
`java -jar SIPMeta.jar cool simple  <loopsFile> <hicFile1> <outdir> <chrSizeFile> <cooler> <cooltools> <script> <sMetaPlot> <sImg> [options]`
		
`java -jar SIPMeta.jar cool subtraction <loopsFile> <hicFile1> <hicFile2> <outdir1> <outdir2> <chrSizeFile> <cooler> <cooltools> <script> <sMetaPlot> <sImg> [options]`

More information for the command line mode [here](https://github.com/PouletAxel/SIPMeta/wiki/Command-line-usage)

## Parameters:
* sMetaPlot: size of the desired metaplot (default 21 bins). Must be an odd number.
* sImg: size of the image analyzed by SIP. Corresponds to –mat option in SIP (default 2000 bins).
* chrSizeFile: path to the chr size file, with the same name of the chr as in the hic file (i.e. chr1 does not match Chr1 or 1).
* -norm: <NONE/VC/VC_SQRT/KR> only for hic option (default KR).
* -res: resolution in bp (default 5000 bp).
* -c: COLORSCHEME  matplotlib_colors (Blues, BuGn, Greens, Purples, Reds, coolwarm, magma, inferno, spectral, viridis) default Reds. Can be any matplotlib color gradient.
* -z: Set this option to znorm each ring.
* -t: Threshold value tests the distance normalized value, all the value > T will be replaced by zero. Set high to avoid outliers skewing the average.
* -prefix: Prefix desired when naming the output files.
* -s: Set this option to trim edges to make a square plot but with Manhattan distances. (Not recommended as normal square plots are already created).
* -min: Min minimum value for color scale.
* -max: Max maximum value for color scale.
* -cpu: Number of CPU used for processing (default 1).
* -h or --help: print help.

## Authors:
* **Axel Poulet**: Department of Molecular, Cellular  and Developmental Biology, Yale University, 165 Prospect St New Haven, CT 06511, USA. _Contact: pouletaxel@gmail.com_ 
* **M. Jordan Rowley**: Department of Genetics, Cell Biology and Anatomy, University of Nebraska Medical Center Omaha,NE 68198-5805. _Contact: jordan.rowley@unmc.edu_
