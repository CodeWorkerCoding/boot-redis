package com.nchu.tech.jedis.service.common;

import com.nchu.tech.jedis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fujianjian on 2017/5/8.
 */
@Component
public class CommonService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisService redisService;

    @PostConstruct
    private void syncHBXJRegionToRedis() {
        String querySql = "select  code, region_desc from hbxj_region limit 100;";
        List<Map<String, Object>> ret = this.jdbcTemplate.queryForList(querySql);

        Map<byte[], byte[]> hbxjRegion = new HashMap<>();
        ret.stream().forEach(item -> hbxjRegion.put(String.valueOf(item.get("code")).getBytes(),
                String.valueOf(item.get("region_desc")).getBytes()));

        this.redisService.hmset(null, hbxjRegion);

        //System.out.println(String.format("======================>jdbcTemplate query result size: %d ", ret.size()));
        //System.out.println(String.format("======================>the first record is %s", ret.get(0).toString()));
    }

}