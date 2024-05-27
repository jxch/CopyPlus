package io.github.jxch.copyplus.controller;

import cn.hutool.extra.spring.SpringUtil;
import io.github.jxch.copyplus.controller.converter.TemplateEngineConverter;
import io.github.jxch.copyplus.controller.converter.TemplateModelConverter;
import io.github.jxch.copyplus.core.CopyPlusUtils;
import io.github.jxch.copyplus.core.engine.ClipboardTemplateEngine;
import io.github.jxch.copyplus.core.engine.JSClipboardTemplateEngine;
import io.github.jxch.copyplus.core.event.TemplateRefreshEvent;
import io.github.jxch.copyplus.core.model.AutoPasteClipboardTemplateModel;
import io.github.jxch.copyplus.core.model.ClipboardTemplateModel;
import io.github.jxch.copyplus.db.dao.CopyTemplateRepository;
import io.github.jxch.copyplus.db.po.CopyTemplate;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;

@Slf4j
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TemplateController implements Initializable {
    @FXML
    public ChoiceBox<ClipboardTemplateModel> modelBox;
    @FXML
    private ChoiceBox<ClipboardTemplateEngine> engineBox;
    @FXML
    private TextField shortcutKeys;
    @FXML
    private TextArea template;

    @FXML
    private Label usable;
    private boolean isUsable = false;
    private CopyTemplate copyTemplate = new CopyTemplate();

    @Autowired
    private List<ClipboardTemplateModel> models;
    @Autowired
    private List<ClipboardTemplateEngine> engines;
    @Autowired
    private CopyTemplateRepository repository;

    @FXML
    protected void enterShortcutKeys(@NonNull KeyEvent event) {
        String key = event.getCode().toString();
        shortcutKeys.setText("");
        if (CopyPlusUtils.hasModifierKey(event)) {
            List<String> keys = new ArrayList<>();
            keys.add(event.isMetaDown() ? "Meta" : null);
            keys.add(event.isControlDown() ? "Ctrl" : null);
            keys.add(event.isShiftDown() ? "Shift" : null);
            keys.add(event.isAltDown() ? "Alt" : null);
            keys.add(CopyPlusUtils.notModifierKey(event) ? key : null);
            keys.removeAll(Collections.singleton(null));
            shortcutKeys.setText(String.join("+", keys));
        }
        isUsable = CopyPlusUtils.notModifierKey(event) &&
                Objects.isNull(repository.findByShortcutKey(shortcutKeys.getText()));
        usable.setText(isUsable ? "可用" : "不可用");
    }

    public void add() {
        if (!isUsable) {
            CopyPlusUtils.notification("当前快捷键不可用，请换一个试试");
            return;
        }
        repository.save(copyTemplate.setTemplate(template.getText())
                .setModel(modelBox.getValue().getTemplateModel())
                .setEngine(engineBox.getValue().getTemplateEngine())
                .setShortcutKey(shortcutKeys.getText()));
        CopyPlusUtils.notification("添加成功");
        SpringUtil.publishEvent(new TemplateRefreshEvent(this));
    }

    public void del() {
        if (Objects.nonNull(copyTemplate.getId())) {
            repository.deleteById(copyTemplate.getId());
            CopyPlusUtils.notification("删除成功");
        }
        SpringUtil.publishEvent(new TemplateRefreshEvent(this));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engines = SpringUtil.getBeansOfType(ClipboardTemplateEngine.class).values().stream().toList();
        models = SpringUtil.getBeansOfType(ClipboardTemplateModel.class).values().stream().toList();

        engineBox.getItems().addAll(engines);
        engineBox.setValue(SpringUtil.getBean(JSClipboardTemplateEngine.class));
        engineBox.setConverter(SpringUtil.getBean(TemplateEngineConverter.class));

        modelBox.getItems().addAll(models);
        modelBox.setValue(SpringUtil.getBean(AutoPasteClipboardTemplateModel.class));
        modelBox.setConverter(SpringUtil.getBean(TemplateModelConverter.class));

        template.setText(SpringUtil.getBean(JSClipboardTemplateEngine.class).defaultScript());
    }

    public void setTemplate(CopyTemplate copyTemplate) {
        this.copyTemplate = copyTemplate;
        engineBox.setValue(engines.stream().filter(engine ->
                Objects.equals(copyTemplate.getEngine(), engine.getTemplateEngine())).findFirst().orElseThrow());
        modelBox.setValue(models.stream().filter(model ->
                Objects.equals(copyTemplate.getModel(), model.getTemplateModel())).findFirst().orElseThrow());
        template.setText(copyTemplate.getTemplate());
        shortcutKeys.setText(copyTemplate.getShortcutKey());
    }

}
