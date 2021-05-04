# mathspring-V2

Mathspring Version 2.0 compatible with AWS

This repo can be cloned or forked but you will still need to do some setup.

This repo contains the Java source files that make up the server side of mathspring and the Javascript/HTML/CSS
that makes up the client side.

The project is set up to work with Eclipse Oxygen.1a Release (4.7.1a) edition.

There is a large amount of math content that is also not part of this repo.  You can still develop and run the tutor
without it but if you need to see math problems as part of your testing, then you will need to get this.
That is explained below in step 6.

---INSTALLATION INSTRUCTIONS---

1. If you are person who wants to do a project based on mathspring with the possibility of having your
changes added to the system, fork the repository on Github, and clone the forked repository under your user.
You will then need to keep track of the upstream branch to make sure you get 
fixes that are being added as you work on your own branch.  At some point you
may submit a pull request and your code will be reviewed and potentially integrated.
It will go smoother for you if you make sure your commits are small and commented.

Mathspring developers: Clone the repository on your local machine.
   - On Windows use Git Bash/  Linux use CLI git.  Mac Users are on your own.
   - cd to dir where you want it
   - git clone https://github.com/center-for-knowledge-communication/mathspring.git

2. You will need to install Tomcat (apache-tomcat-9.0.35 is the latest version) and configure eclipse to find its installation directory.  You will also need Java runtime environment. For a MAC OSX you will need the whole Java Development Kit. 

Useful video for installing Java on a MAC, and updating  path variables:

https://www.youtube.com/watch?v=sNYvYTmOGhU

3.  Install Eclipse. Make sure your Eclipse project points at the correct Java JDK 
(vers 8 or higher - same with the language level because of lambdas in use)

A few videos that were useful for installing Eclipse for the MAC:
https://www.youtube.com/watch?v=CL36-szyr1c

There is a Video here where Ankit explains how to set up Eclipse with Maven and Tomcat and the database:
https://drive.google.com/drive/u/0/folders/1QdoEQaZ1iBiVH0J6bGjFv6BiZ9xHQDoY

We got an error at the end, as apparently it needed Java 8 and no later.
See here how to install an older version of Java on a MAC:
https://support.planwithvoyant.com/hc/en-us/articles/209725003-Install-or-revert-to-a-prior-version-of-Java-Mac-OS-X

4.  Project dependencies are managed by Maven so you will want to make sure you have the eclipse with maven plugin installed.
The project is maintained as a single module (pom.xml) with all the dependencies and plugins configured under them.You should make changes to the Project's pom.xml unless you
need to bring in new libraries to support what you are doing (take a look at
whats in there, as there are likely libraries that will be useful).

5. The META-INF/context.xml is how mathspring knows how to connect to a database.The web.xml file in web/WEB-INF/web.xml
defines an init-paramter called wodb.datasource.  It should be set to
jdbc/wodblocal if you want to connect to your local db.   Thus your context.xml
must have a resource like:

```
<Resource name="jdbc/wodblocal" type="javax.sql.DataSource"
            auth="Container" description="Mysql database for Mathforum"
            maxActive="100" maxIdle="30" maxWait="10000" username="MyDBUser"
            password="MyDBPassword" removeAbandoned="true" removeAbandonedTimeout="60"
            logAbandoned="true"  testWhileIdle="true"
            testOnBorrow="true"
            testOnReturn="true"
            timeBetweenEvictionRunMillis="600000"
            numTestsPerEvictionRun="10"
            validationQuery="SELECT 1"
            driverClassName="com.mysql.jdbc.Driver"
            url="jdbc:mysql://localhost/wayangoutpostdb?autoReconnect=true" />
```
If you want to connect to a database elsewhere (e.g. on the rose server) you will
need to add additional resources to the context.xml and set the web.xml init parameter wodb.datasource so it uses
them.

Use Mysql Workbench and make sure this user and password is defined for your database (with only localhost access to be safe) and that
it has select,insert,delete,update access.


Note:  The database was originally designed on a Windows machine where table names
are not case sensitive.   When you install the database on other systems that
have case sensitive file systems, you will need to configure mysql (ubuntu has a 
my.cnf file in /etc/mysql that you can edit  to have lower_case_table_names = 1 )


6. Next you need to get the mathspring math content and unzip it.  This is a dir
called mscontent and it must be placed in your woServer/webResources/mathspring dir (you will need 
to create these dirs).
Get the latest mscontent zip from rose.cs.umass.edu/mathspring/backup/
The result should be /woServer/webResources/mathspring/mscontent.  If you run 
mathspring and get a bunch of 404 errors so that it doesn't show the learning companion
and math problems, it will be because you didn't do this right.

