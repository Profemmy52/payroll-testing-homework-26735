package payroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * Plain-Java test runner (no libraries needed).
 * Each test returns "OK: ..." when the code accepts the input, or
 * "REJECTED: ..." when the code throws an IllegalArgumentException.
 * A test is MATCHED when the actual text starts with the expected text.
 */
public class TestRunner {
    private static int matched = 0;
    private static int total = 0;

    private static void test(String id, String technique, String expected, Supplier<String> body) {
        String actual;
        try {
            actual = "OK: " + body.get();
        } catch (IllegalArgumentException ex) {
            actual = "REJECTED: " + ex.getMessage();
        } catch (Exception ex) {
            actual = "CRASH: " + ex.getClass().getSimpleName();
        }
        boolean ok = actual.startsWith(expected);
        total++;
        if (ok) {
            matched++;
        }
        System.out.println(id + " | " + (ok ? "MATCHED" : "MISMATCH") + " | " + technique
                + " | expected: " + expected + " | actual: " + actual);
    }

    private static String money(double v) {
        return String.format(Locale.US, "%.2f", v);
    }

    /** Fresh service holding one employee: 1001, Alice Uwase, STAFF, 150000. */
    private static EmployeeService withAlice() {
        EmployeeService s = new EmployeeService();
        s.addEmployee(1001, "Alice Uwase", "STAFF", 150000);
        return s;
    }

    public static void main(String[] args) {
        PayrollCalculator calc = new PayrollCalculator();
        ReportGenerator rep = new ReportGenerator();

        // ---------------- BLACK-BOX TESTS (from the requirements only) ----------------
        test("TC1", "BB valid partition", "OK: 1001",
                () -> "" + new EmployeeService().addEmployee(1001, "Alice Uwase", "STAFF", 150000).getId());
        test("TC2", "BB boundary (ID below minimum)", "REJECTED: Invalid ID",
                () -> "" + new EmployeeService().addEmployee(0, "Alice Uwase", "STAFF", 150000));
        test("TC3", "BB boundary (name too short)", "REJECTED: Invalid name",
                () -> "" + new EmployeeService().addEmployee(1002, "A", "STAFF", 150000));
        test("TC4", "BB invalid partition (position)", "REJECTED: Invalid position",
                () -> "" + new EmployeeService().addEmployee(1003, "Bob Kamali", "Director", 150000));
        test("TC5", "BB boundary (salary below minimum)", "REJECTED: Invalid salary",
                () -> "" + new EmployeeService().addEmployee(1004, "Bob Kamali", "STAFF", 49999));
        test("TC6", "BB boundary (salary = minimum)", "OK: 1005",
                () -> "" + new EmployeeService().addEmployee(1005, "Bob Kamali", "STAFF", 50000).getId());
        test("TC7", "BB boundary (salary = maximum)", "OK: 1006",
                () -> "" + new EmployeeService().addEmployee(1006, "Bob Kamali", "MANAGER", 2000000).getId());
        test("TC8", "BB boundary (salary above maximum)", "REJECTED: Invalid salary",
                () -> "" + new EmployeeService().addEmployee(1007, "Bob Kamali", "MANAGER", 2000001));
        test("TC9", "BB error guessing (duplicate ID)", "REJECTED: Duplicate ID",
                () -> "" + withAlice().addEmployee(1001, "Alice Uwase", "STAFF", 150000));
        test("TC10", "BB valid partition (search)", "OK: Alice Uwase",
                () -> withAlice().findById(1001).getName());
        test("TC11", "BB valid partition (update)", "OK: 200000.00",
                () -> {
                    EmployeeService s = withAlice();
                    s.updateSalary(1001, 200000);
                    return money(s.findById(1001).getBasicSalary());
                });
        test("TC12", "BB invalid partition (unknown ID)", "REJECTED: Employee not found",
                () -> {
                    withAlice().updateSalary(9999, 100000);
                    return "updated";
                });
        test("TC13", "BB valid partition (remove)", "OK: removed=true, count=0",
                () -> {
                    EmployeeService s = withAlice();
                    boolean r = s.removeEmployee(1001);
                    return "removed=" + r + ", count=" + s.getAll().size();
                });
        test("TC14", "BB valid partition (payment)", "OK: gross=450000.00, net=370000.00",
                () -> {
                    Payslip p = calc.calculate(new Employee(2001, "Carine Mutesi", "MANAGER", 400000), 0);
                    return "gross=" + money(p.gross) + ", net=" + money(p.net);
                });
        test("TC15", "BB boundary (absent days above maximum)", "REJECTED: Invalid absent days",
                () -> "" + calc.calculate(withAlice().findById(1001), 23));

        // ---------------- WHITE-BOX TESTS (from the code structure) ----------------
        test("TC16", "WB branch 1 of calculateTax", "OK: 0.00",
                () -> money(calc.calculateTax(100000)));
        test("TC17", "WB branch 2 of calculateTax", "OK: 10000.00",
                () -> money(calc.calculateTax(200000)));
        test("TC18", "WB branch 3 of calculateTax", "OK: 45000.00",
                () -> money(calc.calculateTax(400000)));
        test("TC19", "WB statement coverage (SUPERVISOR + absence)", "OK: gross=75000.00, net=71250.00",
                () -> {
                    Payslip p = calc.calculate(new Employee(2002, "David Habimana", "SUPERVISOR", 110000), 11);
                    return "gross=" + money(p.gross) + ", net=" + money(p.net);
                });
        test("TC20", "WB condition coverage (absentDays < 0)", "REJECTED: Invalid absent days",
                () -> "" + calc.calculate(withAlice().findById(1001), -1));
        test("TC21", "WB branch (employee is null)", "REJECTED: Employee not found",
                () -> "" + calc.calculate(null, 0));
        test("TC22", "WB branch (empty list)", "OK: No payroll records.",
                () -> rep.payrollReport(new ArrayList<Payslip>()));
        test("TC23", "WB loop + highest/lowest branches", "OK: Payments: 2 | Total net: 108750.00 | Highest: 71250.00 | Lowest: 37500.00",
                () -> {
                    List<Payslip> list = Arrays.asList(
                            new Payslip(1, "A", 75000, 3750, 0, 71250),
                            new Payslip(2, "B", 40000, 2000, 500, 37500));
                    return rep.payrollReport(list);
                });

        test("TC24", "WB branch (empty employee list)", "OK: No employees registered.",
                () -> rep.employeeReport(new ArrayList<Employee>()));

        System.out.println("SUMMARY | total=" + total + " | matched=" + matched + " | mismatched=" + (total - matched));
        if (matched != total) {
            System.exit(1);
        }
    }
}
