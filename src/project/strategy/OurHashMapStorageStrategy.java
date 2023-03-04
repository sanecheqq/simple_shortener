package project.strategy;

public class OurHashMapStorageStrategy implements StorageStrategy {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    Entry[] table = new Entry[DEFAULT_INITIAL_CAPACITY];
    int size;
    int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    float loadFactor = DEFAULT_LOAD_FACTOR;

    final int hash(Long k) {
        k ^= (k >>> 20) ^ (k >>> 12);
        return (int) (k ^ (k >>> 7) ^ (k >>> 4));
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @Override
    public void put(Long key, String value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Entry entry = table[index]; entry != null; entry = entry.next)
            if (entry.getKey().equals(key)) {
                entry.value = value;
                return;
            }
        addEntry(hash, key, value, index);
    }

    private void addEntry(int hash, Long key, String value, int bucketIndex) {
        if (size >= threshold) {
            resize(table.length * 3/2);
//            hash = hash(key); try to do w/o it
            bucketIndex = indexFor(hash, table.length);
        }
        createEntry(hash, key, value, bucketIndex);
    }

    private void createEntry(int hash, Long key, String value, int bucketIndex) {
        Entry e = table[bucketIndex];
        table[bucketIndex] = new Entry(hash, key, value, e);
        size++;
    }

    public void resize(int newCapacity) {
        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    public void transfer(Entry[] newTable) {
        int newCapacity = newTable.length;
        for (Entry entry : table) {
            while (entry != null) {
                Entry nextEntry = entry.next;
                int indexInNewTable = indexFor(hash(entry.getKey()), newCapacity);
                entry.next = newTable[indexInNewTable];
                newTable[indexInNewTable] = entry;
                entry = nextEntry;
            }
        }
    }

    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(String value) {
        for (Entry tableElement : table)
            for (Entry entry = tableElement; entry != null; entry = entry.next)
                if (entry.getValue().equals(value))
                    return true;
        return false;
    }

    @Override
    public Long getKey(String value) {
        for (Entry tableElement : table)
            for (Entry entry = tableElement; entry != null; entry = entry.next)
                if (entry.getValue().equals(value))
                    return entry.getKey();
        return null;
    }

    @Override
    public String getValue(Long key) {
        return getEntry(key).getValue();
    }

    public Entry getEntry(Long key) {
        if (size == 0)
            return null;
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Entry entry = table[index]; entry != null; entry = entry.next)
            if (key.equals(entry.key))
                return entry;
        return null;
    }
}
