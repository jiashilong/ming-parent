package com.ming.k8s.configuration;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class K8SConfiguration {
    @Value("${application.k8s.apiServer.url}")
    private String url;

    @Value("${application.k8s.apiServer.token}")
    private String token;

    private KubernetesClient kubernetesClient;

    @PostConstruct
    public void init() {
        Config config = new ConfigBuilder().withMasterUrl(url)
//                .withTrustCerts(true)
//                .withOauthToken(token)
                .build();
        kubernetesClient = new DefaultKubernetesClient(config);
    }

    @Bean
    public KubernetesClient getK8SClient() {
        return kubernetesClient;
    }
}
