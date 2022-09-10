package com.ming.k8s.service.impl;

import com.ming.k8s.BaseTest;
import com.ming.k8s.service.AccountService;
import io.fabric8.kubernetes.api.model.ObjectReference;
import io.fabric8.kubernetes.api.model.ServiceAccount;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class AccountServiceImplTest extends BaseTest {
    @Autowired
    private AccountService accountService;

    @Test
    public void getServiceAccountList() {
        String namespace = "kube-system";
        List<ServiceAccount> accountList = accountService.getServiceAccountList(namespace);
        for (ServiceAccount serviceAccount : accountList) {
            StringBuffer sb = new StringBuffer();
            String kind = serviceAccount.getKind();
            String apiVersion = serviceAccount.getApiVersion();
            String name = serviceAccount.getMetadata().getName();
            sb.append(kind);
            sb.append(":");
            sb.append(apiVersion);
            sb.append(":");
            sb.append(name);
            sb.append(":");
            List<ObjectReference> secrets = serviceAccount.getSecrets();
            for (ObjectReference secret : secrets) {
                String secretName = secret.getName();
                sb.append(secretName);
                String token = accountService.getToken(namespace, secretName);
                sb.append("@");
                sb.append(token);
                sb.append(":");
            }
            String item = sb.toString();
            item = item.substring(0, item.length()-1);
            System.out.println(item);
        }
    }

    @Test
    public void createApiAdminAccount() {
        boolean b = accountService.createApiAdminAccount("kube-system", "k8s-api-admin");
        System.out.println(b);
    }

    @Test
    public void getToken() {
        String token = accountService.getToken("kube-system", "k8s-api-admin");
        System.out.println(token);
    }

    @Test
    public void base64Decode() {
        String str = "dV8yODk5MjI3MDA6ZXlKMGVYQWlPaUpLVjFRaUxDSmhiR2NpT2lKSVV6STFOaUo5LmV5SnJaWGxPWVcxbElqb2lOMkl6TjJVeU5HUmtORE5rTkdaalpEbGxaVFV5WmpSbE1USXdOek5sWXpJaUxDSmxlSEFpT2pFMk5EZzNNRGs0TlRFc0luVnpaWEpKWkNJNk1qZzVPVEl5TnpBd0xDSjBiMnRsYmlJNkluWXpYekExT1RNMk5XUXdaV0l3TVRRMU5XSTVZV1ZsTkRNME56a3hOakZpWlRKa0luMC5jcmc5QWZteGh4QmIyYmVfYVRSb1ZCclJVWEltVzJsTEFoSnFoVmxvZk1N";
        System.out.println(accountService.base64Decode(str));
    }
}