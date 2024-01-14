package com.getcode.global;

import com.getcode.domain.community.CommunityCategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CommunityTypeConverter implements AttributeConverter<CommunityCategory, String> {
    @Override
    public String convertToDatabaseColumn(CommunityCategory attribute) {
        return attribute.name();
    }

    @Override
    public CommunityCategory convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return CommunityCategory.valueOf(dbData);
    }
}
