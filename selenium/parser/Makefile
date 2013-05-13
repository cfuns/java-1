all:
	mvn -Pbase,test clean install
clean:
	mvn -Pbase,test clean
installwotest:
	mvn -Pbase -Dmaven.test.skip=true resources:resources install
dir:
	mkdir -p src/main/resources src/main/java src/test/resources src/test/java