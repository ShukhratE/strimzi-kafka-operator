/*
 * Copyright Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.api.kafka.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.fabric8.kubernetes.api.model.Doneable;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.CustomResource;
import io.strimzi.api.kafka.model.status.HasStatus;
import io.strimzi.api.kafka.model.status.KafkaConnectStatus;
import io.strimzi.crdgenerator.annotations.Crd;
import io.strimzi.crdgenerator.annotations.Description;
import io.sundr.builder.annotations.Buildable;
import io.sundr.builder.annotations.Inline;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

@JsonDeserialize
@Crd(
        apiVersion = KafkaConnect.CRD_API_VERSION,
        spec = @Crd.Spec(
                names = @Crd.Spec.Names(
                        kind = KafkaConnect.RESOURCE_KIND,
                        plural = KafkaConnect.RESOURCE_PLURAL,
                        shortNames = {KafkaConnect.SHORT_NAME},
                        categories = {Constants.STRIMZI_CATEGORY}
                ),
                group = KafkaConnect.RESOURCE_GROUP,
                scope = KafkaConnect.SCOPE,
                version = KafkaConnect.V1BETA1,
                versions = {
                        @Crd.Spec.Version(
                                name = KafkaConnect.V1BETA1,
                                served = true,
                                storage = true
                        ),
                        @Crd.Spec.Version(
                                name = KafkaConnect.V1ALPHA1,
                                served = true,
                                storage = false
                        )
                },
                subresources = @Crd.Spec.Subresources(
                        status = @Crd.Spec.Subresources.Status(),
                        scale = @Crd.Spec.Subresources.Scale(
                                specReplicasPath = KafkaConnect.SPEC_REPLICAS_PATH,
                                statusReplicasPath = KafkaConnect.STATUS_REPLICAS_PATH,
                                labelSelectorPath = KafkaConnect.LABEL_SELECTOR_PATH
                        )
                ),
                additionalPrinterColumns = {
                        @Crd.Spec.AdditionalPrinterColumn(
                                name = "Desired replicas",
                                description = "The desired number of Kafka Connect replicas",
                                jsonPath = ".spec.replicas",
                                type = "integer"
                        )
                }
        )
)
@Buildable(
        editableEnabled = false,
        builderPackage = Constants.FABRIC8_KUBERNETES_API,
        inline = @Inline(type = Doneable.class, prefix = "Doneable", value = "done")
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"apiVersion", "kind", "metadata", "spec", "status"})
@EqualsAndHashCode
public class KafkaConnect extends CustomResource implements UnknownPropertyPreserving, HasStatus<KafkaConnectStatus> {

    private static final long serialVersionUID = 1L;

    public static final String SCOPE = "Namespaced";
    public static final String V1ALPHA1 = Constants.V1ALPHA1;
    public static final String V1BETA1 = Constants.V1BETA1;
    public static final List<String> VERSIONS = unmodifiableList(asList(V1BETA1, V1ALPHA1));
    public static final String RESOURCE_KIND = "KafkaConnect";
    public static final String RESOURCE_LIST_KIND = RESOURCE_KIND + "List";
    public static final String RESOURCE_GROUP = Constants.RESOURCE_GROUP_NAME;
    public static final String RESOURCE_PLURAL = "kafkaconnects";
    public static final String RESOURCE_SINGULAR = "kafkaconnect";
    public static final String CRD_API_VERSION = Constants.V1BETA1_API_VERSION;
    public static final String CRD_NAME = RESOURCE_PLURAL + "." + RESOURCE_GROUP;
    public static final String SHORT_NAME = "kc";
    public static final List<String> RESOURCE_SHORTNAMES = singletonList(SHORT_NAME);
    public static final String SPEC_REPLICAS_PATH = ".spec.replicas";
    public static final String STATUS_REPLICAS_PATH = ".status.replicas";
    public static final String LABEL_SELECTOR_PATH = ".status.selector";

    private String apiVersion;
    private KafkaConnectSpec spec;
    private ObjectMeta metadata;
    private KafkaConnectStatus status;
    private Map<String, Object> additionalProperties = new HashMap<>(0);

    @Override
    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @JsonProperty("kind")
    @Override
    public String getKind() {
        return RESOURCE_KIND;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Override
    public ObjectMeta getMetadata() {
        return super.getMetadata();
    }

    @Override
    public void setMetadata(ObjectMeta metadata) {
        super.setMetadata(metadata);
    }

    @Description("The specification of the Kafka Connect cluster.")
    public KafkaConnectSpec getSpec() {
        return spec;
    }

    public void setSpec(KafkaConnectSpec spec) {
        this.spec = spec;
    }

    @Override
    @Description("The status of the Kafka Connect cluster.")
    public KafkaConnectStatus getStatus() {
        return status;
    }

    public void setStatus(KafkaConnectStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        YAMLMapper mapper = new YAMLMapper().disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @Override
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
