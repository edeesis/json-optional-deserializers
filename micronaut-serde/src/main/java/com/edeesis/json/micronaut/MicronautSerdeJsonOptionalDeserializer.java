package com.edeesis.json.micronaut;

import com.edeesis.json.optional.JsonOptional;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.Decoder;
import io.micronaut.serde.Deserializer;
import io.micronaut.serde.exceptions.SerdeException;
import jakarta.inject.Singleton;

import java.io.IOException;

@Singleton
public class MicronautSerdeJsonOptionalDeserializer<T> implements Deserializer<JsonOptional<T>> {
    @Override
    public JsonOptional<T> deserialize(@NonNull Decoder decoder, @NonNull DecoderContext context, Argument<? super JsonOptional<T>> type) throws IOException {
        @SuppressWarnings("unchecked") final Argument<T> generic =
                (Argument<T>) type.getFirstTypeVariable().orElse(null);
        if (generic == null) {
            throw new SerdeException("Cannot deserialize raw JsonOptional");
        }
        final Deserializer<? extends T> deserializer = context.findDeserializer(generic)
                .createSpecific(context, generic);
        if (decoder.decodeNull()) {
            return JsonOptional.nullValue();
        } else {
            return JsonOptional.ofNullable(
                    deserializer.deserialize(
                            decoder,
                            context,
                            generic
                    )
            );
        }
    }

    @Override
    public JsonOptional<T> getDefaultValue(@NonNull DecoderContext context, @NonNull Argument<? super JsonOptional<T>> type) {
        return JsonOptional.empty();
    }
}
