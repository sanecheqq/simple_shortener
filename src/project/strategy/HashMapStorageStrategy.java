package project.strategy;

import java.util.HashMap;

public class HashMapStorageStrategy implements StorageStrategy {
    private HashMap<Long, String> data = new HashMap<Long, String>();

    @Override
    public void put(Long key, String value) {
        data.put(key, value);
    }

    @Override
    public boolean containsKey(Long key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(String value) {
        return data.containsValue(value);
    }

    @Override
    public Long getKey(String value) {
        return findKeyInData(value);
    }

    public Long findKeyInData(String value) {
        Long key = -1L;
        for (HashMap.Entry<Long, String> entry : data.entrySet())
            if (entry.getValue().equals(value))
                key = entry.getKey();
        return key;
    }

    @Override
    public String getValue(Long key) {
        return data.get(key);
    }
}
