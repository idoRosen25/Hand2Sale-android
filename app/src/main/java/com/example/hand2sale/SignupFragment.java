package com.example.hand2sale;

import static com.example.hand2sale.model.AuthModel.getCurrentUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hand2sale.MainActivity;
import com.example.hand2sale.R;
import com.example.hand2sale.databinding.FragmentSignupBinding;
import com.example.hand2sale.model.Model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import com.example.hand2sale.model.AuthModel;
import com.example.hand2sale.model.DBModel;
import com.example.hand2sale.model.User;

public class SignupFragment extends Fragment {
    FragmentSignupBinding binding;

    private Context context;
    private ProgressBar pgsBar;


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater,container,false);
        View signupView = binding.getRoot();


        EditText emailEt = signupView.findViewById(R.id.signup_email_et);
        EditText passwordEt = signupView.findViewById(R.id.signup_password_et);
        EditText fullNameEt = signupView.findViewById(R.id.signup_full_name_et);
        EditText usernameEt = signupView.findViewById(R.id.signup_username_et);
        EditText phoneEt = signupView.findViewById(R.id.signup_phone_et);

        pgsBar = signupView.findViewById(R.id.signup_load_spinner);

        signupView.findViewById(R.id.signup_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pgsBar.setVisibility(View.VISIBLE);
                if (emailEt.getText().toString().length() != 0 && passwordEt.getText().toString().length() != 0 && fullNameEt.getText().toString().length() != 0 && usernameEt.getText().toString().length() != 0 && phoneEt.getText().toString().length() != 0) {
                    AuthModel.signupWithEmail(
                            emailEt.getText().toString(),
                            passwordEt.getText().toString(),
                            usernameEt.getText().toString(),
                            fullNameEt.getText().toString(),
                            phoneEt.getText().toString(),
                            new AuthModel.OnAuthDataListener() {
                        @Override
                        public void onSuccess(@Nullable User user) {
                            Log.d("signupSuccess", "user data added successfully");
                            Log.d("user from signup",user.toString());
                            context.startActivity(new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }

                        @Override
                        public void onStart() {
                            emailEt.setEnabled(false);
                            passwordEt.setEnabled(false);
                            fullNameEt.setEnabled(false);
                            usernameEt.setEnabled(false);
                            phoneEt.setEnabled(false);
                        }

                        @Override
                        public void onEnd() {
                            emailEt.setEnabled(true);
                            passwordEt.setEnabled(true);
                            fullNameEt.setEnabled(true);
                            usernameEt.setEnabled(true);
                            phoneEt.setEnabled(true);
                            pgsBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(@NonNull Task<AuthResult> task) {
                            pgsBar.setVisibility(View.GONE);
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    pgsBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Missing fields. Please complete the form", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return signupView;
    }
}