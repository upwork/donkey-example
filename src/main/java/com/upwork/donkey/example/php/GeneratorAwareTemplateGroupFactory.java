package com.upwork.donkey.example.php;

import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

class GeneratorAwareTemplateGroupFactory {
    public STGroup createTemplateGroup(String fileName, Map<Class<?>, AttributeRenderer> renderers) {
        return new GeneratorAwareSTGroup(
                fileName,
                renderers,
                this.getClass().getCanonicalName() + " v" + getGeneratorVersion()
        );
    }

    private String getGeneratorVersion() {
        try {
            Properties props = new Properties();
            props.load(this.getClass().getClassLoader().getResourceAsStream("moduleVersion.properties"));

            return props.getProperty("moduleVersion");
        } catch (IOException e) {
            return "";
        }
    }

    private static final class GeneratorAwareSTGroup extends STGroupFile {
        private final String generator;

        public GeneratorAwareSTGroup(String fileName, Map<Class<?>, AttributeRenderer> renderers, String generator) {
            super(fileName);

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
