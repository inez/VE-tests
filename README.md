VE-tests (2013)
========

These are written in Java using the Selenium test framework.
You'll need to install
* Java JDK
* Apache Maven
* chromedriver from http://chromedriver.storage.googleapis.com/index.html (and a compatible version of Google Chrome/chromium).

Run with 'mvn clean test'. This should download all the needed files.

You can use FirefoxDriver instead, or load VE at a different URL, by editing test/java/BaseTest.java
