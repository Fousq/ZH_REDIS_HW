package kz.zhanbolat.redis;

import org.apache.commons.lang3.time.StopWatch;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class App {
    public static void main( String[] args ) {
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7000));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7001));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7002));
        JedisCluster jedis = new JedisCluster(jedisClusterNodes);

        try {
            Path path = Paths.get(App.class.getClassLoader().getResource("example.json").toURI());
            BufferedReader reader = Files.newBufferedReader(path);
            List<String> strings = reader.lines().collect(Collectors.toList());
            reader.close();
            String data = String.join("\n", strings);

            StopWatch stopWatch = new StopWatch();

            stopWatch.start();
            jedis.set("strdata", data);
            stopWatch.stop();
            System.out.println("String written in: " + stopWatch.getTime());

            stopWatch.reset();
            stopWatch.start();
            jedis.get("strdata");
            stopWatch.stop();
            System.out.println("String read in: " + stopWatch.getTime());

            stopWatch.reset();
            stopWatch.start();
            jedis.hset("hsetdata", "datakey", data);
            stopWatch.stop();
            System.out.println("Hash written in: " + stopWatch.getTime());

            stopWatch.reset();
            stopWatch.start();
            jedis.hget("hsetdata", "datakey");
            stopWatch.stop();
            System.out.println("Hash read in: " + stopWatch.getTime());

            stopWatch.reset();
            stopWatch.start();
            jedis.zadd("zsetdata", 1, data);
            stopWatch.stop();
            System.out.println("Sorted set written in: " + stopWatch.getTime());

            stopWatch.reset();
            stopWatch.start();
            jedis.zrange("zsetdata", 0, 2);
            stopWatch.stop();
            System.out.println("Sorted set read in: " + stopWatch.getTime());

            stopWatch.reset();
            stopWatch.start();
            jedis.lpush("listdata", data);
            stopWatch.stop();
            System.out.println("List written in: " + stopWatch.getTime());

            stopWatch.reset();
            stopWatch.start();
            jedis.lpop("listdata");
            stopWatch.stop();
            System.out.println("List read in: " + stopWatch.getTime());
        } catch (URISyntaxException e) {
            System.out.println("Cannot find json file");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Cannot read from file");
            throw new RuntimeException(e);
        } finally {
            jedis.close();
        }
    }
}
