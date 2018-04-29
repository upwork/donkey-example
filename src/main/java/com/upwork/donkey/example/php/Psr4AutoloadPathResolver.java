package com.upwork.donkey.example.php;

import com.upwork.donkey.core.ast.ServiceDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;

class Psr4AutoloadPathResolver {
    public String resolveBasePath(
            ServiceDefinition serviceDefinition,
            Map<String, Optional<String>> properties
    ) {
        String phpNamespace = serviceDefinition.getNamespaces().get("php");

        if (properties.containsKey("autoloadPrefix")) {
            String autoloadPrefix = properties.get("autoloadPrefix").orElse("");
            if (phpNamespace.startsWith(autoloadPrefix)) {
                if (phpNamespace.length() == autoloadPrefix.length()) {
                    phpNamespace = "";
                } else {
                    phpNamespace = phpNamespace.substring(autoloadPrefix.length() + 1);
                }
            }
        }

        String basePath;
        if (StringUtils.isBlank(phpNamespace)) {
            basePath = "";
        } else {
            basePath = phpNamespace.replaceAll("\\.", "/") + "/";
        }

        return basePath;
    }
}
