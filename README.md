# Lite2Edit
Java application: Converts Litematics to WorldEdit schematics

## GUI
Run the jar file to open the GUI application.
Click 'browse' and select your litematic files you want converted.

## CLI
Run the jar file with the --cli argument and add all files you want converted after that.

Example:
```
java -jar Lite2Edit-1.2.0.jar --cli test.litematic ../anothertestfile.litematic
```


## Building
Run maven install:
```
mvn install -f pom.xml
```
The correct output file is the one that doesn't start with "original-"