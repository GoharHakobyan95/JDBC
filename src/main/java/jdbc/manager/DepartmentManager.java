package jdbc.manager;

import jdbc.db.DBConnectionProvider;
import jdbc.entity.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentManager implements Manager<Department, String> {
    private Connection connection;

    public DepartmentManager() {
        connection = DBConnectionProvider.getInstance().getConnection();
    }


    @Override
    public void create(Department department) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO departments (dept_no, dept_name) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, generateDepartmentId());
            ps.setString(2, department.getDepartmentName());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: Department_no has already exists. ");
        }
    }

    private String generateDepartmentId() {
        String lastId = "d000";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT MAX(dept_no) FROM departments");
            if (rs.next()) {
                String maxId = rs.getString(1);
                if (maxId != null) {
                    lastId = maxId;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Increment the last ID and return
        int num = Integer.parseInt(lastId.substring(1));
        num++;
        String newId = "d" + String.format("%03d", num);
        return newId;
    }

    @Override
    public Department getById(String id) throws SQLException {
        Department department = new Department();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM departments WHERE dept_no = ?");
            ps.setString(1, id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                department.setDepartmentName(resultSet.getString("dept_no"));
                department.setDepartmentName(resultSet.getString("dept_name"));
            }
            System.out.println(department.getDepartmentName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return department;
    }

    @Override
    public List<Department> getAll() {
        List<Department> departments = new ArrayList<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM departments");
            while (resultSet.next()) {
                Department department = new Department();
                department.setDepartmentNumber(resultSet.getString("dept_no"));
                department.setDepartmentName(resultSet.getString("dept_name"));
                departments.add(department);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return departments;
    }

    @Override
    public void update(Department department) {
        if (department == null || department.getDepartmentNumber() == null || department.getDepartmentName() == null) {
            throw new IllegalArgumentException("Invalid department object.");
        }
        try (PreparedStatement ps = connection.prepareStatement("UPDATE departments SET dept_name = ? WHERE dept_no = ?")) {
            ps.setString(1, department.getDepartmentName());
            ps.setString(2, department.getDepartmentNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(String id) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM departments WHERE dept_no = ?");
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Department NO doesn't found.");
        }

    }

}
