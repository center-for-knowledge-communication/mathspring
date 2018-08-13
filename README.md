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
   - git clone git@github.com:nsmenon/mathspring-V2.git


2. You will need to install Tomcat 8 (apache-tomcat-8.0.53 recommended) and configure eclipse to find its installation directory.

3.  Make sure your Eclipse project points at the correct Java JDK 
(vers 8 or higher - same with the language level because of lambdas in use)

4.  Project dependencies are managed by Maven so you will want to make sure you have the eclipse with maven plugin installed.
The project is maintained as a single module (pom.xml) with all the dependencies and plugins configured under them.You should make changes to the Project's pom.xml unless you
need to bring in new libraries to support what you are doing (take a look at
whats in there, as there are likely libraries that will be useful).

5. The META-INF/context.xml is how mathspring knows how to connect to a database.The web.xml file in web/WEB-INF/web.xml
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

7. Once you have created the project, add the server and configured them to your project in eclipse. Go to project settings
and open the deployment asssembly tab and make sure you have the following entries there
Source					Deploy Path
/src					WEB-INF/classes
/resources				/
/web					/
Maven Dependencies		WEB-INF/lib
