//package com.example.AzureKeyVault;
//
//import com.azure.security.keyvault.secrets.SecretClient;
//import com.azure.security.keyvault.secrets.SecretClientBuilder;
//import com.azure.identity.ClientSecretCredential;
//import com.azure.identity.ClientSecretCredentialBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KeyVaultService {
//    private static final Logger logger = LoggerFactory.getLogger(KeyVaultService.class);
//    private SecretClient secretClient;
//    private final boolean keyVaultEnabled;
//
//    public KeyVaultService(Environment env) {
//        this.keyVaultEnabled = Boolean.parseBoolean(env.getProperty("spring.cloud.azure.keyvault.secret.enabled", "true"));
//
//        if (!keyVaultEnabled) {
//            logger.info("Azure Key Vault is disabled.");
//            return;
//        }
//
//        logger.info("Initializing Azure Key Vault integration...");
//
//        String clientId = env.getProperty("azure.keyvault.client-id");
//        String clientSecret = env.getProperty("azure.keyvault.client-secret");
//        String tenantId = env.getProperty("azure.keyvault.tenant-id");
//
//        if (clientId == null || clientSecret == null || tenantId == null) {
//            logger.error("Missing required environment variables for Azure authentication!");
//            throw new IllegalArgumentException("Must provide non-null values for clientId, tenantId, clientSecret");
//        }
//
//        ClientSecretCredential credential = new ClientSecretCredentialBuilder()
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .tenantId(tenantId)
//                .build();
//
//        this.secretClient = new SecretClientBuilder()
//                .vaultUrl(env.getProperty("azure.keyvault.uri"))
//                .credential(credential)
//                .buildClient();
//    }
//
//    public String getSecret(String secretName) {
//        if (!keyVaultEnabled) {
//            logger.warn("Attempting to fetch secret in local mode. Key Vault is disabled.");
//            return "HardcodedLocalSecret";
//        }
//
//        return secretClient.getSecret(secretName).getValue();
//    }
//}
