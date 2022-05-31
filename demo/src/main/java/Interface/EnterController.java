package Interface;

import data.DataBase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import static Interface.EnterApplication.frontStage;

public class EnterController {
    public Pane enterPane;
    public Button enterButton;
    public Button registrationButton;
    public Pane registrationPane;
    public Button enterMenuButton;
    public TextField name;
    public TextField surName;
    public TextField telNumber;
    public TextField email;
    public TextField login;
    public TextField password;
    public Label errorLabelRegistration;
    public TextField loginEnter;
    public TextField passwordEnter;
    public Label errorLabelEnter;


    @FXML
    protected void registrationButtonClick() throws Exception {
        frontStage.close();
        RegistrationApplication registrationApplication = new RegistrationApplication();
        registrationApplication.start(frontStage);
    }

    @FXML
    protected void enterInSystem() throws Exception {
       if (loginEnter.getText().isEmpty() || passwordEnter.getText().isEmpty()){
           errorLabelEnter.setText("Enter your login and password");
       } else if (passwordEnter.getText().equals(DataBase.selectFromUsersByLogin(loginEnter.getText()))) {
           frontStage.close();
           MainApplication mainApplication = new MainApplication();
           mainApplication.start(frontStage);
       } else {
           errorLabelEnter.setText("Incorrect login or password");
       }
    }

    @FXML
    protected void enterByLoginAndPasswordButton() throws Exception {
        frontStage.close();
        EnterApplication enterApplication = new EnterApplication();
        enterApplication.start(frontStage);
    }

    @FXML
    protected void registrationUserButton() throws Exception {
        if (name.getText().isEmpty() || surName.getText().isEmpty() || telNumber.getText().isEmpty()
                || email.getText().isEmpty() || login.getText().isEmpty() ||password.getText().isEmpty()){
            errorLabelRegistration.setText("Fill in all fields.");
        }else if (DataBase.insertNewUserInDataBase(name.getText(), surName.getText(), Long.parseLong(telNumber.getText())
                , email.getText(), login.getText(), password.getText())) {
            errorLabelRegistration.setText("You registered");
            clearTextAreaRegistration();
        }
    }

    protected void clearTextAreaRegistration(){
        name.clear();
        surName.clear();
        telNumber.clear();
        email.clear();
        login.clear();
        password.clear();
    }
}