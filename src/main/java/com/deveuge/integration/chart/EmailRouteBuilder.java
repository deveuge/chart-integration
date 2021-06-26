package com.deveuge.integration.chart;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deveuge.integration.chart.config.EmailPropertiesHolder;

public class EmailRouteBuilder extends RouteBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailRouteBuilder.class);
	
	@Inject
    @Named("emailPropertiesHolder")
    private EmailPropertiesHolder emailPropertiesHolder;

	@Override
	public void configure() throws Exception {
		if(emailPropertiesHolder.isEmailSet()) {
			if(emailPropertiesHolder.isOKEmailSet())
				configureOKEmailRoute();
			if(emailPropertiesHolder.isKOEmailSet())
				configureKOEmailRoute();
		} else {
			LOGGER.info("Email properties are not configured. Email notifications will not be sent");
		}
	}
	
	private void configureOKEmailRoute() {
        from("direct:EmailOKRoute")
            .routeId("EmailOKRoute")
            .routeDescription("Notification mailing route for correct processing")
            .log(LoggingLevel.INFO, "Sending correct processing notification email for the file: '${file:name}'")
            .setHeader("from", constant(emailPropertiesHolder.getNotificationFrom()))
            .setHeader("subject", constant(emailPropertiesHolder.getNotificationSubjectOK()))
            .setHeader("contentType", constant("text/html"))
            .setHeader("to", constant(emailPropertiesHolder.getNotificationTo()))
            .process("emailProcessor")
            .to(getEmailEndpoint());

        LOGGER.info("Created mailing route for correct processing. Emails will be sent from '{}' to '{}' with the subject '{}'", new String[] {emailPropertiesHolder.getNotificationFrom(), Arrays.toString(emailPropertiesHolder.getNotificationTo()), emailPropertiesHolder.getNotificationSubjectOK()});
    }
	
	private void configureKOEmailRoute() {
        from("direct:EmailKORoute")
            .routeId("EmailKORoute")
            .routeDescription("Notification mailing route for erroneous processing")
            .log(LoggingLevel.INFO, "Sending erroneous processing notification email for the file: '${file:name}'")
            .setHeader("from", constant(emailPropertiesHolder.getNotificationFrom()))
            .setHeader("subject", constant(emailPropertiesHolder.getNotificationSubjectKO()))
            .setHeader("contentType", constant("text/html"))
            .setHeader("to", constant(emailPropertiesHolder.getNotificationTo()))
            .process("emailProcessor")
            .to(getEmailEndpoint());

        LOGGER.info("Created mailing route for erroneous processing. Emails will be sent from '{}' to '{}' with the subject '{}'", new String[] {emailPropertiesHolder.getNotificationFrom(), Arrays.toString(emailPropertiesHolder.getNotificationTo()), emailPropertiesHolder.getNotificationSubjectKO()});
    }
	
	private String getEmailEndpoint() {
        if (emailPropertiesHolder.isRequiresAuthentication()) {
            return String.format("smtp://%s@%s:%s?password=%s&mail.smtp.starttls.enable=true&mail.smtp.ssl.trust=smtp.gmail.com", emailPropertiesHolder.getSmtpUsername(), emailPropertiesHolder.getSmtpServer(), emailPropertiesHolder.getSmtpPort(), emailPropertiesHolder.getSmtpPassword());
        }
        return String.format("smtp://%s:%s", emailPropertiesHolder.getSmtpServer(), emailPropertiesHolder.getSmtpPort());
    }

}
