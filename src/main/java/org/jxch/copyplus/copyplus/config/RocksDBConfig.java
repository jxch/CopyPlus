package org.jxch.copyplus.copyplus.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.util.Objects;

@Slf4j
public class RocksDBConfig {
    @Getter
    private static final String dbPath = "D:\\CopyPlus\\src\\main\\resources\\db\\data\\";
    private static class OptionsSingle {
        private static final Options OPTIONS = new Options();
        private static RocksDB db;

        @NonNull
        private static RocksDB open() {
            try{
                db = RocksDB.open(getOptions(), getDbPath());
                return db;
            } catch (RocksDBException e) {
                throw new RuntimeException(e);
            }
        }

        static {
            OPTIONS.setCreateIfMissing(true);
            db = open();
        }
    }

    public static Options getOptions() {
        return OptionsSingle.OPTIONS;
    }

    @NonNull
    public static RocksDB getDB() {
        if (Objects.equals(OptionsSingle.db, null)) {
            return OptionsSingle.open();
        }
        return OptionsSingle.db;
    }

    public static void close() {
        OptionsSingle.db.close();
        OptionsSingle.db = null;
    }

}
