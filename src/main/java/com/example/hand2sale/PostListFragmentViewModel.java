package com.example.hand2sale;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hand2sale.model.DBModel;
import com.example.hand2sale.model.Model;
import com.example.hand2sale.model.Post;

import java.util.List;

public class PostListFragmentViewModel extends ViewModel {
    private LiveData<List<Post>> data = Model.instance().getAllPosts();

    LiveData<List<Post>> getData(){return data;}
}
