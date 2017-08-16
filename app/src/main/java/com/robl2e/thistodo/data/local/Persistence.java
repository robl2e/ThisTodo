package com.robl2e.thistodo.data.local;

/**
 * Created by robl2e on 8/15/2017.
 */

public interface Persistence {
    <T> void write(String key, T data);
    <T> T read(String key);
    <T> T read(String key, T defaultValue);
    void delete(String key);
    boolean exist(String key);
}
