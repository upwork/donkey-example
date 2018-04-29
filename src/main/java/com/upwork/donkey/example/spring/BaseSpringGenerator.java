package com.upwork.donkey.example.spring;

import com.upwork.donkey.core.Generator;
import com.upwork.donkey.core.ast.CollectionTypeReference;
import com.upwork.donkey.core.ast.MethodDefinition;
import com.upwork.donkey.core.ast.ResourceDefinition;
import com.upwork.donkey.core.ast.SingularTypeReference;
import com.upwork.donkey.example.spring.renderer.MethodRenderer;
import com.upwork.donkey.example.spring.renderer.ResourceRenderer;
import com.upwork.donkey.example.spring.renderer.SingularTypeRenderer;
import com.upwork.donkey.example.spring.renderer.CollectionTypeRenderer;
import org.stringtemplate.v4.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class BaseSpringGenerator implements Generator {
    private final String generator;
    private final Map<Class<?>, AttributeRenderer> renderers;

    public BaseSpringGenerator() {
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("moduleVersion.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        generator = this.getClass().getCanonicalName() + ", " + properties.getProperty("moduleVersion");

        renderers = new HashMap<Class<?>, AttributeRenderer>() {{
            put(String.class, new StringRenderer());
            put(SingularTypeReference.class, new SingularTypeRenderer());
            put(CollectionTypeReference.class, new CollectionTypeRenderer());
            put(MethodDefinition.class, new MethodRenderer());
            put(ResourceDefinition.class, new ResourceRenderer());
        }};
    }

    protected STGroup createTemplateGroup(
            String templateGroupName) {
        return new TemplateGroup("template/spring/" + templateGroupName + ".stg", renderers, generator);
    }

    private static final class TemplateGroup extends STGroupFile {
        private final String generator;

        public TemplateGroup(String templateGroupFileName, Map<Class<?>, AttributeRenderer> renderers, String generator) {
            super(templateGroupFileName);
            this.generator = generator;

            for (Map.Entry<Class<?>, AttributeRenderer> rendererEntry : renderers.entrySet()) {
                registerRenderer(rendererEntry.getKey(), rendererEntry.getValue());
            }
        }

        @Override
        public ST getInstanceOf(String name) {
            ST template = super.getInstanceOf(name);
            // This check is needed because StringTemplate throws if we add an attribute that isn't defined on the template
            // Hint: the method's name is a bit misleading because it doesn't support accessing any attribute in the template,
            // only formal arguments (the ones defined in the template's header, e.g. clientException(generator, exceptionDefinition)::= <<)
            // See: https://stackoverflow.com/questions/20363331/stringtemplate-list-of-attributes-defined-for-a-given-template#comment59373150_20366869
            if (template.getAttributes() != null && template.getAttributes().containsKey("generator")) {
                template.add("generator", generator);
            }

            return template;
        }
    }
}
