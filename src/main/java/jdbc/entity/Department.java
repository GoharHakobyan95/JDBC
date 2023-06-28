package jdbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    private String departmentNumber;
    private String departmentName;


    public Department(String departmentName) {
        this.departmentName = departmentName;
    }

}
