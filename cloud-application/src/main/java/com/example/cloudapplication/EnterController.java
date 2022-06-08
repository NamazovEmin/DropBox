package com.example.cloudapplication;

import com.example.cloud.Auth;
import com.example.cloud.CloudMessage;
import com.example.cloud.Reg;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;


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
    AuthenticationService authenticationService;

    @FXML
    protected void registrationButtonClick() throws Exception {
        EnterApplication.frontStage.close();
        RegistrationApplication registrationApplication = new RegistrationApplication();
        registrationApplication.start(EnterApplication.frontStage);
    }

    @FXML
    protected void enterInSystem() throws Exception {


       if (loginEnter.getText().isEmpty() || passwordEnter.getText().isEmpty()){
           errorLabelEnter.setText("Enter your login and password");
       } else {
           authenticationService = new AuthenticationService(8189);
           Thread readThread = new Thread(this::readLoop);
           readThread.setDaemon(true);
           readThread.start();
           authenticationService.write(new Auth(loginEnter.getText(),passwordEnter.getText(),false));
           clearTextAreaEnter();
           System.out.println("Auth send");
       }
    }

    @FXML
    protected void enterByLoginAndPasswordButton() throws Exception {
        EnterApplication.frontStage.close();
       EnterApplication enterApplication = new EnterApplication();
       enterApplication.start(EnterApplication.frontStage);

    }

    @FXML
    protected void registrationUserButton() throws Exception {
        try{
            Long.parseLong(telNumber.getText());
        } catch (NumberFormatException e){
            errorLabelRegistration.setText("wrong phone number");
        }
        if (name.getText().isEmpty() || surName.getText().isEmpty() || telNumber.getText().isEmpty()
                || email.getText().isEmpty() || login.getText().isEmpty() ||password.getText().isEmpty()){
            errorLabelRegistration.setText("Fill in all fields.");
        } else {
            authenticationService = new AuthenticationService(8189);
            Thread readThread = new Thread(this::readLoop);
            readThread.setDaemon(true);
            readThread.start();
            authenticationService.write(new Reg(name.getText(), surName.getText(), Long.parseLong(telNumber.getText())
                   , email.getText(), login.getText(),password.getText(),false));
            clearTextAreaRegistration();
        }

    }
    private void readLoop() {
        try {
            while (true) {
                CloudMessage message = authenticationService.read();
                if (message instanceof Reg reg ) {
                    if (reg.getReg()) {
                        Platform.runLater(() -> {
                            try {
                                errorLabelRegistration.setText("Registration completed");
                                clearTextAreaRegistration();
                                authenticationService.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } else { Platform.runLater(() -> {
                        try {
                            errorLabelRegistration.setText("Server Error");
                            clearTextAreaRegistration();
                            authenticationService.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    }
                } else if (message instanceof Auth auth) {
                    if (auth.isAccess()){
                        Platform.runLater(() -> {
                            try {
                                authenticationService.close();
                                EnterApplication.frontStage.close();
                                MainApplication mainApplication = new MainApplication(new Network(8189, auth.getName(),
                                        auth.getSurname(),auth.getTeNumber(), auth.getEmail(), auth.getLogin(), auth.getPassword()));
                                mainApplication.start(EnterApplication.frontStage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        authenticationService.close();
                        Platform.runLater(() -> {
                            errorLabelEnter.setText("Incorrect username or password");
                        });
                    }

                }
            }
        } catch (Exception e) {
            System.err.println("Connection lost");
        }
    }

    private void clearTextAreaRegistration(){
        name.clear();
        surName.clear();
        telNumber.clear();
        email.clear();
        login.clear();
        password.clear();
    }

    private void clearTextAreaEnter(){
        loginEnter.clear();
        passwordEnter.clear();
    }

}