package com.example.galleryapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.galleryapp.ImageLoader;
import com.example.galleryapp.MainActivity;
import com.example.galleryapp.R;

import java.util.ArrayList;
import java.util.List;

public class PhotosFragment extends Fragment {

    //Get all String Data
    private ArrayList<String> _imgPath;
    private MainActivity main;
    private RecyclerView photo_recyclerview;
    private ImageDataAdapter imageDataAdapter;
    private ImageLoader imgLoader;
    private String currentLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main = (MainActivity) getActivity();

        imgLoader = new ImageLoader(main);
        _imgPath = imgLoader.get_imgPath();
        imageDataAdapter = new ImageDataAdapter(_imgPath,main);

    }

    public void updateAllImage(){
        _imgPath = imgLoader.get_imgPath();
        imageDataAdapter = new ImageDataAdapter(_imgPath,main);
        photo_recyclerview.setAdapter(imageDataAdapter);
    }

    public void updateImagePath(ArrayList<String> newImagePath){
        _imgPath = newImagePath;
        imageDataAdapter = new ImageDataAdapter(_imgPath,main);
        photo_recyclerview.setAdapter(imageDataAdapter);
    }

    public void updateImgageDataList(){
        ImageLoader imgLoader = new ImageLoader(main);
        _imgPath = imgLoader.get_imgPath();
        for (String test:
             _imgPath) {
            Log.d("check update whole album", test);
        }
        imageDataAdapter = new ImageDataAdapter(_imgPath,main);
        photo_recyclerview.setAdapter(imageDataAdapter);
        //imageDataAdapter.notifyDataSetChanged();
    }

    public void changeLayoutManager(){
        ChangePhotoLayout changePhotoLayout = new ChangePhotoLayout();
        changePhotoLayout.run();
    }

    public ImageDataAdapter getPhotoAdapter() {
        return imageDataAdapter;
    }


    private class ChangePhotoLayout extends Thread{

        @Override
        public void run() {
            //prototype LayoutManager
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(main);


            if(currentLayout.equals("Grid")){
                photo_recyclerview.setLayoutManager(linearLayoutManager);
                currentLayout = "Linear";

            }else{
                photo_recyclerview.setLayoutManager(gridLayoutManager);
                currentLayout = "Grid";
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        photo_recyclerview = view.findViewById(R.id.photo_recyclerview);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        photo_recyclerview.setLayoutManager(gridLayoutManager);
        currentLayout = "Grid";
        photo_recyclerview.setAdapter(imageDataAdapter);
        return view;
    }
}
