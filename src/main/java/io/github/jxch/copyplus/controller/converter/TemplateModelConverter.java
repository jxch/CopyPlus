package io.github.jxch.copyplus.controller.converter;

import io.github.jxch.copyplus.core.model.ClipboardTemplateModel;
import io.github.jxch.copyplus.core.model.TemplateModel;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TemplateModelConverter extends StringConverter<ClipboardTemplateModel> {
    private final List<ClipboardTemplateModel> models;

    @Override
    public String toString(ClipboardTemplateModel templateModel) {
        return templateModel.getTemplateModel().name();
    }

    @Override
    public ClipboardTemplateModel fromString(String s) {
        return models.stream().filter(model -> Objects.equals(model.getTemplateModel(), TemplateModel.valueOf(s))).findFirst().orElseThrow();
    }

}
