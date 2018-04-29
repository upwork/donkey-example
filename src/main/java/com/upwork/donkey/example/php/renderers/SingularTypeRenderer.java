package com.upwork.donkey.example.php.renderers;

import com.upwork.donkey.core.BadInputException;
import com.upwork.donkey.core.ast.ServiceDefinition;
import com.upwork.donkey.core.ast.SingularTypeReference;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

class SingularTypeRenderer extends NullHandlingTypeRenderer {
    private static final String CLASS_PATH_DELIMITER = "\\";

    private static final Map<String, String> phpTypeByDonkeyPrimitive = new HashMap<String, String>() {{
        put("string", "string");
        put("bool", "bool");
        put("i16", "int");
        put("i32", "int");
        put("i64", "int");
        put("byte", "int");
        put("double", "float");
    }};
    private ServiceDefinition serviceDefinition;

    private StringRenderer stringRenderer;

    public SingularTypeRenderer(ServiceDefinition serviceDefinition, StringRenderer stringRenderer) {
        this.serviceDefinition = serviceDefinition;
        this.stringRenderer = stringRenderer;
    }

    @Override
    protected String renderType(Object object, String format, Locale locale) {
        SingularTypeReference singularTypeReference = (SingularTypeReference) object;
        if (singularTypeReference.isPrimitive()) {
            return renderPrimitiveType(singularTypeReference, format);
        } else {
            return renderObjectType(singularTypeReference, format);
        }
    }

    private String renderObjectType(SingularTypeReference singularTypeReference, String format) {
        List<String> phpClassImports = serviceDefinition.getClassImports().get("php");
        if (phpClassImports != null) {
            Optional<String> foundImportedClass = serviceDefinition.getClassImports()
                    .get("php")
                    .stream()
                    .filter(importedClass -> importedClass.endsWith(singularTypeReference.getIdentifier()))
                    .findFirst();
            if (foundImportedClass.isPresent()) {
                return StringUtils.substringAfterLast(foundImportedClass.get(), ".");
            }
        }

        String phpNamespaceImport = serviceDefinition.getNamespaceImports().get("php");
        if (phpNamespaceImport == null) {
            throw new BadInputException(
                    "Namespace import not found for PHP, for class " + singularTypeReference.getIdentifier());
        }

        String classPath = StringUtils.substringAfterLast(phpNamespaceImport, ".")
                + CLASS_PATH_DELIMITER
                + singularTypeReference.getIdentifier();

        return stringRenderer.phpNamespace(classPath);
    }

    private String renderPrimitiveType(SingularTypeReference singularTypeReference, String format) {
        String phpType = phpTypeByDonkeyPrimitive.get(singularTypeReference.getIdentifier());
        if (phpType == null) {
            throw new BadInputException(singularTypeReference.getIdentifier() + " is unknown");
        }
        return phpType;
    }
}
