central:
	javac -cp AIMA.jar:CentralEnergia.jar:. *.java

.PHONY: run
run:
	java -Xmx6048m -cp AIMA.jar:CentralEnergia.jar:. CentralDemo
