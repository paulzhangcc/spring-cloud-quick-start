package com.paulzhangcc.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.paulzhangcc.common.util.InetUtils.findFirstNonLoopbackAddress;


@Component
@ConditionalOnProperty(prefix = "lock.redis", name = "enabled", matchIfMissing = true)
public class RedisLock {

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    private final static Logger logger = LoggerFactory.getLogger(RedisLock.class);
    /**
     * 单个锁有效期
     */
    private static final int DEFAULT_SINGLE_EXPIRE_SECONDS = 1000;
    /**
     * 批量锁有效期
     */
    private static final int DEFAULT_BATCH_EXPIRE_SECONDS = 1000;
    /**
     * 尝试间隔时间
     */
    private static final int RETRY_INTERVAL_SECONDS = 100;

    private static InetAddress inetAddress = findFirstNonLoopbackAddress();

    public String generateUniqueValue() {
        String host = "";
        if (inetAddress != null) {
            host = host + inetAddress.getHostAddress() + "-";
        }
        return host + UUID.randomUUID().toString().replaceAll("-", "");
    }

    protected int retryInterval() {
        return RETRY_INTERVAL_SECONDS;
    }

    protected int singleLockExpireSeconds() {
        return DEFAULT_SINGLE_EXPIRE_SECONDS;
    }

    protected int batchLockExpireSeconds() {
        return DEFAULT_BATCH_EXPIRE_SECONDS;
    }

    protected boolean seeLockCompetition() {
        return false;
    }

    public boolean tryLock(String key, String generateUniqueValue) {
        return tryLock(key, generateUniqueValue, 0L, null);
    }

    public boolean tryLock(String key, String generateUniqueValue, long timeout, TimeUnit unit) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(generateUniqueValue)) {
            throw new IllegalArgumentException();
        }
        try {
            logger.info("lock key start:" + key + ",value:" + generateUniqueValue);
            //系统计时器的当前值，以毫微秒为单位。
            long nano = System.nanoTime();
            do {
                Boolean setIfAbsent = stringRedisTemplate.opsForValue().setIfAbsent(key, generateUniqueValue, singleLockExpireSeconds(), TimeUnit.SECONDS);
                if (setIfAbsent != null && setIfAbsent.booleanValue()) {
                    logger.info("lock key end:" + key + ",isSuccess:true");
                    return Boolean.TRUE;
                } else if (logger.isDebugEnabled() || seeLockCompetition()) {
                    String desc = stringRedisTemplate.opsForValue().get(key);
                    logger.info("lock key: " + key + " locked by another business:" + desc);
                }
                if (timeout <= 0) {
                    break;
                }
                Thread.sleep(retryInterval());
            } while ((System.nanoTime() - nano) < unit.toNanos(timeout));
        } catch (Exception e) {
            logger.error("lock key error:" + key, e);
        }
        logger.info("lock key end:" + key + ",isSuccess:false");
        return Boolean.FALSE;
    }

    public boolean tryLock(List<String> keys, String generateUniqueValue) {
        return tryLock(keys, generateUniqueValue, 0L, null);
    }

    public boolean tryLock(List<String> keys, String generateUniqueValue, long timeout, TimeUnit unit) {
        if (keys == null || keys.size() == 0 || StringUtils.isEmpty(generateUniqueValue)) {
            throw new IllegalArgumentException();
        }
        logger.info("lock keys start:" + keys + ",value:" + generateUniqueValue);
        try {
            Map<String, String> mkeys = new HashMap<>(8);
            long nano = System.nanoTime();
            for (String key : keys) {
                mkeys.put(key, generateUniqueValue);
            }
            do {
                Boolean multiSetIfAbsent = stringRedisTemplate.opsForValue().multiSetIfAbsent(mkeys);
                if (multiSetIfAbsent != null && multiSetIfAbsent.booleanValue()) {
                    for (String key : keys) {
                        stringRedisTemplate.expire(key, batchLockExpireSeconds(), TimeUnit.SECONDS);
                    }
                    logger.info("lock keys end:" + keys + ",isSuccess:true");
                    return Boolean.TRUE;
                } else if (logger.isDebugEnabled() || seeLockCompetition()) {
                    String desc = stringRedisTemplate.opsForValue().get(keys.get(0));
                    logger.info("lock keys: " + keys + " locked by another business:" + desc);
                }

                if (timeout <= 0) {
                    break;
                }
                Thread.sleep(retryInterval());
            } while ((System.nanoTime() - nano) < unit.toNanos(timeout));

        } catch (Exception e) {
            logger.error("lock keys error:" + keys, e);
        }
        logger.info("lock keys end:" + keys + ",isSuccess:false");
        return Boolean.FALSE;
    }

    public RedisScript<String> unlockScript = RedisScript.of(
            "local v = ARGV[1]\n" +
                    "for i,key in pairs(KEYS) do\n" +
                    "\tlocal v1 = redis.call(\"get\",key)\n" +
                    "\tif (v == v1) \n" +
                    "\tthen\n" +
                    "\t\tredis.call(\"del\",key)\n" +
                    //"\t\tredis.log(redis.LOG_NOTICE,\"delete key=\"..key..\",value=\"..v1)\n" +
                    "\tend\n" +
                    "end  ");

    public void unLock(String key, String generateUniqueValue) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(generateUniqueValue)) {
            throw new IllegalArgumentException();
        }
        List<String> keyList = new ArrayList();
        keyList.add(key);
        unLock(keyList, generateUniqueValue);
    }

    public void unLock(List<String> keys, String generateUniqueValue) {
        if (keys == null || keys.size() == 0 || StringUtils.isEmpty(generateUniqueValue)) {
            throw new IllegalArgumentException();
        }
        stringRedisTemplate.execute(unlockScript, keys, generateUniqueValue);
    }
}
