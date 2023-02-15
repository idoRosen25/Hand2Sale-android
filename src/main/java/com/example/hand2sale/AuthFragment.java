package com.example.hand2sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.hand2sale.databinding.FragmentAuthBinding;

public class AuthFragment extends Fragment {
    FragmentAuthBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.authLoginBtn.setOnClickListener(
                Navigation.createNavigateOnClickListener(
                        AuthFragmentDirections.actionAuthFragmentToLoginFragment()
                ));

        binding.authSignupBtn.setOnClickListener(
                Navigation.createNavigateOnClickListener(
                        AuthFragmentDirections.actionAuthFragmentToSignupFragment()
                ));

        return view;
    }
}
