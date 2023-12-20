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
import org.jxch.copyplus.copyplus.db.TemplateDao;

import java.util.List;


@Slf4j
public class GlobalKeyListener  implements NativeKeyListener {
    @Override
    public void nativeKeyPressed(@NonNull NativeKeyEvent e) {
        log.info("{}, {}", e.getKeyCode(), NativeKeyEvent.getKeyText(e.getKeyCode()));
        List<String> shortcutKeys = TemplateDao.getShortcutKeys();
//
//        String keyName = NativeKeyEvent.getKeyText(e.getKeyCode());
//        // 判断是否按下了修饰键
//        if ((e.getModifiers() == NativeKeyEvent.CTRL_L_MASK || (e.getModifiers() == NativeKeyEvent.CTRL_R_MASK) {
//            keyName = Objects.equals(keyName, "Ctrl") ? "Ctrl" : "Ctrl+" + keyName;
//        }
//        if (event.isAltDown()) {
//            keyName = Objects.equals(keyName, "Alt") ? "Alt" : "Alt+" + keyName;
//        }
//        if (event.isShiftDown()) {
//            keyName = Objects.equals(keyName, "Shift") ? "Shift" : "Shift+" + keyName;
//        }
//        if (event.isMetaDown()) {
//            keyName = Objects.equals(keyName, "Windows") ? "Windows" : "Windows+" + keyName;
//        }
//        if (notKey(sourceKeyName)) {
//            ArrayList<String> keys = new ArrayList<>(Arrays.asList(keyName.split("\\+")));
//            if (keys.size() > 1) {
//                keys.remove(keys.size() - 1);
//                log.info(keys.toString());
//                keyName = String.join("+", keys);
//            }
//        }

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
