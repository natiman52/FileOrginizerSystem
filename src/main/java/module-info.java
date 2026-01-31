module com.aau.storagemanagment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens com.aau.storagemanagment to javafx.fxml;
    exports com.aau.storagemanagment;
}