# INSTALL FROM SCRATCH
PLAY_HOME/activator-1.3.9-minimal/bin/
activator new kaleo play-java
 
 
vim project/plugins.sbt
> activator
[kaleo] $ eclipse


Fro Eclipse:
Import -> General -> Existing Projects Into Workspace


-----------
1) add into conf/routes

# service in POST
POST	/geturldata					controllers.JKaleoController.getUrlData

----------
markdown2 README.md > app/views/index.html
