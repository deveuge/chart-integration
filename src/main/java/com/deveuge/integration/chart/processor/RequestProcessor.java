package com.deveuge.integration.chart.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.ws.rs.core.MediaType;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.deveuge.integration.chart.dto.ChartRequest;
import com.deveuge.integration.chart.dto.types.ChartDataset;
import com.deveuge.integration.chart.dto.types.ChartType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Named("requestProcessor")
public class RequestProcessor implements Processor {
	
	@Value("${general.file.delimiter}")
	String fileDelimiter;

	public void process(Exchange exchange) throws Exception {
		final File file = exchange.getIn().getBody(File.class);
		String[] dataLabels = getDataLabels(file);
		final List<ChartDataset> dataSets = getDataSets(file);
		final String filename = exchange.getProperty("filename", String.class);
		final ChartType type = getFileFormat(filename); 
		
		ChartRequest request = new ChartRequest(dataLabels, dataSets, type);
		
		ObjectMapper objectMapper = new ObjectMapper();
		exchange.setPattern(ExchangePattern.InOut);
		exchange.getIn().setBody(objectMapper.writeValueAsString(request));
		exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
	}
	
	/**
	 * Reads the first line of the document and returns the data labels of the chart excluding the first value.
	 * @param file The CSV file to process
	 * @return String array containing the labels of the chart
	 */
	private String[] getDataLabels(File file) {
		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        	String dataLabelsExcludingFirst = br.readLine().split(fileDelimiter, 2)[1];	// Exclude the first value as its not considered a label
            br.close();
            return dataLabelsExcludingFirst.trim().split("\\s*" + fileDelimiter + "\\s*"); // Split the labels in an array and remove trailing spaces
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	/**
	 * Reads the CSV file excluding the first line and builds de datasets of the chart
	 * @param file The CSV file to process
	 * @return List of ChartDataset containing the datasets of the chart
	 */
	private List<ChartDataset> getDataSets(File file) {
        try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
		    br.readLine(); // We skip the first line as it contains the labels

            List<ChartDataset> dataset = new ArrayList<>();
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if(StringUtils.hasText(line)) {
			    	String[] lineSplitFirst = line.split(fileDelimiter, 2);
			    	String[] datasetData = lineSplitFirst[1].trim().split("\\s*,\\s*");
			    	dataset.add(new ChartDataset(lineSplitFirst[0], datasetData));
		    	}
		    }
		    
		    br.close();
		    return dataset;
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	/** 
	 * Obtains the type of the chart to generate from the name of the CSV file. By default BAR.
	 * @param filename
	 * @return ChartType The type of chart to generate
	 */
	private ChartType getFileFormat(String filename) {
		try {
			String filenameType = filename.split("-")[1];
			return ChartType.valueOf(filenameType);
		} catch (Exception ex) {
			return ChartType.BAR;
		}
	}
}
