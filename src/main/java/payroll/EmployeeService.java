package payroll;

import java.util.ArrayList;
import java.util.List;

/** Manages employees (add, search, update salary, remove). Data is kept in memory. */
public class EmployeeService {
    public static final double MIN_SALARY = 50000;
    public static final double MAX_SALARY = 2000000;

    private final List<Employee> employees = new ArrayList<>();

    public Employee addEmployee(int id, String name, String position, double basicSalary) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid ID: must be a positive number");
        }
        if (name == null || name.trim().length() < 2 || name.trim().length() > 30) {
            throw new IllegalArgumentException("Invalid name: 2-30 characters required");
        }
        if (position == null) {
            throw new IllegalArgumentException("Invalid position");
        }
        String pos = position.trim().toUpperCase();
        if (!pos.equals("STAFF") && !pos.equals("SUPERVISOR") && !pos.equals("MANAGER")) {
            throw new IllegalArgumentException("Invalid position: use STAFF, SUPERVISOR or MANAGER");
        }
        checkSalary(basicSalary);
        if (findById(id) != null) {
            throw new IllegalArgumentException("Duplicate ID: employee already exists");
        }
        Employee e = new Employee(id, name.trim(), pos, basicSalary);
        employees.add(e);
        return e;
    }

    public Employee findById(int id) {
        for (Employee e : employees) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    public void updateSalary(int id, double newSalary) {
        Employee e = findById(id);
        if (e == null) {
            throw new IllegalArgumentException("Employee not found");
        }
        checkSalary(newSalary);
        e.setBasicSalary(newSalary);
    }

    public boolean removeEmployee(int id) {
        Employee e = findById(id);
        if (e == null) {
            return false;
        }
        employees.remove(e);
        return true;
    }

    public List<Employee> getAll() {
        return new ArrayList<>(employees);
    }

    private void checkSalary(double salary) {
        if (Double.isNaN(salary) || salary < MIN_SALARY || salary > MAX_SALARY) {
            throw new IllegalArgumentException("Invalid salary: must be between 50000 and 2000000");
        }
    }
}
