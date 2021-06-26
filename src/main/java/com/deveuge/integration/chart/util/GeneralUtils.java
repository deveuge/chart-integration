package com.deveuge.integration.chart.util;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralUtils.class);

    public static String readBodyFile(final String path) {
        try {
            final byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, Charset.forName("UTF-8"));
        }
        catch(final Exception e) {
        	String msj = "Error while reading text file " + path;
            LOGGER.error(msj, e);
            throw new RuntimeException(msj);
        }
    }

    public static DataHandler loadResourceAsDataSource(final String resource) {
        return new DataHandler(new FileDataSource(resource));
    }
}
