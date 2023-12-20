package org.jxch.copyplus.copyplus.controller;

import javafx.fxml.Initializable;
import lombok.NonNull;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    public static final Map<String, RootController> CONTROLLERS = new HashMap<>();

    protected void record() {
        CONTROLLERS.put(getClass().getSimpleName(), this);
    }

    @NonNull
    public static <T extends RootController> T getControllerBean(@NonNull Class<T> clazz) {
        return clazz.cast(CONTROLLERS.get(clazz.getSimpleName()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
