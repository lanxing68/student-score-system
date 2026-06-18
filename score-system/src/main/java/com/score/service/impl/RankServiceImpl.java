package com.score.service.impl;

import com.score.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RankServiceImpl implements RankService {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String RANK_KEY_PREFIX = "rank:course:";

    @Override
    public Long getStudentRank(Long courseId, Long studentId) {
        // ZREVRANK：从高到低排，第 0 名是最高分，所以 +1 变成人类第 1 名
        Long rank = redisTemplate.opsForZSet().reverseRank(RANK_KEY_PREFIX + courseId, studentId);
        return rank == null ? null : rank + 1;
    }

    @Override
    public Map<String, Object> getCourseRankList(Long courseId) {
        Set<ZSetOperations.TypedTuple<Object>> tuples =
                redisTemplate.opsForZSet().reverseRangeWithScores(RANK_KEY_PREFIX + courseId, 0, -1);

        List<Map<String, Object>> rankList = new ArrayList<>();
        int rank = 1;
        for (ZSetOperations.TypedTuple<Object> tuple : tuples) {
            Map<String, Object> item = new HashMap<>();
            item.put("rank", rank++);
            item.put("studentId", tuple.getValue());
            item.put("total", tuple.getScore());
            rankList.add(item);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("list", rankList);
        return result;
    }
}
