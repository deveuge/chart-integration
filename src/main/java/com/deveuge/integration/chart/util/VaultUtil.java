package com.deveuge.integration.chart.util;

import org.jboss.security.vault.SecurityVaultException;
import org.jboss.security.vault.SecurityVaultUtil;

public class VaultUtil {
    
    public static String vaultDecrypt(final String value)  {
        if (!SecurityVaultUtil.isVaultFormat(value)) {
        	return value;
        }
        
        try {
            return SecurityVaultUtil.getValueAsString(value);
        } catch (final SecurityVaultException e) {
            throw new IllegalArgumentException("Failed to decrypt string '" + value + "'", e);
        }
    }
}