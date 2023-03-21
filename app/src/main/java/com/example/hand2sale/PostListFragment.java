package com.example.hand2sale;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hand2sale.databinding.FragmentPostsListBinding;
import com.example.hand2sale.model.Model;
import com.example.hand2sale.model.Post;


public class PostListFragment extends Fragment {
    FragmentPostsListBinding binding;
    PostRecyclerAdapter adapter;
    PostListFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPostsListBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostRecyclerAdapter(getLayoutInflater(),viewModel.getData().getValue());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PostRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG","Row was clicked in pos: "+pos);

                Navigation.findNavController(view).navigate(PostListFragmentDirections.actionPostListToDisplay(viewModel.getData().getValue().get(pos).getId()));

            }
        });

        viewModel.getData().observe(getViewLifecycleOwner(),list->{
            adapter.setData(list);
            for(Post p: list){
                Log.d("post form view",""+p.getAuthorEmail());
            }
            adapter.notifyDataSetChanged();
        });

        Model.instance().EventPostListLoadingState.observe(getViewLifecycleOwner(),status->{
            binding.swipeRefresh.setRefreshing(status== Model.LoadingState.LOADING);
        });

        binding.swipeRefresh.setOnRefreshListener(()->{
            reloadData();
            adapter.notifyDataSetChanged();

        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel= new ViewModelProvider(this).get(PostListFragmentViewModel.class);
    }

    void reloadData(){
        Model.instance().refreshAllPosts();
    }
}
