package org.example.model;

import java.time.LocalDate;

public class Document {
    private long id;
    private int type;
    private int serial;
    private long number;
    private LocalDate date;
    private String given_by;
    private long employee_id;

    public Document() {
    }

    public Document(long id, int type, int serial, long number, LocalDate date, String given_by, long employee_id) {
        this.id = id;
        this.type = type;
        this.serial = serial;
        this.number = number;
        this.date = date;
        this.given_by = given_by;
        this.employee_id = employee_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getGiven_by() {
        return given_by;
    }

    public void setGiven_by(String given_by) {
        this.given_by = given_by;
    }

    public long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(long employee_id) {
        this.employee_id = employee_id;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", type=" + type +
                ", serial=" + serial +
                ", number=" + number +
                ", date=" + date +
                ", given_by='" + given_by + '\'' +
                ", employee_id=" + employee_id +
                '}';
    }
}

