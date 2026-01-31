package com.aau.storagemanagment;

import com.aau.storagemanagment.dao.FileDAO;
import com.aau.storagemanagment.dao.UserDAO;
import com.aau.storagemanagment.model.FileModel;
import com.aau.storagemanagment.model.User;
import com.aau.storagemanagment.util.FileUtil;
import com.aau.storagemanagment.util.NavigationUtil;
import com.aau.storagemanagment.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import java.awt.Desktop;
import java.io.IOException;

public class MainController {

    @FXML
    private Button logoutButton;

    @FXML
    private ListView<String> categoryListView;

    @FXML
    private FlowPane filesFlowPane;

    @FXML
    private Label statusLabel;

    private FileDAO fileDAO;
    private User currentUser;
    private ObservableList<FileModel> allFiles;

    public void initialize() {
        fileDAO = new FileDAO();
        currentUser = UserSession.getInstance().getUser();

        if (currentUser == null) {
            NavigationUtil.goToLogin();
            return;
        }

        setupCategories();
        setupDragAndDrop();
        loadFiles();
    }

    private void setupCategories() {
        ObservableList<String> categories = FXCollections.observableArrayList(
                "All", "Images", "Documents", "Videos", "Audio", "Unknown"
        );
        categoryListView.setItems(categories);
        categoryListView.getSelectionModel().select("All");
        categoryListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filterFiles(newVal);
        });
    }

    private void loadFiles() {
        Task<List<FileModel>> task = new Task<>() {
            @Override
            protected List<FileModel> call() throws Exception {
                return fileDAO.getFiles(currentUser);
            }
        };

        task.setOnSucceeded(e -> {
            allFiles = FXCollections.observableArrayList(task.getValue());
            filterFiles(categoryListView.getSelectionModel().getSelectedItem());
            statusLabel.setText("Files loaded.");
        });

        task.setOnFailed(e -> statusLabel.setText("Failed to load files."));

        new Thread(task).start();
    }

    private void filterFiles(String category) {
        filesFlowPane.getChildren().clear();
        if (allFiles == null) return;

        List<FileModel> filtered = allFiles;
        if (category != null && !"All".equals(category)) {
            filtered = allFiles.stream()
                    .filter(f -> category.equalsIgnoreCase(f.getCategory()))
                    .collect(Collectors.toList());
        }

        for (FileModel file : filtered) {
            FileCard card = new FileCard(file);
            setupContextMenu(card);
            setupFileOpenListener(card);
            filesFlowPane.getChildren().add(card);
        }
    }

    private void setupDragAndDrop() {
        filesFlowPane.setOnDragOver(event -> {
            if (event.getGestureSource() != filesFlowPane && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        filesFlowPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                handleDroppedFiles(db.getFiles());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void handleDroppedFiles(List<File> files) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (File file : files) {
                    String category = FileUtil.getCategory(file);
                    FileModel fileModel = new FileModel(
                            file.getAbsolutePath(),
                            file.getName(),
                            file.length(),
                            FileUtil.getCreatedDate(file),
                            category,
                            currentUser.getId()
                    );
                    fileDAO.addFile(fileModel);
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            statusLabel.setText("Files imported.");
            loadFiles();
        });

        task.setOnFailed(e -> statusLabel.setText("Failed to import files."));

        new Thread(task).start();
    }

    private void setupContextMenu(FileCard card) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem renameItem = new MenuItem("Rename");
        MenuItem deleteItem = new MenuItem("Delete");

        renameItem.setOnAction(e -> handleRename(card));
        deleteItem.setOnAction(e -> handleDelete(card));

        contextMenu.getItems().addAll(renameItem, deleteItem);
        card.setOnContextMenuRequested(e -> contextMenu.show(card, e.getScreenX(), e.getScreenY()));
    }

    private void setupFileOpenListener(FileCard card) {
        card.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                File file = new File(card.getFileModel().getFilepath());
                if (file.exists()) {
                    new Thread(() -> {
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                            javafx.application.Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open file: " + e.getMessage());
                                alert.showAndWait();
                            });
                        }
                    }).start();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "File no longer exists at this location.");
                    alert.showAndWait();
                }
            }
        });
    }

    private void handleRename(FileCard card) {
        TextInputDialog dialog = new TextInputDialog(card.getFileModel().getName());
        dialog.setTitle("Rename File");
        dialog.setHeaderText("Enter new name:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    fileDAO.updateFileName(card.getFileModel().getFilepath(), name);
                    return null;
                }
            };
            task.setOnSucceeded(e -> loadFiles());
            new Thread(task).start();
        });
    }

    private void handleDelete(FileCard card) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete File");
        alert.setHeaderText("Are you sure you want to delete this file?");
        alert.setContentText(card.getFileModel().getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    fileDAO.deleteFile(card.getFileModel().getFilepath());
                    return null;
                }
            };
            task.setOnSucceeded(e -> loadFiles());
            new Thread(task).start();
        }
    }

    @FXML
    protected void onLogoutButtonClick() {
        UserSession.getInstance().logout();
        NavigationUtil.goToLogin();
    }

    @FXML
    protected void onAddFileButtonClick() {
        showAddFileModal();
    }

    private void showAddFileModal() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add File");

        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(20));

        TextField pathField = new TextField();
        pathField.setPromptText("File Path");
        pathField.setEditable(false);
        HBox.setHgrow(pathField, Priority.ALWAYS);

        Button browseButton = new Button("Browse");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File");
            File file = fileChooser.showOpenDialog(dialog);
            if (file != null) {
                pathField.setText(file.getAbsolutePath());
            }
        });

        HBox pathBox = new HBox(10, pathField, browseButton);

        TextField nameField = new TextField();
        nameField.setPromptText("Custom Name (Optional)");

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        saveButton.setOnAction(e -> {
            String path = pathField.getText();
            if (path == null || path.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a file.");
                alert.showAndWait();
                return;
            }

            File file = new File(path);
            if (!file.exists()) {
                 Alert alert = new Alert(Alert.AlertType.ERROR, "File does not exist.");
                 alert.showAndWait();
                 return;
            }

            String customName = nameField.getText();
            String finalName = (customName != null && !customName.trim().isEmpty()) ? customName.trim() : file.getName();

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    String category = FileUtil.getCategory(file);
                    FileModel fileModel = new FileModel(
                            file.getAbsolutePath(),
                            finalName,
                            file.length(),
                            FileUtil.getCreatedDate(file),
                            category,
                            currentUser.getId()
                    );
                    fileDAO.addFile(fileModel);
                    return null;
                }
            };

            task.setOnSucceeded(event -> {
                statusLabel.setText("File added successfully.");
                loadFiles();
                dialog.close();
            });

            task.setOnFailed(event -> {
                statusLabel.setText("Failed to add file.");
                event.getSource().getException().printStackTrace();
            });

            new Thread(task).start();
        });

        cancelButton.setOnAction(e -> dialog.close());

        dialogVBox.getChildren().addAll(new Label("Select File:"), pathBox, new Label("Name (Optional):"), nameField, buttonBox);

        Scene dialogScene = new Scene(dialogVBox, 400, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}