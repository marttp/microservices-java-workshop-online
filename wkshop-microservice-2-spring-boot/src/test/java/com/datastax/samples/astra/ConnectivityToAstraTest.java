package com.datastax.samples.astra;

import java.io.File;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

/**
 * First Connectivity test with Astra.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class ConnectivityToAstraTest {

    /** Logger for the class. */
    private static Logger LOGGER = LoggerFactory.getLogger(ConnectivityToAstraTest.class);
    
    /** Settings. */
    public static String ASTRA_ZIP_FILE = "/Users/cedricklunven/Downloads/secure-connect-devworkshopdb.zip";
    public static String ASTRA_USERNAME = "todouser";
    public static String ASTRA_PASSWORD = "todopassword";
    public static String ASTRA_KEYSPACE = "todoapp";
    
    @Test
    @DisplayName("Test connectivity to Astra explicit values")
    public void should_connect_to_Astra_static() {
        
        // Given interface is properly populated
        Assertions.assertTrue(new File(ASTRA_ZIP_FILE).exists(), 
                    "File '" + ASTRA_ZIP_FILE + "' has not been found\n"
                    + "To run this sample you need to download the secure bundle file from ASTRA WebPage\n"
                    + "More info here:");
        
        // When connecting to ASTRA
        try (CqlSession cqlSession = CqlSession.builder()
                //.addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withCloudSecureConnectBundle(Paths.get(ASTRA_ZIP_FILE))
                .withAuthCredentials(ASTRA_USERNAME, ASTRA_PASSWORD)
                .withKeyspace(ASTRA_KEYSPACE)
                .build()) {
            
            // Then connection is successfull
            LOGGER.info(" + [OK] - Connection Established to Astra with Keyspace {}", 
                    cqlSession.getKeyspace().get());
        }
    }
    
    @Test
    @DisplayName("Test connectivity to Astra delegate file")
    public void should_connect_to_Astra_withConfig() {

        // Config loader from file
        DriverConfigLoader loader = DriverConfigLoader.fromFile(
                new File(ConnectivityToAstraTest.class.getResource("/test_astra.conf").getFile()));
        
        // Use it to create the session
        try (CqlSession cqlSession = CqlSession.builder().withConfigLoader(loader).build()) {
            LOGGER.info(" + [OK] - Connection Established to Astra with Keyspace {}", 
                    cqlSession.getKeyspace().get());
        }
    }

}
