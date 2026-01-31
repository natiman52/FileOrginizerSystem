package com.aau.storagemanagment;

import com.aau.storagemanagment.dao.UserDAO;
import com.aau.storagemanagment.model.User;
import com.aau.storagemanagment.util.NavigationUtil;
import com.aau.storagemanagment.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private UserDAO userDAO;

    public void initialize() {
        userDAO = new UserDAO();
    }

    @FXML
    protected void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
            return;
        }

        User user = userDAO.getUser(username, password);
        if (user != null) {
            UserSession.getInstance().login(user);
            NavigationUtil.goToDashboard();
        } else {
            errorLabel.setText("Invalid credentials.");
        }
    }
    @FXML
    protected void onSignup(){
        NavigationUtil.goToSignup();
    }
}
