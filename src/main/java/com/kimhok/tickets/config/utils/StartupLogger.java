package com.kimhok.tickets.config.utils;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class StartupLogger implements ApplicationListener<ApplicationReadyEvent> {

    private final Environment environment;

    public StartupLogger(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String port = environment.getProperty("server.port");
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        String healthEndpoint = "http://localhost:" + port + contextPath + "/actuator/health";
        String activeProfile = Arrays.toString(environment.getActiveProfiles());

        System.out.println("\n\n" +
                "=======================================================\n" +
                "✅ Application is running!\n" +
                "🔌 Port: " + port + "\n" +
                "🌐 Local URL: http://localhost:" + port + contextPath + "\n" +
                "🩺 Health Check: " + healthEndpoint + "\n" +
                " Active On Profile " + activeProfile + "\n"  +
                "=======================================================\n\n");
    }
}
