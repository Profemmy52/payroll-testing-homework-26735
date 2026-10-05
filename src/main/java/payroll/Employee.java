package payroll;

/** A simple employee record. Position is always stored in UPPER CASE. */
public class Employee {
    private final int id;
    private final String name;
    private final String position;
    private double basicSalary;

    public Employee(int id, String name, String position, double basicSalary) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.basicSalary = basicSalary;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | %.2f", id, name, position, basicSalary);
    }
}
