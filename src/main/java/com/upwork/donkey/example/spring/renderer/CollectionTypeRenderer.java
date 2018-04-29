package com.upwork.donkey.example.spring.renderer;

import com.upwork.donkey.core.BadInputException;
import com.upwork.donkey.core.ast.CollectionTypeReference;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CollectionTypeRenderer implements AttributeRenderer {

    private static final Map<String, String> javaByDonkeyTypes = new HashMap<String, String>() {{
        put("list", "java.util.List");
    }};

    @Override
    public String toString(Object o, String format, Locale locale) {
        CollectionTypeReference collectionTypeReference = (CollectionTypeReference) o;
        return String.format(
                "%s<%s>",
                renderCollectionType(collectionTypeReference.getCollectionType()),
                new SingularTypeRenderer().toString(collectionTypeReference.getElementType(), format, locale)
        );
    }

    private String renderCollectionType(String collectionType) {
        if (!javaByDonkeyTypes.containsKey(collectionType)) {
            throw new BadInputException("unknown collectionType: " + collectionType);
        }
        return javaByDonkeyTypes.get(collectionType);
    }
}
