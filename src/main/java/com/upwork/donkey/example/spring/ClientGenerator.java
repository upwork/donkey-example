package com.upwork.donkey.example.spring;

import com.upwork.donkey.core.ast.*;
import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientGenerator extends BaseSpringGenerator {
    @Override
    public Map<String, String> generateSources(
            ServiceDefinition serviceDefinition, Map<String, Optional<String>> properties) {
        return new HashMap<String, String>() {{
            putAll(generateConnectionSettings(serviceDefinition));
            putAll(generateResourceClients(serviceDefinition));
            putAll(generateBaseClientException(serviceDefinition));
            putAll(generateClientExceptions(serviceDefinition));
        }};
    }

    private Map<String, String> generateConnectionSettings(
            ServiceDefinition serviceDefinition) {
        String mainTemplateName = "connectionSettings";
        ST template = createTemplateGroup(mainTemplateName).getInstanceOf(mainTemplateName);
        template.add("serviceDefinition", serviceDefinition);
        return new HashMap<String, String>() {{
            put(getBasePath(serviceDefinition) + StringUtils.capitalize(mainTemplateName) + ".java", template.render());
        }};
    }

    private Map<String, String> generateResourceClients(
            ServiceDefinition serviceDefinition) {
        String mainTemplateName = "resourceClient";
        STGroup group = createTemplateGroup(mainTemplateName);
        String basePath = getBasePath(serviceDefinition);
        Map<String, String> generatedClasses = new HashMap<>();
        serviceDefinition.getResourceDefinitions().forEach(resourceDefinition -> {
            ST template = group.getInstanceOf(mainTemplateName);
            template.add("resourceDefinition", resourceDefinition);
            String className = StringUtils.capitalize(resourceDefinition.getName()) + StringUtils.capitalize(mainTemplateName);
            generatedClasses.put(basePath + className + ".java", template.render());
        });
        return generatedClasses;
    }

    private Map<String, String> generateBaseClientException(
            ServiceDefinition serviceDefinition) {
        String mainTemplateName = "baseClientException";
        ST template = createTemplateGroup(mainTemplateName).getInstanceOf(mainTemplateName);
        template.add("serviceDefinition", serviceDefinition);
        String fileName = StringUtils.capitalize(serviceDefinition.getName()) + "Exception.java";
        return new HashMap<String, String>() {{
            put(getExceptionsPath(serviceDefinition) + fileName, template.render());
        }};
    }

    private Map<String, String> generateClientExceptions(
            ServiceDefinition serviceDefinition) {
        String mainTemplateName = "clientException";
        Map<String, String> generatedClasses = new HashMap<>();
        String exceptionsPath = getExceptionsPath(serviceDefinition);
        serviceDefinition.getExceptionDefinitions().forEach(exceptionDefinition -> {
            ST template = createTemplateGroup(mainTemplateName).getInstanceOf(mainTemplateName);
            template.add("exceptionDefinition", exceptionDefinition);
            generatedClasses.put(
                    exceptionsPath + exceptionDefinition.getClassName() + ".java", template.render()
            );
        });
        return generatedClasses;
    }

    private String getBasePath(ServiceDefinition serviceDefinition) {
        return serviceDefinition.getNamespaces().get("java").replaceAll("\\.", "/") + "/";
    }

    private String getExceptionsPath(ServiceDefinition serviceDefinition) {
        return getBasePath(serviceDefinition) + "exception/";
    }
}
