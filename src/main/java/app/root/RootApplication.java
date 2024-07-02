package app.root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Main class for starting app
 */
@SpringBootApplication
public class RootApplication implements CommandLineRunner {

    @Autowired
    private MainService service;

    public static void main(String[] args) {
        SpringApplication.run(RootApplication.class, args);
    }

    /**
     * Function to define which functions will be called in next run of the app
     * (AKTUALNE) -> vycisti DB, nacti a uloz data, precti data z db
     * cleanDB isnt necessary its just for program to run repeatedly
     * @param args input args
     */
    @Override
    public void run(String... args) {
        service.cleanDB();
        try {
            service.getData();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        service.readDb();

    }
}
