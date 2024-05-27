package io.github.jxch.copyplus.core.engine;

public interface ClipboardTemplateEngine {

    String eval(String script, String clipboard);

    String defaultScript();

    TemplateEngine getTemplateEngine();

}
