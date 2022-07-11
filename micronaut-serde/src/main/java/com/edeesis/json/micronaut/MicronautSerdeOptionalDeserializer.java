package com.edeesis.json.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.Decoder;
import io.micronaut.serde.Deserializer;
import io.micronaut.serde.exceptions.SerdeException;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.util.Optional;

@Singleton
public class MicronautSerdeOptionalDeserializer<T> implements Deserializer<Optional<T>> {
    @Override
    public Optional<T> deserialize(@NonNull Decoder decoder, @NonNull DecoderContext context, Argument<? super Optional<T>> type) throws IOException {
        @SuppressWarnings("unchecked") final Argument<T> generic =
                (Argument<T>) type.getFirstTypeVariable().orElse(null);
        if (generic == null) {
            throw new SerdeException("Cannot deserialize raw optional");
        }
        final Deserializer<? extends T> deserializer = context.findDeserializer(generic)
                .createSpecific(context, generic);
        if (decoder.decodeNull()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(
                    deserializer.deserialize(
                            decoder,
                            context,
                            generic
                    )
            );
        }
    }

    @Override
    public boolean allowNull() {
        return true;
    }

    @Override
    public Optional<T> getDefaultValue(@NonNull DecoderContext context, @NonNull Argument<? super Optional<T>> type) {
        //noinspection OptionalAssignedToNull
        return null;
    }
}
