package org.jxch.copyplus.copyplus.template;

import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@Slf4j
public class JSTemplate {
    private static final ScriptEngine ENGINE = new ScriptEngineManager().getEngineByName("graal.js");
    private static final String RES_NAME = "text";

    /**
     * @param jsFunction
     * function(clipboard){
     *     return clipboard;
     * }
     *
     * @return RES_NAME
     */
    public static String eval(String jsFunction, String clipboard) throws ScriptException {
        String jsCode = String.format("var %s = %s('%s')", RES_NAME, jsFunction, clipboard);
        ENGINE.eval(jsCode);
        return ENGINE.get(RES_NAME).toString();
    }

}
