package io.github.jxch.copyplus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class CopyPlusConfig {

    @Bean
    public ScriptEngine scriptEngine() {
        return new ScriptEngineManager().getEngineByName("graal.js");
    }

}
