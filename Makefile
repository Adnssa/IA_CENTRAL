central:
	javac -cp AIMA.jar:CentralEnergia.jar:. *.java

.PHONY: run
run:
	java -Xmx1512m -cp AIMA.jar:CentralEnergia.jar:. CentralDemo 1234
