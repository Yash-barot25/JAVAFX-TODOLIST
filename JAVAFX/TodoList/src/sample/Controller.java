package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import sample.DataModel.TodoData;
import sample.DataModel.TodoItem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Controller {

    private List<TodoItem> todolist;

    @FXML
    private ListView<TodoItem> todoItemListView;

    @FXML
    private Label dateLabel;

    @FXML
    private TextArea textArea;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ContextMenu contextMenu;


    @FXML
    public void initialize() {
        contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TodoItem item = todoItemListView.getSelectionModel().getSelectedItem();
                deleteSelectedItem(item);
            }
        });
        contextMenu.getItems().setAll(deleteItem);
//        TodoItem item1 = new TodoItem("Call jaydat", "Greet jaydat on his birthday", LocalDate.of(2019, Month.DECEMBER, 21));
//        TodoItem item2 = new TodoItem("Call parth", "Greet parth on his birthday", LocalDate.of(2019, Month.APRIL, 25));
//        TodoItem item3 = new TodoItem("Call dipu", "Greet dipu on his birthday", LocalDate.of(2019, Month.SEPTEMBER, 06));
//        TodoItem item4 = new TodoItem("Call appu", "Greet appu on his birthday", LocalDate.of(2019, Month.MARCH, 15));
//        TodoItem item5 = new TodoItem("Call me", "remind me on his birthday", LocalDate.of(2019, Month.JUNE, 02));
//
//        todolist = new ArrayList<>();
//        todolist.add(item1);
//        todolist.add(item2);
//        todolist.add(item3);
//        todolist.add(item4);
//        todolist.add(item5);
//        TodoData.getInstance().setTodoItems(todolist);


        todoItemListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observableValue, TodoItem todoItem, TodoItem t1) {
                if (t1 != null) {

                    TodoItem item = todoItemListView.getSelectionModel().getSelectedItem();
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    dateLabel.setText(df.format(item.getLocalDate()));
                    textArea.setText(item.getDetails());

                }
            }
        });

        SortedList<TodoItem> sortedList = new SortedList<>(TodoData.getInstance().getTodoItems(), new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                return o1.getLocalDate().compareTo(o2.getLocalDate());
            }
        });
        todoItemListView.setItems(sortedList);
        todoItemListView.getSelectionModel().selectFirst();
        todoItemListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        todoItemListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> todoItemListView) {
                ListCell<TodoItem> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(TodoItem todoItem, boolean b) {
                        super.updateItem(todoItem, b);
                        if (b) {
                            setText(null);
                        } else {
                            setText(todoItem.getDescription());
                            if (todoItem.getLocalDate().isBefore(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.RED);

                            } else if (todoItem.getLocalDate().equals(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.GREEN);
                            }
                        }
                    }
                };

//                cell.emptyProperty().addListener(new ChangeListener<Boolean>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
//                        if (t1){
//                            cell.setContextMenu(null);
//                        }else{
//                            cell.setContextMenu(contextMenu);
//                        }
//                    }
//                });
                cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(contextMenu);
                            }
                        }
                );
                return cell;

            }
        });


    }

    @FXML
    public void dialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add new todo item");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("dialog.fxml"));

        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> optionalButtonType = dialog.showAndWait();
        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.OK) {
            DialogPaneControlller dialogPaneControlller = loader.getController();
            TodoItem item = dialogPaneControlller.processData();
//            todoItemListView.getItems().setAll(TodoData.getInstance().getTodoItems());
            todoItemListView.getSelectionModel().select(item);

            System.out.println("Ok pressed");
        } else {
            System.out.println("Cancel pressed");
        }

    }

    private void deleteSelectedItem(TodoItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Delete " + item.getDescription());
        alert.setContentText("Do you wants to delete...? Press ok to confirm , Cancel to backout");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            TodoData.getInstance().delete(item);
        }
    }

    @FXML
    public void deleteMe(KeyEvent event){
        TodoItem item = todoItemListView.getSelectionModel().getSelectedItem();
        if (item != null){
            if (event.getCode().equals(KeyCode.DELETE)){
                deleteSelectedItem(item);
            }
        }
    }
}
