package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Student;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentFormController {

    public TextField txtId;
    public TextField txtName;
    public TextField txtEmail;
    public TextField txtContact;
    public TextField txtAddress;
    public TextField txtNIC;
    public Button btnAdd;
    public Button btnUpdate;
    public Button btnDelete;
    public TableView tblStudent;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colNIC;

    public void addOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Student s = new Student(
                txtId.getText(),txtName.getText(),txtEmail.getText(),txtContact.getText(),txtAddress.getText()
                ,txtNIC.getText()
        );

        try {
            if (CrudUtil.execute("INSERT INTO student VALUES (?,?,?,?,?,?)",s.getId(),s.getName(),s.getEmail(),s.getContact(),s.getAddress(),s.getNic())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!..").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();

        }
        loadAllStudent();
        clearOnAction();


    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result =  CrudUtil.execute("SELECT * FROM student WHERE student_id=?",txtId.getText());
            if (result.next()) {
                txtName.setText(result.getString(2));
                txtEmail.setText(result.getString(3));
                txtContact.setText(result.getString(4));
                txtAddress.setText(result.getString(5));
                txtNIC.setText(result.getString(6));

            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Student student = new Student(
                txtId.getText(),
                txtName.getText(),
                txtEmail.getText(),
                txtContact.getText(),
                txtAddress.getText(),
                txtNIC.getText()
        );
        boolean isStUpdated = CrudUtil.execute("UPDATE student SET student_name=?, email=?, contact=?, address=?, NIC=? WHERE student_id=?",
                //student.getId(),
                student.getName(),
                student.getEmail(),
                student.getContact(),
                student.getAddress(),
                student.getNic(),
                student.getId());

        if (isStUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }

        loadAllStudent();
        clearOnAction();
    }

    private void clearOnAction() {
        txtId.clear();
        txtName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
        txtNIC.clear();
    }

    private void loadAllStudent() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM student");
        ObservableList<Student> oblist = FXCollections.observableArrayList();
        while (resultSet.next()) {
            oblist.add(
                    new Student(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getString(6)
                    )
            );
        }
    }

    public void initialize() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));

        loadAllStudent();
    }

    public void deleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Student emp = (Student) tblStudent.getSelectionModel().getSelectedItem();
        tblStudent.getItems().remove(emp);

        boolean isEmDeleted=CrudUtil.execute("DELETE FROM student WHERE student_id=? ",
                emp.getId()

        );
        if (isEmDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }
    }
}
