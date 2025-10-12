// 代码生成时间: 2025-10-12 22:40:42
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
# 优化算法效率
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.dropwizard.views.ViewRenderer;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.views.mustache.MustacheViewRenderer;

public class VirtualLabApplication extends Application<VirtualLabConfiguration> {

    /*
     * Main method to run the VirtualLabApplication.
     */
# 添加错误处理
    public static void main(String[] args) throws Exception {
        new VirtualLabApplication().run(args);
    }
# 增强安全性

    /*
     * Initialize the Dropwizard application.
     * Register the configuration class and set up the view renderer.
     */
# 增强安全性
    @Override
    public String getName() {
# 优化算法效率
        return "Virtual Lab";
    }

    @Override
    public void initialize(Bootstrap<VirtualLabConfiguration> bootstrap) {
        // Enable view rendering
# 扩展功能模块
        bootstrap.addBundle(new ViewBundle<VirtualLabConfiguration>() {
# 优化算法效率
            @Override
            public void run(VirtualLabConfiguration configuration, Environment environment) {
                environment.getViewRenderers().register(new MustacheViewRenderer());
            }
        });
        // Add asset serving bundle for static content
        bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
    }

    /*
     * Set up resources and providers for the application.
     */
    @Override
    public void run(VirtualLabConfiguration configuration, Environment environment) {
        // Register the health check
        environment.healthChecks().register("virtual-lab", new VirtualLabHealthCheck());
        // Register the resources
        environment.jersey().register(new VirtualLabResource(configuration));
    }
}
