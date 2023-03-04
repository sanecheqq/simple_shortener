package project.strategy;

public interface StorageStrategy {
    public boolean containsKey(Long key);
    public boolean containsValue(String value);
    public void put(Long key, String value);
    public Long getKey(String value);
    public String getValue(Long key);
}
