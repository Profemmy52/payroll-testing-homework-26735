# Staff Payroll Management System (Java) - Software Testing Homework 1

A small console application that manages employees, processes their payments and produces reports.
It was built to practise **black-box** and **white-box** testing and **fault injection**.

## Functional requirements
| ID | Requirement |
|----|-------------|
| FR1 | Register an employee (ID, name, position, basic salary) |
| FR2 | Validate employee data (ID > 0, name 2-30 chars, position STAFF/SUPERVISOR/MANAGER, salary 50,000-2,000,000, unique ID) |
| FR3 | Search an employee by ID |
| FR4 | Update an employee's salary |
| FR5 | Remove an employee |
| FR6 | Gross pay = basic - (basic / 22 x absent days) + allowance (MANAGER 50,000; SUPERVISOR 20,000; STAFF 0) |
| FR7 | Social contribution 5%, progressive tax (0% to 100,000; 10% to 300,000; 25% above), net pay |
| FR8 | Refuse payment for an unknown employee or absent days outside 0-22 |
| FR9 | Employee list report |
| FR10 | Payroll report (count, total, highest, lowest) |

## Project structure
```
src/main/java/payroll/   Employee, EmployeeService, Payslip, PayrollCalculator, ReportGenerator, Main
src/test/java/payroll/   TestRunner.java  (24 test cases: TC1-TC15 black-box, TC16-TC24 white-box)
docs/                    Test_Case_Report.docx  (report in the test-case-report template layout)
run_tests.sh             compile and run all test cases
run_faults.sh            fault injection: injects 6 bugs one at a time and shows which test cases catch them
```

## Requirements
JDK 11 or newer. No external libraries.

## Run the application
```bash
mkdir -p out && javac -d out src/main/java/payroll/*.java
java -cp out payroll.Main
```
Menu input examples: add `1001, Alice Uwase, STAFF, 150000`; pay `1001, 0`.

## Run the tests
```bash
./run_tests.sh      # expect: SUMMARY | total=24 | matched=24 | mismatched=0
./run_faults.sh     # expect: every fault F1-F6 detected
```
On Windows without bash, use the `javac` / `java` commands inside the scripts.

## Testing techniques used
- **Black-box:** equivalence partitioning, boundary value analysis (ID, name length, salary 49,999 / 50,000 / 2,000,000 / 2,000,001, absent days 23), error guessing (duplicate ID).
- **White-box:** branch coverage of `calculateTax` (3 paths), condition coverage of `calculate`, statement coverage, loop coverage in `payrollReport`.
- **Fault injection:** see `run_faults.sh` and Section 6 of the report.

The test report (`docs/Test_Case_Report.docx`) lists every test case with its input, expected result, actual result and status.
