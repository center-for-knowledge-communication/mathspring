# mathspring-V2

Mathspring Version 2.0 compatible with AWS

This repo can be cloned or forked but you will still need to do some setup.

This repo contains the Java source files that make up the server side of mathspring and the Javascript/HTML/CSS
that makes up the client side.

The project is set up to work with Intellij IDEA 2016 Ultimate edition.

The web/META-INF has not been included in this repository because it contains
the context.xml which has database user names and passwords.  More explanation of
this is given in step 5 below.

There is a large amount of math content that is also not part of this repo.  You can still develop and run the tutor
without it but if you need to see math problems as part of your testing, then you will need to get this.
That is explained below in step 6.

Many IntelliJ files have been submitted, and this code is easiest to work with in IntelliJ.

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
   - git clone git@github.com:marshall62/mathspring.git


2. You will need to install Tomcat 8 and configure IntelliJ to find its installation directory.
This is done by Clicking Run | Edit Configurations , Click + in upper left.
Add a local Tomcat Server (you may need to enable the Tomcat plug-in for IDEA)

3.  Make sure your Intellij project points at the correct Java JDK 
(vers 8 or higher - same with the language level because of lambdas in use)

4.  Project dependencies are managed by Maven so you will want to enable the IDEA
maven plug-in.  Each module has its own pom.xml which you shouldn't mess with unless you
need to bring in new libraries to support what you are doing (take a look at
whats in there, as there are likely libraries that will be useful).  Open the Maven Projects
tool window (available off the View | Tool Window menu).   Once you have loaded all
the code into Intellij IDEA, you need to tell Maven to go fetch all the libraries
that the project depends on.   In the Maven tool window, click on the recycle
arrow icon to Reimport all Maven projects.   THis may take some time to get all the
stuff and put it in your local maven repo 

5. The META-INF/context.xml is how mathspring knows how to connect to a database.  This
file has been removed from the git repo.   If you are a member of the project
you can get one from me (marshall62@gmail.com).   If you are not, you
can create a standard context.xml that defines where your mysql db
is and the user/password for connecting to a wayangoutpostdb (which
you will also need to get from me).   The web.xml file in web/WEB-INF/web.xml
defines an init-paramter called wodb.datasource.  It should be set to
jdbc/wodblocal if you want to connect to your local db.   Thus your context.xml
must have a resource like:

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

7.  Now you should be able to run the system.  Click the Run | Edit Configurations
menu and create a new Tomcat Server (local).  It should be pointing at your 
installation tomcat if you followed step 2 correctly.  In the lower right
you will see a button that says "Warning no artifacts marked for deployment" with a 
"Fix" button .  Click the button and select woServer web-localhost exploded.
Now go to the deployment tab of this dialog and give it a deployment context
of /mt.  Return to the main tab and make sure the URL to main page is now:
http://localhost:8080/mt/  and that it will automatically open a browser (Chrome is best
for mathspring) to this page when you run.   Call this runtime configuration "Mathspring"
.  You should now be able to Click Run | Run Mathspring or Run | Debug Mathspring
and it will pop up a browser that takes you to an index.html with links
to various parts of the system.  Note this index.html lives in /woServer/resources/index.html
where you will find much of the front-end support for the system including JSPs, Javascript,
HTML, CSS and images.



The instructions below relate to set-up for a Mac OS and haven't been tested
in awhile.  

Getting MathSpring to Local

(For Mac OS)

1. Open a GIT hub account at https://github.com

2. Fork the repository on Github, and clone the forked repository under your user.

4. Download and install IntelliJ IDEA. You can start with a 30 day free trial - https://

www.jetbrains.com/idea/download/

5. Get JDK if you don't have one already https://www.java.com/en/download/

> java -version

should display your java version

A. Get tar.gz from here - http://tomcat.apache.org/download-70.cgi (Binary Distributions /

B. Unarchive the download

C. Move to /usr/local

D. Create a symbolic link to refer to this tomcat

E. Change ownership of this folder hierarchy:

F. Make all scripts executable:

7. Install MySQL driver for Mac OS

> sudo mv ~/Downloads/apache-tomcat-7.0.64 /usr/local

> sudo ln -s /usr/local/apache-tomcat-7.0.47 /Library/Tomcat

> sudo chown -R <username> /Library/Tomcat

> sudo chmod +x /Library/Tomcat/bin/*.sh

A. Download DMG from https://dev.mysql.com/downloads/mysql/ and follow through the

instructions to install.

B. Set up your user and password-

> cd /usr/local/mysql/bin/

> ./mysql -u root -h localhost -p

mysql> update mysql.user SET Password = PASSWORD(‘< >’) WHERE User = 'root’;

mysql> FLUSH PRIVILEGES;

Mathspring Set up

1. Open IntelliJ IDEA Environment

2. In the welcome screen, select “Checkout from Version Control” > GitHub

3. Select the mathspring repository

4. The project should be ready to use. But if you see an error in the BaseServlet.java, you

need to fix a dependency -

Right click on project > Open Module Settings > Modules > + > Module dependency >

select emailer module > OK

5. Create wayangoutpost DB in local

> ./mysqldump --single-transaction -uWayangServer -p -h rose.cs.umass.edu

wayangoutpostdb > "/Users/S****/Doc***/local.sql”

> ./mysql -u root -h localhost -p

mysql> create database wayangoutpostdb;

> ./mysql -u root -p wayangoutpostdb < "/Users/S****/Doc***/local.sql”


Note: Some versions of MySQL are not happy with the import.  You may 
have to search from strings like ROW_FORMAT=FIXED in the .sql file and replace
with empty string if MySQL complains about these options not being available

A. View > Tools Window > Databases > + > datasource > MySQL

B. URL: jdbc:mysql://localhost:3306/wayangoutpostdb

C. Enter your user and password

D. Run basic SQL queries to validate

7. Update context.xml with the values of your local datasource
