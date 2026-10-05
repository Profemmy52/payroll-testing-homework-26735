#!/bin/bash
# Fault injection: copy the code, inject one bug at a time, run the tests,
# and list which test cases detect (fail on) the bug.
run_fault () {
  name="$1"; file="$2"; from="$3"; to="$4"
  rm -rf /tmp/fi && mkdir -p /tmp/fi/src
  cp -r src/main src/test /tmp/fi/src/
  python3 - "$file" "$from" "$to" <<'PY'
import sys
f, a, b = sys.argv[1:4]
p = "/tmp/fi/src/main/java/payroll/" + f
s = open(p).read()
assert a in s, "pattern not found"
open(p, "w").write(s.replace(a, b, 1))
PY
  mkdir -p /tmp/fi/out
  javac -d /tmp/fi/out /tmp/fi/src/main/java/payroll/*.java /tmp/fi/src/test/java/payroll/TestRunner.java
  found=$(java -cp /tmp/fi/out payroll.TestRunner | grep MISMATCH | cut -d' ' -f1 | tr '\n' ' ')
  echo "$name -> detected by: ${found:-NONE (fault survived)}"
}
run_fault "F1 tax rate 10% changed to 15%"        PayrollCalculator.java "* 0.10;" "* 0.15;"
run_fault "F2 salary check < changed to <="        EmployeeService.java  "salary < MIN_SALARY" "salary <= MIN_SALARY"
run_fault "F3 absent-days limit off by one"        PayrollCalculator.java "absentDays > WORKING_DAYS" "absentDays > WORKING_DAYS + 1"
run_fault "F4 MANAGER allowance 50000 -> 5000"     PayrollCalculator.java "return 50000;" "return 5000;"
run_fault "F5 null check removed in calculate()"   PayrollCalculator.java "if (e == null)" "if (false)"
run_fault "F6 lowest-net comparison reversed"      ReportGenerator.java  "p.net < lowest" "p.net > lowest"
