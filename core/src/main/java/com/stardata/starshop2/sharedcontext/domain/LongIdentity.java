package com.stardata.starshop2.sharedcontext.domain;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.Random;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/20 11:26
 */
@Embeddable
public class LongIdentity implements Identity<Long>{
    @Column(name = "id")
    private long value;

    protected LongIdentity(long value) {
        this.value = value;
    }

    protected LongIdentity() {
        this.value = 0;
    }

    private static Snowflake getSnowflake() {
        long datacenterId = new Random().nextLong(0L, 31L);
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

    @Override
    public Long value() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongIdentity that)) return false;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

}
