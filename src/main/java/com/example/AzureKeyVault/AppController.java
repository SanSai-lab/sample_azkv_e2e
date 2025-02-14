package com.example.AzureKeyVault;

import com.example.AzureKeyVault.SecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    private final SecretService secretService;

    @Autowired
    public AppController(SecretService secretService) {
        this.secretService = secretService;
    }

    @GetMapping("/secrets")
    public String getSecrets() {
        return "Secret Key: " + secretService.getSecretKey() + ", DB Password: " + secretService.getDbPassword();
    }

    @GetMapping("/print")
    public String printStatement() {
        return "I'm Here!";
    }
}