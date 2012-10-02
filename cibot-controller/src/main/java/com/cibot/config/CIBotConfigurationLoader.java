package com.cibot.config;

import org.apache.commons.io.IOUtils;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Uefix
 */
public class CIBotConfigurationLoader {

    public CIBotConfiguration loadConfiguration(String filename) {
        URL resourceUrl = getClass().getClassLoader().getResource(filename);

        InputStream is = null;
        try {
            is = resourceUrl.openStream();
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
}
