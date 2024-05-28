package io.github.jxch.copyplus.core.model;

import io.github.jxch.copyplus.core.engine.ClipboardTemplateEngine;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.springframework.stereotype.Component;

@Component
public class ModifyPasteboardModel implements ClipboardTemplateModel {

    @Override
    public void execute(ClipboardTemplateEngine engine, String script) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        String newValue = engine.eval(script, clipboard.getString());
        ClipboardContent content = new ClipboardContent();
        content.putString(newValue);
        clipboard.setContent(content);
    }

    @Override
    public TemplateModel getTemplateModel() {
        return TemplateModel.MODIFY_PASTEBOARD;
    }

}
