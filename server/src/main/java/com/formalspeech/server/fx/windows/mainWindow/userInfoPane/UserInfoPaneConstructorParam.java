package com.formalspeech.server.fx.windows.mainWindow.userInfoPane;

import com.formalspeech.databaseService.User;
import com.formalspeech.networkService.serverSide.ServerConnectionHandler;
import com.formalspeech.server.fx.windows.mainWindow.MainWindowController;
import com.formalspeech.server.fx.windows.mainWindow.TabPaneConstructorParam;
import javafx.scene.control.Tab;

public class UserInfoPaneConstructorParam extends TabPaneConstructorParam {
    public final User user;

    public UserInfoPaneConstructorParam(Tab parentTab, ServerConnectionHandler connectionHandler, MainWindowController mainController, User user) {
        super(parentTab, connectionHandler, mainController);
        this.user = user;
    }
}
