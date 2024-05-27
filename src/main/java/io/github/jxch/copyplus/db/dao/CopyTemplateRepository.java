package io.github.jxch.copyplus.db.dao;


import io.github.jxch.copyplus.db.po.CopyTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CopyTemplateRepository extends JpaRepository<CopyTemplate, Long> {

    CopyTemplate findByShortcutKey(String shortcutKey);

}
