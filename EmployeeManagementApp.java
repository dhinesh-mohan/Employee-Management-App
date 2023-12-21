import java.io.*;
import java.util.*;

class EmployeeManagementApp {

    private static final String EMPLOYEE_FILE = "employee_records.txt";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\nEmployee Management System");
            System.out.println("1. Show all Employees");
            System.out.println("2. Filter Employees based on criteria");
            System.out.println("3. Search for an Employee");
            System.out.println("4. Update an Employee's Record");
            System.out.println("5. Delete an Employee");
            System.out.println("6. Get Average Salary of a Department");
            System.out.println("7. Calculate the Average Salary of an Employee in the Company");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    showAllEmployees();
                    break;
                case 2:
                    System.out.print("Enter filter criteria (e.g., salary > 50000): ");
                    String criteria = scanner.nextLine();
                    filterEmployees(criteria);
                    break;
                case 3:
                    System.out.print("Enter the employee's full name: ");
                    String employeeName = scanner.nextLine();
                    searchEmployee(employeeName);
                    break;
                case 4:
                    System.out.print("Enter the employee's full name: ");
                    String empName = scanner.nextLine();
                    updateEmployeeRecord(empName);
                    break;
                case 5:
                    System.out.print("Enter the employee's full name: ");
                    String nameToDelete = scanner.nextLine();
                    deleteEmployee(nameToDelete);
                    break;
                case 6:
                    System.out.print("Enter the department: ");
                    String department = scanner.nextLine();
                    calculateAverageSalaryDepartment(department);
                    break;
                case 7:
                    calculateAverageSalaryCompany();
                    break;
                case 8:
                    System.out.println("Exiting Employee Management System. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static List<Map<String, String>> loadEmployeeRecords() {
        List<Map<String, String>> records = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(EMPLOYEE_FILE))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Map<String, String> employee = new HashMap<>();
                String[] parts = line.split(",");
                employee.put("Full Name", parts[0]);
                employee.put("Age", parts[1]);
                employee.put("Date of Birth", parts[2]);
                employee.put("Salary", parts[3]);
                employee.put("Department", parts[4]);
                records.add(employee);
            }
        } catch (FileNotFoundException e) {
            // File not found, create an empty list
        }
        return records;
    }

    private static void saveEmployeeRecords(List<Map<String, String>> records) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EMPLOYEE_FILE))) {
            for (Map<String, String> employee : records) {
                writer.println(
                        String.join(",", employee.get("Full Name"), employee.get("Age"),
                                employee.get("Date of Birth"), employee.get("Salary"),
                                employee.get("Department"))
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showAllEmployees() {
        List<Map<String, String>> records = loadEmployeeRecords();
        if (records.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            for (Map<String, String> employee : records) {
                printEmployeeDetails(employee);
            }
        }
    }

    private static void filterEmployees(String criteria) {
        List<Map<String, String>> records = loadEmployeeRecords();
        List<Map<String, String>> filteredEmployees = new ArrayList<>();
        for (Map<String, String> employee : records) {
            if (evaluateCriteria(employee, criteria)) {
                filteredEmployees.add(employee);
            }
        }
        if (filteredEmployees.isEmpty()) {
            System.out.println("No employees match the criteria.");
        } else {
            for (Map<String, String> employee : filteredEmployees) {
                printEmployeeDetails(employee);
            }
        }
    }

    private static boolean evaluateCriteria(Map<String, String> employee, String criteria) {
        // Implement criteria evaluation logic here
        // For simplicity, assume the criteria is always salary > 50000
        int salary = Integer.parseInt(employee.get("Salary"));
        return salary > 50000;
    }

    private static void searchEmployee(String employeeName) {
        List<Map<String, String>> records = loadEmployeeRecords();
        for (Map<String, String> employee : records) {
            if (employee.get("Full Name").equalsIgnoreCase(employeeName)) {
                printEmployeeDetails(employee);
                return;
            }
        }
        System.out.println("Employee not found.");
    }

    private static void updateEmployeeRecord(String employeeName) {
        List<Map<String, String>> records = loadEmployeeRecords();
        for (Map<String, String> employee : records) {
            if (employee.get("Full Name").equalsIgnoreCase(employeeName)) {
                System.out.print("Enter new full name: ");
                employee.put("Full Name", scanner.nextLine());
                System.out.print("Enter new age: ");
                employee.put("Age", scanner.nextLine());
                System.out.print("Enter new date of birth: ");
                employee.put("Date of Birth", scanner.nextLine());
                System.out.print("Enter new salary: ");
                employee.put("Salary", scanner.nextLine());
                System.out.print("Enter new department: ");
                employee.put("Department", scanner.nextLine());

                saveEmployeeRecords(records);
                System.out.println("Employee record updated successfully.");
                printEmployeeDetails(employee);
                return;
            }
        }
        System.out.println("Employee not found.");
    }

    private static void deleteEmployee(String employeeName) {
        List<Map<String, String>> records = loadEmployeeRecords();
        Iterator<Map<String, String>> iterator = records.iterator();
        while (iterator.hasNext()) {
            Map<String, String> employee = iterator.next();
            if (employee.get("Full Name").equalsIgnoreCase(employeeName)) {
                iterator.remove();
                saveEmployeeRecords(records);
                System.out.println("Employee record deleted successfully.");
                return;
            }
        }
        System.out.println("Employee not found.");
    }

    private static void calculateAverageSalaryDepartment(String department) {
        List<Map<String, String>> records = loadEmployeeRecords();
        List<Map<String, String>> departmentEmployees = new ArrayList<>();
        for (Map<String, String> employee : records) {
            if (employee.get("Department").equalsIgnoreCase(department)) {
                departmentEmployees.add(employee);
            }
        }
        if (departmentEmployees.isEmpty()) {
            System.out.println("No employees in the specified department.");
        } else {
            double averageSalary = departmentEmployees.stream()
                    .mapToDouble(employee -> Double.parseDouble(employee.get("Salary")))
                    .average()
                    .orElse(0.0);
            System.out.printf("Average Salary in %s: %.2f%n", department, averageSalary);
        }
    }

    private static void calculateAverageSalaryCompany() {
        List<Map<String, String>> records = loadEmployeeRecords();
        if (records.isEmpty()) {
            System.out.println("No employees in the company.");
        } else {
            double averageSalary = records.stream()
                    .mapToDouble(employee -> Double.parseDouble(employee.get("Salary")))
                    .average()
                    .orElse(0.0);
            System.out.printf("Average Salary in the Company: %.2f%n", averageSalary);
        }
    }

    private static void printEmployeeDetails(Map<String, String> employee) {
        System.out.println("Full Name: " + employee.get("Full Name"));
        System.out.println("Age: " + employee.get("Age"));
        System.out.println("Date of Birth: " + employee.get("Date of Birth"));
        System.out.println("Salary: " + employee.get("Salary"));
        System.out.println("Department: " + employee.get("Department"));
        System.out.println();
    }
}

