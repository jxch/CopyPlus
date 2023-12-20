package org.jxch.copyplus.copyplus.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.jxch.copyplus.copyplus.FXUtils;
import org.jxch.copyplus.copyplus.db.TemplateDao;
import org.jxch.copyplus.copyplus.db.TemplateDto;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class MainController extends RootController {

    @FXML
    private VBox templateBox;


    public AddTemplateController add(ActionEvent actionEvent)  {
        try {
            FXMLLoader fxmlLoader = FXUtils.getFXMLLoader(FXUtils.getFxmlName(AddTemplateController.class));
            templateBox.getChildren().add(fxmlLoader.load());
            templateBox.getChildren().add(new Separator());
            return fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TemplateDao.getShortcutKeys().forEach((shortcut) -> {
            TemplateDto dto = TemplateDao.searchByShortcut(shortcut);
            AddTemplateController controller = add(null);
            controller.init(dto);
        });
    }
}