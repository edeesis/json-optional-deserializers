package com.edeesis.json.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.util.Optional;

public class JacksonOptionalDeserializer extends ReferenceTypeDeserializer<Optional<?>> {
    public JacksonOptionalDeserializer(JavaType fullType, ValueInstantiator vi, TypeDeserializer typeDeser, JsonDeserializer<?> deser) {
        super(fullType, vi, typeDeser, deser);
    }

    @Override
    protected ReferenceTypeDeserializer<Optional<?>> withResolved(TypeDeserializer typeDeser, JsonDeserializer<?> valueDeser) {
        return new JacksonOptionalDeserializer(_fullType, _valueInstantiator,
                typeDeser, valueDeser);
    }

    @Override
    public Optional<?> getNullValue(DeserializationContext ctxt) throws JsonMappingException {
        return Optional.empty();
    }

    @Override
    public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
        return null;
    }

    @Override
    public Optional<?> referenceValue(Object contents) {
        return Optional.ofNullable(contents);
    }

    @Override
    public Optional<?> updateReference(Optional<?> reference, Object contents) {
        return Optional.ofNullable(contents);
    }

    @Override
    public Object getReferenced(Optional<?> reference) {
        return reference.orElse(null);
    }
}
