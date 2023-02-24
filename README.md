# ApiDemo-appium-java-maven-ufg
A Basic example of Applitools Native Mobile Grid (NMG) test repository using Appium, Java, Maven for Android apps.

## Pre-requisites

### IntelliJ IDEA

A very useful tool may be used for Java code editing while managing dependencies.

### Appium Server

The Appium server provides an API to interact with the mobile devices. In the current example only Android devices will be considered. 

### Appium Inspector

The Appium Inspector is now separately distributed and it is necessary to determine element selector in the app view.

### Android Studio

This is required only to spawn Android devices where the app is expected to run. The visual tests will be performed on these apps.

### Python 3

The python3 is required to execute an Applitools script which instruments the Android app before performing the visual test.

## Installation

### Project setup

Build the project dependencies based on the `pom.xml` file contents. 

### Add Applitools dependencies 

Download the above mentioned python script using the command below:
```bash
curl -O https://raw.githubusercontent.com/applitools/applitoolsify/main/applitoolsify.py
```
Next, run the script to instrument the app (or .apk file):
```bash
python3 applitoolsify.py <relative path to .apk file> android_nmg
```
On successful execution of the above command the instrumented app will be created in the folder `intrumented-apk` and the new app will be `ready.apk`.
The `Eyes` object in the test code should execute the method `setNMGCapabilities` with Appium Capability object, Applitools API Key and Applitools Server URL as mentioned below:
```bash
Eyes.setNMGCapabilities(caps, <APPLITOOLS API KEY>, <APPLITOOLS SERVER URL>);
```

## Running the test

### Start an Android device

Use Android Studio to launch a device. Please note that the same device type and Android Sdk version must be used in the test code.

### Start Appium server

The server can be started on the standard port either by command line or by the application. The repository uses the version 1.X of the package. The Appium logs provides vital logging information.

### Launch IntelliJ IDEA

The code can be executed using the `Arrow` adjacent to the starting line number of the test methods. If all the tests within a class needs to be executed then click the `arrow` next to the starting line number of the class itself. The runtime logs of IntelliJ IDEA must be open to ascertain the test results and logging information.
