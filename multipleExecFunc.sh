#! /bin/bash
java -Xmx1512m -cp AIMA.jar:CentralEnergia.jar:. CentralDemo 1234 5 10 25 100 > out/1.out
java -Xmx1512m -cp AIMA.jar:CentralEnergia.jar:. CentralDemo 1234 5 10 25 200 > out/2.out
java -Xmx6000m -cp AIMA.jar:CentralEnergia.jar:. CentralDemo 1234 5 10 25 400 > out/3.out
java -Xmx6000m -cp AIMA.jar:CentralEnergia.jar:. CentralDemo 1234 5 10 25 800 > out/4.out
java -Xmx6000m -cp AIMA.jar:CentralEnergia.jar:. CentralDemo 1234 5 10 25 1200 > out/5.out

java -Xmx6000m -cp AIMA.jar:CentralEnergia.jar:. CentralDemo 1234 5 10 25 1000 > out/6.out
java -Xmx6000m -cp AIMA.jar:CentralEnergia.jar:. CentralDemo 1234 10 20 50 1000 > out/7.out
java -Xmx6000m -cp AIMA.jar:CentralEnergia.jar:. CentralDemo 1234 15 30 75 1000 > out/8.out
java -Xmx6000m -cp AIMA.jar:CentralEnergia.jar:. CentralDemo 1234 20 40 100 1000 > out/9.out
java -Xmx6000m -cp AIMA.jar:CentralEnergia.jar:. CentralDemo 1234 25 50 125 1000 > out/10.out
