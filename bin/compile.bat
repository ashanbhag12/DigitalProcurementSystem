cd ..

call mvn install:install-file -Dfile=bin/lib/ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0 -Dpackaging=jar

call mvn clean install -DskipTests