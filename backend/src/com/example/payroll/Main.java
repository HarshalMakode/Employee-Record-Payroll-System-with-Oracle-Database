import java.sql.*;
import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Employee Payroll System (Oracle DB) ===");

        while (true) {
            System.out.println("\n1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Generate Payslip");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> addEmployee();
                case 2 -> viewEmployees();
                case 3 -> updateEmployee();
                case 4 -> deleteEmployee();
                case 5 -> generatePayslip();
                case 6 -> { System.out.println("Goodbye!"); System.exit(0); }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void addEmployee() {
        try (Connection con = DBConnection.getConnection()) {
            System.out.print("Enter ID: ");
            int id = sc.nextInt(); sc.nextLine();
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Department: ");
            String dept = sc.nextLine();
            System.out.print("Enter Basic Salary: ");
            double basic = sc.nextDouble();

            String sql = "INSERT INTO employees VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, dept);
            ps.setDouble(4, basic);
            ps.executeUpdate();
            System.out.println("✅ Employee added successfully.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void viewEmployees() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM employees")) {

            System.out.println("\n--- Employee List ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Dept: %s | Basic: %.2f\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("basic_salary"));
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void updateEmployee() {
        try (Connection con = DBConnection.getConnection()) {
            System.out.print("Enter ID to update: ");
            int id = sc.nextInt(); sc.nextLine();
            System.out.print("Enter new Name: ");
            String name = sc.nextLine();
            System.out.print("Enter new Department: ");
            String dept = sc.nextLine();
            System.out.print("Enter new Basic Salary: ");
            double basic = sc.nextDouble();

            String sql = "UPDATE employees SET name=?, department=?, basic_salary=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, dept);
            ps.setDouble(3, basic);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Updated successfully.");
            else
                System.out.println("⚠️ Employee not found.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void deleteEmployee() {
        try (Connection con = DBConnection.getConnection()) {
            System.out.print("Enter ID to delete: ");
            int id = sc.nextInt();
            String sql = "DELETE FROM employees WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Deleted successfully.");
            else
                System.out.println("⚠️ Employee not found.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void generatePayslip() {
        try (Connection con = DBConnection.getConnection()) {
            System.out.print("Enter Employee ID: ");
            int id = sc.nextInt();
            String sql = "SELECT basic_salary FROM employees WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double basic = rs.getDouble("basic_salary");
                PayrollService.generatePayslip(basic);
            } else {
                System.out.println("⚠️ Employee not found.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
