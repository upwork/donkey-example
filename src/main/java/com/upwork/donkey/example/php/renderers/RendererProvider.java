package com.upwork.donkey.example.php.renderers;

import com.upwork.donkey.core.ast.ArgumentDefinition;
import com.upwork.donkey.core.ast.CollectionTypeReference;
import com.upwork.donkey.core.ast.ServiceDefinition;
import com.upwork.donkey.core.ast.SingularTypeReference;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.HashMap;
import java.util.Map;

public class RendererProvider {
    public static final Map<Class<?>, AttributeRenderer> getRenderers(ServiceDefinition serviceDefinition) {
        StringRenderer stringRenderer = new StringRenderer();
        SingularTypeRenderer singularTypeRenderer = new SingularTypeRenderer(serviceDefinition, stringRenderer);
        CollectionTypeRenderer collectionTypeRenderer = new CollectionTypeRenderer(singularTypeRenderer);
        ArgumentRenderer argumentRenderer = new ArgumentRenderer(
                collectionTypeRenderer,
                singularTypeRenderer,
                stringRenderer
        );

        Map<Class<?>, AttributeRenderer> renderers = new HashMap<>();
        renderers.put(String.class, stringRenderer);
        renderers.put(SingularTypeReference.class, singularTypeRenderer);
        renderers.put(CollectionTypeReference.class, collectionTypeRenderer);
        renderers.put(ArgumentDefinition.class, argumentRenderer);

        return renderers;
    }
}
