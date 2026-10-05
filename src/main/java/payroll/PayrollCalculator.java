package payroll;

/** Calculates gross pay, social contribution, tax and net pay. */
public class PayrollCalculator {
    public static final int WORKING_DAYS = 22;
    public static final double SOCIAL_RATE = 0.05;

    public double getAllowance(String position) {
        if (position.equals("MANAGER")) {
            return 50000;
        } else if (position.equals("SUPERVISOR")) {
            return 20000;
        } else {
            return 0;
        }
    }

    /** Progressive tax: 0% up to 100,000; 10% up to 300,000; 25% above. */
    public double calculateTax(double gross) {
        if (gross <= 100000) {
            return 0;
        } else if (gross <= 300000) {
            return (gross - 100000) * 0.10;
        } else {
            return 20000 + (gross - 300000) * 0.25;
        }
    }

    public Payslip calculate(Employee e, int absentDays) {
        if (e == null) {
            throw new IllegalArgumentException("Employee not found");
        }
        if (absentDays < 0 || absentDays > WORKING_DAYS) {
            throw new IllegalArgumentException("Invalid absent days: must be 0-22");
        }
        double absenceCut = e.getBasicSalary() / WORKING_DAYS * absentDays;
        double gross = e.getBasicSalary() - absenceCut + getAllowance(e.getPosition());
        double social = gross * SOCIAL_RATE;
        double tax = calculateTax(gross);
        double net = gross - social - tax;
        return new Payslip(e.getId(), e.getName(), gross, social, tax, net);
    }
}
