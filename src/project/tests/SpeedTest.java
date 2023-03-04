package project.tests;

import project.*;
import org.junit.Assert;
import org.junit.Test;
import project.strategy.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SpeedTest {
    @Test
    public void testHashMapStorage() {
        Shortener shortener1 = new Shortener(new HashMapStorageStrategy());
        Shortener shortener2 = new Shortener(new HashBiMapStorageStrategy());
        Set<String> srcStrings = new HashSet<>();
        for (int i = 0; i < 10000; i++)
            srcStrings.add(Helper.generateRandomString());

        Set<Long> ids1 = new HashSet<>();
        Set<String> strings1 = new HashSet<>();
        long time1Ids = getTimeToGetIds(shortener1, srcStrings, ids1);
        long time1Strings = getTimeToGetStrings(shortener1, ids1, strings1);

        Set<Long> ids2 = new HashSet<>();
        Set<String> strings2 = new HashSet<>();
        long time2Ids = getTimeToGetIds(shortener2, srcStrings, ids2);
        long time2Strings = getTimeToGetStrings(shortener2, ids2, strings2);

        Assert.assertTrue(time1Ids > time2Ids);
        Assert.assertEquals(time1Strings, time2Strings, 30);
    }


    public long getTimeToGetIds(Shortener shortener, Set<String> strings, Set<Long> ids) {
        Date startTime = new Date();
        for (String string : strings) {
            ids.add(shortener.getId(string));
        }
        Date endTime = new Date();
        return endTime.getTime() - startTime.getTime();
    }

    public long getTimeToGetStrings(Shortener shortener, Set<Long> ids, Set<String> strings) {
        Date startTime = new Date();
        for (Long id : ids) {
            strings.add(shortener.getString(id));
        }
        Date endTime = new Date();
        return endTime.getTime() - startTime.getTime();
    }
}
