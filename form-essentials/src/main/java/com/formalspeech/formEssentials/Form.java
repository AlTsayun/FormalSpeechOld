package com.formalspeech.formEssentials;

import com.formalspeech.formEssentials.components.Component;

public class Form {

    private String name;
    private Class<Component>[] componentsClasses;
    private Component[] filledComponents;

    public Form(String name, Class<Component>[] componentsClasses) {
        this.name = name;
        this.componentsClasses = componentsClasses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<Component>[] getComponentsClasses() {
        return componentsClasses;
    }

    public void setComponentsClasses(Class<Component>[] componentsClasses) {
        this.componentsClasses = componentsClasses;
    }

    public Component[] getFilledComponents() {
        return filledComponents;
    }

    public void setFilledComponents(Component[] filledComponents) {
        this.filledComponents = filledComponents;
    }

    public String getCreateTableQuery(){
        return null;
//        return "CREATE TABLE IF NOT EXISTS `users` (\n" +
//                "  `UserID` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                "  `Password` varchar(250) COLLATE utf8_unicode_ci NOT NULL,\n" +
//                "  `AccessLevel` int(11) NOT NULL,\n" +
//                "  `RegistrationDate` date DEFAULT current_timestamp(),\n" +
//                "  `Email` varchar(250) COLLATE utf8_unicode_ci NOT NULL,\n" +
//                "  `Login` varchar(250) COLLATE utf8_unicode_ci NOT NULL,\n" +
//                "  PRIMARY KEY (`UserID`)\n" +
//                ") ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;\n" +
//                "COMMIT;";
    }
}
