package com.formalspeech.server.fx.windows.mainWindow.userInfoPane;

import com.formalspeech.server.fx.windows.mainWindow.TabPaneConstructorParam;
import javafx.scene.control.Tab;

public class UserInfoPaneController {

    private final Tab parentTab;

    public UserInfoPaneController(TabPaneConstructorParam param) {
        parentTab = param.parentTab;
    }

    public static String getFXMLFileName(){
        return "userInfoPane.fxml";
    }

}
