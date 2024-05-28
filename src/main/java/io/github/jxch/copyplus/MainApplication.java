package io.github.jxch.copyplus;

import io.github.jxch.copyplus.controller.TemplateConfigController;
import org.jxch.capital.fx.SpringFXApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringFXApplication.run(MainApplication.class, TemplateConfigController.class, args);
    }

}