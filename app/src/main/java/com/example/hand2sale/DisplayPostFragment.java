package com.example.hand2sale;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.hand2sale.databinding.FragmentDisplayPostBinding;
import com.example.hand2sale.model.AuthModel;
import com.example.hand2sale.model.Model;
import com.example.hand2sale.model.Post;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DisplayPostFragment extends Fragment {
    FragmentDisplayPostBinding binding;
    PostListFragmentViewModel viewModel;
    String postId;
    Post post;
    public static DisplayPostFragment newInstance(int postId){
        DisplayPostFragment frag = new DisplayPostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID",postId);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            this.postId = bundle.getString("ID");
        }
        viewModel= new ViewModelProvider(this).get(PostListFragmentViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDisplayPostBinding.inflate(inflater,container,false);

        View view = binding.getRoot();

        postId = DisplayPostFragmentArgs.fromBundle(getArguments()).getPostId();
        FirebaseUser user = AuthModel.getCurrentUser();
        List<Post> allPosts = viewModel.getData().getValue();

        for(Post p : allPosts){
            if(p.getId()==postId){
                post=p;
            }
        }


        ImageView postImage = binding.postImg;
        TextView email = binding.displayUserEmailTv;
        TextView uploadAt = binding.displayUploadDateTv;
        TextView title = binding.displayTitleTv;
        TextView description = binding.displayDescriptionTv;
        TextView price = binding.displayPriceTv;

        email.setText(user.getEmail());
        if(post.getUploadTimestamp()!=null){
            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(post.getUploadTimestamp()));
            uploadAt.setText(date);
        }else{
            uploadAt.setText("N/A");
        }

        if(post.getImage() != null && post.getImage().length()>5){
            Picasso.get().load(post.getImage()).placeholder(R.drawable.ic_launcher_background).into(postImage);
        }else{
            postImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
        title.setText(post.getTitle());
        description.setText(post.getDesc());
        price.setText("$"+post.getPrice());


        return view;
    }
}
