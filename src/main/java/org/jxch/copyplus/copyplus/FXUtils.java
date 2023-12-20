package org.jxch.copyplus.copyplus;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import lombok.NonNull;
import org.controlsfx.control.Notifications;
import org.jxch.copyplus.copyplus.controller.FXMLController;

import java.util.Objects;

public class FXUtils {
    @NonNull
    public static FXMLLoader getFXMLLoader(@NonNull String fxml) {
        return new FXMLLoader(FXUtils.class.getResource(fxml));
    }

    public static String getFxmlName(@NonNull Class<?> ControllerClass) {
        return ControllerClass.getAnnotation(FXMLController.class).fxml();
    }

    public static boolean notKey(String keyName) {
        return Objects.equals(keyName, "Ctrl")
                || Objects.equals(keyName, "Alt")
                || Objects.equals(keyName, "Shift")
                || Objects.equals(keyName, "Windows");
    }

    public static void notification(String text) {
        Notifications notification = Notifications.create();
        notification.title("操作提示");
        notification.text(text);
        notification.position(Pos.TOP_LEFT);
        notification.show();
    }
}
