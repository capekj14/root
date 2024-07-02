package app.root;

import app.root.model.*;
import app.root.repository.CastObceRepository;
import app.root.repository.ObecRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * class for managing whole app,
 * controlling functionality of app
 */
@Service
public class MainService {

    @Autowired
    private ObecRepository obecRepository;
    @Autowired
    private CastObceRepository castObceRepository;

    /**
     * deletes all data from both tables
     */
    public void cleanDB() {
        obecRepository.deleteAll();
        castObceRepository.deleteAll();
    }

    /**
     * reads all data from both tables
     * writes them into console
     */
    public void readDb() {
        System.out.println("---------------------------------------------");
        System.out.println("OBCE (nazev, kod)");
        var obce = obecRepository.findAll();
        for(var obec : obce) {
            System.out.println(obec.getNazev() + " " + obec.getKod());
        }
        System.out.println("---------------------------------------------");
        System.out.println("CASTI OBCI (nazev, kodCasti, kodObce)");
        var casti = castObceRepository.findAll();
        for(var cast : casti) {
            System.out.println(cast.getNazev() + " " + cast.getKodCastObce() + " " + cast.getKodObce());
        }
        System.out.println("---------------------------------------------");
    }

    /**
     * parses Obce from "doc" and saves them into db
     * @param doc Document object with XML structure
     */
    public void parseObce(Document doc) {
        Obec obec = new Obec();
        Node obecNode = doc.getElementsByTagName(Utility.OBEC_TAG).item(0);
        NodeList nodeList = obecNode.getChildNodes();
        int size = nodeList.getLength();
        Node current;
        for (int i = 0; i < size; i++) {
            current = nodeList.item(i);
            if(Objects.equals(current.getNodeName(), Utility.KOD_OBCE_TAG)) {
                obec.setKod(current.getTextContent());
            }
            else if(Objects.equals(current.getNodeName(), Utility.NAZEV_OBCE_TAG)) {
                obec.setNazev(current.getTextContent());
            }
        }
        obecRepository.save(obec);
    }

    /**
     * parses CastiObce from "doc" and saves them into db
     * @param doc Document object with XML structure
     */
    public void parseCastiObci(Document doc) {
        NodeList castiList = doc.getElementsByTagName(Utility.CAST_OBCE_TAG);
        int castiSize = castiList.getLength();
        for(int i = 0 ; i < castiSize; i++) {
            CastObce castObce = new CastObce();
            Node castObceNode = castiList.item(i);
            NodeList nodeList = castObceNode.getChildNodes();
            int n = nodeList.getLength();
            for (int j = 0; j < n; j++) {
                Node current = nodeList.item(j);
                if(Objects.equals(current.getNodeName(), Utility.KOD_CASTI_TAG)) {
                    castObce.setKodCastObce(current.getTextContent());
                }
                else if(Objects.equals(current.getNodeName(), Utility.NAZEV_CASTI_TAG)) {
                    castObce.setNazev(current.getTextContent());
                }
                else if(Objects.equals(current.getNodeName(), Utility.KOD_REF_OBCE_TAG)) {
                    castObce.setKodObce(current.getTextContent().trim());
                }
            }
        castObceRepository.save(castObce);
        }
    }

    /**
     * downloads zip file from url, loads file,
     * @throws IOException Input/Output exception for file handling
     * @throws SAXException XML parsing can produce SAXExc.
     * @throws ParserConfigurationException some other exception
     */
    public void getData() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = null;
        URL url = new URL(Utility.URL);
        Path tempDir = Files.createTempDirectory("xml");
        Path zipFile = tempDir.resolve(Utility.ZIP);
        try (InputStream in = url.openStream()) {
            Files.copy(in, zipFile);
        }
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                Path xmlFile = tempDir.resolve(zipEntry.getName());
                Files.copy(zis, xmlFile);
                doc = builder.parse(new File(String.valueOf(xmlFile)));
                doc.getDocumentElement().normalize();
                zipEntry = zis.getNextEntry();
            }
        }
        catch (Exception e ) {
            throw e;
        }
        assert doc != null;
        parseObce(doc);
        parseCastiObci(doc);

    }

}
