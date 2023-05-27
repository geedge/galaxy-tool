package com.zdjizhi.test;

import com.zdjizhi.utils.IPUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.datasketches.frequencies.ErrorType;
import org.apache.datasketches.frequencies.ItemsSketch;
import org.apache.datasketches.hll.HllSketch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class HllSketchTest {
    @Test
    public void testHllSketch() {
        HllSketch sketch = new HllSketch();
        long ipaddr = IPUtil.getIpDesimal("1.1.1.1");
        long n = 10000000;
        List<String> ipSets = new ArrayList<String>();
        for (int i = 0; i < n; i++) {
            String ip = String.valueOf(ipaddr + RandomUtils.nextInt(1, 100000000));
            ipSets.add(ip);
            sketch.update(IPUtil.getIpString(ip));
        }

        long startTime = System.nanoTime();
        System.out.println("Unique IPs: " + ipSets.stream().distinct().count());
        System.out.println((System.nanoTime() - startTime) / 1000 / 1000 + "s");

        startTime = System.nanoTime();
        System.out.println("Estimated IPs:" + sketch.getEstimate());
        System.out.println(sketch.toString());
        System.out.println((System.nanoTime() - startTime) / 1000 / 1000 + "s");

    }

    @Test
    public void testFrequentItems() {
        ItemsSketch<String> sketch = new ItemsSketch<String>(67108864);
        sketch.update("abc.com", 10);
        sketch.update("bcd.com", 5);
        sketch.update("def.com", 5);
        sketch.update("efg.com", 8);
        sketch.update("abc.com", 5);
        sketch.update("121.com", 5);
        sketch.update("bcd1.com", 5);
        sketch.update("def2.com", 5);
        sketch.update("ef3.com", 8);
        sketch.update("abc4.com", 5);

        for (ItemsSketch.Row<String> item : sketch.getFrequentItems(ErrorType.NO_FALSE_POSITIVES)) {
            System.out.println(item.getItem() + "-" + item.getEstimate());

        }
    }


    @Test
    public void topK() {
        ItemsSketch<String> sk = new ItemsSketch<>(16);
        sk.update("1.1.1.1", 1020);
        sk.update("1.1.1.2", 21020);
        sk.update("1.1.1.1", 100);
        sk.update("1.1.1.3", 100);
        sk.update("1.1.1.3", 100);
        sk.update("1.1.1.4", 100);
        sk.update("1.1.1.5", 100);
        sk.update("1.1.1.6", 100);
        sk.update("1.1.1.10", 100);
        sk.update("1.1.1.5", 100000);
        sk.update("1.1.1.9", 100);
        sk.update("1.1.1.234", 100);
        sk.update("1.1.2.234", 100);
        sk.update("2.1.2.234", 100);
        sk.update("2.2.2.234", 100);
        sk.update("10.1.2.234", 100);
        sk.update("102.1.2.234", 100);


        ItemsSketch.Row<String>[] items = sk.getFrequentItems(10, ErrorType.NO_FALSE_POSITIVES);
        System.out.println(sk.getNumActiveItems());
        for (ItemsSketch.Row<String> item : items) {

            System.out.println(item.getItem()
                    + " - " + item.getEstimate()
                    + " - " + item.getLowerBound()
                    + " - " + item.getUpperBound());


        }
    }


}
