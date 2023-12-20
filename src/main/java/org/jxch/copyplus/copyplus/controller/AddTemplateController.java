package org.jxch.copyplus.copyplus.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jxch.copyplus.copyplus.db.TemplateDao;
import org.jxch.copyplus.copyplus.db.TemplateDto;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

@Slf4j
@FXMLController(fxml = "add-template-view.fxml")
public class AddTemplateController extends RootController implements ControllerInit<TemplateDto> {
    @FXML
    private ChoiceBox<String> types;
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
        usable.setText(event.isConsumed()
                || notKey(sourceKeyName)
                || !TemplateDao.notHasShortcut(keyName) ? "不可用" : "可用");
    }


    public void add(ActionEvent actionEvent) {
        String shortcut = shortcutKeys.getText();
        String type = types.getValue();
        String template = jsTemplate.getText();
        TemplateDao.put(TemplateDto.builder()
                .shortcut(shortcut)
                .type(type)
                .template(template)
                .build());
    }

    public void del(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jsTemplate.setText("""
                function(clipboard){
                    // 此处处理粘贴板文字

                    return clipboard;
                }
                """);

        types.getItems().add("js");
        types.setValue("js");
    }

    @Override
    public  void init(@NonNull TemplateDto dto) {
        jsTemplate.setText(dto.getTemplate());
        types.setValue(dto.getType());
        shortcutKeys.setText(dto.getShortcut());
    }

}
