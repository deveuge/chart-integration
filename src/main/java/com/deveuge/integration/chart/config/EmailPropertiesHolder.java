package com.deveuge.integration.chart.config;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.springframework.util.StringUtils;

import com.deveuge.integration.chart.util.VaultUtil;

@Named("emailPropertiesHolder")
public class EmailPropertiesHolder extends PropertiesHolder {

    private String smtpServer;
    private String smtpPort;
    private String smtpUsername;
    private String smtpPassword;
    private boolean requiresAuthentication;

    private String notificationFrom;
    private String[] notificationTo;

    private String notificationSubjectOK;
    private String notificationBodyOK;
    private String notificationSubjectKO;
    private String notificationBodyKO;

    @PostConstruct
    public void initEmail() {
        this.smtpServer = getStringProperty("email.smtp.server");
        this.smtpPort = getStringProperty("email.smtp.port");
        this.requiresAuthentication = getBooleanProperty("email.smtp.requiresAuthentication");
        this.smtpUsername = getStringProperty("email.smtp.authentication.username");
        this.smtpPassword = VaultUtil.vaultDecrypt(getStringProperty("email.smtp.authentication.password"));

        this.notificationFrom = getStringProperty("email.notification.from");
        this.notificationTo = getArrayStringProperty("email.notification.to");

        this.notificationSubjectOK = getStringProperty("email.notification.subject.ok");
        this.notificationBodyOK = getStringProperty("email.notification.body.ok");
        this.notificationSubjectKO = getStringProperty("email.notification.subject.ko");
        this.notificationBodyKO = getStringProperty("email.notification.body.ko");
    }

	public String getSmtpServer() {
		return smtpServer;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public String getSmtpUsername() {
		return smtpUsername;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public boolean isRequiresAuthentication() {
		return requiresAuthentication;
	}

	public String getNotificationFrom() {
		return notificationFrom;
	}

	public String[] getNotificationTo() {
		return notificationTo;
	}
	
	public String getNotificationSubjectOK() {
		return notificationSubjectOK;
	}

	public String getNotificationBodyOK() {
		return notificationBodyOK;
	}

	public String getNotificationSubjectKO() {
		return notificationSubjectKO;
	}

	public String getNotificationBodyKO() {
		return notificationBodyKO;
	}
    
	public boolean isEmailSet() {
		return StringUtils.hasText(smtpServer) && StringUtils.hasText(notificationFrom) && notificationTo != null;
	}
	
	public boolean isOKEmailSet() {
		return StringUtils.hasText(notificationSubjectOK) && StringUtils.hasText(notificationBodyOK);
	}
	
	public boolean isKOEmailSet() {
		return StringUtils.hasText(notificationSubjectKO) && StringUtils.hasText(notificationBodyKO);
	}
}
