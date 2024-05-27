package io.github.jxch.copyplus;

import org.jxch.capital.fx.SpringFXApplication;
import io.github.jxch.copyplus.controller.TemplateController;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringFXApplication.run(MainApplication.class, TemplateController.class, args);
    }

}