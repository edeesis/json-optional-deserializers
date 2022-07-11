package com.edeesis.json.objects;

import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
@Data
public class JavaObject {
    private @Nonnull  Optional<String> optional;
    private @Nullable Optional<String> defaultedOptional = Optional.empty();
    private @Nullable Optional<String> nullableOptional;

    @SuppressWarnings("OptionalAssignedToNull")
    private @Nullable Optional<String> defaultedNullableOptional = null;
    private @Nullable String nullable;

    private @Nullable String defaultNullable = null;
    private @Nonnull  String required;
}