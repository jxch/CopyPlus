package org.jxch.copyplus.copyplus.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class MainController {
    @FXML
    private TextField shortcutKeys;
    @FXML
    private Label usable;
    @FXML
    private TextArea jsTemplate;

    private boolean notKey(String keyName) {
        return Objects.equals(keyName, "Ctrl")
                || Objects.equals(keyName, "Alt")
                || Objects.equals(keyName, "Shift")
                || Objects.equals(keyName, "Windows");
    }

    @FXML
    protected void enterShortcutKeys(@NonNull KeyEvent event) {
        // 获取按键的编码
        KeyCode keyCode = event.getCode();
        // 获取按键的名称
        String keyName = keyCode.getName();
        String sourceKeyName = keyName;
        // 判断是否按下了修饰键
        if (event.isControlDown()) {
            keyName = Objects.equals(keyName, "Ctrl") ? "Ctrl" : "Ctrl+" + keyName;
        }
        if (event.isAltDown()) {
            keyName = Objects.equals(keyName, "Alt") ? "Alt" : "Alt+" + keyName;
        }
        if (event.isShiftDown()) {
            keyName = Objects.equals(keyName, "Shift") ? "Shift" : "Shift+" + keyName;
        }
        if (event.isMetaDown()) {
            keyName = Objects.equals(keyName, "Windows") ? "Windows" : "Windows+" + keyName;
        }
        if (notKey(sourceKeyName)) {
            ArrayList<String> keys = new ArrayList<>(Arrays.asList(keyName.split("\\+")));
            if (keys.size() > 1) {
                keys.remove(keys.size() - 1);
                log.info(keys.toString());
                keyName = String.join("+", keys);
            }
        }


        shortcutKeys.setText(keyName);

        usable.setText(event.isConsumed() ? "不可用" : "可用");

        jsTemplate.setText("""
                 function(clipboard){
                                // 此处处理粘贴板文字
                                
                                return clipboard;
                            }
                """);
    }


}