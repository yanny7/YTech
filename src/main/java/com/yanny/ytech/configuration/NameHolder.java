package com.yanny.ytech.configuration;

import org.jetbrains.annotations.Nullable;

public record NameHolder(
        @Nullable String prefix,
        @Nullable String suffix
){}
