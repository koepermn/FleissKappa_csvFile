library("irr")

#library("dplyr")
#data <- read.csv("full-an/an_task-4.csv", sep =",", header=TRUE) 
data <- read.csv.sql("example_input.csv", sep =",", header=TRUE)               
print(kappam.fleiss(data,exact=FALSE, detail=FALSE))