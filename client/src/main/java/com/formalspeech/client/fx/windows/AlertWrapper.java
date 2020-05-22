package com.formalspeech.client.fx.windows;

import javafx.scene.control.Alert;

public class AlertWrapper {
    public static void showAlert(Alert.AlertType alertType, String title, String headerText){
        Alert editFileInfo = new Alert(alertType);
        editFileInfo.setTitle(title);
        editFileInfo.setHeaderText(headerText);
        editFileInfo.showAndWait();
    }

}
