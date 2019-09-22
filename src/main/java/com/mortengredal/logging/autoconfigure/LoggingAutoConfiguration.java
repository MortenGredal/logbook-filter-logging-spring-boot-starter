package com.mortengredal.logging.autoconfigure;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mortengredal.logging.autoconfigure.filter.RequestLoggingFilter;
import com.mortengredal.logging.autoconfigure.filter.RequestTimingFilter;
import com.mortengredal.logging.autoconfigure.filter.StatusCodeFilter;
import com.mortengredal.logging.autoconfigure.logbook.LogbookCustomsFormatter;
import net.logstash.logback.composite.ContextJsonProvider;
import net.logstash.logback.composite.GlobalCustomFieldsJsonProvider;
import net.logstash.logback.composite.loggingevent.*;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.zalando.logbook.BodyFilter;
import org.zalando.logbook.HttpLogFormatter;

import javax.servlet.Filter;

import static org.zalando.logbook.BodyFilters.defaultValue;
import static org.zalando.logbook.BodyFilters.replaceJsonStringProperty;

@Configuration
@ConditionalOnClass(value = {HttpLogFormatter.class, Filter.class, MDC.class})
@ConditionalOnProperty(value = "${sample.app.enable-logging}", matchIfMissing = true)
@EnableConfigurationProperties(LoggngConfigurationProperties.class)
@PropertySources({
        @PropertySource(value = "${spring.application.name}", ignoreResourceNotFound = true),
        @PropertySource(value = "${spring.cloud.client.ip-address}", ignoreResourceNotFound = true)
})
public class LoggingAutoConfiguration {

    private static final String CONSOLE_APPENDER_NAME = "CONSOLE";

    private String appName;

    private String ipAddress;
    private final LoggngConfigurationProperties properties;

    public LoggingAutoConfiguration(@Value("${spring.application.name}") String appName,
                                    @Value("${spring.cloud.client.ip-address}")String ipAddress,
                                    LoggngConfigurationProperties loggngConfigurationProperties) {
        this.properties = loggngConfigurationProperties;
        this.appName = appName;
        this.ipAddress = ipAddress;
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        if (loggngConfigurationProperties.isLogAsJson()){
            addJsonConsoleAppender(context);
        }
    }

    @Bean
    @ConditionalOnMissingBean(RequestLoggingFilter.class)
    public RequestLoggingFilter requestLoggingFilter() {
        return new RequestLoggingFilter();
    }

    @Bean
    @ConditionalOnMissingBean(RequestTimingFilter.class)
    public RequestTimingFilter requestTimingFilter() {
        return new RequestTimingFilter();
    }

    @Bean
    @ConditionalOnMissingBean(StatusCodeFilter.class)
    public StatusCodeFilter responseLoggingFilter() {
        return new StatusCodeFilter();
    }

    @Bean
    @ConditionalOnMissingBean(BodyFilter.class)
    public BodyFilter bodyFilter() {
        return BodyFilter.merge(
                defaultValue(),
                replaceJsonStringProperty(properties.getBodyFilters(), "****"));
    }

    @Bean
    @ConditionalOnMissingBean(HttpLogFormatter.class)
    LogbookCustomsFormatter logbookCustomsFormatter(){
        return new LogbookCustomsFormatter(new ObjectMapper());
    }

    private void addJsonConsoleAppender(LoggerContext context) {
        // More documentation is available at: https://github.com/logstash/logstash-logback-encoder
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(context);
        consoleAppender.setEncoder(compositeJsonEncoder(context));
        consoleAppender.setName(CONSOLE_APPENDER_NAME);
        consoleAppender.start();

        context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).detachAppender(CONSOLE_APPENDER_NAME);
        context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).addAppender(consoleAppender);
    }

    private LoggingEventCompositeJsonEncoder compositeJsonEncoder(LoggerContext context) {
        final LoggingEventCompositeJsonEncoder compositeJsonEncoder = new LoggingEventCompositeJsonEncoder();
        compositeJsonEncoder.setContext(context);
        compositeJsonEncoder.setProviders(jsonProviders(context));
        compositeJsonEncoder.start();
        return compositeJsonEncoder;
    }

    private LoggingEventJsonProviders jsonProviders(LoggerContext context) {
        final LoggingEventJsonProviders jsonProviders = new LoggingEventJsonProviders();
        jsonProviders.addArguments(new ArgumentsJsonProvider());
        jsonProviders.addContext(new ContextJsonProvider<>());
        jsonProviders.addGlobalCustomFields(customFieldsJsonProvider());
        jsonProviders.addLogLevel(new LogLevelJsonProvider());
        jsonProviders.addLoggerName(loggerNameJsonProvider());
        jsonProviders.addMdc(new MdcJsonProvider());
        jsonProviders.addMessage(new MessageJsonProvider());
        jsonProviders.addPattern(new LoggingEventPatternJsonProvider());
        jsonProviders.addStackTrace(stackTraceJsonProvider());
        jsonProviders.addThreadName(new ThreadNameJsonProvider());
        jsonProviders.addTimestamp(timestampJsonProvider());
        jsonProviders.setContext(context);
        return jsonProviders;
    }

    private GlobalCustomFieldsJsonProvider<ILoggingEvent> customFieldsJsonProvider() {
        final GlobalCustomFieldsJsonProvider<ILoggingEvent> customFieldsJsonProvider = new GlobalCustomFieldsJsonProvider<>();
        customFieldsJsonProvider.setCustomFields(customFields());
        return customFieldsJsonProvider;
    }

    private String customFields() {
        return "{" +
                "\"app_name\":\"" + this.appName + "\"," +
                "\"ip_address\":\"" + this.ipAddress + "\"" +
                "}";
    }

    private LoggerNameJsonProvider loggerNameJsonProvider() {
        final LoggerNameJsonProvider loggerNameJsonProvider = new LoggerNameJsonProvider();
        loggerNameJsonProvider.setShortenedLoggerNameLength(20);
        return loggerNameJsonProvider;
    }

    private LoggingEventFormattedTimestampJsonProvider timestampJsonProvider() {
        final LoggingEventFormattedTimestampJsonProvider timestampJsonProvider = new LoggingEventFormattedTimestampJsonProvider();
        timestampJsonProvider.setTimeZone("UTC");
        timestampJsonProvider.setFieldName("timestamp");
        return timestampJsonProvider;
    }

    private StackTraceJsonProvider stackTraceJsonProvider() {
        StackTraceJsonProvider stackTraceJsonProvider = new StackTraceJsonProvider();
        stackTraceJsonProvider.setThrowableConverter(throwableConverter());
        return stackTraceJsonProvider;
    }

    private ShortenedThrowableConverter throwableConverter() {
        final ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
        throwableConverter.setRootCauseFirst(true);
        return throwableConverter;
    }
}
