package com.example.hand2sale.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AuthModel {

    private static final FirebaseAuth _instance = FirebaseAuth.getInstance();

    public AuthModel() {
    }

    public static FirebaseUser getCurrentUser() {
        return _instance.getCurrentUser();
    }

    public interface OnAuthDataListener {
        void onSuccess(@Nullable User user);

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
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(task);
                    listener.onEnd();
                }
            }
        });
    }

    public static void signupWithEmail(String email, String password,String username,String fullName,String phone, final OnAuthDataListener listener) {

        listener.onStart();
        _instance.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("signup result",task.getResult().getUser().toString());
                    User user = new User(
                            task.getResult().getUser().getUid(),
                            username,
                            fullName,
                            phone);
                    Model.instance().updateUser(user,Void->{
                        Log.d("user updated","User upadted in db");
                        listener.onSuccess(user);
                    });

                } else {
                    listener.onFailure(task);
                    listener.onEnd();
                }
            }
        });

    }

    public static void getUserById(String uid,OnAuthDataListener listener){
        DBModel.getDB().collection(User.COLLECTION).document(getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    User user = User.fromJson(task.getResult().getData());
                    listener.onSuccess(user);
                }else{
                    listener.onEnd();
                }
            }
        });
    }

    public static void logout() {
        _instance.signOut();
    }


}
