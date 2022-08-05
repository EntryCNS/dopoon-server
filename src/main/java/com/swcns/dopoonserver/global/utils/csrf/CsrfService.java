package com.swcns.dopoonserver.global.utils.csrf;

import com.swcns.dopoonserver.global.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@RequiredArgsConstructor
@Component
public class CsrfService {
    private final long EXP_WITH_MINUTES = 6;
    private final Map<String, Long> csrfTokenTable = new ConcurrentHashMap<>();
    private final RandomUtil randomUtil;

    public String generateToken() {
        return generateToken(32);
    }

    public String generateToken(int length) {
        String token;
        while(csrfTokenTable.containsKey((token = randomUtil.generateRandomString(length)))) { }
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
