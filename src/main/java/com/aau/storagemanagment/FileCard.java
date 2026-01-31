package com.aau.storagemanagment;

import com.aau.storagemanagment.model.FileModel;
import com.aau.storagemanagment.util.FileUtil;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FileCard extends VBox {
    private FileModel fileModel;

    public FileCard(FileModel fileModel) {
        this.fileModel = fileModel;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(5);
        this.setPrefSize(120, 150);
        this.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");

        // Icon placeholder (Rectangle for now)
        Rectangle icon = new Rectangle(50, 50, Color.LIGHTBLUE);

        Label nameLabel = new Label(fileModel.getName());
        nameLabel.setStyle("-fx-font-weight: bold;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(100);
        nameLabel.setAlignment(Pos.CENTER);

        Label sizeLabel = new Label(FileUtil.getFormattedSize(fileModel.getSize()));
        sizeLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 10px;");

        this.getChildren().addAll(icon, nameLabel, sizeLabel);
    }

    public FileModel getFileModel() {
        return fileModel;
    }
}
