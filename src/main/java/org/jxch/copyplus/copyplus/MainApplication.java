package org.jxch.copyplus.copyplus;

import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class MainApplication extends Application implements NativeKeyListener {
    private static final String MAIN_FXML = "main-view.fxml";
    private static final String TITLE = "Copy Plus";

    @Override
    public void start(@NonNull Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(MAIN_FXML));
        Scene scene = new Scene(fxmlLoader.load(), 500, 800);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();

        GlobalKeyListener.registerNativeHook();
        stage.setOnCloseRequest((event) -> GlobalKeyListener.unregisterNativeHook());
    }

    public static void main(String[] args) {
        launch();
    }

}