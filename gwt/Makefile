all:
	mvn -Pbase,slow,test clean install
clean:
	mvn -Pbase,slow,test clean
installwotest:
	mvn -Pbase,slow-Dmaven.test.skip=true resources:resources install
