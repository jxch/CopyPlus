package io.github.jxch.copyplus.core.key;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jxch.capital.fx.processor.StagePostProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GlobalKeyListener implements NativeKeyListener, StagePostProcessor {
    private final OnceShortCutKeyTrigger onceShortCutKeyTrigger;

    @Override
    public void nativeKeyPressed(NativeKeyEvent keyEvent) {
        String shortcutKey = NativeKeyEvent.getModifiersText(keyEvent.getModifiers()) + "+" + NativeKeyEvent.getKeyText(keyEvent.getKeyCode());
        onceShortCutKeyTrigger.trigger(shortcutKey);
    }

    @Override
    @SneakyThrows
    public void postProcess(Stage stage) {
        GlobalScreen.addNativeKeyListener(this);
        stage.setOnShowing(event -> registerNativeHook());
        stage.setOnCloseRequest(event -> unregisterNativeHook());
    }

    @SneakyThrows
    private void registerNativeHook() {
        GlobalScreen.registerNativeHook();
    }

    @SneakyThrows
    private void unregisterNativeHook() {
        GlobalScreen.unregisterNativeHook();
    }

}
