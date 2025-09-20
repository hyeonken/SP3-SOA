package com.example.operum.advisor.util.crypto;

import jakarta.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptionService {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    private final SecureRandom secureRandom = new SecureRandom();
    private final String secret;
    private javax.crypto.spec.SecretKeySpec keySpec;

    public EncryptionService(@Value("${app.encryption.secret}") String secret) {
        this.secret = secret;
    }

    @PostConstruct
    void init() {
        if (secret == null || secret.length() < 16) {
            throw new IllegalStateException("Chave de criptografia deve ter ao menos 16 caracteres");
        }
        byte[] keyBytes = secret.substring(0, 16).getBytes(StandardCharsets.UTF_8);
        keySpec = new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String value) {
        if (value == null) {
            return null;
        }
        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(TRANSFORMATION);
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, keySpec, new javax.crypto.spec.GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);
            buffer.put(iv);
            buffer.put(encrypted);
            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Falha ao criptografar dado", ex);
        }
    }

    public String decrypt(String value) {
        if (value == null) {
            return null;
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(value);
            ByteBuffer buffer = ByteBuffer.wrap(decoded);
            byte[] iv = new byte[IV_LENGTH];
            buffer.get(iv);
            byte[] payload = new byte[buffer.remaining()];
            buffer.get(payload);
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(TRANSFORMATION);
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keySpec, new javax.crypto.spec.GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] decrypted = cipher.doFinal(payload);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Falha ao descriptografar dado", ex);
        }
    }
}
