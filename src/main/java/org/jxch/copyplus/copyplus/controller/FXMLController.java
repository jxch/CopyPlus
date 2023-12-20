package org.jxch.copyplus.copyplus.controller;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FXMLController {
    String fxml();
}
