package be.pxl.VisitorsApplication.builder;

import be.pxl.VisitorsApplication.model.Department;

import java.util.Map;
import java.util.Random;

public class DepartmentBuilder {
    private static final Map<String, String> DEPARTMENTS = Map.of(
            "CAR", "Cardiology",
            "GER", "Geriatrics",
            "KRA", "Maternity ward",
            "ONC", "Oncology");;

    private final Random random;
    private final Department department;

    public DepartmentBuilder() {
        this.random = new Random();
        this.department = new Department();
    }

    public Department buildRandom() {
        int seed = random.nextInt(DEPARTMENTS.size());

        String[] keys = DEPARTMENTS.keySet().toArray(new String[0]);
        String key = keys[seed];

        this.department.setCode(key);
        this.department.setName(DEPARTMENTS.get(key));

        Department department = new Department();
        department.setCode(this.department.getCode());
        department.setName(this.department.getName());
        return department;
    }
}
