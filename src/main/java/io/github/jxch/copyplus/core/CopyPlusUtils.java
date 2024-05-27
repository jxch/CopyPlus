package io.github.jxch.copyplus.core;

import javafx.geometry.Pos;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.Notifications;

import java.util.Objects;


public class CopyPlusUtils {

    public static boolean hasModifierKey(KeyEvent event) {
        return event.isMetaDown() || event.isAltDown() || event.isShiftDown() || event.isControlDown();
    }

    public static boolean notModifierKey(KeyEvent event) {
        String key = event.getCode().toString();
        return !Objects.equals("CONTROL", key) && !Objects.equals("SHIFT", key) && !Objects.equals("ALT", key) && !Objects.equals("WINDOWS", key);
    }

    public static void notification(String text) {
        Notifications notification = Notifications.create();
        notification.title("操作提示");
        notification.text(text);
        notification.position(Pos.TOP_LEFT);
        notification.show();
    }

}
