package com.upwork.donkey.example.php.renderers;

import com.upwork.donkey.core.BadInputException;
import com.upwork.donkey.core.ast.CollectionTypeReference;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class CollectionTypeRenderer extends NullHandlingTypeRenderer {
    private static final Map<String, String> phpByDonkeyTypes = new HashMap<String, String>() {{
        put("list", "array");
    }};

    private SingularTypeRenderer singularTypeRenderer;

    public CollectionTypeRenderer(SingularTypeRenderer singularTypeRenderer) {
        this.singularTypeRenderer = singularTypeRenderer;
    }

    @Override
    protected String renderType(Object object, String format, Locale locale) {
        if (StringUtils.isBlank(format)) {
            format = "typeHint";
        }

        String collectionType = ((CollectionTypeReference) object).getCollectionType();
        if (!phpByDonkeyTypes.containsKey(collectionType)) {
            throw new BadInputException("unknown collectionType: " + collectionType);
        }

        if (StringUtils.equals(format, "docBlock")) {
            return singularTypeRenderer.toString(((CollectionTypeReference) object).getElementType(), format, locale) + "[]";
        }

        if (StringUtils.equals(format, "typeHint")) {
            return phpByDonkeyTypes.get(collectionType);
        }

        throw new IllegalArgumentException("Unsupported collection type rendering format " + format);
    }
}
