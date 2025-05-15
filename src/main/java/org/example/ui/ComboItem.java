package org.example.ui;

public class ComboItem {
    private long id;
    private String displayText;

    public ComboItem(long id, String displayText) {
        this.id = id;
        this.displayText = displayText;
    }

    public long getId() {
        return id;
    }

    public String getDisplayText() {
        return displayText;
    }

    @Override
    public String toString() {
        return displayText;
    }
}
