// 代码生成时间: 2025-09-23 11:42:02
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.View;
import io.dropwizard.views.ViewBundle;
import io.dropwizard.views.freemarker.FreemarkerViewRenderer;
import io.dropwizard.views.mustache.MustacheViewRenderer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/math")
public class MathematicsTool {

    // 计算两个数的加法
    @GET
    @Path("/add")
    @Produces(MediaType.TEXT_PLAIN)
    public Response add(@QueryParam("a") double a, @QueryParam("b") double b) {
        try {
            double result = a + b;
            return Response.ok("Result: " + result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e.getMessage()).build();
        }
    }

    // 计算两个数的减法
    @GET
    @Path("/subtract")
    @Produces(MediaType.TEXT_PLAIN)
    public Response subtract(@QueryParam("a") double a, @QueryParam("b") double b) {
        try {
            double result = a - b;
            return Response.ok("Result: " + result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e.getMessage()).build();
        }
    }

    // 计算两个数的乘法
    @GET
    @Path("/multiply")
    @Produces(MediaType.TEXT_PLAIN)
    public Response multiply(@QueryParam("a") double a, @QueryParam("b") double b) {
        try {
            double result = a * b;
            return Response.ok("Result: " + result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e.getMessage()).build();
        }
    }

    // 计算两个数的除法
    @GET
    @Path("/divide")
    @Produces(MediaType.TEXT_PLAIN)
    public Response divide(@QueryParam("a") double a, @QueryParam("b") double b) {
        try {
            if (b == 0) {
                throw new ArithmeticException("Denominator cannot be zero");
            }
            double result = a / b;
            return Response.ok("Result: " + result).build();
        } catch (ArithmeticException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: " + e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e.getMessage()).build();
        }
    }
}
