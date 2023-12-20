package org.jxch.copyplus.copyplus.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDto {
    private String shortcut;
    private String type;
    private String template;

    public String typeId() {
        return TemplateDao.getTypeId(getShortcut());
    }

    public String templateId() {
        return TemplateDao.getTemplateId(getShortcut());
    }

}
