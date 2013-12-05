all:
	mvn -Pbase,lib,test clean install
clean:
	mvn -Pbase,lib,test clean
installwotest:
	mvn -Pbase,lib -Dmaven.test.skip=true resources:resources install
