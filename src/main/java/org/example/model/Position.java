package org.example.model;

public class Position {
    private long id;
    private String name;
    private String description;
    private int day_payment;

    public Position() {
    }

    public Position(long id, String name, String description, int day_payment) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.day_payment = day_payment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDay_payment() {
        return day_payment;
    }

    public void setDay_payment(int day_payment) {
        this.day_payment = day_payment;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dayPayment=" + day_payment +
                '}';
    }
}
