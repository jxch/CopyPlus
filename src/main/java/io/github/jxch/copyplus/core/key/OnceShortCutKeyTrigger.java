package io.github.jxch.copyplus.core.key;

import cn.hutool.core.collection.ConcurrentHashSet;
import io.github.jxch.copyplus.core.converter.TemplateEngineConverter;
import io.github.jxch.copyplus.core.converter.TemplateModelConverter;
import io.github.jxch.copyplus.core.engine.ClipboardTemplateEngine;
import io.github.jxch.copyplus.core.event.TemplateRefreshEvent;
import io.github.jxch.copyplus.core.model.ClipboardTemplateModel;
import io.github.jxch.copyplus.db.dao.CopyTemplateRepository;
import io.github.jxch.copyplus.db.po.CopyTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OnceShortCutKeyTrigger implements ShortCutKeyTrigger, SmartInitializingSingleton, ApplicationListener<TemplateRefreshEvent> {
    private final static Set<String> CURRENT_SHORT_CUT_KEY = new ConcurrentHashSet<>();
    private final TemplateEngineConverter engineConverter;
    private final TemplateModelConverter modelConverter;
    private Map<String, CopyTemplate> copyTemplateMap;
    private final CopyTemplateRepository repository;

    @Async
    @Override
    @SneakyThrows
    public void trigger(String shortcutKey) {
        if (!CURRENT_SHORT_CUT_KEY.contains(shortcutKey) && copyTemplateMap.containsKey(shortcutKey)) {
            synchronized (CURRENT_SHORT_CUT_KEY) {
                if (!CURRENT_SHORT_CUT_KEY.contains(shortcutKey)) {
                    CURRENT_SHORT_CUT_KEY.add(shortcutKey);
                    CopyTemplate copyTemplate = copyTemplateMap.get(shortcutKey);
                    ClipboardTemplateEngine engine = engineConverter.fromString(copyTemplate.getEngine().name());
                    ClipboardTemplateModel model = modelConverter.fromString(copyTemplate.getModel().name());
                    model.execute(engine, copyTemplate.getTemplate());
                    System.out.println(Thread.currentThread());
                } else {
                    return;
                }
            }

            TimeUnit.MILLISECONDS.sleep(900);
            CURRENT_SHORT_CUT_KEY.remove(shortcutKey);
        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        copyTemplateMap = repository.findAll().stream().collect(Collectors.toMap(CopyTemplate::getShortcutKey, Function.identity()));
    }

    @Override
    public void onApplicationEvent(TemplateRefreshEvent event) {
        afterSingletonsInstantiated();
    }

}
