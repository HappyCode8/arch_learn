package com.wyj.util;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class EncryptionUtilTest {
    public static void main(String[] args) {
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        System.out.println(encryptionUtil.encode("MFfTW3uNI4eqhwDkG7HP9p2mzEUu/r2"));
        System.out.println(new BCryptPasswordEncoder().matches("MFfTW3uNI4eqhwDkG7HP9p2mzEUu/r2","$2a$10$LTqKTXXRb26SYG3MvFG1UuKhMgc/i6IbUl2RgApiWd39y1EqlXbD6"));
    }
}