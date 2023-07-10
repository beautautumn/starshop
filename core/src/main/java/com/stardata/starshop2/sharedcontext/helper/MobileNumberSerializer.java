package com.stardata.starshop2.sharedcontext.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.stardata.starshop2.sharedcontext.domain.MobileNumber;

import java.io.IOException;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/10 22:38
 */
public class MobileNumberSerializer extends StdSerializer<MobileNumber> {
    public final static MobileNumberSerializer instance = new MobileNumberSerializer();

    public MobileNumberSerializer() {
        this(null);
    }

    protected MobileNumberSerializer(Class<MobileNumber> t) {
        super(t);
    }

    @Override
    public void serialize(MobileNumber mobileNumber, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (mobileNumber == null) {
            gen.writeNull();
            return;
        }
        gen.writeString(mobileNumber.value());
    }

    @Override
    public void serializeWithType(MobileNumber value, JsonGenerator g, SerializerProvider provider,
                                  TypeSerializer typeSer) throws IOException
    {
        WritableTypeId typeIdDef = typeSer.writeTypePrefix(g,
                typeSer.typeId(value, JsonToken.VALUE_STRING));
        serialize(value, g, provider);
        typeSer.writeTypeSuffix(g, typeIdDef);
    }
}