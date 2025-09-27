package ru.valkeru.libdemo.config.serialization;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import ru.valkeru.libdemo.annotation.Secret;
import ru.valkeru.libdemo.serializer.SecretSerializer;

/**
 * <a href="https://stackoverflow.com/questions/56070451/mask-json-fields-using-jackson">Link</a>
 */
public class SecretIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public Object findSerializer(Annotated a) {
        return a.hasAnnotation(Secret.class) ? SecretSerializer.class : super.findSerializer(a);
    }
}
