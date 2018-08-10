#!/bin/sh

read -p "Enter mathseed username: " uname

echo "Build war file..."
ant -S -buildfile buildHmngoDev.xml DeployRose_WOJ_WarWithoutContent
echo "Copy war to mathseed server..."
scp ./__artifacts_temp/woServer_war_Rose/woj.war $uname@mathseeds.wpi.edu:~
echo "SSH into mathseeds server..."
ssh -t $uname@mathseeds.wpi.edu "cd /var/lib/tomcat7/webapps && sudo mv ~/woj.war ."
