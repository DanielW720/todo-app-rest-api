package com.frontendmentor.todoapp.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirebaseAuthenticationUtil {

    static Logger logger = LoggerFactory.getLogger(FirebaseAuthenticationUtil.class);

    public static FirebaseToken verifyIdToken(String idToken) throws FirebaseAuthException {
        try {
            // Additional logic to check user roles, custom claims, etc., can be added here if needed
            return FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            // Token verification failed, handle the exception as needed
            logger.info("Firebase idToken verification failed: {}", e.getMessage());
            throw new FirebaseAuthException(e);
        }
    }

}
