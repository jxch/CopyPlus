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
import org.jxch.copyplus.copyplus.db.TemplateDto;
import org.jxch.copyplus.copyplus.template.JSTemplate;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.*;


@Slf4j
public class GlobalKeyListener implements NativeKeyListener {

    private static final Set<String> GLOBAL_SHORTCUT = Collections.synchronizedSortedSet(new TreeSet<>());

    @Override
    public void nativeKeyPressed(@NonNull NativeKeyEvent e) {
        List<String> shortcutKeys = TemplateDao.getShortcutKeys();

        String keyName = NativeKeyEvent.getKeyText(e.getKeyCode());
        GLOBAL_SHORTCUT.add(keyName);

        if (Objects.equals(e.getModifiers(), NativeKeyEvent.CTRL_L_MASK)
                || Objects.equals(e.getModifiers(), NativeKeyEvent.CTRL_R_MASK)) {
            GLOBAL_SHORTCUT.add("Ctrl");
        }
        if (Objects.equals(e.getModifiers(), NativeKeyEvent.SHIFT_L_MASK)
                || Objects.equals(e.getModifiers(), NativeKeyEvent.SHIFT_R_MASK)) {
            GLOBAL_SHORTCUT.add("Shift");
        }
        if (Objects.equals(e.getModifiers(), NativeKeyEvent.ALT_L_MASK)
                || Objects.equals(e.getModifiers(), NativeKeyEvent.ALT_R_MASK)) {
            GLOBAL_SHORTCUT.add("Alt");
        }
        if (Objects.equals(e.getModifiers(), NativeKeyEvent.META_L_MASK)
                || Objects.equals(e.getModifiers(), NativeKeyEvent.META_R_MASK)) {
            GLOBAL_SHORTCUT.add("Windows");
        }

        if (!FXUtils.notKey(keyName)) {
            String shortcut = String.join("+", GLOBAL_SHORTCUT);
            if (shortcutKeys.contains(shortcut)) {
                TemplateDto dto = TemplateDao.searchByShortcut(shortcut);

                log.info("{}", shortcut);
                Platform.runLater(() -> {
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    if (clipboard.hasString()) {
                        String contentT = JSTemplate.eval(dto.getTemplate(), clipboard.getString());

                        ClipboardContent newContent = new ClipboardContent();
                        newContent.putString(contentT);
                        clipboard.setContent(newContent);
                        log.info("{}", contentT);
                        if (Boolean.parseBoolean(dto.getIsPaste())){
                            try {
                                // 创建一个Robot对象
                                Robot robot = new Robot();
                                // 模拟按下Ctrl键
                                robot.keyPress(KeyEvent.VK_CONTROL);
                                // 模拟按下V键
                                robot.keyPress(KeyEvent.VK_V);
                                // 模拟释放V键
                                robot.keyRelease(KeyEvent.VK_V);
                                // 模拟释放Ctrl键
                                robot.keyRelease(KeyEvent.VK_CONTROL);
                            } catch (AWTException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            }

            GLOBAL_SHORTCUT.clear();
        }

    }

    @Override
    public void nativeKeyReleased(@NonNull NativeKeyEvent e) {
        String keyName = NativeKeyEvent.getKeyText(e.getKeyCode());
        GLOBAL_SHORTCUT.remove(keyName);
    }

    public static void registerNativeHook() {
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
