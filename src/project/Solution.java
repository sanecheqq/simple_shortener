package project;

import project.strategy.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Solution {
    public static void main(String[] args) {
        HashMapStorageStrategy hashMapStorageStrategy = new HashMapStorageStrategy();
        testStrategy(hashMapStorageStrategy, 10000);

        OurHashMapStorageStrategy strategy = new OurHashMapStorageStrategy();
        testStrategy(strategy, 10000);

        FileStorageStrategy fileStorageStrategy = new FileStorageStrategy();
        testStrategy(fileStorageStrategy, 100);

        OurHashBiMapStorageStrategy ourBiMapStorageStrategy = new OurHashBiMapStorageStrategy();
        testStrategy(ourBiMapStorageStrategy, 10000);

        HashBiMapStorageStrategy biMapSS = new HashBiMapStorageStrategy();
        testStrategy(biMapSS, 10000);

        DualHashBidiMapStorageStrategy dualHashBidiSS = new DualHashBidiMapStorageStrategy();
        testStrategy(dualHashBidiSS, 10000);
    }



    public static Set<String> getStrings(Shortener shortener, Set<Long> keys) {
        Set<String> strings = new HashSet<>();
        for (Long key : keys) {
            strings.add(shortener.getString(key));
        }
        return strings;
    }

    public static void testStrategy(StorageStrategy strategy, long elementsNumber) {
        System.out.println(strategy.getClass().getSimpleName());
        Set<String> sourceStrings = generateSetOfStrings(elementsNumber);

        Shortener shortener = new Shortener(strategy);
        Set<Long> keys = testKeys(shortener, sourceStrings);
        Set<String> targetStrings = testStrings(shortener, keys);

        getTestResult(sourceStrings, targetStrings);
    }

    public static Set<String> generateSetOfStrings(long elementsNumber) {
        Set<String> sourceStrings = new HashSet<>();
        for (long i = 0; i < elementsNumber; i++) {
            sourceStrings.add(Helper.generateRandomString());
        }
        return sourceStrings;
    }

    public static Set<Long> testKeys(Shortener shortener, Set<String> testStrings) {
        Date timeStartGetIds = new Date();
        Set<Long> keys = getKeys(shortener, testStrings);
        Date timeEndGetIds = new Date();

        Date workTimeGetIds = new Date(timeEndGetIds.getTime() - timeStartGetIds.getTime());
        System.out.println("Time for getIds: " + workTimeGetIds.getTime());
        return keys;
    }

    public static Set<Long> getKeys(Shortener shortener, Set<String> strings) {
        Set<Long> keys = new HashSet<>();
        for (String str : strings) {
            keys.add(shortener.getId(str));
        }
        return keys;
    }

    public static Set<String> testStrings(Shortener shortener, Set<Long> keys) {
        Date timeStartGetStrings = new Date();
        Set<String> strings = getStrings(shortener, keys);
        Date timeEndGetStrings = new Date();

        Date workTimeGetStrings = new Date(timeEndGetStrings.getTime() - timeStartGetStrings.getTime());
        System.out.println("Time for getStrings: " + workTimeGetStrings.getTime());
        return strings;
    }

    public static void getTestResult(Set<String> sourceStrings, Set<String> targetStrings) {
        if (sourceStrings.equals(targetStrings)) {
            System.out.println("Тест пройден.");
        } else {
            System.out.println("Тест не пройден.");
        }
    }
}
