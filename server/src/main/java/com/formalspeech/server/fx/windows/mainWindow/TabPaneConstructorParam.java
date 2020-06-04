package com.formalspeech.server.fx.windows.mainWindow;

import com.formalspeech.networkService.serverSide.ServerConnectionHandler;
import javafx.scene.control.Tab;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TabPaneConstructorParam {
    public final Tab parentTab;
    public final ServerConnectionHandler connectionHandler;
    public final MainWindowController mainController;
}
