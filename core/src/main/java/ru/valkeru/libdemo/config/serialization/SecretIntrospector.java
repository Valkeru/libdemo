package ru.valkeru.libdemo.config.serialization;

import ru.valkeru.libdemo.annotation.Secret;
import ru.valkeru.libdemo.serializer.SecretSerializer;
import tools.jackson.core.Version;
import tools.jackson.databind.AnnotationIntrospector;
import tools.jackson.databind.cfg.MapperConfig;
import tools.jackson.databind.introspect.Annotated;

/**
 * <a href="https://stackoverflow.com/questions/56070451/mask-json-fields-using-jackson">Link</a>
 */
public class SecretIntrospector extends AnnotationIntrospector {

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public Object findSerializer(MapperConfig<?> config, Annotated a) {
        return a.hasAnnotation(Secret.class) ? SecretSerializer.class : super.findSerializer(config, a);
    }
}
