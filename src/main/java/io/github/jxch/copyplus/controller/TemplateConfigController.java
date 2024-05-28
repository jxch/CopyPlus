package io.github.jxch.copyplus.controller;

import io.github.jxch.copyplus.core.event.TemplateRefreshEvent;
import io.github.jxch.copyplus.db.dao.CopyTemplateRepository;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jxch.capital.fx.util.SpringFXUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
@Component
public class TemplateConfigController implements Initializable, ApplicationListener<TemplateRefreshEvent> {
    public VBox container;

    @Autowired
    private CopyTemplateRepository repository;

    @SneakyThrows
    public TemplateController add() {
        FXMLLoader fxmlLoader = SpringFXUtil.getFXMLLoader(TemplateController.class);
        container.getChildren().add(fxmlLoader.load());
        return fxmlLoader.getController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        container.getChildren().clear();
        repository.findAll().forEach(template -> add().setTemplate(template));
    }

    @Override
    public void onApplicationEvent(TemplateRefreshEvent event) {
        initialize(null, null);
    }

}
