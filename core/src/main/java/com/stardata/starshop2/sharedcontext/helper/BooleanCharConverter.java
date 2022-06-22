package com.stardata.starshop2.sharedcontext.helper;

import jakarta.persistence.AttributeConverter;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/19 00:22
 */
public class BooleanCharConverter implements AttributeConverter<Boolean, Character> {
    @Override
    public Character convertToDatabaseColumn(Boolean attribute) {
        if (attribute) {
            return '1';
        }
        return '0';
    }

    @Override
    public Boolean convertToEntityAttribute(Character dbData) {
        return dbData.equals('1');
    }
}
