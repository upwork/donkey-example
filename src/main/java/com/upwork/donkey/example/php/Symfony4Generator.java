package com.upwork.donkey.example.php;

import com.upwork.donkey.core.ast.ServiceDefinition;
import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A simple generator implementation based on StringTemplate 4 that generates a Symfony4 service
 * for the provided service definition
 */
public class Symfony4Generator extends BasePhpGenerator {
    @Override
    public Map<String, String> doGenerateSources(
            String basePath,
            ServiceDefinition serviceDefinition,
            Map<String, Optional<String>> properties
    ) {
        Map<String, String> sources = new HashMap<>();
        sources.putAll(generateResources(basePath, serviceDefinition));
        sources.putAll(generateServices(basePath, serviceDefinition));
        sources.putAll(generateExceptions(basePath, serviceDefinition));

        return sources;
    }

    private Map<String, String> generateResources(String basePath, ServiceDefinition serviceDefinition) {
        Map<String, String> generatedClasses = new HashMap<>();

        String resourcePath = basePath + "/Controller/";

        STGroup baseResourceTemplateGroup = createTemplateGroup(
                "template/symfony4-service/baseResource.stg",
                serviceDefinition
        );
        ST baseResourceTemplate = baseResourceTemplateGroup.getInstanceOf("baseResource");
        baseResourceTemplate.add("serviceDefinition", serviceDefinition);
        generatedClasses.put(
                resourcePath + StringUtils.capitalize(serviceDefinition.getName()) + "Controller.php",
                baseResourceTemplate.render()
        );

        STGroup resourceTemplateGroup = createTemplateGroup(
                "template/symfony4-service/resource.stg",
                serviceDefinition
        );
        serviceDefinition.getResourceDefinitions().forEach(resourceDefinition -> {
            ST resourceTemplate = resourceTemplateGroup.getInstanceOf("resource");
            resourceTemplate.add("resourceDefinition", resourceDefinition);
            generatedClasses.put(
                    resourcePath + StringUtils.capitalize(resourceDefinition.getName()) + "Controller.php",
                    resourceTemplate.render()
            );
        });

        return generatedClasses;
    }

    private Map<String, String> generateServices(String basePath, ServiceDefinition serviceDefinition) {
        Map<String, String> generatedClasses = new HashMap<>();

        String servicePath = basePath + "/Service/";

        STGroup serviceTemplateGroup = createTemplateGroup(
                "template/symfony4-service/service.stg",
                serviceDefinition
        );
        serviceDefinition.getResourceDefinitions().forEach(resourceDefinition -> {
            ST serviceTemplate = serviceTemplateGroup.getInstanceOf("service");
            serviceTemplate.add("resourceDefinition", resourceDefinition);
            generatedClasses.put(
                    servicePath + StringUtils.capitalize(resourceDefinition.getName()) + "Service.php",
                    serviceTemplate.render()
            );
        });

        return generatedClasses;
    }

    private Map<String, String> generateExceptions(String basePath, ServiceDefinition serviceDefinition) {
        Map<String, String> generatedClasses = new HashMap<>();

        String exceptionPath = basePath + "/Exception/";

        STGroup baseExceptionTemplateGroup = createTemplateGroup(
                "template/symfony4-service/baseServiceException.stg",
                serviceDefinition
        );
        ST baseExceptionTemplate = baseExceptionTemplateGroup.getInstanceOf("baseServiceException");
        baseExceptionTemplate.add("serviceDefinition", serviceDefinition);
        generatedClasses.put(
                exceptionPath + StringUtils.capitalize(serviceDefinition.getName()) + "Exception.php",
                baseExceptionTemplate.render()
        );

        STGroup exceptionTemplateGroup = createTemplateGroup(
                "template/symfony4-service/serviceException.stg",
                serviceDefinition
        );
        serviceDefinition.getExceptionDefinitions().forEach(exceptionDefinition -> {
            ST exceptionTemplate = exceptionTemplateGroup.getInstanceOf("serviceException");
            exceptionTemplate.add("exceptionDef", exceptionDefinition);
            generatedClasses.put(
                    exceptionPath + exceptionDefinition.getClassName() + ".php",
                    exceptionTemplate.render()
            );
        });

        return generatedClasses;
    }
}
