package com.looppan.looppan.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

@Configuration
public class LoggingConfig {


    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger("MyAppLogger");
    }

    @Bean
    public RollingFileAppender<ch.qos.logback.classic.spi.ILoggingEvent> fileAppender() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        RollingFileAppender<ch.qos.logback.classic.spi.ILoggingEvent> fileAppender = new RollingFileAppender<>();
        fileAppender.setContext(context);
        fileAppender.setName("fileLogger");
        fileAppender.setFile("logs/app.log");

        TimeBasedRollingPolicy<ch.qos.logback.classic.spi.ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
        policy.setContext(context);
        policy.setFileNamePattern("logs/app.%d{yyyy-MM-dd}.log");
        policy.setParent(fileAppender);
        policy.start();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n");
        encoder.start();

        fileAppender.setEncoder(encoder);
        fileAppender.setRollingPolicy(policy);
        fileAppender.start();

        return fileAppender;
    }

    @Bean
    public ConsoleAppender<ch.qos.logback.classic.spi.ILoggingEvent> consoleAppender() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        ConsoleAppender<ch.qos.logback.classic.spi.ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(context);
        consoleAppender.setName("consoleLogger");

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n");
        encoder.start();

        consoleAppender.setEncoder(encoder);
        consoleAppender.start();

        return consoleAppender;
    }

    @Bean
    public Logger rootLogger() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(fileAppender());
        rootLogger.addAppender(consoleAppender());
        return rootLogger;
    }
}
