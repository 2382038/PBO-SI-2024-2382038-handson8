package repositories;

import config.Database;
import entities.TodoList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TodoListRepositoryDblmpl implements TodoListRepository {
    private final Database database;

    public TodoListRepositoryDblmpl(Database database) {
        this.database = database;
    }

    @Override
    public TodoList[] getAll() {
        Connection connection = database.getConnection();
        String sqlStatement = "SELECT * FROM todos";
        List<TodoList> todoLists = new ArrayList<>();
        try{ //utk error handling
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                TodoList todoList = new TodoList();
                Integer id = resultSet.getInt(1);
                String todo = resultSet.getString(2);
                todoList.setId(id);
                todoList.setTodo(todo);
                todoLists.add(todoList);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return todoLists.toArray(TodoList[]::new);
    }

    @Override
    public void add(final TodoList todoList) {
        String sqlStatement = "INSERT INTO todo(todo) values(?)";
        Connection conn = database.getConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
            preparedStatement.setString(1, todoList.getTodo());

            int rowsEffected = preparedStatement.executeUpdate();
            if(rowsEffected > 0){
                System.out.println("Insert successful !");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Boolean remove(final Integer id) {
        String sqlStatement = "DELETE FROM todo WHERE id = ?";
        Connection conn = database.getConnection();
        var dbId = getDbId(id);
        if (dbId == null){
        return false;
    }
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, dbId);

            int rowsEffected = preparedStatement.executeUpdate();
            if(rowsEffected>0){
                System.out.println("Delete successful !");
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private Integer getDbid(final Integer id){
        TodoList[] todoLists = getAll();
        Long countElement = Arrays.stream(todolists).filter(Objects::nonNull).count();
        if (countElement.intValue() == 0){
            return null;
        }
        var dbId = todoLists[id-1].getId();
        return dbId;
    }


    @Override
    public Boolean edit(TodoList todoList) {
        String sqlStatement = "UPDATE todo set todo = ? WHERE id = ?";
        Connection conn = database.getConnection();
        var dbId = getDbId(todoList.getId());
        if (dbId == null) {
            return false;
        }
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
            preparedStatement.setString(1, todoList.getTodo());
            preparedStatement.setInt(2, dbId);
            int rowsEffected = preparedStatement.executeUpdate();

            if (rowsEffected > 0) {
                System.out.println("Update succsessful !");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
