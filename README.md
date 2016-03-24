# graid-trax
JPA demo for DMACC DIAD

Instructions for local setup:

Download latest version of Hibernate from http://hibernate.org/orm/downloads/

Download JDBC driver for MySQL from: http://dev.mysql.com/downloads/connector/j/

In Eclipse:

File >> Import

Expand Git

Select Projects from Git and click next

Select Clone URI and click next

In the URI field paste https://github.com/lxdraw/graid-trax.git

Click next

Click next again

In the Directory field browse for your workspace

Click next 

Click next again

Click finish

Open Preferences
  Windows: Windows >> Preferences 
  Mac: Eclipse >> Preferences
  
Java >> Build Path >> User Libraries

Click New

Enter a name for the library

Click OK

Select the library from the list then click Add External Jars

Navigate to where you downloaded Hibernate

Go into lib >> required

Select ALL the jars

Once more select the library from the list then click Add External Jars

Navigate to where you downloaded Hibernate

Go into lib >> jpa

Select the jar by the name of hibernate-entitymanager-<versionnumber>.jar

Click Ok

Right click on the graid-trax project and click Build Path >> Configure Build Path

Under the Libraries tab: 

Click Add Library

Select User Library and click next

Click the checkbox next to the library you just created for Hibernate

Click Finish

Click Apply 

Click Add External JARs

Navigate to where you downloaded the MySQL JDBC driver

Select the jar by the name of mysql-connector-java-<versionnumber>.jar

Click Apply 

Click OK

Project >> Clean 

Click the checkbox next to the graid-trax project and click OK

Under the project go to:

scr >> META-INF and open Persistence.xml

Select the source tab at the bottom of the screen

In the property called javax.persistence.jdbc.password change the password from SHAZAM2013 to whatever your password is for the root user on your database

Setup database:

Start your database and enter the mysql client in the terminal/command prompt

Enter the following: CREATE DATABASE graidtrax_db;

Enter the following: use graidtrax_db;

Enter the following: grant all privileges on graidtrax_db.* to shazam@localhost identified by 'SHAZAM2013';

Enter the following: EXIT;

Reopen the mysql client but this time sign in with a userid of shazam and a password of SHAZAM2013

Enter the following: use graidtrax_db;

Enter the following: CREATE TABLE STUDENT_TESTS(ID INT NOT NULL AUTO_INCREMENT,FIRST_NAME VARCHAR(20),LAST_NAME VARCHAR(20), TEST_DATE DATE, SCORE DECIMAL, PRIMARY KEY(ID));
