package com.upwork.donkey.example.spring.renderer;

import com.upwork.donkey.core.BadInputException;
import com.upwork.donkey.core.ast.SingularTypeReference;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SingularTypeRenderer implements AttributeRenderer {

    private static final Map<String, String> javaTypeByDonkeyPrimitive = new HashMap<String, String>() {{
        put("string", "String");
        put("bool", "Boolean");
        put("i16", "Short");
        put("i32", "Integer");
        put("i64", "Long");
        put("byte", "Byte");
        put("double", "Double");
    }};

    @Override
    public String toString(Object o, String s, Locale locale) {
        SingularTypeReference typeReference = (SingularTypeReference) o;

        if (typeReference.isPrimitive()) {
            if (!javaTypeByDonkeyPrimitive.containsKey(typeReference.getIdentifier())) {
                throw new BadInputException(typeReference.getIdentifier() + " is unknown");
            }
            return javaTypeByDonkeyPrimitive.get(typeReference.getIdentifier());
        } else {
            return typeReference.getIdentifier();
        }
    }
}