7. Once you have created the project, add the server and configured them to your project in eclipse. Go to project settings
and open the deployment asssembly tab and make sure you have the following entries there
Source					Deploy Path
/src					WEB-INF/classes
/resources				/
/web					/
Maven Dependencies		WEB-INF/lib



## ***Instructions for Windows - Working with IntelliJ***

### Tomcat Installation:

- Download from http://tomcat.apache.org/
- Install Tomcat in a place which doesn't require admin access (eg. not under Program Files).
- Usually Tomcat is installed as a Service on Windows which is started automatically, need to change this behaviour to work with IntelliJ. Open the "Monitor Tomcat" application that is installed by the Tomcat installer. Go to the "General" tab, "Stop" the service and change the "Startup type" to "Manual".

### Setting Environment Variables:
- Set CATALINA_HOME (required).
    1. Variable Name: CATALINA_HOME
    2. Variable Value: F:\Apache Software Foundation\Tomcat 9.0 (change to the directory where you installed Tomcat)
- Set JRE_HOME or JAVA_HOME (required). JAVA_HOME or JRE HOME are the locations of the Java JDK and JRE respectively. Using JAVA_HOME provides access to certain additional startup options that are not allowed when JRE_HOME is used. If both JRE_HOME and JAVA_HOME are specified, JRE_HOME is used. The recommended place to specify these variables is a "setenv" script as we will see next. There is no harm in setting it in the system's environment variables too.
    1. Variable Name: JAVA_HOME
    2. Variable Value: C:\Program Files\Java\jdk1.8.0_202 (change to whatever version of Java you have installed on your machine)

### Using the "setenv" script:
- Navigate to the bin folder of the Tomcat installation.
- Create a .bat file called setenv.bat with the following contents:
    ```
    set "JRE_HOME=%ProgramFiles%\Java\jre1.8.0_202" (change to your JRE version)
    exit /b 0
    ```

### Configuring Tomcat for IntelliJ Community Edition:
- Open IntelliJ -> File -> Settings -> Tools -> External Tools -> + (Add).
- Add a new tool with the name "Tomcat".
- In the Tool Settings, select the catalina.bat file, usually located in the bin folder of the Tomcat installation.
- Set the argument as "jpda run".
- The working directory should be set automatically, if not, set it to the bin folder of the Tomcat installation.

### Start Tomcat from IntelliJ:
- Tools -> External Tools -> Tomcat, this will start the Tomcat server.
- This will keep the Tomcat server running in a terminal.

### Build using Maven and deploy to Tomcat:
- Run -> Edit Configurations -> + (Add) -> Maven.
- Name: Deploy_to_Tomcat
- Working Directory: path/to/MathSpring (the project folder)
- Command Line:
    ```
    org.codehaus.mojo:wagon-maven-plugin:upload-single -Dwagon.fromFile=path\to\MathSpring\release\ms.war -Dwagon.url=file://path\to\webapps\folder\under\tomcat\
    ```
- Remember to replace any spaces by %20 in the path in the Command Line above as it's a URL. For eg.
   ```
   \Apache Software Foundation\Tomcat 9.0\webapps\
   ```
   should be replaced by
   ```
   \Apache%20Software%20Foundation\Tomcat%209.0\webapps\
   ```
- This command line builds the project, using Maven, and then gets the file from the specified path and copies it to the webapps folder in Tomcat. Tomcat then knows it has a new version of the file and redeploys it.
- Add a Maven Goal in the "Before Launch" tool with the command:
   ```
   -e clean package -P local
   ```
- This command will build the war with the src folder included (the previous command doesn't take the src folder into account). Uses the local user profile instead of the production profile.
- Save the configuration and run it.
- This should create a war file under "release" called "ms.war".

### Remote debugging with Tomcat:
- Add a new configuration: Run -> Edit Configurations -> + (Add) -> Remote.
- Name: Debug_Tomcat
- Debugger Mode: Attach to remote JVM
- Transport: Socket
- Host: localhost
- Port: 8000 (can be changed in server.xml under Tomcat)
- The final command line argument (auto-generated) should look like this:
    ```
    -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000
    ```
- Save the configuration and run it (will run in debug mode).
- Now, you should be able to debug with breakpoints.

### Access the application:
    http://localhost:8080/ms/welcome.html
