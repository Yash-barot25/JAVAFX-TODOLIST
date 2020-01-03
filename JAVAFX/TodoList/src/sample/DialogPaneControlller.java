package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.DataModel.TodoData;
import sample.DataModel.TodoItem;

import java.time.LocalDate;

public class DialogPaneControlller {

    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button okBtn;




    public TodoItem processData() {
        String desc = textField.getText().trim();
        String details = textArea.getText().trim();
        LocalDate localDate = datePicker.getValue();
        TodoItem item = new TodoItem(desc, details, localDate);
        TodoData.getInstance().addNewTodo(item);
        return item;
    }

}
