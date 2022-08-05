package com.swcns.dopoonserver.global.utils.csrf;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
public class CsrfService {
    private final long EXP_WITH_MINUTES = 6;
    private final char[] SOURCES = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    private final Map<String, Long> csrfTokenTable = new ConcurrentHashMap<>();

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++) {
            sb.append(SOURCES[(int)(Math.random() * SOURCES.length)]);
        }
        return sb.toString();
    }

    public String generateToken() {
        return generateToken(32);
    }

    public String generateToken(int length) {
        String token;
        while(csrfTokenTable.containsKey((token = generateRandomString(length)))) { }
        csrfTokenTable.put(token, System.currentTimeMillis() + 1000 * 60 * EXP_WITH_MINUTES);

        log.info("token: {}, expires at: {}", token, csrfTokenTable.get(token));

        return token;
    }

    public boolean isValidToken(String token) {
        if(csrfTokenTable.containsKey(token)) {
            long expiresAt = csrfTokenTable.get(token);
            csrfTokenTable.remove(token);

            log.info("found token: {}, expires at: {}", token, expiresAt);

            return System.currentTimeMillis() < expiresAt;
        }else {
            log.info("token '{}' not found", token);
            return false;
        }
    }

}
