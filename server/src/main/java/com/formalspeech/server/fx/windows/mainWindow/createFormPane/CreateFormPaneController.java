package com.formalspeech.server.fx.windows.mainWindow.createFormPane;

import com.formalspeech.formEssentials.Form;
import com.formalspeech.formEssentials.IdentifierAndValue;
import com.formalspeech.formEssentials.annotations.ComponentAnnotation;
import com.formalspeech.formEssentials.components.Component;
import com.formalspeech.formEssentials.components.ComponentsHandler;
import com.formalspeech.fxmlEssentials.AlertWrapper;
import com.formalspeech.server.fx.windows.mainWindow.TabPaneConstructorParam;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
public class CreateFormPaneController implements Initializable {

    private final Tab parentTab;

    public CreateFormPaneController(TabPaneConstructorParam param) {
        parentTab = param.parentTab;
    }

    public static String getFXMLFileName(){
        return "createFormPane.fxml";
    }


    @FXML
    private ListView<Component> lvAvailableComponents;

    @FXML
    private ListView<Component> lvSelectedComponents;

    @FXML
    private TextField tfFormName;

    @FXML
    private VBox vbPreview;

    @FXML
    public void onCancelClicked(ActionEvent event) {
        parentTab.getTabPane().getTabs().remove(parentTab);
    }


