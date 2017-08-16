package com.robl2e.thistodo.data.local;

import io.paperdb.Paper;

/**
 * Created by robl2e on 8/15/2017.
 */

public class LocalPersistence implements Persistence {
    private static final class Holder {
        static final LocalPersistence localPersistence = new LocalPersistence();
    }

    /**
     * Returns the convenience global LocalPersistence.
     * <br/>Note that <code>MyClass</code> is not a singleton.
     * @return persistence
     */
    public static Persistence getInstance() {
        return Holder.localPersistence;
    }

    @Override
    public <T> void write(String key, T data) {
        Paper.book().write(key, data);
    }

    @Override
    public <T> T read(String key) {
        return Paper.book().read(key);
    }

    @Override
    public <T> T read(String key, T defaultValue) {
        return Paper.book().read(key, defaultValue);
    }

    @Override
    public void delete(String key) {
        Paper.book().delete(key);
    }

    @Override
    public boolean exist(String key) {
        return Paper.book().exist(key);
    }
}
