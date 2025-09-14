package pl.archala.infrastructure.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    public static final String SINGLE_THREAD_EXECUTOR_BEAN = "singleThreadTaskExecutor";

    @Bean(name = SINGLE_THREAD_EXECUTOR_BEAN)
    public Executor singleThreadTaskExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("single-thread-");
        executor.initialize();
        return executor;
    }
}
