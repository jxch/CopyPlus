package org.jxch.copyplus.copyplus;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GlobalKeyListener  implements NativeKeyListener {
    @Override
    public void nativeKeyPressed(@NonNull NativeKeyEvent e) {
        log.info("{}, {}", e.getKeyCode(), NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public static void main(String[] args) {
        NativeKeyEvent e = null;
        if (e.getModifiers() == NativeKeyEvent.CTRL_L_MASK && e.getKeyCode() == NativeKeyEvent.VC_V) {

            // 获取系统的粘贴板对象
            Platform.runLater(() -> {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                if (clipboard.hasString()) {
                    String content = clipboard.getString();
                    String contentT = "This is from my JavaFX program: " + content;

                    ClipboardContent newContent = new ClipboardContent();
                    newContent.putString(contentT);
                    clipboard.setContent(newContent);

                    log.info(contentT);
                }
            });

        }
    }

    public static void registerNativeHook()  {
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
            log.info("监听全局键盘事件");
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unregisterNativeHook() {
        try {
            GlobalScreen.unregisterNativeHook();
            log.info("退出全局键盘事件");
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
    }


}
