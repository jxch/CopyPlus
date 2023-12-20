package org.jxch.copyplus.copyplus.db;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jxch.copyplus.copyplus.config.RocksDBConfig;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class TemplateDao {
    private static final String SHORTCUT_KEYS = "shortcut_keys";
    private static final String SHORTCUT_KEYS_SPLIT = ",";
    private static final String TYPE_SUFFIX = "-type";
    private static final String TEMPLATE_SUFFIX = "-template";
    private static final RocksDB DB = RocksDBConfig.getDB();


    public static void put(@NonNull TemplateDto dto) {
        try {
            DB.put(dto.typeId().getBytes(), dto.getType().getBytes());
            DB.put(dto.templateId().getBytes(), dto.getTemplate().getBytes());

            List<String> shortcutKeys = getShortcutKeys();
            shortcutKeys.add(dto.getShortcut());
            DB.put(SHORTCUT_KEYS.getBytes(), String.join(SHORTCUT_KEYS_SPLIT, shortcutKeys).getBytes());
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public static List<String> getShortcutKeys() {
        try {
            if (!Objects.equals(DB.get(SHORTCUT_KEYS.getBytes()), null)) {
                return new ArrayList<>(Arrays.asList(new String(DB.get(SHORTCUT_KEYS.getBytes())).split(SHORTCUT_KEYS_SPLIT)));
            }
            return new ArrayList<>();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean notHasShortcut(String shortcut) {
        return !getShortcutKeys().contains(shortcut);
    }

    public static TemplateDto searchByShortcut(String shortcut) {
        try {
            String type = new String(DB.get(getTypeId(shortcut).getBytes()));
            String template = new String(DB.get(getTemplateId(shortcut).getBytes()));
            return TemplateDto.builder()
                    .shortcut(shortcut)
                    .type(type)
                    .template(template)
                    .build();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public static String getTypeId(@NonNull String shortcut) {
        return shortcut + TYPE_SUFFIX;
    }

    @NonNull
    public static String getTemplateId(@NonNull String shortcut) {
        return shortcut + TEMPLATE_SUFFIX;
    }

}

