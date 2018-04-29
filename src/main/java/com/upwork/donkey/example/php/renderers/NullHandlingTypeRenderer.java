package com.upwork.donkey.example.php.renderers;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

abstract class NullHandlingTypeRenderer implements AttributeRenderer {
    @Override
    public String toString(Object object, String format, Locale locale) {
        List<String> formatOptions = Splitter.on(";").trimResults().splitToList(format);
        boolean optionalType = false;
        if (formatOptions.contains("optional")) {
            optionalType = true;
            // Splitter returns a read-only collection
            formatOptions = new ArrayList<>(formatOptions);
            formatOptions.remove("optional");
        }

        format = Joiner.on(";").join(formatOptions);
        String renderedType = renderType(object, format, locale);

        if (StringUtils.equals(format, "docBlock")) {
            return renderedType + (optionalType ? "|null" : "");
        }

        if (StringUtils.equals(format, "typeHint")) {
            return (optionalType ? "?" : "") + renderedType;
        }

        return renderedType;
    }

    abstract protected String renderType(Object object, String format, Locale locale);
}
