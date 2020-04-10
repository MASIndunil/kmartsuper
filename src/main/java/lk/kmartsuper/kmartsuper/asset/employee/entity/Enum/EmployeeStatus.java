package lk.kmartsuper.kmartsuper.asset.employee.entity.Enum;

public enum EmployeeStatus {
    Working("Working"),
    Leave("Leave"),
    MEDICAL("Medical Leave"),
    Transferred("Transferred"),
    Suspended("Suspended"),
    RESIGNED("Resigned"),
    RETIRED("Retired");

    private final String employeeStatus;

    EmployeeStatus(String employeeStatus) {

        this.employeeStatus = employeeStatus;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }
}
