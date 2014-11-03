all:
	mvn -Pbase,test -DlogConsole=true clean install
clean:
	mvn -Pbase,test clean
installwotest:
	mvn -Pbase -Dmaven.test.skip=true resources:resources install
