package com.upwork.donkey.example.spring.renderer;

import com.upwork.donkey.core.ast.ResourceDefinition;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

public class ResourceRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String format, Locale locale) {
        if ("doc".equals(format)) {
            ResourceDefinition resourceDefinition = (ResourceDefinition) o;
            if (resourceDefinition.getDoc() == null || resourceDefinition.getDoc().isEmpty()) {
                return "";
            } else {
                return new JavaDocBuilder()
                        .addDescriptionLines(resourceDefinition.getDoc()).build();
            }
        }
        throw new IllegalArgumentException(format + " is not supported by " + this.getClass().getSimpleName());
    }
}