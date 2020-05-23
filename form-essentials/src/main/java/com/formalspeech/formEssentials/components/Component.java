package com.formalspeech.formEssentials.components;

import javafx.scene.layout.Pane;

import java.io.IOException;

public interface Component {
    //Constructor Must have no args (creates default object)
    void setValue(Object value);
    Object getValue() throws IOException;
    boolean checkValue();
    void setLoadedPane(Pane root);
    Pane getLoadedPane();

}
