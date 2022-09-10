package com.ming.k8s.service.impl;

import com.ming.k8s.service.AccountService;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.rbac.ClusterRoleBinding;
import io.fabric8.kubernetes.api.model.rbac.RoleRef;
import io.fabric8.kubernetes.api.model.rbac.Subject;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.zjsonpatch.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {
    private static final String EMPUTY_STRING = "";

    @Autowired
    private KubernetesClient k8sClient;

    public List<ServiceAccount> getServiceAccountList(String namespace) {
        if(StringUtils.isEmpty(namespace)) {
            return new ArrayList<>();
        }
        ServiceAccountList serviceAccountList = k8sClient.serviceAccounts().inNamespace(namespace).list();
        List<ServiceAccount> items = serviceAccountList.getItems();
        return items;
    }

    @Override
    public String getToken(String namespace, String accountName) {
        String secretName = this.getSecretWithAccount(namespace, accountName);
        String token = this.getTokenWithSecret(namespace, secretName);
        return token;
    }

    private String getSecretWithAccount(String namespace, String accountName) {
        ServiceAccount serviceAccount = k8sClient.serviceAccounts().inNamespace(namespace).withName(accountName).get();
        if(serviceAccount == null) {
            return EMPUTY_STRING;
        }

        List<ObjectReference> secretList = serviceAccount.getSecrets();
        if(CollectionUtils.isEmpty(secretList)) {
            return EMPUTY_STRING;
        }

        ObjectReference secret = secretList.get(0);
        if(secret == null) {
            return EMPUTY_STRING;
        }

        String secretName = secret.getName();
        return secretName;
    }

    private String getTokenWithSecret(String namespace, String secretName) {
        Secret secret = k8sClient.secrets().inNamespace(namespace)
                .withName(secretName).get();
        if(secret == null) {
            return EMPUTY_STRING;
        }

        Map<String, String> dataMap = secret.getData();
        if(CollectionUtils.isEmpty(dataMap)) {
            return EMPUTY_STRING;
        }

        String token = dataMap.get("token");
        if(StringUtils.isEmpty(token)) {
            return EMPUTY_STRING;
        }

        String decodeToken = this.base64Decode(token);
        return decodeToken;

    }

    @Override
    public String base64Decode(String str) {
        if(StringUtils.isEmpty(str)) {
            return "";
        }

        byte[] bytes = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
        return new String(bytes);
    }

    @Override
    public boolean createApiAdminAccount(String namespace, String accountName) {
        ServiceAccount serviceAccount = new ServiceAccount();
        serviceAccount.setApiVersion("v1");
        serviceAccount.setKind("ServiceAccount");
        ObjectMeta serviceAccountMeta = new ObjectMeta();
        serviceAccountMeta.setName(accountName);
        serviceAccountMeta.setNamespace(namespace);
        serviceAccount.setMetadata(serviceAccountMeta);
        ServiceAccount newServiceAccount = k8sClient.serviceAccounts()
                .inNamespace(namespace)
                .createOrReplace(serviceAccount);

        ClusterRoleBinding clusterRoleBinding = new ClusterRoleBinding();
        clusterRoleBinding.setApiVersion("rbac.authorization.k8s.io/v1");
        clusterRoleBinding.setKind("ClusterRoleBinding");
        ObjectMeta clusterRoleBindingMeta = new ObjectMeta();
        clusterRoleBindingMeta.setName(accountName);
        clusterRoleBindingMeta.setNamespace(namespace);
        clusterRoleBinding.setMetadata(clusterRoleBindingMeta);
        clusterRoleBinding.setRoleRef(new RoleRef("rbac.authorization.k8s.io", "ClusterRole", "cluster-admin"));
        List<Subject> subjectList = new ArrayList<Subject>() {{
            this.add(new Subject("", "ServiceAccount", accountName, namespace));
        }};
        clusterRoleBinding.setSubjects(subjectList);

        Config config = k8sClient.getConfiguration();
        String oldNamespace = config.getNamespace();
        config.setNamespace(namespace);
        ClusterRoleBinding newClusterRoleBinding = k8sClient.rbac().clusterRoleBindings().create(clusterRoleBinding);
        config.setNamespace(oldNamespace);
        return newServiceAccount != null && newClusterRoleBinding != null;
    }
}
