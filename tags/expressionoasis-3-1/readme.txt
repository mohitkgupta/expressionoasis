Application Name: ExpressionOasis
Author Name: Mohit Gupta <mohit.gupta@vedantatree.com> & Contributors

Installation: 
Application build system is based on Maven

To run maven, you should have relevant plugin in your IDE; like m2plugin for Eclipse
Use maven commands, like build, package etc to build the application

There is a dependency which points to a custom library, Utilities-1.1.jar. There are two way to refer to this jar. Either we can set the scope of 
this dependency as 'system' and can mention the lib path. This is being done right now in pom.xml. Alternative and recommended way is to install this
file in local maven repository using following command, set the scope as 'compile' and remove the 'systempath'

Command to install the lib to local maven repository is:

mvn install:install-file -Dfile=<system path of jar file with name> -DgroupId=vedantatree -DartifactId=utilities -Dversion=1.1 -Dpackaging=jar 

Sample:
Code samples can be found in TestEvaluator.java

Documentation:
You can find java documentation in java