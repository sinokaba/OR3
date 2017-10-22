# Project Title

A native Java Application for the desktop. It is a restaurant review and rating system.
Users can create accounts, look up restaurants, write reviews, and give restaurants scores for different fields.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

* Eclipse IDE
* [MySQL Java Connector](https://dev.mysql.com/downloads/connector/j/5.1.html)

### Installing

Create a new Java Project on Eclipse, and name it whatever you want

Download or Fork the repository

Move all the repository files, main/java and static, to the src folder of your created Java project

If you don't have the MySQL J connector, use the link above and download it

Right click the src folder in Eclipse where your project is, and hover over build path. Choose the "Configure Build path" option.

Go to Libraries tab and click the "Add External Jars" option.

Look for where your downloaded java connector is located, and add the jar file to the external libary.

## Still need to do

* Add dummy restaurants for testing
* Add Test client and test cases
* Add search functionality
* Add password hashing
* Add UI for when a restaurant is searched, the restaurant's page itself, and the user's account page
* plus other stuff I probably forgot...
