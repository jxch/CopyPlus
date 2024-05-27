package io.github.jxch.copyplus.core.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JSClipboardTemplateEngine implements ClipboardTemplateEngine {
    private static final String RES_NAME = "text";
    private final ScriptEngine scriptEngine;

    @Override
    public String eval(String script, String clipboard) {
        try {
            String jsCode = String.format("var %s = %s('%s')", RES_NAME, script, clipboard);
            scriptEngine.eval(jsCode);
            return scriptEngine.get(RES_NAME).toString();
        } catch (ScriptException e) {
            log.error("JS 引擎执行失败: {} - {} - {}", script, clipboard, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String defaultScript() {
        return """
                function(clipboard) {
                    // 此处处理粘贴板文字

                    return clipboard;
                }
                """;
    }

    @Override
    public TemplateEngine getTemplateEngine() {
        return TemplateEngine.JS;
    }

}
