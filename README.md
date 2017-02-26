# graid-trax
JPA demo for DMACC DIAD

Instructions for local setup:

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

Install the Gradle Buildship Integration plugin in Eclipse from Help >> Eclipse Marketplace if you don't have it already

Right click on the project

Go down to Gradle and Refresh Gradle Project 

Under the project go to:

scr >> META-INF and open Persistence.xml

Select the source tab at the bottom of the screen

In the property called javax.persistence.jdbc.password change the password from SHAZAM2013 to whatever your password is for the root user on your database

Setup database:

Start your database and enter the mysql client in the terminal/command prompt

Enter the following: CREATE DATABASE graidtrax;

Enter the following: use graidtrax;

Enter the following: grant all privileges on graidtrax_db.* to shazam@localhost identified by 'SHAZAM2013';

Enter the following: EXIT;

Reopen the mysql client but this time sign in with a userid of shazam and a password of SHAZAM2013

Enter the following: use graidtrax_db;

Enter the following: CREATE TABLE STUDENT_TESTS(ID INT NOT NULL AUTO_INCREMENT,FIRST_NAME VARCHAR(20),LAST_NAME VARCHAR(20), TEST_DATE DATE, SCORE DECIMAL, PRIMARY KEY(ID));
