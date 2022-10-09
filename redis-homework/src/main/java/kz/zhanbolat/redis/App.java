package kz.zhanbolat.redis;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App {
    public static void main( String[] args ) throws URISyntaxException, IOException {
        JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);

        Path path = Paths.get(App.class.getClassLoader().getResource("example.json").toURI());
        byte[] data = Files.readAllBytes(path);

        try (Jedis resource = jedisPool.getResource()) {
            for (int i = 1; i <= 10_000; i++) {
                String key = "data:" + i;
                System.out.println("insert " + key);
                resource.set(key.getBytes(), data);
                String usedDataKey = "data:" + RandomUtils.nextInt(1, i);
                System.out.println("used " + usedDataKey);
                resource.get(usedDataKey);
            }
            int count = 0;
            for (int i = 1; i <= 10_000; i++) {
                String key = "data:" + i;
                if (StringUtils.isEmpty(resource.get(key))) {
                    System.out.println(key + " was deleted");
                } else {
                    count++;
                    System.out.println(key + " is keep");
                }
            }
            System.out.println("Out of 10,000, a " + ((count / 10_000d) * 100d) + "% is keep");
        } finally {
            jedisPool.close();
        }
    }
}
