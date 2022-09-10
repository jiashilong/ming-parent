package com.ming.k8s.service;

import io.fabric8.kubernetes.api.model.ServiceAccount;

import java.util.List;

public interface AccountService {
    List<ServiceAccount> getServiceAccountList(String namespace);
    String getToken(String namespace, String accountName);
    String base64Decode(String str);
    boolean createApiAdminAccount(String namespace, String accountName);
}
