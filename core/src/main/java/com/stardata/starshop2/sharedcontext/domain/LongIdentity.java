package com.stardata.starshop2.sharedcontext.domain;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.Random;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/20 11:26
 */
@Embeddable
public class LongIdentity implements Identity<Long>, Comparable<Object>{
    private long id;

    protected LongIdentity(long id) {
        this.id = id;
    }

    protected LongIdentity() {
        this.id = 0;
    }

    private static Snowflake getSnowflake() {
        long datacenterId = new Random().nextInt( 31);
        long workerId;
        try {
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
        } catch (Exception e) {
            workerId = NetUtil.getLocalhostStr().hashCode();
        }
        workerId = workerId % 32;
        return IdUtil.getSnowflake(workerId, datacenterId);
    }

    public static LongIdentity from(Long id) {
        return new LongIdentity(id);
    }

    public static LongIdentity snowflakeId() {
        return new LongIdentity(getSnowflake().nextId());
    }

    public static LongIdentity dummyId() {
        return new LongIdentity(-1L);
    }

    @Override
    public Long value() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof LongIdentity) {
            LongIdentity that = (LongIdentity) o;
            return that.id == this.id;
        }
        if (o instanceof Long ) {
            Long that = (Long)o;
            return that == this.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (o instanceof LongIdentity ) {
            LongIdentity other = (LongIdentity) o;
            return (int)(this.id - other.id);
        }
        if (o instanceof Long ) {
            Long other = (Long) o;
            return (int)(this.id - other);
        }
        return -1;
    }
}
