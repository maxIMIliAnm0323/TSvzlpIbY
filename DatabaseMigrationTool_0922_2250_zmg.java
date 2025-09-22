// 代码生成时间: 2025-09-22 22:50:29
 * @author [Your Name]
 * @version 1.0
 */

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseMigrationTool {

    /**
     * Main method to run the database migration.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        try {
            // Load the properties file
            Properties properties = loadProperties("database.properties");

            // Connect to the database
            Connection connection = connectToDatabase(properties);

            // Perform the migration
            performMigration(properties, connection);

        } catch (Exception e) {
            // Handle any exceptions that occur
            e.printStackTrace();
        }
    }

    /**
     * Loads the properties from the specified file.
     * 
     * @param filename The name of the properties file.
     * @return The loaded properties.
     */
    private static Properties loadProperties(String filename) {
        try {
            Properties properties = new Properties();
            properties.load(DatabaseMigrationTool.class.getClassLoader().getResourceAsStream(filename));
            return properties;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties file: " + filename, e);
        }
    }

    /**
     * Establishes a connection to the database using the provided properties.
     * 
     * @param properties The database connection properties.
     * @return A connection to the database.
     */
    private static Connection connectToDatabase(Properties properties) {
        try {
            String url = properties.getProperty("database.url");
            String user = properties.getProperty("database.user");
            String password = properties.getProperty("database.password");

            // Load the JDBC driver
            Class.forName(properties.getProperty("database.driver"));

            // Establish the connection
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    /**
     * Performs the database migration using Liquibase.
     * 
     * @param properties The database connection properties.
     * @param connection The database connection.
     */
    private static void performMigration(Properties properties, Connection connection) {
        try {
            // Create a Liquibase object
            Liquibase liquibase = new Liquibase(
                properties.getProperty("migration.changelog"),
                new ClassLoaderResourceAccessor(),
                new JdbcConnection(connection)
            );

            // Update the database by applying all available migrations
            liquibase.update("latest");

            System.out.println("Database migration completed successfully.");

        } catch (Exception e) {
            throw new RuntimeException("Failed to perform database migration", e);
        }
    }
}
