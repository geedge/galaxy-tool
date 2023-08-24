package com.geedgenetworks.test;

import com.google.common.collect.Maps;
import com.geedgenetworks.utils.CommonUtil;
import com.geedgenetworks.utils.JsonMapper;
import org.junit.Test;

import java.util.Map;

public class JsonMapperTest {
    @Test
    public void testDoubleDecimal() {
        Map testResult = Maps.newHashMap();
        testResult.put("DoubleValue", CommonUtil.roundPrecision(26376811204L *1.0/52015135172670L, 4) );
        System.out.println(JsonMapper.toJsonString(testResult));
    }
}
