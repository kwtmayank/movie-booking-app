package com.example.moviebooking.service;

import com.example.moviebooking.exception.InvalidGoogleTokenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.webtoken.JsonWebSignature;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GoogleTokenVerifierServiceTest {

    @Test
    void verifyReturnsMappedGoogleUserInfoForValidToken() {
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload()
                .setSubject("sub-1")
                .setEmail("user@example.com")
                .set("name", "User Name")
                .set("picture", "https://pic")
                .set("email_verified", true);
        GoogleIdToken idToken = new GoogleIdToken(new JsonWebSignature.Header(), payload, new byte[0], new byte[0]);
        GoogleTokenVerifierService service = new GoogleTokenVerifierService("test-client-id");
        ReflectionTestUtils.setField(service, "verifier", new StubVerifier(idToken, null));

        GoogleUserInfo info = service.verify("any-token");

        assertThat(info.googleSub()).isEqualTo("sub-1");
        assertThat(info.email()).isEqualTo("user@example.com");
        assertThat(info.name()).isEqualTo("User Name");
        assertThat(info.pictureUrl()).isEqualTo("https://pic");
        assertThat(info.emailVerified()).isTrue();
    }

    @Test
    void verifyTreatsNonBooleanEmailVerifiedAsFalse() {
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload()
                .setSubject("sub-2")
                .setEmail("user2@example.com")
                .set("name", "User 2")
                .set("picture", "https://pic-2")
                .set("email_verified", "true");
        GoogleIdToken idToken = new GoogleIdToken(new JsonWebSignature.Header(), payload, new byte[0], new byte[0]);
        GoogleTokenVerifierService service = new GoogleTokenVerifierService("test-client-id");
        ReflectionTestUtils.setField(service, "verifier", new StubVerifier(idToken, null));

        GoogleUserInfo info = service.verify("any-token");

        assertThat(info.emailVerified()).isFalse();
    }

    @Test
    void verifyMapsBooleanFalseEmailVerifiedAsFalse() {
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload()
                .setSubject("sub-3")
                .setEmail("user3@example.com")
                .set("name", "User 3")
                .set("picture", "https://pic-3")
                .set("email_verified", false);
        GoogleIdToken idToken = new GoogleIdToken(new JsonWebSignature.Header(), payload, new byte[0], new byte[0]);
        GoogleTokenVerifierService service = new GoogleTokenVerifierService("test-client-id");
        ReflectionTestUtils.setField(service, "verifier", new StubVerifier(idToken, null));

        GoogleUserInfo info = service.verify("any-token");
        assertThat(info.emailVerified()).isFalse();
    }

    @Test
    void verifyThrowsForNullTokenFromVerifier() {
        GoogleTokenVerifierService service = new GoogleTokenVerifierService("test-client-id");
        ReflectionTestUtils.setField(service, "verifier", new StubVerifier(null, null));

        assertThatThrownBy(() -> service.verify("any-token"))
                .isInstanceOf(InvalidGoogleTokenException.class);
    }

    @Test
    void verifyWrapsVerifierFailuresAsInvalidGoogleTokenException() {
        IOException ioException = new IOException("verification failed");
        GoogleTokenVerifierService service = new GoogleTokenVerifierService("test-client-id");
        ReflectionTestUtils.setField(service, "verifier", new StubVerifier(null, ioException));

        assertThatThrownBy(() -> service.verify("any-token"))
                .isInstanceOf(InvalidGoogleTokenException.class)
                .hasMessage("Unable to verify Google token")
                .hasCause(ioException);
    }

    @Test
    void verifyWrapsRuntimeVerifierFailuresAsInvalidGoogleTokenException() {
        RuntimeException runtimeException = new IllegalArgumentException("runtime failure");
        GoogleTokenVerifierService service = new GoogleTokenVerifierService("test-client-id");
        ReflectionTestUtils.setField(service, "verifier", new StubVerifier(null, runtimeException));

        assertThatThrownBy(() -> service.verify("any-token"))
                .isInstanceOf(InvalidGoogleTokenException.class)
                .hasMessage("Unable to verify Google token")
                .hasCause(runtimeException);
    }

    @Test
    void verifyWrapsGeneralSecurityFailuresAsInvalidGoogleTokenException() {
        GeneralSecurityException securityException = new GeneralSecurityException("security failure");
        GoogleTokenVerifierService service = new GoogleTokenVerifierService("test-client-id");
        ReflectionTestUtils.setField(service, "verifier", new StubVerifier(null, securityException));

        assertThatThrownBy(() -> service.verify("any-token"))
                .isInstanceOf(InvalidGoogleTokenException.class)
                .hasMessage("Unable to verify Google token")
                .hasCause(securityException);
    }

    private static class StubVerifier extends GoogleIdTokenVerifier {
        private final GoogleIdToken idToken;
        private final Exception error;

        StubVerifier(GoogleIdToken idToken, Exception error) {
            super(new NetHttpTransport(), GsonFactory.getDefaultInstance());
            this.idToken = idToken;
            this.error = error;
        }

        @Override
        public GoogleIdToken verify(String idTokenString) throws GeneralSecurityException, IOException {
            if (error instanceof IOException ioException) {
                throw ioException;
            }
            if (error instanceof GeneralSecurityException securityException) {
                throw securityException;
            }
            if (error instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            return idToken;
        }
    }
}
