package io.github.jxch.copyplus.core.model;

import io.github.jxch.copyplus.core.engine.ClipboardTemplateEngine;
import javafx.scene.input.Clipboard;
import org.springframework.stereotype.Component;

@Component
public class AutoPasteClipboardTemplateModel implements ClipboardTemplateModel {


    @Override
    public void execute(ClipboardTemplateEngine engine, String script) {
        String newValue = engine.eval(script, Clipboard.getSystemClipboard().getString());

    }

    @Override
    public TemplateModel getTemplateModel() {
        return TemplateModel.AUTO_PASTE;
    }

}