    @FXML
    public void onLoadClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file with form");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Form files", "*" + Form.FORM_FILE_EXTENSION));
        File selectedFile = fileChooser.showOpenDialog(tfFormName.getScene().getWindow());
        if (selectedFile != null) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(selectedFile))) {
                Form loadedForm = (Form) inputStream.readObject();
                tfFormName.setText(loadedForm.getName());
                ComponentsHandler componentsHandler = new ComponentsHandler();
                ArrayList<IdentifierAndValue> componentsIdentifiersAndValues = loadedForm.getComponentsIdentifiersAndValues();
                List<Component> components= new ArrayList<>();
                for (IdentifierAndValue identifierAndValue :
                        componentsIdentifiersAndValues) {
                    components.add(componentsHandler.getNewInstance(identifierAndValue.identifier, identifierAndValue.value));
                }
                lvSelectedComponents.getItems().setAll(components);
            } catch (Exception ex) {
                AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Cannot load file!");
            }
        }
    }
    @FXML
    public void onRemoveFromSelectedClicked(ActionEvent event) {
        ObservableList<Component> items = lvSelectedComponents.getSelectionModel().getSelectedItems();
        moveItems(items, false);
    }

    @FXML
    public void onMoveToSelectedClicked(ActionEvent event) {
        ObservableList<Component> items = lvAvailableComponents.getSelectionModel().getSelectedItems();
        moveItems(items, true);
    }

    @FXML
    public void onSaveClicked(ActionEvent event) {
            if (isFileNameValid(tfFormName.getText())){
                try {
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setTitle("Choose folder to save form file into");
                    File selectedDirectory = directoryChooser.showDialog(tfFormName.getScene().getWindow());
                    if (selectedDirectory != null) {
                        log.info(selectedDirectory.getAbsolutePath());
                        String fullFileName = selectedDirectory.getAbsolutePath() + "\\" + tfFormName.getText() + Form.FORM_FILE_EXTENSION;


                        ArrayList<IdentifierAndValue> identifiersAndValues = new ArrayList<>();
                        for (Component item :
                                lvSelectedComponents.getItems()) {
                            identifiersAndValues.add(new IdentifierAndValue(item.getClass().getAnnotation(ComponentAnnotation.class).identifier(), item.getValueAsString()));
                        }
                        Form formToSave = new Form(tfFormName.getText(), identifiersAndValues);


                        log.info("saving " + fullFileName);
                        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fullFileName))) {
                            outputStream.writeObject(formToSave);
                            AlertWrapper.showAlert(Alert.AlertType.INFORMATION, "Success", "File " + fullFileName + " saved correctly!");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Wrong data entered!");
                        }
                    }
                    tfFormName.setStyle("");
                } catch (IOException e) {
                    AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Entered data is incorrect!");
                }

            } else{
                tfFormName.setStyle("-fx-focus-color: red; -fx-text-box-border: red");
            }
    }

    private boolean isFileNameValid(String fileName) {
        File f = new File(fileName);
        try {
            f.getCanonicalPath();
            log.info(f.getAbsolutePath());
            return !fileName.equals("");
        }
        catch (IOException e) {
            return false;
        }
    }

    private void moveItems(ObservableList<Component>  items, boolean isSelection){
        ObservableList<Component> selectedItems = lvSelectedComponents.getItems();
        if (isSelection){
            ComponentsHandler componentsHandler = new ComponentsHandler();
            for (Component item:
                 items) {
                try{
                    Component newItem = componentsHandler.getNewInstance(item.getClass(), "");
                    selectedItems.add(newItem);

                }catch (Exception e){
//                    try {
                        log.info("Cannot load component!");
//                        selectedItems.add(componentsHandler.getErrorComponent());
//                    } catch (IOException ioException) {
//                        ioException.printStackTrace();
//                    }
                }
            }
        } else {
            //todo: fix loop throwing exception on iterations
            for (Component item :
                    items) {
                selectedItems.remove(item);
            }
        }
    }

    void drawFormPreview(){
        ArrayList<Component> selectedComponents = new ArrayList<>(lvSelectedComponents.getItems());
        List<Pane> panes = new ArrayList<>();
        for (Component item :
                selectedComponents) {
            try {
//                panes.add(item.getLoadedPaneForFilling());
                panes.add(item.getLoadedPaneForEditing());

            } catch (IOException e) {
                log.info("Cannot load component!");
                e.printStackTrace();
            }
        }
        vbPreview.getChildren().setAll(panes);
    }

    private void setCellFactoryForLvAvailableComponents(){
        lvAvailableComponents.setCellFactory(lv ->{
            ListCell<Component> cell = new ListCell<>(){
                @Override
                public void updateItem(Component item, boolean empty){
                    super.updateItem(item, empty);
                    if(item == null){
                        setText(null);
                    } else {
                        setText(item.getClass().getAnnotation(ComponentAnnotation.class).label());
                    }
                }
            };


            MenuItem select = new MenuItem();
            select.textProperty().bind(Bindings.format("Select"));
            select.setOnAction(event -> onMoveToSelectedClicked(new ActionEvent()));

            ContextMenu componentContextMenu = new ContextMenu();

            componentContextMenu.getItems().setAll(select);



            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (!isNowEmpty) {
                    cell.setContextMenu(componentContextMenu);
                }
            });


            cell.setOnMouseClicked((mouseEvent)->{
                if (mouseEvent.getClickCount() == 2){
                    onMoveToSelectedClicked(new ActionEvent());
                }
            });

            cell.setPrefHeight(40);

            return cell;


        });
    }

    private void setCellFactoryForLvSelectedComponents(){
        lvSelectedComponents.setCellFactory(lv ->{
            ListCell<Component> cell = new ListCell<>(){
                @Override
                public void updateItem(Component item, boolean empty){
                    super.updateItem(item, empty);
                    if(item == null){
                        setText(null);
                    } else {
                        setText(item.getClass().getAnnotation(ComponentAnnotation.class).label());
                    }
                }
            };


            MenuItem remove = new MenuItem();
            remove.textProperty().bind(Bindings.format("Remove"));
            remove.setOnAction(event -> {
                onRemoveFromSelectedClicked(new ActionEvent());
            });


            //todo: arrange lists of components in lvSelectedComponents

            MenuItem moveToTop = new MenuItem();
            moveToTop.textProperty().bind(Bindings.format("Move to top"));
            moveToTop.setOnAction(event -> {
                ObservableList<Component> allItems = cell.getListView().getItems();
                Component selectedItem = cell.getListView().getSelectionModel().getSelectedItem();
                allItems.remove(selectedItem);
                allItems.add(0, selectedItem);
            });

            MenuItem moveUp = new MenuItem();
            moveUp.textProperty().bind(Bindings.format("Move up"));
            moveUp.setOnAction(event -> {

                try {
                    ObservableList<Component> items = cell.getListView().getItems();
                    Component tmpItem = items.remove(cell.getListView().getSelectionModel().getSelectedIndex() - 1);
                    items.add(cell.getListView().getSelectionModel().getSelectedIndex() + 1, tmpItem);
                } catch (IndexOutOfBoundsException e) {
                }
            });

            MenuItem moveDown = new MenuItem();
            moveDown.textProperty().bind(Bindings.format("Move down"));
            moveDown.setOnAction(event -> {
                try {
                    ObservableList<Component> items = cell.getListView().getItems();
                    Component tmpItem = items.remove(cell.getListView().getSelectionModel().getSelectedIndex() + 1);
                    items.add(cell.getListView().getSelectionModel().getSelectedIndex() - 1, tmpItem);
                } catch (IndexOutOfBoundsException e) {

                }
            });

            MenuItem moveToBottom = new MenuItem();
            moveToBottom.textProperty().bind(Bindings.format("Move to bottom"));
            moveToBottom.setOnAction(event -> {
                ObservableList<Component> allItems = cell.getListView().getItems();
                Component selectedItem = cell.getListView().getSelectionModel().getSelectedItem();
                allItems.remove(selectedItem);
                allItems.add(allItems.size(), selectedItem);
            });

            ContextMenu componentContextMenu = new ContextMenu();
            componentContextMenu.getItems().setAll(remove, moveToTop, moveUp, moveDown, moveToBottom);



            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (!isNowEmpty) {
                    cell.setContextMenu(componentContextMenu);
                }
            });

            cell.setPrefHeight(40);

            return cell;


        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCellFactoryForLvAvailableComponents();
        setCellFactoryForLvSelectedComponents();

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
            List<Component> components = componentsHandler.getAvailableComponents();
            lvAvailableComponents.setItems(FXCollections.observableList(components));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

