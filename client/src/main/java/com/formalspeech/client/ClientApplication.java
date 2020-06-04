package com.formalspeech.client;

import com.formalspeech.client.fx.windows.mainWindow.MainWindowController;
import com.formalspeech.fxmlEssentials.FXMLFileLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;
import java.net.URL;


//--module-path "D:\Programs\Java\javafx-sdk-11.0.2\lib" --add-modules=javafx.controls,javafx.fxml
public class ClientApplication extends Application{

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception{

        URL resource = MainWindowController.class.getResource("mainWindow.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = (Parent) loader.load();
        MainWindowController controller = (MainWindowController) loader.getController();
        Scene scene = new Scene(root);
        stage.setTitle("FormalSpeech Client");
        stage.setScene(scene);
        stage.setResizable(false);
        controller.configureWindowClosing();
        stage.show();
    }
}
