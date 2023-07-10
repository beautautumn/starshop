package com.stardata.starshop2.sharedcontext.helper;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;

import java.io.IOException;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/10 22:38
 */
public class LongIdentityDeserializer extends StdDeserializer<LongIdentity> {
    public final static LongIdentityDeserializer instance = new LongIdentityDeserializer();

    public LongIdentityDeserializer() {
        this(null);
    }

    protected LongIdentityDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LongIdentity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return LongIdentity.from(jsonParser.getLongValue());
    }

}
