package com.stardata.starshop2.sharedcontext.helper;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.stardata.starshop2.sharedcontext.domain.MobileNumber;

import java.io.IOException;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/10 22:38
 */
public class MobileNumberDeserializer extends StdDeserializer<MobileNumber> {
    public final static MobileNumberDeserializer instance = new MobileNumberDeserializer();

    public MobileNumberDeserializer() {
        this(null);
    }

    protected MobileNumberDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public MobileNumber deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return MobileNumber.from(jsonParser.getValueAsString());
    }

}
