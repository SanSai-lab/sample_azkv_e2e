package com.example.AzureKeyVault;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SecretService {
    private static final Logger logger = LoggerFactory.getLogger(SecretService.class);

    private final SecretClient secretClient;
    private final boolean keyVaultEnabled;

    @Value("${app.secretKey:NOT_FOUND}")
    private String localSecretKey;

    @Value("${app.db.password:NOT_FOUND}")
    private String localDbPassword;

    public SecretService(Environment env) {
        this.keyVaultEnabled = Boolean.parseBoolean(env.getProperty("spring.cloud.azure.keyvault.secret.enabled", "false"));

        if (keyVaultEnabled) {
            logger.info("Azure Key Vault enabled - Secrets will be fetched from Key Vault.");

            ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                    .clientId(env.getProperty("azure.keyvault.client-id"))
                    .clientSecret(env.getProperty("azure.keyvault.client-secret"))
                    .tenantId(env.getProperty("azure.keyvault.tenant-id"))
                    .build();

            this.secretClient = new SecretClientBuilder()
                    .vaultUrl(env.getProperty("azure.keyvault.uri"))
                    .credential(credential)
                    .buildClient();
        } else {
            logger.warn(" Azure Key Vault is disabled - Using local properties.");
            this.secretClient = null;
        }
    }

    public String getSecretKey() {
        return getSecret("app-secretKey", localSecretKey);
    }

    public String getDbPassword() {
        return getSecret("db-password", localDbPassword);
    }

    private String getSecret(String secretName, String localValue) {
        if (keyVaultEnabled && secretClient != null) {
            try {
                logger.info("Fetching {} from Azure Key Vault...", secretName);
                return secretClient.getSecret(secretName).getValue();
            } catch (Exception e) {
                logger.error(" Error fetching secret '{}' from Key Vault: {}", secretName, e.getMessage());
                return "ERROR_FETCHING_SECRET";
            }
        }
        logger.warn("Using local value for {}: {}", secretName, localValue);
        return localValue;
    }
}
