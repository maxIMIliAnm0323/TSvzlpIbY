// 代码生成时间: 2025-10-20 19:47:28
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.View;
import io.dropwizard.views.ViewBundle;
import org.eclipse.jetty.util.log.StacklessLogging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import net.logstash.logback.appender.LogstashTcpSocketAppender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ErrorLogCollector extends Application<ErrorLogCollectorConfig> {
    private static final Logger logger = LoggerFactory.getLogger(ErrorLogCollector.class);

    // Entry point for the application
    public static void main(String[] args) throws Exception {
        new ErrorLogCollector().run(args);
    }

    @Override
    public void initialize(Bootstrap<ErrorLogCollectorConfig> bootstrap) {
        // Initializes the configuration
        bootstrap.addBundle(new ViewBundle<ErrorLogCollectorConfig>()
          .setConfigurations(Collections.singletonList(new ErrorLogViewConfiguration()))
          .setRenderer(new ErrorLogViewRenderer())
        );
    }

    @Override
    public void run(ErrorLogCollectorConfig configuration, Environment environment) throws Exception {
        // Custom configuration for the error log appender
        configureErrorLogAppender(configuration);
    }

    // Configures the error log appender to send logs to Logstash
    private void configureErrorLogAppender(ErrorLogCollectorConfig configuration) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        LogstashTcpSocketAppender<ILoggingEvent> logstashAppender = new LogstashTcpSocketAppender<>();
        logstashAppender.setContext(context);
        logstashAppender.setName("LogstashAppender");
        logstashAppender.setHost(configuration.getLogstashHost());
        logstashAppender.setPort(configuration.getLogstashPort());

        // Add filters to the appender to only include error logs
        logstashAppender.addFilter(new Filter<ILoggingEvent>() {
            @Override
            public FilterReply decide(ILoggingEvent event) {
                // Filter to include only ERROR and above level logs
                return event.getLevel().isGreaterOrEqual(org.slf4j.event.Level.ERROR) ? FilterReply.ACCEPT : FilterReply.DENY;
            }
        });

        // Add the appender to the root logger
        context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).addAppender(logstashAppender);

        // Start the appender
        logstashAppender.start();
    }
}

// Configuration class for the application
class ErrorLogCollectorConfig extends Configuration {
    // Configuration for Logstash host and port
    private String logstashHost;
    private int logstashPort;

    public String getLogstashHost() {
        return logstashHost;
    }

    public void setLogstashHost(String logstashHost) {
        this.logstashHost = logstashHost;
    }

    public int getLogstashPort() {
        return logstashPort;
    }

    public void setLogstashPort(int logstashPort) {
        this.logstashPort = logstashPort;
    }
}

// View configuration for error logging
class ErrorLogViewConfiguration extends DefaultRenderingBuilderImpl<ErrorLogCollectorConfig> {
}

// View renderer for error logging
class ErrorLogViewRenderer extends DefaultRenderer<ErrorLogViewConfiguration> {
}

// Custom view for error logging
class ErrorLogView extends View {
    public ErrorLogView() {
        super("text/plain");
    }
}
