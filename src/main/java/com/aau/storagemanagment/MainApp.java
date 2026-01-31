package com.aau.storagemanagment;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.aau.storagemanagment.util.NavigationUtil;
import com.aau.storagemanagment.dao.UserDAO;
import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        NavigationUtil.setPrimaryStage(stage);
        UserDAO userDAO = new UserDAO();

        if (!userDAO.hasAdmin()) {
            NavigationUtil.goToSignup();
        } else {
            NavigationUtil.goToLogin();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}