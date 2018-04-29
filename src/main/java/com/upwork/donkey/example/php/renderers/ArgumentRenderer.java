package com.upwork.donkey.example.php.renderers;

import com.upwork.donkey.core.ast.ArgumentDefinition;
import com.upwork.donkey.core.ast.CollectionTypeReference;
import com.upwork.donkey.core.ast.SingularTypeReference;
import com.upwork.donkey.core.ast.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

class ArgumentRenderer implements AttributeRenderer {
    private final CollectionTypeRenderer collectionTypeRenderer;
    private final SingularTypeRenderer singularTypeRenderer;
    private StringRenderer stringRenderer;

    public ArgumentRenderer(
            CollectionTypeRenderer collectionTypeRenderer,
            SingularTypeRenderer singularTypeRenderer,
            StringRenderer stringRenderer
    ) {
        this.collectionTypeRenderer = collectionTypeRenderer;
        this.singularTypeRenderer = singularTypeRenderer;
        this.stringRenderer = stringRenderer;
    }

    @Override
    public String toString(Object object, String format, Locale locale) {
        ArgumentDefinition argumentDefinition = (ArgumentDefinition) object;

        if (StringUtils.isBlank(format)) {
            format = "argument";
        }

        if (StringUtils.equals(format, "docBlock")) {
            return docBlock(argumentDefinition, locale);
        }

        if (StringUtils.equals(format, "parameter")) {
            return parameter(argumentDefinition, locale);
        }

        if (StringUtils.equals(format, "argument")) {
            return argument(argumentDefinition);
        }

        if (StringUtils.equals(format, "defaultValue")) {
            return defaultValue(argumentDefinition);
        }

        throw new IllegalArgumentException("Unsupported argument definition rendering format " + format);
    }

    private String docBlock(ArgumentDefinition argumentDefinition, Locale locale) {
        return "@param "
                + type(argumentDefinition.getType(), "docBlock" + (argumentDefinition.isPathParam() ? "" : ";optional"), locale)
                + " "
                + argument(argumentDefinition)
                + (argumentDefinition.getDoc() != null ? " " + StringUtils.trim(argumentDefinition.getDoc()) : "");
    }

    private String parameter(ArgumentDefinition argumentDefinition, Locale locale) {
        String text = type(
                argumentDefinition.getType(),
                "typeHint" + (argumentDefinition.isPathParam() ? "" : ";optional"),
                locale
        );
        if (!text.isEmpty()) {
            text += " ";
        }

        text += argument(argumentDefinition);

        return text;
    }

    private String argument(ArgumentDefinition argumentDefinition) {
        return "$" + stringRenderer.camelCase(argumentDefinition.getName());
    }

    private String defaultValue(ArgumentDefinition argumentDefinition) {
        StringBuilder builder = new StringBuilder();
        if (argumentDefinition.getType() instanceof CollectionTypeReference) {
            builder.append('[');
        }

        SingularTypeReference type;
        if (argumentDefinition.getType() instanceof SingularTypeReference) {
            type = (SingularTypeReference) argumentDefinition.getType();
        } else {
            type = ((CollectionTypeReference) argumentDefinition.getType()).getElementType();
        }

        if ("string".equals(type.getIdentifier())) {
            builder.append('"');
        }

        builder.append(argumentDefinition.getDefaultValue());

        if ("string".equals(type.getIdentifier())) {
            builder.append('"');
        }

        if (argumentDefinition.getType() instanceof CollectionTypeReference) {
            builder.append(']');
        }

        return builder.toString();
    }

    private String type(TypeReference typeReference, String format, Locale locale) {
        if (typeReference instanceof CollectionTypeReference) {
            return collectionTypeRenderer.toString(typeReference, format, locale);
        } else {
            return singularTypeRenderer.toString(typeReference, format, locale);
        }
    }
}
