package sharafi.parsa.validator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig {

	@Bean(name = "scheduledTaskExecutor", destroyMethod="shutdown")
    ThreadPoolTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("Scheduled-");
        executor.setDaemon(true);
        executor.initialize();
        return executor;
    }
    
    @Bean(name = "encryptionTaskExecutor", destroyMethod="shutdown")
    TaskExecutor encryptionTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("enc-thread-");
        executor.setDaemon(true);
        executor.initialize();
        return executor;
    }
    
    @Bean(name = "customersTaskExecutor", destroyMethod="shutdown")
    TaskExecutor customersTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("cus-thread-");
        executor.setDaemon(true);
        executor.initialize();
        return executor;
    }
    
    @Bean(name = "accountsTaskExecutor", destroyMethod="shutdown")
    TaskExecutor accountsTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("acc-thread-");
        executor.setDaemon(true);
        executor.initialize();
        return executor;
    }
}