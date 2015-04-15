# Introduction #

This project contains a series of nodes ('programs' if you will) for
the KNIME (knime.org) platform to make it easier for users to perform basic bioinformatics tasks, even for large datasets eg. RNA-seq.

These nodes provide partial integration of:
  * EMBOSS (emboss.sourceforge.net)
  * JavaProtLib
  * BioJava
  * JUNG
  * GenePattern
  * NCBI BLAST+
  * ESTScan, SignalP, TargetP, GolgiP + numerous other prediction programs
  * and many other toolkits

# Details #

Licensed under the LGPL license, but uses many third-party libraries. See the respective LICENSE.txt and ACKNOWLEDGEMENTS.txt in the distribution for more details.

# Installation #

## Binary ##

To add the extension to your platform, simply start KNIME and then:
  * choose Help -> Install New Software...
  * in the 'Work With' box, type http://knime.plantcell.unimelb.edu.au
  * press Enter
  * Select "PlantCell KNIME nodes"
  * click 'Next' and follow the prompts, you'll need to restart KNIME

After KNIME restarts, you should have the bioinformatics nodes. Note that not all nodes are usable unless you are located at the University of Melbourne.

## Source ##

  * install KNIME SDK (see www.knime.org for details)
  * start eclipse
  * download the knime-plantcell source tree from google code
  * file -> import the tree into eclipse
  * build it
  * use "PlantCell update site" to create the necessary files for an eclipse update site (strongly recommended)
  * copy these files to a website somewhere
  * start KNIME and check help->install new software
  * enter the website root URL eg. http://my.web.site/plantcell
  * click on 'Cell Biology -> PlantCell' and follow the prompts
  * done! PHEW!