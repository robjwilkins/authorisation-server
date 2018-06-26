package com.wilkins.authorisation.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountLockedException;

@Component
public class ExceptionTranslator implements WebResponseExceptionTranslator {

    private static final String ACCESS_DENIED = "access_denied";
    private static final String ACCOUNT_LOCKED_MESSAGE = "This account is locked";

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception exception) {
        if (exception instanceof AccountLockedException) {
            OAuth2Exception oAuth2Exception = OAuth2Exception.create(ACCESS_DENIED, ACCOUNT_LOCKED_MESSAGE);
            return ResponseEntity.status(HttpStatus.LOCKED).body(oAuth2Exception);
        }
        OAuth2Exception oAuth2Exception = OAuth2Exception.create(exception.getMessage(), exception.getLocalizedMessage());
        return ResponseEntity.badRequest().body(oAuth2Exception);
    }
}
