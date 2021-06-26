package com.deveuge.integration.chart;

import javax.annotation.PostConstruct;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.deveuge.integration.chart.dto.ChartRequest;
import com.fasterxml.jackson.databind.MapperFeature;

public class ChartRouteBuilder extends RouteBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChartRouteBuilder.class);
	
	@Value("${general.directory.input}")
    private String inputDirectory;

    @Value("${general.directory.output}")
    private String outputDirectory;

    @Value("${general.directory.processed}")
    private String processedDirectory;
    
    private static String URI_FROM;
    private static String URI_TO_OK;
    private static String URI_TO_KO;
    
    @PostConstruct
    private void init() {
    	URI_FROM = "file:" + inputDirectory + "?move=" + processedDirectory + "/${exchangeProperty.filename}-${exchangeProperty.timestamp}.bak&include=.*.csv";
    	URI_TO_OK = "file:" + outputDirectory + "?fileName=${exchangeProperty.filename}-${exchangeProperty.timestamp}.png";
    	URI_TO_KO = "file:" + outputDirectory + "?fileName=${exchangeProperty.filename}-${exchangeProperty.timestamp}.err";
    }

	@Override
	public void configure() throws Exception {
		configureExceptionHandler();
        configureChartRoute();
	}
	
	private void configureExceptionHandler() {
		onException(Exception.class)
	        .handled(true)
	        .log(LoggingLevel.ERROR, "An exception occurred: ${exception.stacktrace}")
	        .setBody().simple("${exception.stacktrace}")
	        .to(URI_TO_KO)
	        .to("direct:EmailKORoute?failIfNoConsumers=false")
	        .stop();
	}
	
	private void configureChartRoute() {
		JacksonDataFormat dataFormat = new JacksonDataFormat(ChartRequest.class);
		dataFormat.enableFeature(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
		
		from(URI_FROM)
	        .routeId("QuickChartRoute")
	        .routeDescription("Integration route with Quickchart.io")
	        .log(LoggingLevel.INFO, "A new file with name '${file:name}' is detected")
	        .setProperty("timestamp", simple("${date:now:yyyyMMddHHmmss}"))
	        .setProperty("filename", simple("${file:name.noext}"))
	        
	        .process("requestProcessor")
	        .to("cxfrs:bean:quickChartClient")
	        .to(URI_TO_OK, "direct:EmailOKRoute?failIfNoConsumers=false");
		
		LOGGER.info("Integration route with QuickChart.io created through the input directory. The input directory '{}' is monitored", inputDirectory);
	}
}
