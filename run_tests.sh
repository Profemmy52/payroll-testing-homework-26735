#!/bin/bash
# Compile the project and run all test cases.
set -e
rm -rf out && mkdir out
javac -d out src/main/java/payroll/*.java src/test/java/payroll/TestRunner.java
java -cp out payroll.TestRunner
