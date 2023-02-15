package com.example.hand2sale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hand2sale.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

class PostViewHolder extends RecyclerView.ViewHolder{
    TextView titleTv;
    TextView descriptionTv;
    TextView priceTv;
    ImageView postImage;
    List<Post> data;

    public PostViewHolder(@NonNull View itemView, PostRecyclerAdapter.OnItemClickListener listener, List<Post> data){
        super(itemView);
        this.data=data;
        titleTv = itemView.findViewById(R.id.postlistrow_title_tv);
        descriptionTv = itemView.findViewById(R.id.postlistrow_desc_tv);
        priceTv = itemView.findViewById(R.id.postlistrow_price_tv);
        postImage = itemView.findViewById(R.id.postlistrow_post_image);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                listener.onItemClick(pos);
            }
        });
    }

    public void bind(Post post, int pos){
        titleTv.setText(post.getTitle());
        descriptionTv.setText(post.getDesc());
        priceTv.setText("$"+post.getPrice());
        if(post.getImage() != null && post.getImage().length()>5){
            Picasso.get().load(post.getImage()).placeholder(R.drawable.ic_launcher_background).into(postImage);
        }else{
            postImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }
}
public class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder>{
    OnItemClickListener listener;
    public static interface OnItemClickListener{
        void onItemClick(int pos);
    }

    LayoutInflater inflater;
    List<Post> data;

    public void setData(List<Post> data){
        this.data=data;
    }

    public PostRecyclerAdapter(LayoutInflater inflater,List<Post> data){
        this.inflater=inflater;
        this.data=data;
    }

    void setOnItemClickListener(OnItemClickListener listener){this.listener=listener;}

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.post_list_row,parent,false);
        return new PostViewHolder(view, listener,data);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = data.get(position);
        holder.bind(post,position);
    }

    @Override
    public int getItemCount() {
        if(data==null) return 0;
        return data.size();
    }
}
