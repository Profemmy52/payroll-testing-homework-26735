package payroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Console menu for the Employee Payroll System. */
public class Main {
    public static void main(String[] args) {
        EmployeeService service = new EmployeeService();
        PayrollCalculator calculator = new PayrollCalculator();
        ReportGenerator reports = new ReportGenerator();
        List<Payslip> payslips = new ArrayList<>();
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("\n1 Add  2 List  3 Update salary  4 Remove  5 Search  6 Pay  7 Payroll report  0 Exit");
            System.out.print("Choose: ");
            String choice = in.nextLine().trim();
            try {
                if (choice.equals("0")) {
                    break;
                } else if (choice.equals("1")) {
                    System.out.print("ID, name, position, salary (comma separated): ");
                    String[] p = in.nextLine().split(",");
                    service.addEmployee(Integer.parseInt(p[0].trim()), p[1], p[2], Double.parseDouble(p[3].trim()));
                    System.out.println("Employee added.");
                } else if (choice.equals("2")) {
                    System.out.println(reports.employeeReport(service.getAll()));
                } else if (choice.equals("3")) {
                    System.out.print("ID, new salary: ");
                    String[] p = in.nextLine().split(",");
                    service.updateSalary(Integer.parseInt(p[0].trim()), Double.parseDouble(p[1].trim()));
                    System.out.println("Salary updated.");
                } else if (choice.equals("4")) {
                    System.out.print("ID: ");
                    boolean removed = service.removeEmployee(Integer.parseInt(in.nextLine().trim()));
                    System.out.println(removed ? "Employee removed." : "Employee not found.");
                } else if (choice.equals("5")) {
                    System.out.print("ID: ");
                    Employee e = service.findById(Integer.parseInt(in.nextLine().trim()));
                    System.out.println(e == null ? "Employee not found." : e.toString());
                } else if (choice.equals("6")) {
                    System.out.print("ID, absent days: ");
                    String[] p = in.nextLine().split(",");
                    Payslip slip = calculator.calculate(service.findById(Integer.parseInt(p[0].trim())),
                            Integer.parseInt(p[1].trim()));
                    payslips.add(slip);
                    System.out.println(slip);
                } else if (choice.equals("7")) {
                    System.out.println(reports.payrollReport(payslips));
                } else {
                    System.out.println("Unknown option.");
                }
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        System.out.println("Goodbye.");
    }
}
