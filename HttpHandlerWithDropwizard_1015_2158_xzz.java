// 代码生成时间: 2025-10-15 21:58:31
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.View;
import io.dropwizard.views.ViewBundle;
import io.dropwizard.views.freemarker.FreemarkerViewRenderer;
import io.dropwizard.views.mustache.MustacheViewRenderer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.atomic.AtomicLong;

// 定义Http请求处理器
@Path("/hello")
public class HelloResource {
    private final AtomicLong counter = new AtomicLong();

    // 构造函数
    public HelloResource() {
    }

    // HTTP GET请求处理器
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response sayHello() {
        return Response.ok("Hello World").build();
    }
}

// 定义Dropwizard应用程序
public class HttpHandlerWithDropwizard extends Application<HttpHandlerWithDropwizardConfiguration> {
    public static void main(String[] args) throws Exception {
        new HttpHandlerWithDropwizard().run(args);
    }

    @Override
    public void initialize(Bootstrap<HttpHandlerWithDropwizardConfiguration> bootstrap) {
        // 无需额外配置
    }

    @Override
    public void run(HttpHandlerWithDropwizardConfiguration configuration, Environment environment) {
        // 配置视图渲染器
        final ViewBundle<HttpHandlerWithDropwizardConfiguration> viewBundle = new ViewBundle<HttpHandlerWithDropwizardConfiguration>() {
            @Override
            public void run(HttpHandlerWithDropwizardConfiguration configuration, Environment environment) {
                environment.jersey().register(new HelloResource());
            }
        };
        environment.jersey().register(viewBundle);
    }
}
