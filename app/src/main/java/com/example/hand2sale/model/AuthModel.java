package com.example.hand2sale.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthModel {

    private static final FirebaseAuth _instance = FirebaseAuth.getInstance();

    public AuthModel() {
    }

    public static FirebaseUser getCurrentUser() {
        return _instance.getCurrentUser();
    }

    public interface OnAuthDataListener {
        void onSuccess();

        void onStart();

        void onEnd();

        void onFailure(@NonNull Task<AuthResult> task);
    }

    public static void loginWithEmail(String email, String password, final OnAuthDataListener listener) {

        listener.onStart();
        _instance.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onFailure(task);
                    listener.onEnd();
                }
            }
        });
    }

    public static void signupWithEmail(String email, String password, final OnAuthDataListener listener) {

        listener.onStart();
        _instance.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onFailure(task);
                    listener.onEnd();
                }
            }
        });

    }

    public static void logout() {
        _instance.signOut();
    }


}
