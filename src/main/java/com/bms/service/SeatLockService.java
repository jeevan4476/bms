package com.bms.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SeatLockService {

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    private final ConcurrentHashMap<String, String> localLocks = new ConcurrentHashMap<>();

    private static final long LOCK_TTL = 300; // 5 minutes

    private String getKey(Long showId, Long seatId) {
        return "seat_lock:" + showId + ":" + seatId;
    }

    public boolean lockSeat(Long showId, Long seatId, String userId) {
        String key = getKey(showId, seatId);
        if (redisTemplate != null) {
            try {
                Boolean success = redisTemplate.opsForValue().setIfAbsent(
                        key,
                        userId,
                        LOCK_TTL,
                        TimeUnit.SECONDS
                );
                if (Boolean.TRUE.equals(success)) return true;
            } catch (Exception e) {
                // Redis failing, fallback to local memory
            }
        }
        // Memory fallback
        return localLocks.putIfAbsent(key, userId) == null;
    }

    public void releaseSeat(Long showId, Long seatId) {
        String key = getKey(showId, seatId);
        localLocks.remove(key);
        if (redisTemplate != null) {
            try {
                redisTemplate.delete(key);
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    public boolean isSeatLocked(Long showId, Long seatId) {
        String key = getKey(showId, seatId);
        if (localLocks.containsKey(key)) return true;
        if (redisTemplate != null) {
            try {
                return Boolean.TRUE.equals(redisTemplate.hasKey(key));
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public String getLockOwner(Long showId, Long seatId) {
        String key = getKey(showId, seatId);
        String localOwner = localLocks.get(key);
        if (localOwner != null) return localOwner;

        if (redisTemplate != null) {
            try {
                return redisTemplate.opsForValue().get(key);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
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
