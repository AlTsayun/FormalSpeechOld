package com.formalspeech.client.fx.windows.mainWindow;

import com.formalspeech.fxmlEssentials.AlertWrapper;
import com.formalspeech.formEssentials.Form;
import com.formalspeech.fxmlEssentials.FXMLFileLoader;
import com.formalspeech.fxmlEssentials.FXMLFileLoaderResponse;
import com.formalspeech.client.fx.windows.editWindow.EditWindowConstructorParam;
import com.formalspeech.client.fx.windows.editWindow.EditWindowController;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class MainWindowController implements Initializable {

    @FXML
    private ListView<Form> lvMain;

    @FXML
    private Button btnLoad;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnEdit;


    @FXML
    void onBtnEditClicked(ActionEvent event) {
        Form item;
        if ((item = lvMain.getSelectionModel().getSelectedItem()) == null){
            AlertWrapper.showAlert(Alert.AlertType.INFORMATION, "No item selected", "No item selected");
        } else {
            fillForm(item);
        }
    }

//    @FXML
//    void onBtnSaveClicked(ActionEvent event) {
//        FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML("saveFileDialog",
//                SaveFileDialogController.class,
//                new SaveFileDialogConstructorParam(new SaveFileDialogListener() {
//                    @Override
//                    @SneakyThrows
//                    public void sendFileInfo(String path, SerializersTypes serializersType, String pluginName) {
//                        try {
//                            SerializersHandler serializersHandler = new SerializersHandler(serializersType);
//                            HierarchyObject[] objects = lvMain.getItems().stream().map((new Function<Form, Object>() {
//                                @Override
//                                @SneakyThrows
//                                public Object apply(Form component){
//                                    return (HierarchyObject) component.getValue();
//                                }
//                            })).toArray(HierarchyObject[]::new);
//
//                            byte[] data = serializersHandler.write(objects);
//                            String extension = "";
//
//                            if(pluginName != null){
//                                PluginsLoader pluginsLoader = new PluginsLoader();
//                                Plugin plugin = (Plugin) pluginsLoader.loadPlugin(pluginName).getConstructor().newInstance();
//                                data = plugin.convert(data);
//                                extension = plugin.getFileExtension();
//                            }
//                            try (FileOutputStream stream = new FileOutputStream(path + extension)) {
//                                stream.write(data);
//                            }
//
//
//                            Alert saveFileInfo = new Alert(Alert.AlertType.INFORMATION);
//                            saveFileInfo.setTitle("Done!");
//                            saveFileInfo.setHeaderText("File successfully saved");
//                            saveFileInfo.showAndWait();
//                        } catch (IOException e) {
//                            Alert saveFileError = new Alert(Alert.AlertType.ERROR);
//                            saveFileError.setTitle("Error");
//                            saveFileError.setHeaderText("Cannot save file!");
//                            saveFileError.showAndWait();
//                            e.printStackTrace();
//                        }
//                    }
//                }));
//        showModalWindow((Parent) loaderResponse.loadedObject, "Configure file saving");
//    }

//    @FXML
//    void onBtnLoadClicked(ActionEvent event) {
//        FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML("loadFileDialog",
//                LoadFileDialogController.class,
//                new LoadFileDialogConstructorParam(new LoadFileDialogListener() {
//                    @Override
//                    public void sendFileInfo(String path, SerializersTypes serializersType) {
//                        try {
//                            byte[] data = Files.readAllBytes(Path.of(path));
//
//                            PluginsLoader pluginsLoader = new PluginsLoader();
//                            String fileExtension = path.substring(path.lastIndexOf("."));
//                            try {
//                                Plugin plugin = pluginsLoader.getPluginForFileExtension(fileExtension);
//                                data = plugin.revert(data);
//                            } catch (IllegalArgumentException e) {
//                                log.info("No plugin applied on loading file \"" + path + "\"");
//                            }
//
//
//                            List<HierarchyObject> hierarchyObjects = new ArrayList<>();
//                            try {
//                                SerializersHandler serializersHandler = new SerializersHandler(serializersType);
//                                hierarchyObjects = serializersHandler.read(data);
//                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
//                                throw new IOException(e);
//                            }
//
//
//                            lvMain.setItems(FXCollections.observableArrayList(hierarchyObjects.stream().map((hierarchyObject) ->{
//                                FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML("objectComponent",
//                                        ObjectComponent.class,
//                                        new ComponentConstructorParam(null, hierarchyObject));
//                                return (Form) loaderResponse.controller;
//                            }).collect(Collectors.toList())));
//
//                            Alert loadedFileInfo = new Alert(Alert.AlertType.INFORMATION);
//                            loadedFileInfo.setTitle("Done!");
//                            loadedFileInfo.setHeaderText("File successfully loaded");
//                            loadedFileInfo.showAndWait();
//
//
//                        } catch (IOException e) {
//                            Alert loadFileError = new Alert(Alert.AlertType.ERROR);
//                            loadFileError.setTitle("Error");
//                            loadFileError.setHeaderText("Cannot load file!");
//                            loadFileError.showAndWait();
//                            e.printStackTrace();
//                        }
//                    }
//                }));
//        showModalWindow((Parent) loaderResponse.loadedObject, "Configure file loading");
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lvMain.setCellFactory(lv ->{
            ListCell<Form> cell = new ListCell<Form>(){
                @Override
                @SneakyThrows
                public void updateItem(Form item, boolean empty){
                    super.updateItem(item, empty);
                    if(item == null){
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };


            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Edit"));
            editItem.setOnAction(event -> {
                Form item = cell.getItem();
                fillForm(item);
            });

            ContextMenu componentContextMenu = new ContextMenu();

            componentContextMenu.getItems().setAll(editItem);


            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (!isNowEmpty) {
                    cell.setContextMenu(componentContextMenu);
                }
            });

            cell.setPrefHeight(40);

            return cell;


        });
    }

    private void showModalWindow(Parent loadedObject, String title) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene((Parent) loadedObject));
        stage.setResizable(false);
        stage.showAndWait();
    }

    private void fillForm(Form form) {

        try {
            log.info("Editing: " + form.toString());
            final Form[] filledForm = new Form[1];
            FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML(
                    EditWindowController.getFXMLFileName(),
                    EditWindowController.class, new EditWindowConstructorParam(
                            form,
                            form1 -> filledForm[0] = form1
                    )
            );

            showModalWindow((Parent) loaderResponse.loadedObject, EditWindowController.getFXMLFileName());
            int selectedIndex = lvMain.getSelectionModel().getSelectedIndex();
            ObservableList<Form> items = lvMain.getItems();
            items.set(selectedIndex, filledForm[0]);
            lvMain.setItems(items);
        } catch (IOException e) {
            AlertWrapper.showAlert(Alert.AlertType.ERROR, "Cannot edit form", "Cannot edit form");
        }

    }

}
