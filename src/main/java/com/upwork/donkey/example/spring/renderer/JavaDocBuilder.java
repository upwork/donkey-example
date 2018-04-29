package com.upwork.donkey.example.spring.renderer;

import java.util.*;

public class JavaDocBuilder {

    private final StringJoiner docJoiner = new StringJoiner("\n *", "/**\n *", "\n */\n").setEmptyValue("");
    private final StringJoiner descriptionJoiner = new StringJoiner("<br/>\n *");
    private final List<String> descriptionLines = new ArrayList<>();
    private final Map<String, String> params = new LinkedHashMap<>();
    private String returnDoc;
    private final Map<String, String> exceptions = new LinkedHashMap<>();

    JavaDocBuilder addDescriptionLines(List<String> lines) {
        descriptionLines.addAll(lines);
        return this;
    }

    JavaDocBuilder addReturn(String returnDoc) {
        this.returnDoc = returnDoc;
        return this;
    }

    JavaDocBuilder addArg(String argName, String argDoc) {
        params.put(argName, argDoc);
        return this;
    }

    JavaDocBuilder addException(String exceptionName, String exceptionDoc) {
        exceptions.put(exceptionName, exceptionDoc);
        return this;
    }

    String build() {
        if (!descriptionLines.isEmpty()) {
            descriptionLines.forEach(descriptionJoiner::add);
            docJoiner.add(descriptionJoiner.toString());
        }
        for (Map.Entry<String, String> param : params.entrySet()) {
            String value = param.getValue();
            value = value.startsWith(" ") ? value : ' ' + value;
            docJoiner.add(" @param " + param.getKey() + value);
        }
        if (returnDoc != null) {
            docJoiner.add(" @return" + (returnDoc.startsWith(" ") ? returnDoc : ' ' + returnDoc));
        }
        for (Map.Entry<String, String> exceptionDoc : exceptions.entrySet()) {
            docJoiner.add(" @throws " + exceptionDoc.getKey() + ' ' + exceptionDoc.getValue());
        }

        return docJoiner.toString();
    }
}
