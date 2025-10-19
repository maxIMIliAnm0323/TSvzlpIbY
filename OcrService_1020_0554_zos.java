// 代码生成时间: 2025-10-20 05:54:48
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.View;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;

@Path("/ocr")
public class OcrService extends ResourceConfig {

    public OcrService() {
        // Registering the OCR resource
        register(OcrResource.class);
    }
}

@Path("/ocr")
public class OcrResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response performOcr(@FormDataParam("image") byte[] imageBytes) {
        try {
            // Extracting the image from the multipart request
            File image = new File("tmp_image.jpg");
            java.nio.file.Files.write(image.toPath(), imageBytes);

            // Initializing Tesseract for OCR
            ITesseract instance = new Tesseract();
            instance.setDatapath("tessdata"); // Path to the Tessdata directory
            instance.setLanguage("eng"); // Language for OCR

            // Performing OCR and extracting text
            String text = instance.doOCR(image);

            // Cleaning up the temporary file
            image.delete();

            // Returning the extracted text as JSON
            return Response.ok(text).build();
        } catch (TesseractException | IOException e) {
            // Handling exceptions and returning an error message
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error occurred during OCR: " + e.getMessage()).build();
        }
    }
}

public class OcrApplication extends Application<OcrConfiguration> {

    @Override
    public void initialize(Bootstrap<OcrConfiguration> bootstrap) {
        // Initialize any resources, like databases, configurations, etc.
    }

    @Override
    public void run(OcrConfiguration configuration, Environment environment) throws Exception {
        // Registering the OcrService
        environment.jersey().register(new OcrService());
    }

    public static void main(String[] args) throws Exception {
        new OcrApplication().run(args);
    }
}

/*
 * OcrConfiguration.java
 *
 * Configuration class for the OCR service.
 */
import io.dropwizard.Configuration;
import javax.validation.constraints.NotNull;

public class OcrConfiguration extends Configuration {
    // Configuration properties can be added here
    @NotNull
    private String tessdataPath;

    public String getTessdataPath() {
        return tessdataPath;
    }
    public void setTessdataPath(String tessdataPath) {
        this.tessdataPath = tessdataPath;
    }
}

/*
 * OcrConfigurationValidator.java
 *
 * Validator class for the OCR service configuration.
 */
import io.dropwizard.configuration.ConfigurationValidator;
import io.dropwizard.validation.ValidationMethod;

public class OcrConfigurationValidator extends ConfigurationValidator<OcrConfiguration> {
    @ValidationMethod(message = "Tessdata path cannot be null")
    public void checkTessdataPath(OcrConfiguration configuration) {
        if (configuration.getTessdataPath() == null || configuration.getTessdataPath().isEmpty()) {
            throw new IllegalArgumentException("Tessdata path must not be null or empty");
        }
    }
}