package com.bms.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SeatLockService {

    private final org.springframework.data.redis.core.StringRedisTemplate redisTemplate;

    public SeatLockService(java.util.Optional<org.springframework.data.redis.core.StringRedisTemplate> redisTemplate) {
        this.redisTemplate = redisTemplate.orElse(null);
    }

    private static final long LOCK_TTL = 300; // 5 minutes

    private String getKey(Long showId, Long seatId) {
        return "seat_lock:" + showId + ":" + seatId;
    }

    public boolean lockSeat(Long showId, Long seatId, String userId) {
        if (redisTemplate == null) return true;
        try {
            String key = getKey(showId, seatId);
            Boolean success = redisTemplate.opsForValue().setIfAbsent(
                    key,
                    userId,
                    LOCK_TTL,
                    TimeUnit.SECONDS
            );
            return Boolean.TRUE.equals(success);
        } catch (Exception e) {
            // Redis is down or misconfigured, fallback to allowing the selection
            return true;
        }
    }

    public void releaseSeat(Long showId, Long seatId) {
        if (redisTemplate == null) return;
        try {
            String key = getKey(showId, seatId);
            redisTemplate.delete(key);
        } catch (Exception e) {
            // Ignore
        }
    }

    public boolean isSeatLocked(Long showId, Long seatId) {
        if (redisTemplate == null) return false;
        try {
            String key = getKey(showId, seatId);
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            return false;
        }
    }

    public String getLockOwner(Long showId, Long seatId) {
        if (redisTemplate == null) return null;
        try {
            String key = getKey(showId, seatId);
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean lockSeats(Long showId, List<Long> seatIds, String userId) {

        for (Long seatId : seatIds) {

            boolean locked = lockSeat(showId, seatId, userId);

            if (!locked) {
                releaseSeats(showId, seatIds);
                return false;
            }
        }

        return true;
    }

    public void releaseSeats(Long showId, List<Long> seatIds) {

        for (Long seatId : seatIds) {
            releaseSeat(showId, seatId);
        }
    }
}
