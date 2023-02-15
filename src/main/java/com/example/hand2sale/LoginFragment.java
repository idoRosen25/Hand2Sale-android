package com.example.hand2sale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hand2sale.databinding.FragmentLoginBinding;
import com.example.hand2sale.model.AuthModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    private Context context;
    private ProgressBar pgsBar;
    private EditText passwordEt,emailEt;
    public LoginFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=  this.getContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(pgsBar!=null){
            pgsBar.setVisibility(View.GONE);
        }
        if(emailEt!=null){
            emailEt.setText("");
        }
        if(passwordEt!=null){
            passwordEt.setText("");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        View loginView =  binding.getRoot();

        Button loginBtn = binding.loginBtn;

        emailEt = binding.loginEmailEt;
        passwordEt = binding.loginPasswordEt;

        pgsBar = binding.loginLoadSpinner;

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailEt.getText().toString().length()!=0 && passwordEt.getText().toString().length()!=0) {
                    pgsBar.setVisibility(View.VISIBLE);
                    AuthModel.loginWithEmail(emailEt.getText().toString(), passwordEt.getText().toString(), new AuthModel.OnAuthDataListener() {
                        @Override
                        public void onSuccess() {
                            context.startActivity(new Intent(context, MainActivity.class));
                        }

                        @Override
                        public void onStart() {
                            emailEt.setEnabled(false);
                            passwordEt.setEnabled(false);
                        }

                        @Override
                        public void onEnd() {
                            emailEt.setEnabled(true);
                            passwordEt.setEnabled(true);
                            pgsBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(@NonNull Task<AuthResult> task) {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(context, "Missing Email/Password", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return loginView;
    }
}
