package com.aau.storagemanagment.util;

import com.aau.storagemanagment.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.aau.storagemanagment.dao.UserDAO;

import java.io.IOException;

public class NavigationUtil {
    private static Stage primaryStage;
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void goToLogin() {
        loadScene("login-view.fxml", "Login", 400, 300);
    }

    public static void goToSignup() {
        String title;
        UserDAO userDAO = new UserDAO();
        if(userDAO.hasAdmin()){
            title = "Sign Up";
        }else{
            title = "Initial System Setup";
        }
        loadScene("signup-view.fxml",title , 400, 350);
    }

    public static void goToDashboard() {
        loadScene("main-view.fxml", "Secure File Manager", 800, 600);
    }

    private static void loadScene(String fxml, String title, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
