package com.upwork.donkey.example.php;

import com.upwork.donkey.core.ast.ServiceDefinition;
import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A simple generator implementation based on StringTemplate 4 that generates a Guzzle client
 * for the provided service definition
 */
public class GuzzleGenerator extends BasePhpGenerator {
    @Override
    public Map<String, String> doGenerateSources(
            String basePath,
            ServiceDefinition serviceDefinition,
            Map<String, Optional<String>> properties
    ) {
        Map<String, String> sources = new HashMap<>();
        sources.putAll(generateClients(basePath, serviceDefinition));
        sources.putAll(generateExceptions(basePath, serviceDefinition));

        return sources;
    }

    private Map<String, String> generateClients(String basePath, ServiceDefinition serviceDefinition) {
        Map<String, String> generatedClasses = new HashMap<>();

        String clientPath = basePath + "/Client/";

        STGroup baseClientTemplateGroup = createTemplateGroup(
                "template/guzzle-client/baseClient.stg",
                serviceDefinition
        );
        ST baseClientTemplate = baseClientTemplateGroup.getInstanceOf("baseClient");
        baseClientTemplate.add("serviceDefinition", serviceDefinition);
        generatedClasses.put(
                clientPath + StringUtils.capitalize(serviceDefinition.getName()) + "Client.php",
                baseClientTemplate.render()
        );

        STGroup clientTemplateGroup = createTemplateGroup(
                "template/guzzle-client/client.stg",
                serviceDefinition
        );
        serviceDefinition.getResourceDefinitions().forEach(resourceDefinition -> {
            ST clientTemplate = clientTemplateGroup.getInstanceOf("client");
            clientTemplate.add("resourceDefinition", resourceDefinition);
            generatedClasses.put(
                    clientPath + StringUtils.capitalize(resourceDefinition.getName()) + "Client.php",
                    clientTemplate.render()
            );
        });

        return generatedClasses;
    }

    private Map<String, String> generateExceptions(String basePath, ServiceDefinition serviceDefinition) {
        Map<String, String> generatedClasses = new HashMap<>();

        String exceptionPath = basePath + "/Exception/";

        STGroup baseExceptionTemplateGroup = createTemplateGroup(
                "template/guzzle-client/baseServiceException.stg",
                serviceDefinition
        );
        ST baseExceptionTemplate = baseExceptionTemplateGroup.getInstanceOf("baseServiceException");
        baseExceptionTemplate.add("serviceDefinition", serviceDefinition);
        generatedClasses.put(
                exceptionPath + StringUtils.capitalize(serviceDefinition.getName()) + "Exception.php",
                baseExceptionTemplate.render()
        );

        STGroup exceptionTemplateGroup = createTemplateGroup(
                "template/guzzle-client/serviceException.stg",
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
