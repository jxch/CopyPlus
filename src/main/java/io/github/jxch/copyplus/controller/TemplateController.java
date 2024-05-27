package io.github.jxch.copyplus.controller;

import io.github.jxch.copyplus.db.TemplateDao;
import io.github.jxch.copyplus.db.TemplateDto;
import io.github.jxch.copyplus.util.CopyPlusUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Slf4j
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TemplateController implements Initializable {
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
    private boolean isUsable = false;
    @FXML
    private TextArea jsTemplate;
    private String shortcutOld;
    @Setter
    @Getter
    private Node thisNode;
    @Setter
    @Getter
    private Separator thisSeparator;

    @FXML
    protected void enterShortcutKeys(@NonNull KeyEvent event) {
        String key = event.getCode().toString();
        shortcutKeys.setText("");
        isUsable = false;

        if (CopyPlusUtils.hasModifierKey(event)) {
            List<String> keys = new ArrayList<>();
            if (event.isMetaDown()) {
                keys.add("Meta");
            }
            if (event.isControlDown()) {
                keys.add("Ctrl");
            }
            if (event.isShiftDown()) {
                keys.add("Shift");
            }
            if (event.isAltDown()) {
                keys.add("Alt");
            }

            if (CopyPlusUtils.notModifierKey(event)) {
                keys.add(key);
                isUsable = TemplateDao.notHasShortcut(key);
            }

            shortcutKeys.setText(String.join("+", keys));
        }

        usable.setText(isUsable ? "可用" : "不可用");
    }


    public void add(ActionEvent actionEvent) {
        if (!isUsable) {
            CopyPlusUtils.notification("当前快捷键不可用，请换一个试试");
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
        CopyPlusUtils.notification("添加成功");
    }

    public void del(ActionEvent actionEvent) {
        TemplateDao.del(Objects.equals(shortcutOld, null) ? shortcutKeys.getText() : shortcutOld);
        CopyPlusUtils.notification("删除成功");
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

    public void init(@NonNull TemplateDto dto) {
        jsTemplate.setText(dto.getTemplate());
        types.setValue(dto.getType());
        shortcutKeys.setText(dto.getShortcut());
        isPaste.setValue(dto.getIsPaste());
        shortcutOld = dto.getShortcut();
    }

}
