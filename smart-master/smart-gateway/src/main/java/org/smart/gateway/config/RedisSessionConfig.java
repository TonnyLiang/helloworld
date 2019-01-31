package org.smart.gateway.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configurable
@EnableRedisHttpSession(redisFlushMode = RedisFlushMode.IMMEDIATE)
public class RedisSessionConfig {

}
