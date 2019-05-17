package cn.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;

import cn.edu.utils.JedisClient;
import cn.edu.utils.impl.JedisClientSingle;

@EnableHystrix
//@EnableApolloConfig
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
//@MapperScan(basePackages = "cn.edu.mapper.pim")
public class EduPimApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduPimApplication.class, args);
	}

	@Bean
	public JedisClient jedisClient() {
		return new JedisClientSingle();
	}
	
}
