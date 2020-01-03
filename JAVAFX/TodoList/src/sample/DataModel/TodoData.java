package sample.DataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class TodoData {

    private static TodoData instance = new TodoData();
    private static final String fileName = "todoListItems.txt";

    private ObservableList<TodoItem> todoItems;
    private static DateTimeFormatter df;

    private TodoData() {
        df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public static TodoData getInstance() {
        return instance;
    }

    public  ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void addNewTodo(TodoItem item){
        todoItems.add(item);
    }

//    public  void setTodoItems(List<TodoItem> todoItems) {
//        this.todoItems = todoItems;
//    }

    public void loadData() throws IOException {
        todoItems = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);
        BufferedReader bf = Files.newBufferedReader(path);
        String input;
        try {
            while ((input = bf.readLine()) != null) {
                String[] info = input.split("\t");
                TodoItem item = new TodoItem(info[0], info[1], LocalDate.parse(info[2],df));
                todoItems.add(item);
            }
        } finally {
            if (bf != null) {
                bf.close();
            }
        }


    }

    public void storeData() throws IOException {
        Path path = Paths.get(fileName);
        BufferedWriter br = Files.newBufferedWriter(path);
        Iterator<TodoItem> iter = todoItems.iterator();
        try {
            while (iter.hasNext()) {
                TodoItem item = iter.next();
                br.write(String.format("%s\t%s\t%s", item.getDescription(), item.getDetails(), item.getLocalDate().format(df)));
                br.newLine();
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }

    }

    public void delete(TodoItem item){
        if (item != null){
            todoItems.remove(item);
        }
    }
}
