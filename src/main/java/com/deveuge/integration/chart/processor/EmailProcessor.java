package com.deveuge.integration.chart.processor;

import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;

import com.deveuge.integration.chart.util.GeneralUtils;

@Named("emailProcessor")
public class EmailProcessor implements Processor {
    
    @Value("${email.notification.body.ok}")
    private String notificationBodyOK;
    
    @Value("${email.notification.body.ko}")
    private String notificationBodyKO;
    
    @Value("${general.directory.output}")
    private String outputDirectory;

    @Override
    public void process(final Exchange exchange) throws Exception {
    	String routeId = exchange.getUnitOfWork().getRouteContext().getRoute().getId();
        final String body = getBodyTemplate(routeId);
        final String filename = exchange.getProperty("filename", String.class);
        final String timestamp = exchange.getProperty("timestamp", String.class);
        final Object response = exchange.getIn().getBody();
        
        if(isSuccessfulProcessing(routeId)) {
        	exchange.getIn().setBody(buildOKBody(body, filename, response));
        	exchange.getIn().addAttachment("chartImage.png", GeneralUtils.loadResourceAsDataSource(buildChartImage(filename, timestamp)));
        } else {
        	exchange.getIn().setBody(buildKOBody(body, filename, response));
        }
    }

    private String getBodyTemplate(final String routeId) {
    	if(isSuccessfulProcessing(routeId))
    		return GeneralUtils.readBodyFile(notificationBodyOK);
    	return GeneralUtils.readBodyFile(notificationBodyKO);
    }
    
    private String buildOKBody(String body, final String filename, final Object response) {
    	body = body.replace("${FILENAME}",  filename);
    	return body;
    }
    
    private String buildKOBody(String body, final String filename, final Object response) {
    	body = body.replace("${FILENAME}",  filename);
    	body = body.replace("${EXCEPTION}",  response.toString());
    	return body;
    }
    
    private boolean isSuccessfulProcessing(final String routeId) {
    	return routeId.equalsIgnoreCase("EmailOKRoute");
    }
    
    private String buildChartImage(final String filename, final String timestamp) {
    	return String.format("%s/%s-%s.png", outputDirectory, filename, timestamp);
    }

}
