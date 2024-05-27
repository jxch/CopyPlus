package io.github.jxch.copyplus.core.event;

import org.springframework.context.ApplicationEvent;

public class TemplateRefreshEvent extends ApplicationEvent {
    
    public TemplateRefreshEvent(Object source) {
        super(source);
    }
    
}
