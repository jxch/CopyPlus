package org.jxch.copyplus.copyplus.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jxch.copyplus.copyplus.FXUtils;
import org.jxch.copyplus.copyplus.db.TemplateDao;
import org.jxch.copyplus.copyplus.db.TemplateDto;

import java.net.URL;
import java.util.*;

@Slf4j
@FXMLController(fxml = "add-template-view.fxml")
public class AddTemplateController extends RootController implements ControllerInit<TemplateDto> {
    @FXML
    public ChoiceBox<String> isPaste;
    @FXML
    public VBox panel;
    @FXML
    private ChoiceBox<String> types;
    @FXML
    private TextField shortcutKeys;
    @FXML
    private Label usable;
    @FXML
    private TextArea jsTemplate;
    private String shortcutOld;
    @Setter
    @Getter
    private Node thisNode;
    @Setter
    @Getter
    private Separator thisSeparator;

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
        // 获取按键的名
        String keyName = keyCode.getName();
        Set<String> keys = new TreeSet<>(Collections.singletonList(keyName));

        // 判断是否按下了修饰键
        if (event.isControlDown()) {
            keys.add("Ctrl");
        }
        if (event.isAltDown()) {
            keys.add("Alt");
        }
        if (event.isShiftDown()) {
            keys.add("Shift");
        }
        if (event.isMetaDown()) {
            keys.add("Windows");
        }

        shortcutKeys.setText(String.join("+", keys));
        usable.setText(event.isConsumed()
                || notKey(keyName)
                || !TemplateDao.notHasShortcut(keyName) ? "不可用" : "可用");
    }


    public void add(ActionEvent actionEvent) {
        if (Objects.equals(usable.getText(), "不可用")) {
            FXUtils.notification("当前快捷键不可用，请换一个试试");
            return;
        }

        String shortcut = shortcutKeys.getText();
        String type = types.getValue();
        String template = jsTemplate.getText();
        String paste = isPaste.getValue();
        TemplateDao.putOrUpdate(TemplateDto.builder()
                .shortcut(shortcut)
                .type(type)
                .template(template)
                .isPaste(paste)
                .build(), shortcutOld);
        FXUtils.notification("添加成功");
    }

    public void del(ActionEvent actionEvent) {
        TemplateDao.del(Objects.equals(shortcutOld, null) ? shortcutKeys.getText() : shortcutOld);
        FXUtils.notification("删除成功");
        RootController.getControllerBean(MainController.class).removeNode(this);
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
        isPaste.getItems().add("true");
        isPaste.getItems().add("false");
        isPaste.setValue("true");
    }

    @Override
    public void init(@NonNull TemplateDto dto) {
        jsTemplate.setText(dto.getTemplate());
        types.setValue(dto.getType());
        shortcutKeys.setText(dto.getShortcut());
        isPaste.setValue(dto.getIsPaste());
        shortcutOld = dto.getShortcut();
    }

}
