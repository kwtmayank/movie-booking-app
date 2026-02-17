package com.example.moviebooking.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidGoogleTokenExceptionTest {

    @Test
    void constructorWithMessageSetsMessage() {
        InvalidGoogleTokenException ex = new InvalidGoogleTokenException("invalid");
        assertThat(ex.getMessage()).isEqualTo("invalid");
    }

    @Test
    void constructorWithMessageAndCauseSetsBoth() {
        RuntimeException cause = new RuntimeException("root");
        InvalidGoogleTokenException ex = new InvalidGoogleTokenException("invalid", cause);

        assertThat(ex.getMessage()).isEqualTo("invalid");
        assertThat(ex.getCause()).isSameAs(cause);
    }
}
