package io.github.jxch.copyplus.core.model;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.jxch.capital.fx.processor.StagePostProcessor;
import org.springframework.stereotype.Component;

@Component
public class GlobalKeyListener implements NativeKeyListener, StagePostProcessor {

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {

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
