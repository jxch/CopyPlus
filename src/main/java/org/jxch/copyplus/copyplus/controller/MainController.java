package org.jxch.copyplus.copyplus.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import lombok.NonNull;
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
            Node node = fxmlLoader.load();
            Separator separator = new Separator();
            templateBox.getChildren().add(node);
            templateBox.getChildren().add(separator);
            AddTemplateController controller = fxmlLoader.getController();
            controller.setThisNode(node);
            controller.setThisSeparator(separator);
            return controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeNode(@NonNull AddTemplateController controller) {
        templateBox.getChildren().remove(controller.getThisNode());
        templateBox.getChildren().remove(controller.getThisSeparator());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        record();
        TemplateDao.getShortcutKeys().forEach((shortcut) -> {
            TemplateDto dto = TemplateDao.searchByShortcut(shortcut);
            AddTemplateController controller = add(null);
            controller.init(dto);
        });
    }
}