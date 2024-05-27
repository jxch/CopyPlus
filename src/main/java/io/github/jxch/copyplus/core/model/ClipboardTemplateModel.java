package io.github.jxch.copyplus.core.model;

import io.github.jxch.copyplus.core.engine.ClipboardTemplateEngine;

public interface ClipboardTemplateModel {

    void execute(ClipboardTemplateEngine engine, String script);

    TemplateModel getTemplateModel();

}
