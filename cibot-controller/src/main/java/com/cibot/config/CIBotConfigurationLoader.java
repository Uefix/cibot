package com.cibot.config;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Uefix
 */
public class CIBotConfigurationLoader {


    public static final String CONFIG_FILENAME_SYSPROPERTY = "cibot.config";


    public CIBotConfiguration loadConfiguration() {
        String configFilename = System.getProperty(CONFIG_FILENAME_SYSPROPERTY);
        return loadConfiguration(configFilename);
    }


    ///---- Internal ----//

    private CIBotConfiguration loadConfiguration(String filename) {
        InputStream is = null;
        try {
            is = openStream(filename);
            InputSource source = new InputSource(is);

            JAXBContext jaxbContext = JAXBContext.newInstance(CIBotConfiguration.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            CIBotConfiguration configuration = (CIBotConfiguration) jaxbUnmarshaller.unmarshal(source);

            return configuration;
        } catch (Exception e) {
            throw new IllegalStateException("Exception while loading config from " + filename, e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }


    private InputStream openStream(String filename) throws IOException {
        URL resourceUrl = getClass().getClassLoader().getResource(filename);
        if (resourceUrl != null) {
            return resourceUrl.openStream();
        } else {
            return new FileInputStream(new File(filename));
        }
    }
}
