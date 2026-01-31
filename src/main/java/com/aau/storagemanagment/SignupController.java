package com.aau.storagemanagment;

import com.aau.storagemanagment.dao.UserDAO;
import com.aau.storagemanagment.util.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.application.Platform;
public class SignupController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;
    @FXML private Label h1;
    @FXML private Label h2;
    @FXML private Button button;
    @FXML private HBox myHBox;
    private UserDAO userDAO;

    public void initialize() {
        userDAO = new UserDAO();
        if(userDAO.hasAdmin()){
            Button newButton = new Button("Login");
            newButton.setOnAction(event -> NavigationUtil.goToLogin());
            Platform.runLater(() -> {
                myHBox.getChildren().add(0,newButton);
            });
            h1.setText("Sign Up");
            h2.setText("Create User Account");
            button.setText("Sign Up");
        }else{
            h1.setText("Initial System Setup");
            h2.setText("Create Admin Account");
            button.setText("Create Admin");
        }
    }
    @FXML
    protected void onSignupButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        if (userDAO.isUsernameTaken(username)) {
            errorLabel.setText("Username already taken.");
            return;
        }

        // Force ADMIN role for First-Run Setup
        if(!userDAO.hasAdmin()){
            userDAO.createUser(username, password, "ADMIN");
        }else{
            userDAO.createUser(username, password, "USER");

        }

        // Redirect to Login (or Dashboard, but let's do Login to be safe/standard)
        NavigationUtil.goToLogin();
    }
}

