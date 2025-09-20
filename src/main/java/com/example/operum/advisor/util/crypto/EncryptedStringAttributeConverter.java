package com.example.operum.advisor.util.crypto;

import com.example.operum.advisor.util.ApplicationContextHolder;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EncryptedStringAttributeConverter implements AttributeConverter<String, String> {

    private EncryptionService encryptionService;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) {
            return attribute;
        }
        return getEncryptionService().encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return dbData;
        }
        return getEncryptionService().decrypt(dbData);
    }

    private EncryptionService getEncryptionService() {
        if (encryptionService == null) {
            encryptionService = ApplicationContextHolder.getBean(EncryptionService.class);
        }
        return encryptionService;
    }
}
