package org.example.model;

public class Employee {
    private int id;
    private String full_name;
    private long phone_number;
    private String education;

    public Employee() {
    }

    public Employee(int id, String full_name, long phone_number, String education) {
        this.id = id;
        this.full_name = full_name;
        this.phone_number = phone_number;
        this.education = education;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", full_name='" + full_name + '\'' +
                ", phoneNumber=" + phone_number +
                ", education='" + education + '\'' +
                '}';
    }
}
