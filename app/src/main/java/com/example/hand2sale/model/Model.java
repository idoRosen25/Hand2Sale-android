package com.example.hand2sale.model;

import android.graphics.Bitmap;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.firestore.FieldValue;


public class Model {
    private static final Model _instance = new Model();

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler= HandlerCompat.createAsync(Looper.getMainLooper());
    private final DBModel dbModel = DBModel.instance();
    AppLocalDbRepository localDb = AppLocalDb.getAppDb();
    public static Model instance(){return _instance;}
    private Model(){}

    public interface Listener<T>{
        void onComplete(T data);
    }

    public enum LoadingState{
        LOADING,
        NOT_LOADING
    }
    final public MutableLiveData<LoadingState> EventPostListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);

    private LiveData<List<Post>> postsList;
    public LiveData<List<Post>> getAllPosts(){
        if(postsList==null){
            postsList=localDb.postDao().getAll();
            refreshAllPosts();

        }
        return postsList;
    }


    public Post getPostById(String postId){
        return localDb.postDao().getPostById(postId);
    }

    public void deletePost(Post post){
        EventPostListLoadingState.postValue(LoadingState.LOADING);
        dbModel.deletePostById(post,Void->{

               localDb.postDao().delete(post);
            EventPostListLoadingState.postValue(LoadingState.NOT_LOADING);

        });
    }

    public void refreshAllPosts(){
        EventPostListLoadingState.setValue(LoadingState.LOADING);
        Long localLastUpdate = Post.getLocalLastUpdate();

        dbModel.getAllPostsSince(localLastUpdate,list->{
            executor.execute(()->{
                Log.d("TAG","firebase return: "+list.size());
                Long time = localLastUpdate;
                for(Post post:list){
                    localDb.postDao().insertAll(post);
                    if(post!=null && time<post.getLastUpdated()){
                        time=post.getLastUpdated();
                    }
                }

                try{
                    Thread.sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }


                Post.setLocalLastUpdate(time);
                EventPostListLoadingState.postValue(LoadingState.NOT_LOADING);
            });
        });
    }

    public void addPost(Post post,Listener<Void> listener){
        dbModel.addPost(post,(Void)->{
            refreshAllPosts();
            listener.onComplete(null);
        });
    }

    public void updateUser(User user,Listener<Void> listener){
        dbModel.updateUser(user,(Void)->{
            listener.onComplete(null);
        });
    }

    public void uploadImage(String name, Bitmap bitmap,Listener<String> listener){
        dbModel.uploadImage(name,bitmap,listener);
    }
}
