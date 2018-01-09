library("irr")


data <- read.csv("example_input.csv", sep =",", header=TRUE)               
print(kappam.fleiss(data,exact=FALSE, detail=FALSE)) #<- for More than 2 Annot = FLeiss kappa
#print(kappa2(data,"unweighted")) # Cohens Kappa for 2 Annot