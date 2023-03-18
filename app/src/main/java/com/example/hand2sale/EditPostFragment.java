package com.example.hand2sale;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.hand2sale.databinding.FragmentEditPostBinding;
import com.example.hand2sale.model.AuthModel;
import com.example.hand2sale.model.Model;
import com.example.hand2sale.model.Post;

import java.util.List;
import java.util.UUID;

public class EditPostFragment extends Fragment {

    FragmentEditPostBinding binding;
    PostListFragmentViewModel viewModel;
    String postId;
    Post post=null;

    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isPostImageSelected=false;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            this.postId = bundle.getString("editPostId");
        }
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.addPostFragment);
                menu.removeItem(R.id.userLogout);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        },this, Lifecycle.State.RESUMED);

        viewModel = new ViewModelProvider(this).get(PostListFragmentViewModel.class);
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

        binding = FragmentEditPostBinding.inflate(inflater,container,false);

        View view = binding.getRoot();

        List<Post> list = viewModel.getData().getValue();

        for(Post p :list){
            if(p.getId()==postId){
                this.post=p;
            }
        }


      if(post!=null){
          binding.titleEt.setText(post.getTitle());
          binding.descriptionEt.setText(post.getDesc());
          binding.priceEt.setText(post.getPrice().toString());

          binding.saveBtn.setOnClickListener(view1->{
              post.setTitle(binding.titleEt.getText().toString());
              post.setDesc(binding.descriptionEt.getText().toString());
              post.setPrice(Double.parseDouble(binding.priceEt.getText().toString()));



              if(isPostImageSelected) {
                  binding.postImg.setDrawingCacheEnabled(true);
                  binding.postImg.buildDrawingCache();
                  Bitmap bitmap = ((BitmapDrawable) binding.postImg.getDrawable()).getBitmap();
                  Model.instance().uploadImage(post.getId(), bitmap, url -> {
                      if (url != null) {
                          post.setImage(url);
                      }
                      Model.instance().addPost(post, (unused) -> {
                          Navigation.findNavController(view1).popBackStack(R.id.postListFragment,false);
                      });
                  });
              }else{
                  Model.instance().addPost(post, (unused) -> {
                      Navigation.findNavController(view1).popBackStack(R.id.postListFragment,false);
                  });
              }
          });


          binding.cancelBtn.setOnClickListener(view1->Navigation.findNavController(view1).popBackStack());

          binding.deleteBtn.setOnClickListener(view1->{
              Model.instance().deletePost(post);
              Navigation.findNavController(view1).popBackStack(R.id.postListFragment,false);
          });
          binding.cameraButton.setOnClickListener(view1->{
              cameraLauncher.launch(null);
          });

          binding.galleryButton.setOnClickListener(view1->{
              galleryLauncher.launch("image/*");
          });
      }


        return view;
    }
}
