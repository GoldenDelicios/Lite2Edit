# Lite2Edit
Java application: Converts Litematics to WorldEdit schematics

## GUI
Run the jar file to open the GUI application.

![image](https://github.com/emeraldtip/Lite2Edit/assets/48155462/e745dada-d8be-4d0b-929b-469ee2f8ab11)

Click 'browse' and select your litematic files you want converted.

![image](https://github.com/emeraldtip/Lite2Edit/assets/48155462/148acc5f-77bf-4416-9b57-7b12ce9c6373)


On successful conversion:

![image](https://github.com/emeraldtip/Lite2Edit/assets/48155462/a917a8df-9745-42a2-b03c-c51951290076)



## CLI
Run the jar file with the --cli argument and add all files you want converted after that.

Example:
```
java -jar Lite2Edit-1.2.0.jar --cli test.litematic ../anothertestfile.litematic
```

Both relative and full file paths work.


## Building
Run maven install:
```
mvn install -f pom.xml
```
The correct output file is in the folder "target" and doesn't start with "original-".
The other file doesn't have dependencies in the jar file and won't work.
