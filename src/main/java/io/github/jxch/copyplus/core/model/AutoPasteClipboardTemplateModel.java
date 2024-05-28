package io.github.jxch.copyplus.core.model;

import com.google.common.annotations.Beta;
import io.github.jxch.copyplus.core.User32;
import io.github.jxch.copyplus.core.engine.ClipboardTemplateEngine;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jxch.capital.fx.platform.PlatformRunLater;
import org.springframework.stereotype.Component;

@Beta
@Slf4j
@Component
public class AutoPasteClipboardTemplateModel implements ClipboardTemplateModel {
    private static final byte VK_CONTROL = 0x11;
    private static final byte VK_V = 0x56;

    @PlatformRunLater
    @Override
    @SneakyThrows
    public void execute(ClipboardTemplateEngine engine, String script) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        String newValue = engine.eval(script, clipboard.getString());
        ClipboardContent content = new ClipboardContent();
        content.putString(newValue);
        clipboard.setContent(content);

        User32.INSTANCE.keybd_event(VK_CONTROL, (byte) 0, 0, 0); // 按下Ctrl键
        User32.INSTANCE.keybd_event(VK_V, (byte) 0, 0, 0);       // 按下V键
        User32.INSTANCE.keybd_event(VK_V, (byte) 0, 2, 0);       // 释放V键
        User32.INSTANCE.keybd_event(VK_CONTROL, (byte) 0, 2, 0); // 释放Ctrl键
    }

    @Override
    public TemplateModel getTemplateModel() {
        return TemplateModel.AUTO_PASTE;
    }

}
