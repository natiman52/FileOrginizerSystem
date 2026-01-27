module com.aau.storagemanagment {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.aau.storagemanagment to javafx.fxml;
    exports com.aau.storagemanagment;
}