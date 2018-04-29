package com.upwork.donkey.example.php.renderers;

import java.util.Locale;

class StringRenderer extends org.stringtemplate.v4.StringRenderer {
    @Override
    public String toString(Object object, String formatString, Locale locale) {
        if (formatString != null) {
            if (formatString.equals("phpNamespace")) {
                return phpNamespace((String) object);
            }
            if (formatString.equals("phpPathTemplate")) {
                return phpPathTemplate((String) object);
            }
            if (formatString.equals("fullCap")) {
                return fullCap((String) object);
            }
            if (formatString.equals("camelCase")) {
                return camelCase((String) object);
            }
        }
        return super.toString(object, formatString, locale);
    }

    String phpNamespace(String namespace) {
        return namespace.replaceAll("\\.", "\\\\");
    }

    private String phpPathTemplate(String clientPath) {
        for (String urlPart : clientPath.split("/")) {
            if (urlPart.startsWith("{")) {
                clientPath = clientPath.replace(urlPart, "%s");
            }
        }
        return clientPath;
    }

    private String fullCap(String text) {
        return text.toUpperCase();
    }

    String camelCase(String identifier) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = identifier.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '-') {
                //skipping all consequence dashes
                while (c == '-' && i < chars.length - 1) {
                    c = chars[++i];
                }
                //ignoring ending dash
                if (c != '-') {
                    stringBuilder.append(Character.toUpperCase(c));
                }
            } else {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
}