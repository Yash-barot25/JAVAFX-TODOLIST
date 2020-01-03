package sample.DataModel;

import java.time.LocalDate;

public class TodoItem {

    private String description;
    private String details;
    private LocalDate localDate;

    public TodoItem(String description, String details, LocalDate localDate) {
        this.description = description;
        this.details = details;
        this.localDate = localDate;
    }


    public String getDescription() {
        return description;
    }

    public String getDetails() {
        return details;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

}
