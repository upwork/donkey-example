package com.upwork.donkey.example.spring.renderer;

import com.upwork.donkey.core.ast.MethodDefinition;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

public class MethodRenderer implements AttributeRenderer {

    private static final String SERVICE_DOC = "serviceDoc";
    private static final String CLIENT_DOC = "clientDoc";

    @Override
    public String toString(Object o, String format, Locale locale) {
        MethodDefinition methodDefinition = (MethodDefinition) o;

        if (!SERVICE_DOC.equals(format) && !CLIENT_DOC.equals(format)) {
            throw new IllegalArgumentException(format + " is not supported by " + this.getClass().getSimpleName());
        }

        JavaDocBuilder javaDocBuilder = new JavaDocBuilder();
        if (methodDefinition.getDoc() != null) {
            javaDocBuilder.addDescriptionLines(methodDefinition.getDoc());
        }
        methodDefinition.getArgumentDefinitions().stream()
                .filter(argument -> argument.getDoc() != null)
                .forEach(argument -> javaDocBuilder.addArg(argument.getName(), argument.getDoc()));
        if (methodDefinition.getReturnDoc() != null) {
            javaDocBuilder.addReturn(methodDefinition.getReturnDoc());
        }
        if (CLIENT_DOC.equals(format)) {
            for (String exceptionName : methodDefinition.getThrowableExceptions()) {
                javaDocBuilder.addException(exceptionName, "if server responds with this donkey-declared exception");
            }
            javaDocBuilder.addException("RestClientException", "if communication exception happens");
        }

        return javaDocBuilder.build();
    }
}