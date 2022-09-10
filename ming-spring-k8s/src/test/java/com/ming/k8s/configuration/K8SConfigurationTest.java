package com.ming.k8s.configuration;

import com.ming.k8s.BaseTest;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.rbac.ClusterRole;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ApiextensionsAPIGroupDSL;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class K8SConfigurationTest extends BaseTest {
    @Autowired
    private K8SConfiguration k8SConfiguration;

    @Autowired
    private KubernetesClient kubernetesClient;

    @Test
    public void getApiVersion() {
        KubernetesClient client = k8SConfiguration.getK8SClient();
        ApiextensionsAPIGroupDSL apiextensions = client.apiextensions();
        String apiVersion = apiextensions.v1().getApiVersion();
        System.out.println(apiVersion);
    }

    @Test
    public void getNamespace() {
        String namespace = kubernetesClient.apiextensions().v1().getNamespace();
        System.out.println(namespace);
    }

    @Test
    public void getApiGroupList() {
        APIGroupList apiGroupList = kubernetesClient.apiextensions().v1().getApiGroups();
        for (APIGroup group : apiGroupList.getGroups()) {
            System.out.println( group.getKind() + ":" + group.getApiVersion() + ":" + group.getName());
        }
    }

    @Test
    public void getPodList() {
        List<Pod> podList = kubernetesClient.pods().inNamespace("default").list().getItems();
        for (Pod pod : podList) {
            String kind = pod.getKind();
            String apiVersion = pod.getApiVersion();
            String podIP = pod.getStatus().getPodIP();
            String nodeName = pod.getSpec().getNodeName();
            String name = pod.getMetadata().getName();
            System.out.println(kind + ":" + apiVersion + ":" + podIP + ":" + nodeName + ":" + name);
        }
    }

    @Test
    public void getRoles() {
        List<ClusterRole> items = kubernetesClient.rbac().clusterRoles().list().getItems();
        for (ClusterRole item : items) {
            System.out.println(item);
        }

        //kubernetesClient.rbac().clusterRoleBindings().createOrReplace().set
    }
}