package com.swcns.dopoonserver.global.utils;

import org.springframework.stereotype.Component;

@Component
public class RandomUtil {
    private final char[] SOURCES = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    public String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++) {
            sb.append(SOURCES[(int)(Math.random() * SOURCES.length)]);
        }
        return sb.toString();
    }
}
