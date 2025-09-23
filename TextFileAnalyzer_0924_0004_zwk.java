// 代码生成时间: 2025-09-24 00:04:12
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.dropwizard.views.mustache.MustacheViewRenderer;
import io.dropwizard.views.mustache.MustacheTemplateRenderer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextFileAnalyzer extends Application<TextFileAnalyzerConfiguration> {

    public static void main(String[] args) throws Exception {
        new TextFileAnalyzer().run(args);
    }

    @Override
    public void initialize(Bootstrap<TextFileAnalyzerConfiguration> bootstrap) {
        // Registering a ViewBundle for rendering views
        bootstrap.addBundle(new ViewBundle<>(MustacheTemplateRenderer.class));
    }

    @Override
    public void run(TextFileAnalyzerConfiguration configuration, Environment environment) throws Exception {
        // Initialize ObjectMapper for JSON processing
        ObjectMapper objectMapper = new ObjectMapper();

        // Create a new TextFileAnalyzerService
        TextFileAnalyzerService analyzerService = new TextFileAnalyzerService();

        // Registering a new route for analyzing text files
        environment.jersey().register(new TextFileAnalyzerResource(analyzerService));
    }
}

// TextFileAnalyzerService class to handle text file analysis
class TextFileAnalyzerService {
    // Method to analyze the content of a text file
    public String analyzeTextFile(String filePath) throws IOException {
        // Check if the file exists
        if (!Files.exists(Paths.get(filePath))) {
            throw new IOException("File does not exist: " + filePath);
        }

        // Read all lines from the file
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            // Use Java 8 streams to count lines, words, and characters
            long lines = stream.count();
            long words = stream.flatMap(line -> Stream.of(line.split("\s+"))).count();
            long characters = stream.mapToLong(String::length).sum();

            // Return the analysis results as a JSON string
            return String.format({"lines": %d, "words": %d, "characters": %d}, lines, words, characters);
        } catch (IOException e) {
            // Handle file reading errors
            throw new IOException("Error reading file: " + filePath, e);
        }
    }
}

// Resource class for handling HTTP requests
class TextFileAnalyzerResource {
    private final TextFileAnalyzerService analyzerService;

    public TextFileAnalyzerResource(TextFileAnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    // Method to handle HTTP GET requests for text file analysis
    public String analyzeTextFile(String filePath) {
        try {
            // Analyze the text file and return the results
            return analyzerService.analyzeTextFile(filePath);
        } catch (IOException e) {
            // Handle exceptions and return an error message
            return String.format({"error": "%s"}, e.getMessage());
        }
    }
}
