package org.jxch.copyplus.copyplus.config;

import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

@Slf4j
public class RocksDBConfig {
    public static final String dbPath = "D:\\CopyPlus\\src\\main\\resources\\db\\data\\";
    private static class OptionsSingle {
        private static final Options OPTIONS = new Options();
        static {
            OPTIONS.setCreateIfMissing(true);
        }
    }

    public static Options getOptions() {
        return OptionsSingle.OPTIONS;
    }



    public static void testBasicOperate() {
        Options options = getOptions();

        try (final RocksDB db = RocksDB.open(options, dbPath)) {
            db.put("hello".getBytes(), "world2".getBytes());

            String world = new String(db.get("hello".getBytes()));
            log.info(world);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }
}
