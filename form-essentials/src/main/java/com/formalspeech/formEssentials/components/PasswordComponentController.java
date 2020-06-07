package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ControllerAnnotation;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

@Slf4j
@ControllerAnnotation(fxmlFileName = "passwordComponent.fxml")
public class PasswordComponentController extends Controller<String> {

    public static final String SALT = "my-salt-text";

    @FXML
    private PasswordField pfValue;

    public PasswordComponentController(String value) {
        super(value);
    }

    @Override
    public void showIncorrectEntered() {
        pfValue.setStyle("-fx-border-color: red");
    }

    @Override
    public void removeIncorrectEntered() {
        pfValue.setStyle("");
    }

    public void beforeEditing() {

    }

    public void beforeFilling() {
    }

    @Override
    protected void saveValue() {
        String saltedPassword = SALT + pfValue.getText();
        String hashedPassword = generateHash(saltedPassword);
        value = hashedPassword;
    }

    public static String generateHash(String input) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length; ++idx) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            // handle error here.
        }

        return hash.toString();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}
