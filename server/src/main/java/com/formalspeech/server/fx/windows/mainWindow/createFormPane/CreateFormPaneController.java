package com.formalspeech.server.fx.windows.mainWindow.createFormPane;

import com.formalspeech.formEssentials.Form;
import com.formalspeech.formEssentials.FormHandler;
import com.formalspeech.formEssentials.annotations.ComponentAnnotation;
import com.formalspeech.formEssentials.components.Component;
import com.formalspeech.formEssentials.components.ComponentsHandler;
import com.formalspeech.fxmlEssentials.FXMLFileLoader;
import com.formalspeech.fxmlEssentials.FXMLFileLoaderResponse;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
public class CreateFormPaneController implements Initializable {

    private Form formToCreate;

    private final Tab parentTab;

    public CreateFormPaneController(CreateFormPaneConstructorParam param) {
        parentTab = param.parentTab;
    }

    public static String getFXMLFileName(){
        return "createFormPane.fxml";
    }


    @FXML
    private ListView<Class<Component>> lvAvailableComponents;

    @FXML
    private ListView<Class<Component>> lvSelectedComponents;

    @FXML
    private TextField tfFormFileName;

    @FXML
    private VBox vbPreview;

    @FXML
    void onCancelClicked(ActionEvent event) {

    }

    @FXML
    void onMoveFromSelectedClicked(ActionEvent event) {
        ObservableList<Class<Component>> items = lvSelectedComponents.getSelectionModel().getSelectedItems();
        moveItems(items, false);
    }

    @FXML
    void onMoveToSelectedClicked(ActionEvent event) {
        ObservableList<Class<Component>> items = lvAvailableComponents.getSelectionModel().getSelectedItems();
        moveItems(items, true);
    }

    @FXML
    void onSaveClicked(ActionEvent event) {

    }

    private void moveItems(ObservableList<Class<Component>>  items, boolean isSelection){
        ObservableList<Class<Component>> selectedItems = lvSelectedComponents.getItems();
        if (isSelection){
            selectedItems.addAll(items);
        } else {
            for (Class<Component> item :
                    items) {
                selectedItems.remove(item);
            }
        }
        lvSelectedComponents.setItems(selectedItems);
    }

    void drawFormPreview(){
        ArrayList<Class<Component>> componentClasses = new ArrayList<>(lvSelectedComponents.getItems());
        ArrayList<Pane> panes = new ArrayList<>();
        for (Class<Component> componentClass :
                componentClasses) {
            try {
                FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML(
                        componentClass.getAnnotation(ComponentAnnotation.class).fxmlFileName(),
                        componentClass);
                ((Component) loaderResponse.controller).setLoadedPane((Pane) loaderResponse.loadedObject);
                panes.add((Pane) loaderResponse.loadedObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        vbPreview.getChildren().setAll(panes);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        lvAvailableComponents.setCellFactory(lv -> {
            ListCell<Class<Component>> cell = new ListCell<>() {
                @Override
                public void updateItem(Class<Component> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText(null);
                    } else {
                        setText(item.getAnnotation(ComponentAnnotation.class).label());
                    }
                }
            };

            MenuItem select = new MenuItem();
            select.textProperty().bind(Bindings.format("Select"));
            select.setOnAction(event -> {
                onMoveToSelectedClicked(new ActionEvent());
            });

            ContextMenu componentContextMenu = new ContextMenu();
            componentContextMenu.getItems().setAll(select);


            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (!isNowEmpty) {
                    cell.setContextMenu(componentContextMenu);
                }
            });

            cell.setPrefHeight(40);

            return cell;
        });
        lvSelectedComponents.setCellFactory(lv -> {
            ListCell<Class<Component>> cell = new ListCell<>() {
                @Override
                public void updateItem(Class<Component> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText(null);
                    } else {
                        setText(item.getAnnotation(ComponentAnnotation.class).label());
                    }
                }
            };

            MenuItem remove = new MenuItem();
            remove.textProperty().bind(Bindings.format("Remove"));
            remove.setOnAction(event -> {
                onMoveFromSelectedClicked(new ActionEvent());
            });

            ContextMenu componentContextMenu = new ContextMenu();
            componentContextMenu.getItems().setAll(remove);


            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (!isNowEmpty) {
                    cell.setContextMenu(componentContextMenu);
                }
            });

            cell.setPrefHeight(40);

            return cell;
        });

        lvAvailableComponents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvSelectedComponents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        lvSelectedComponents.getItems().addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                log.info("lvSelectedComponents changed!");
                drawFormPreview();
            }
        });

        try {
            ComponentsHandler componentsHandler = new ComponentsHandler();
            List<Class<Component>> componentClasses = componentsHandler.getAvailableComponents();
            lvAvailableComponents.setItems(FXCollections.observableList(componentClasses));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

