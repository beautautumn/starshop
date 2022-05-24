package com.stardata.starshop2.sharedcontext.domain;

import javax.annotation.concurrent.Immutable;
import java.io.Serial;
import java.util.Random;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/24 18:26
 */
@Immutable
public class RuleRandomStringIdentity implements RandomIdentity<String>{
    private String value;
    private final String prefix;
    private final int seed;
    private final String joiner;

    private static final int DEFAULT_SEED = 100_000;
    private static final String DEFAULT_JOINER = "_";
    @Serial
    private static final long serialVersionUID = 1L;

    public RuleRandomStringIdentity() {
        this("", DEFAULT_SEED, DEFAULT_JOINER);
    }

    public RuleRandomStringIdentity(int seed) {
        this("", seed, DEFAULT_JOINER);
    }

    public RuleRandomStringIdentity(String prefix, int seed) {
        this(prefix, seed, DEFAULT_JOINER);
    }

    public RuleRandomStringIdentity(String prefix, int seed, String joiner) {
        this.prefix = prefix;
        this.seed = seed;
        this.joiner = joiner;

        this.value = compose(prefix, seed, joiner);
    }

    @Override
    public final String value() {
        return this.value;
    }

    @Override
    public String next() {
        this.value = compose(prefix, seed, joiner);
        return this.value;
    }

    private String compose(String prefix, int seed, String joiner) {
        long suffix = new Random(seed).nextLong();
        return String.format("%s%s%s", prefix, joiner, suffix);
    }
}
