package com.jtccia.embargos.security.util;

import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AesPasswordEncoder implements PasswordEncoder {

	private static final String  PASSWORD   = "encript";
    private static final byte[]  SALT       = {
        (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
        (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };
    private static final int     ITERATIONS = 19;

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            KeySpec keySpec = new PBEKeySpec(PASSWORD.toCharArray(), SALT, ITERATIONS);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            PBEParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATIONS);

            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
            cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

            byte[] utf8     = rawPassword.toString().getBytes(StandardCharsets.UTF_8);
            byte[] encBytes = cipher.doFinal(utf8);

            return Base64.getEncoder().encodeToString(encBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encriptando PBEWithMD5AndDES", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String storedHash) {
        String candidateHash = encode(rawPassword);
        return candidateHash.equals(storedHash);
    }
}
