package com.example.hand2sale;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hand2sale.databinding.FragmentUserProfileBinding;
import com.example.hand2sale.model.AuthModel;
import com.example.hand2sale.model.Post;
import com.example.hand2sale.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.LinkedList;
import java.util.List;

public class UserProfileFragment extends Fragment {
    FragmentUserProfileBinding binding;
    PostRecyclerAdapter adapter;
    PostListFragmentViewModel viewModel;
    User currentUser;
    List<Post> postList = new LinkedList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.userProfileFragment);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, this, Lifecycle.State.RESUMED);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        AuthModel.getUserById(AuthModel.getCurrentUser().getUid(), new AuthModel.OnAuthDataListener() {
            @Override
            public void onSuccess(@Nullable User user) {
                Log.d("user in on success",user.toString());
                currentUser = user;
                binding.profileFullnameTv.setText(user.getFullName());
                binding.profileUsernameTv.setText(user.getUsername());
                binding.profilePhoneTv.setText(user.getPhone());
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onEnd() {

            }

            @Override
            public void onFailure(@NonNull Task<AuthResult> task) {

            }
        });
        binding.userRecyclerView.setHasFixedSize(true);
        binding.userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        for(Post p:viewModel.getData().getValue()){

            if(p.getAuthorEmail() !=null && p.getAuthorEmail().compareTo(AuthModel.getCurrentUser().getEmail())==0){
                postList.add(p);
            }

        }
        adapter = new PostRecyclerAdapter(getLayoutInflater(),postList);
        binding.userRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PostRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Navigation.findNavController(view).navigate(UserProfileFragmentDirections.actionDisplayToEdit(postList.get(pos).getId()));
            }
        });


        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostListFragmentViewModel.class);
    }
}
