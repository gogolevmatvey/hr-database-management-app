package org.example.model;

import java.time.LocalDate;

public class EmploymentContract {
    private long id;
    private int number;
    private String type;
    private LocalDate creation_date;
    private String date_of_conclusion;
    private long employee_id;
    private long department_id;
    private long position_id;

    public EmploymentContract() {
    }

    public EmploymentContract(long id, int number, String type, LocalDate creation_date, String date_of_conclusion, long employee_id, long department_id, long position_id) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.creation_date = creation_date;
        this.date_of_conclusion = date_of_conclusion;
        this.employee_id = employee_id;
        this.department_id = department_id;
        this.position_id = position_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDate creation_date) {
        this.creation_date = creation_date;
    }

    public String getDate_of_conclusion() {
        return date_of_conclusion;
    }

    public void setDate_of_conclusion(String date_of_conclusion) {
        this.date_of_conclusion = date_of_conclusion;
    }

    public long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(long employee_id) {
        this.employee_id = employee_id;
    }

    public long getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(long department_id) {
        this.department_id = department_id;
    }

    public long getPosition_id() {
        return position_id;
    }

    public void setPosition_id(long position_id) {
        this.position_id = position_id;
    }

    @Override
    public String toString() {
        return "EmploymentContract{" +
                "id=" + id +
                ", number=" + number +
                ", type='" + type + '\'' +
                ", creationDate=" + creation_date +
                ", conclusionDate='" + date_of_conclusion + '\'' +
                ", employeeId=" + employee_id +
                ", departmentId=" + department_id +
                ", positionId=" + position_id +
                '}';
    }
}
