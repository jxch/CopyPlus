package io.github.jxch.copyplus.db.po;

import io.github.jxch.copyplus.core.model.TemplateModel;
import io.github.jxch.copyplus.core.engine.TemplateEngine;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity(name = "copy_template")
@Table(indexes = {@Index(name = "copy_template_shortcut_key", columnList = "shortcut_key", unique = true)})
public class CopyTemplate {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String shortcutKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TemplateEngine engine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TemplateModel model;

    @Lob
    @Column(nullable = false)
    private String template;
}
