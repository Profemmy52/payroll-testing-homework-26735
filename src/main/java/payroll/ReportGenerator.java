package payroll;

import java.util.List;

/** Builds the text reports. */
public class ReportGenerator {

    public String employeeReport(List<Employee> employees) {
        if (employees.isEmpty()) {
            return "No employees registered.";
        }
        StringBuilder sb = new StringBuilder("ID | Name | Position | Basic salary\n");
        for (Employee e : employees) {
            sb.append(e).append("\n");
        }
        return sb.toString();
    }

    public String payrollReport(List<Payslip> slips) {
        if (slips.isEmpty()) {
            return "No payroll records.";
        }
        double total = 0;
        double highest = slips.get(0).net;
        double lowest = slips.get(0).net;
        for (Payslip p : slips) {
            total += p.net;
            if (p.net > highest) {
                highest = p.net;
            }
            if (p.net < lowest) {
                lowest = p.net;
            }
        }
        return String.format("Payments: %d | Total net: %.2f | Highest: %.2f | Lowest: %.2f",
                slips.size(), total, highest, lowest);
    }
}
