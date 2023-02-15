package com.example.hand2sale;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

import com.example.hand2sale.databinding.FragmentAddPostBinding;
import com.example.hand2sale.model.AuthModel;
import com.example.hand2sale.model.Model;
import com.example.hand2sale.model.Post;

import java.util.UUID;

public class AddPostFragment extends Fragment {
    FragmentAddPostBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;

    Boolean isPostImageSelected=false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.addPostFragment);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        },this, Lifecycle.State.RESUMED);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if(result!=null){
                    binding.postImg.setImageBitmap(result);
                    isPostImageSelected=true;
                }
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result!=null){
                    binding.postImg.setImageURI(result);
                    isPostImageSelected=true;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAddPostBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.saveBtn.setOnClickListener(view1->{
            String title = binding.titleEt.getText().toString();
            String description = binding.descriptionEt.getText().toString();
            Double price = Double.parseDouble(binding.priceEt.getText().toString());

            Post post = new Post(UUID.randomUUID().toString(), AuthModel.getCurrentUser().getUid(),title,description,"",price);

            if(isPostImageSelected){
                binding.postImg.setDrawingCacheEnabled(true);
                binding.postImg.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.postImg.getDrawable()).getBitmap();
                Model.instance().uploadImage(post.getId(),bitmap,url->{
                    if(url!=null){
                        post.setImage(url);
                    }
                    Model.instance().addPost(post,(unused)->{
                        Navigation.findNavController(view1).popBackStack();
                    });
                });
            }
        });

        binding.cancellBtn.setOnClickListener(view1->Navigation.findNavController(view1).popBackStack());

        binding.cameraButton.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.galleryButton.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });
        return view;
    }
}
