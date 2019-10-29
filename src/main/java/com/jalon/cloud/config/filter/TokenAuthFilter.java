package com.jalon.cloud.config.filter;

import com.jalon.cloud.config.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenAuthFilter extends OncePerRequestFilter {

    final static String TOKENHEAD = "token ";
    final static String TOKENHEADER = "Authorization";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SecurityUser securityUser;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(TOKENHEAD);
        if (authHeader != null && authHeader.startsWith(TOKENHEAD)) {
            final String authToken = authHeader.substring(TOKENHEAD.length());
            if (authToken != null && redisTemplate.hasKey(authToken)) {
                String username = redisTemplate.opsForValue().get(authToken);
            }
        }
    }
}
