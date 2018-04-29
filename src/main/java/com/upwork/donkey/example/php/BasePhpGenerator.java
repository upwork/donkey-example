package com.upwork.donkey.example.php;

import com.upwork.donkey.core.Generator;
import com.upwork.donkey.core.ast.ServiceDefinition;
import com.upwork.donkey.example.php.renderers.RendererProvider;
import org.stringtemplate.v4.STGroup;

import java.util.Map;
import java.util.Optional;

abstract public class BasePhpGenerator implements Generator {
    private GeneratorAwareTemplateGroupFactory templateGroupFactory = new GeneratorAwareTemplateGroupFactory();

    private Psr4AutoloadPathResolver pathResolver = new Psr4AutoloadPathResolver();

    @Override
    public Map<String, String> generateSources(
            ServiceDefinition serviceDefinition,
            Map<String, Optional<String>> properties
    ) {
        return doGenerateSources(
                pathResolver.resolveBasePath(serviceDefinition, properties),
                serviceDefinition,
                properties
        );
    }

    abstract protected Map<String, String> doGenerateSources(
            String basePath,
            ServiceDefinition serviceDefinition,
            Map<String, Optional<String>> properties
    );

    protected STGroup createTemplateGroup(String fileName, ServiceDefinition serviceDefinition) {
        return templateGroupFactory.createTemplateGroup(fileName, RendererProvider.getRenderers(serviceDefinition));
    }
}
