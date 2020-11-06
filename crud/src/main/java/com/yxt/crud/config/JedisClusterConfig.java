package com.yxt.crud.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/3/18
 */
//@Configuration
public class JedisClusterConfig {

    @Value("${redis.cluster.nodes}")
    private String clusterNodes;
    @Value("${redis.timeout}")
    private int timeout;
    @Value("${redis.pool.max.idle}")
    private int maxIdle;
    @Value("${redis.pool.max.wait}")
    private long maxWaitMillis;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.node.Address}")
    private String redisNodeAddress;

    @Bean
    public JedisPoolConfig getJedisPoolConfig() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

        return jedisPoolConfig;
    }

    /**
     * Redis集群的配置
     * @return RedisClusterConfiguration
     * @throws
     */
    @Bean
    public RedisClusterConfiguration redisClusterConfiguration(){

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        //Set<RedisNode> clusterNodes
        String[] serverArray = clusterNodes.split(",");
        Set<RedisNode> nodes = new HashSet<RedisNode>();
        for(String ipPort:serverArray){
            String[] ipAndPort = ipPort.split(":");
            nodes.add(new RedisNode(ipAndPort[0].trim(),Integer.valueOf(ipAndPort[1])));
        }
        redisClusterConfiguration.setClusterNodes(nodes);
//        redisClusterConfiguration.setPassword(RedisPassword.of(password));

        return redisClusterConfiguration;
    }

    /**
     * 设置数据存入redis 的序列化方式
     * @paramredisConnectionFactory
     */
    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {

        RedisTemplate<Object,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }

}
