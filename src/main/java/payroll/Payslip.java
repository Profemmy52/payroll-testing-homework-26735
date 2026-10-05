package payroll;

/** Result of one payment calculation. */
public class Payslip {
    public final int employeeId;
    public final String name;
    public final double gross;
    public final double social;
    public final double tax;
    public final double net;

    public Payslip(int employeeId, String name, double gross, double social, double tax, double net) {
        this.employeeId = employeeId;
        this.name = name;
        this.gross = gross;
        this.social = social;
        this.tax = tax;
        this.net = net;
    }

    @Override
    public String toString() {
        return String.format("%d | %s | gross %.2f | social %.2f | tax %.2f | net %.2f",
                employeeId, name, gross, social, tax, net);
    }
}
