package project.strategy;

import com.google.common.collect.HashBiMap;

public class HashBiMapStorageStrategy implements StorageStrategy {
    HashBiMap<Long, String> data = HashBiMap.create();

    @Override
    public void put(Long key, String value) {
        data.put(key, value);
    }

    @Override
    public boolean containsValue(String value) {
        return data.containsValue(value);
    }

    @Override
    public boolean containsKey(Long key) {
        return data.containsKey(key);
    }

    @Override
    public String getValue(Long key) {
        return data.get(key);
    }

    @Override
    public Long getKey(String value) {
        return data.inverse().get(value);
    }
}

