package io.github.jxch.copyplus.core.converter;

import io.github.jxch.copyplus.core.engine.ClipboardTemplateEngine;
import io.github.jxch.copyplus.core.engine.TemplateEngine;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TemplateEngineConverter extends StringConverter<ClipboardTemplateEngine> {
    private final List<ClipboardTemplateEngine> engines;

    @Override
    public String toString(ClipboardTemplateEngine templateEngine) {
        return templateEngine.getTemplateEngine().name();
    }

    @Override
    public ClipboardTemplateEngine fromString(String s) {
        return engines.stream().filter(engine -> Objects.equals(engine.getTemplateEngine(), TemplateEngine.valueOf(s))).findFirst().orElseThrow();
    }

}
