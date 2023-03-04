package project.strategy;

public class FileStorageStrategy implements StorageStrategy {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final long DEFAULT_BUCKET_SIZE_LIMIT = 10000;

    int size;
    long maxBucketSize;
    private long bucketSizeLimit;
    private FileBucket[] table;

    public FileStorageStrategy() {
        bucketSizeLimit = DEFAULT_BUCKET_SIZE_LIMIT;
        initTable();
    }

    private void initTable() {
        table = new FileBucket[DEFAULT_INITIAL_CAPACITY];
        for (int i = 0; i < table.length; i++) {
            table[i] = new FileBucket();
        }
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    public void put(Long key, String value) {
        int hash = key.hashCode();
        int bucketIndex = indexFor(hash, table.length);
        for (Entry entry = table[bucketIndex].getEntry(); entry != null; entry = entry.next)
            if (entry.getKey().equals(key)) {
                entry.value = value;
                return;
            }
        addEntry(hash, key, value, bucketIndex);
    }

    private void addEntry(int hash, Long key, String value, int bucketIndex) {
        if (maxBucketSize > getBucketSizeLimit()) {
            resize(table.length * 3/2);
            bucketIndex = indexFor(hash, table.length);
        }
        createEntry(hash, key, value, bucketIndex);
    }

    private void createEntry(int hash, Long key, String value, int bucketIndex) {
        Entry e = table[bucketIndex].getEntry();
        table[bucketIndex].putEntry(new Entry(hash, key, value, e));
        size++;

        long currentBucketSize = table[bucketIndex].getFileSize();
        if (currentBucketSize > maxBucketSize)
            maxBucketSize = currentBucketSize;
    }

    private void resize(int newCapacity) {
        FileBucket[] newTable = new FileBucket[newCapacity];

        for (int i = 0; i < newTable.length; i++)
            newTable[i] = new FileBucket();

        transfer(newTable);

        for (int i = 0; i < table.length; i++)
            table[i].remove();

        table = newTable;
    }

    private void transfer(FileBucket[] newTable) {
        int newCapacity = newTable.length;
        maxBucketSize = 0;

        for (FileBucket bucket : table) {
            Entry entry = bucket.getEntry();
            while (entry != null) {
                Entry nextEntry = entry.next;
                int indexInNewTable = indexFor(entry.getKey().hashCode(), newCapacity);
                entry.next = newTable[indexInNewTable].getEntry();
                newTable[indexInNewTable].putEntry(entry);
                entry = nextEntry;
            }

            long currentBucketSize = bucket.getFileSize();
            if (currentBucketSize > maxBucketSize)
                maxBucketSize = currentBucketSize;

        }
    }

    private void removeTable(FileBucket[] table) {
        for (FileBucket fileBucket : table)
            fileBucket.remove();
    }

    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(String value) {
        for (FileBucket tableElement : table)
            for (Entry e = tableElement.getEntry(); e != null; e = e.next)
                if (value.equals(e.value))
                    return true;
        return false;
//        return findEntryByValue(value) != null;
    }

    @Override
    public Long getKey(String value) {
//        Entry entry = findEntryByValue(value);
//        return entry != null ? entry.getKey() : null;
        for (FileBucket tableElement : table)
            for (Entry e = tableElement.getEntry(); e != null; e = e.next)
                if (value.equals(e.value))
                    return e.getKey();
        return null;
    }

    public String getValue(Long key) {
        Entry entry = getEntry(key);
        if (entry != null)
            return entry.getValue();
        return null;
    }

    private Entry findEntryByValue(String value) {
        for (FileBucket bucket : table) {
            Entry entry = bucket.getEntry();
            for(; entry != null; entry = entry.next)
                if (entry.getValue().equals(value))
                    return entry;
        }
        return null;
    }

    private Entry getEntry(Long key) {
        if (size == 0)
            return null;
        int index = indexFor(key.hashCode(), table.length);
        for (Entry entry = table[index].getEntry(); entry!= null; entry = entry.next)
            if (entry.getKey().equals(key))
                return entry;
        return null;
    }

    public long getBucketSizeLimit() {
        return bucketSizeLimit;
    }

    public void setBucketSizeLimit(long bucketSizeLimit) {
        this.bucketSizeLimit = bucketSizeLimit;
    }


}
