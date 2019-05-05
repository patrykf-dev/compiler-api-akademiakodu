package pl.akademiakodu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import pl.akademiakodu.service.CompilerApiService;

import java.util.concurrent.Executor;

@EnableAsync
@SpringBootApplication
public class CompilerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompilerApiApplication.class, args);
	}

	@Bean
	public Executor taskExecutor() throws InterruptedException {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);
		executor.setMaxPoolSize(1);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("CompilerApiService-");
		executor.initialize();
		return executor;
	}

}
