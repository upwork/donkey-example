package com.upwork.donkey.example.spring;

import com.upwork.donkey.core.ast.ServiceDefinition;
import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ServiceGenerator extends BaseSpringGenerator {
    @Override
    public Map<String, String> generateSources(
            ServiceDefinition serviceDefinition, Map<String, Optional<String>> properties) {
        return new HashMap<String, String>() {{
            putAll(generateResources(serviceDefinition));
            putAll(generateBaseServiceException(serviceDefinition));
            putAll(generateServiceExceptions(serviceDefinition));
            putAll(generateServices(serviceDefinition));
        }};
    }

    private Map<String, String> generateResources(
            ServiceDefinition serviceDefinition) {
        String mainTemplateName = "resource";
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

    private Map<String, String> generateBaseServiceException(
            ServiceDefinition serviceDefinition) {
        String mainTemplateName = "baseServiceException";
        ST template = createTemplateGroup(mainTemplateName).getInstanceOf(mainTemplateName);
        template.add("serviceDefinition", serviceDefinition);
        String fileName = StringUtils.capitalize(serviceDefinition.getName()) + "Exception.java";
        return new HashMap<String, String>() {{
            put(getExceptionsPath(serviceDefinition) + fileName, template.render());
        }};
    }

    private Map<String, String> generateServiceExceptions(
            ServiceDefinition serviceDefinition) {
        String mainTemplateName = "serviceException";
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

    private Map<String, String> generateServices(
            ServiceDefinition serviceDefinition) {
        String mainTemplateName = "service";
        Map<String, String> generatedClasses = new HashMap<>();
        String serviceClassesPath = getBasePath(serviceDefinition);
        serviceDefinition.getResourceDefinitions().forEach(resourceDefinition -> {
            ST template = createTemplateGroup(mainTemplateName).getInstanceOf(mainTemplateName);
            template.add("resourceDefinition", resourceDefinition);
            generatedClasses.put(
                    serviceClassesPath + resourceDefinition.getName() + "Service.java", template.render()
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
