package com.stardata.starshop2.sharedcontext.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;

import java.io.IOException;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/10 22:38
 */
public class LongIdentitySerializer extends StdSerializer<LongIdentity> {
    public final static LongIdentitySerializer instance = new LongIdentitySerializer();

    public LongIdentitySerializer() {
        this(null);
    }

    protected LongIdentitySerializer(Class<LongIdentity> t) {
        super(t);
    }

    @Override
    public void serialize(LongIdentity id, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (id == null) {
            gen.writeNull();
            return;
        }
        gen.writeNumber(id.value());
    }

    @Override
    public void serializeWithType(LongIdentity value, JsonGenerator g, SerializerProvider provider,
                                  TypeSerializer typeSer) throws IOException
    {
        WritableTypeId typeIdDef = typeSer.writeTypePrefix(g,
                typeSer.typeId(value, JsonToken.VALUE_NUMBER_INT));
        serialize(value, g, provider);
        typeSer.writeTypeSuffix(g, typeIdDef);
    }
}