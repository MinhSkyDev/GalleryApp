package com.example.galleryapp.Fragment.EditPhoto.EditPhotoNavigation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.galleryapp.Fragment.EditPhoto.EditPhotoActivity;
import com.example.galleryapp.Fragment.PhotoDialog.BitmapProcessor;
import com.example.galleryapp.R;

import java.io.File;


public class EditPhotoRotateFragment extends Fragment {


    public Button rotate90;
    public Button flipHorizontal;
    public Button flipVertical;


    public EditPhotoActivity activity;
    public String filePath;
    public Bitmap bitmap_src;


    public EditPhotoRotateFragment(EditPhotoActivity activity, String filePath){
        this.activity = activity;
        this.filePath = filePath;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_photo_rotate,container,false);

        rotate90 = view.findViewById(R.id.rotate90);
        flipHorizontal = view.findViewById(R.id.flipHorizontal);
        flipVertical = view.findViewById(R.id.flipVertical);



        rotate90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap_src = ((BitmapDrawable)activity.preview_imageView.getDrawable()).getBitmap();
                bitmap_src = BitmapProcessor.rotateBitmap(bitmap_src,90);
                activity.currentChooseBitmap = bitmap_src;
                activity.preview_imageView.setImageBitmap(bitmap_src);

            }
        });


        flipVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap_src = ((BitmapDrawable)activity.preview_imageView.getDrawable()).getBitmap();
                bitmap_src = BitmapProcessor.createFlippedBitmap(bitmap_src,true,false);
                activity.currentChooseBitmap = bitmap_src;
                activity.preview_imageView.setImageBitmap(bitmap_src);
            }
        });

        flipHorizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap_src = ((BitmapDrawable)activity.preview_imageView.getDrawable()).getBitmap();
                bitmap_src = BitmapProcessor.createFlippedBitmap(bitmap_src,false,true);
                activity.currentChooseBitmap = bitmap_src;
                activity.preview_imageView.setImageBitmap(bitmap_src);
            }
        });

        return view;
    }
}