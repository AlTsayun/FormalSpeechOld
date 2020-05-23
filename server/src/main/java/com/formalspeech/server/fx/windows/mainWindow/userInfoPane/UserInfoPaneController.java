package com.formalspeech.server.fx.windows.mainWindow.userInfoPane;

import javafx.scene.control.Tab;

public class UserInfoPaneController {

    private final Tab parentTab;

    public UserInfoPaneController(UserInfoPaneConstructorParam param) {
        parentTab = param.parentTab;
    }

    public static String getFXMLFileName(){
        return "userInfoPane.fxml";
    }

}
