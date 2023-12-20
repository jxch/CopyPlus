package org.jxch.copyplus.copyplus.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    public static final Map<String, RootController> CONTROLLERS = new HashMap<>();

    protected void record() {
        CONTROLLERS.put(getClass().getSimpleName(), this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
