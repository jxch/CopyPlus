package org.jxch.copyplus.copyplus;

import javafx.fxml.FXMLLoader;
import lombok.NonNull;
import org.jxch.copyplus.copyplus.controller.FXMLController;

public class FXUtils {
    @NonNull
    public static FXMLLoader getFXMLLoader(@NonNull String fxml) {
        return new FXMLLoader(FXUtils.class.getResource(fxml));
    }

    public static String getFxmlName(@NonNull Class<?> ControllerClass){
        return ControllerClass.getAnnotation(FXMLController.class).fxml();
    }
}
