# FleissKappa_csvFile
Code collection to compute [**Fleiss' Kappa**](https://en.wikipedia.org/wiki/Fleiss%27_kappa)annotator agreemenet given an **easy csv spreadsheet file**. Note that Fleiss Kappa is used for more than two annotators (for 2 you can use [Cohens Kappa](https://de.wikipedia.org/wiki/Cohens_Kappa)). 
This Repository contains code for two different implementation that require the same input format, a **java** implementation and a '**R** implementation.

# Input Format 
See for example `example_input.csv` (random), each column contains the annotation from one annotator. The first line contains the names of the annotator (can be anything). Every other line can contain numeric or categorical values. The seperator is **comma**. 

# Usage
1) for the java code see/modify the `src/KappaCSV.java` code or simply run the executable `java -jar fleisskappa.jar <inpputfile.c>`
2) for the R code replace the path in the `read.csv` function in `src/fleiss_Kappa.R` and execute the script within R (e.g  by running`source('fleiss_kappa.R')`)
# Code Reference
The java implementation is based on  [Fleiss Kappa Java] (https://en.wikipedia.org/wiki/Fleiss%27_kappa) 
The R implementation is based on [Fleiss Kappa R] https://www.rdocumentation.org/packages/irr/versions/0.84/topics/kappam.fleiss)

