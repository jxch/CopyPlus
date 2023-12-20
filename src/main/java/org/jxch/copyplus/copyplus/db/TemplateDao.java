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
    private static final String PASTE_SUFFIX = "-paste";
    private static final RocksDB DB = RocksDBConfig.getDB();


    public static void put(@NonNull TemplateDto dto) {
        try {
            DB.put(dto.typeId().getBytes(), dto.getType().getBytes());
            DB.put(dto.templateId().getBytes(), dto.getTemplate().getBytes());
            DB.put(dto.pasteId().getBytes(), dto.getIsPaste().getBytes());
            addShortcutKeys(dto.getShortcut());
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

    public static void putOrUpdate(@NonNull TemplateDto dto,  String shortcut) {
        try {
            put(dto);
            if (!Objects.equals(shortcut, null)) {
                DB.delete(shortcut.getBytes());
                if (!Objects.equals(dto.getShortcut(), shortcut)) {
                    delShortcutKeys(shortcut);
                }
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

    public static void del(@NonNull String shortcut) {
        try {
            delShortcutKeys(shortcut);
            DB.delete(getTemplateId(shortcut).getBytes());
            DB.delete(getTypeId(shortcut).getBytes());
            DB.delete(getPasteId(shortcut).getBytes());
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addShortcutKeys(@NonNull String shortcut) {
        try {
            List<String> shortcutKeys = getShortcutKeys();
            if (!shortcutKeys.contains(shortcut)) {
                shortcutKeys.add(shortcut);
                DB.put(SHORTCUT_KEYS.getBytes(), String.join(SHORTCUT_KEYS_SPLIT, shortcutKeys).getBytes());
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

    private static void delShortcutKeys(@NonNull String shortcut) {
        try {
            List<String> shortcutKeys = getShortcutKeys();
            shortcutKeys.remove(shortcut);
            DB.put(SHORTCUT_KEYS.getBytes(), String.join(SHORTCUT_KEYS_SPLIT, shortcutKeys).getBytes());
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    @NonNull
    public static List<String> getShortcutKeys() {
        try {
            if (!Objects.equals(DB.get(SHORTCUT_KEYS.getBytes()), null)
                    && DB.get(SHORTCUT_KEYS.getBytes()).length > 0) {
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
            String paste = new String(DB.get(getPasteId(shortcut).getBytes()));
            return TemplateDto.builder()
                    .shortcut(shortcut)
                    .type(type)
                    .template(template)
                    .isPaste(paste)
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

    @NonNull
    public static String getPasteId(@NonNull String shortcut) {
        return shortcut + PASTE_SUFFIX;
    }

}

