package com.example.galleryapp.Fragment.PhotoDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.galleryapp.Fragment.EditPhoto.EditPhotoActivity;
import com.example.galleryapp.MainActivity;
import com.example.galleryapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


//Class này là một class kế thừa DialogFragment nên nhận layout là photo_dialog_layout

public class PhotoDialog extends AppCompatDialogFragment {

    private MainActivity activity;
    private List<String> img_path;
    private int currentPosition;
    private PhotoItemAdapter photoItemAdapter;
    private ViewPager2 viewPager;
    private AlertDialog.Builder builder;
    private View view;

    private ChipNavigationBar navigation_bar_photo_picker;


    private final Integer editPhoto_requestCode = 123;

    //Biến này tự gọi chính nó, dùng cho setEvent cho nút hủy chính nó ở imageView closePhotoDialog_imageView
    private PhotoDialog thisPhotoDialog = this;

    public PhotoDialog(MainActivity activity, List<String> img_path){
        this.activity = activity;
        this.img_path = img_path;
        this.currentPosition = 0;
        photoItemAdapter = new PhotoItemAdapter(img_path,activity);
        builder = new AlertDialog.Builder(activity);

        //Lấy view đại diện cho giao diện
        view = activity.getLayoutInflater().inflate(R.layout.photo_dialog_layout,null);
        builder.setView(view);
        viewPager = view.findViewById(R.id.photo_viewPager);

        navigation_bar_photo_picker = (ChipNavigationBar) view.findViewById(R.id.navigation_bar_photo_picker);
    }



    //Setter Getter for currentPosition
    public void setCurrentPosition(int position){
        this.currentPosition = position;
        viewPager.setCurrentItem(position);

    }

    public int getCurrentPosition(){
        return this.currentPosition;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = builder.create();
        viewPager.setAdapter(photoItemAdapter);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1f - Math.abs(position);
                page.setScaleX(0.90f + r*0.0005f);
            }
        });

        //Thiết kế nút dạng ImageView để tắt Dialog
        ImageView closePhotoDialog_imageView = view.findViewById(R.id.closePhotoDialog_imageView);
        closePhotoDialog_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisPhotoDialog.dismiss();
            }
        });


        viewPager.setPageTransformer(compositePageTransformer);
        viewPager.setCurrentItem(this.currentPosition);

        setupNavigationBar();

        return dialog;
    }


    private void setupNavigationBar(){
        navigation_bar_photo_picker.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch(i){
                    case R.id.editPhoto:
                        onNavigation_edit();

                        break;
                    case R.id.addToAlbum:
                        onNavigation_addToAlbum();
                        break;
                    case R.id.sharePhoto:
                        onNavigation_share();
                        break;
                    case R.id.favouritePhoto:
                        onNavigation_favourite();
                        break;
                }
            }
        });
    }

    private void onNavigation_edit(){
        int currentItem = viewPager.getCurrentItem();
        String currentFilepath = img_path.get(currentItem);

        Bundle bundle = new Bundle();
        bundle.putString("filepath", currentFilepath);
        Intent intent = new Intent(activity, EditPhotoActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, editPhoto_requestCode);
    }

    private void onNavigation_addToAlbum(){

        // Hiển thị bottomsheet popup
        int currentViewPagerPosition = viewPager.getCurrentItem();
        BottomSheetDialog addAlbumDialong = new BottomSheetDialog(activity,R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(activity).inflate(R.layout.choose_album_layout,
                null
                );

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item,activity.albumNames);
        Spinner spinner = (Spinner) bottomSheetView.findViewById(R.id.chooseAlbumSpinner);
        spinner.setAdapter(spinnerArrayAdapter);
        Button confirmButton = (Button) bottomSheetView.findViewById(R.id.chooseAlbumConfirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedAlbum = spinner.getSelectedItem().toString();
                if(selectedAlbum.equals("All Images")){
                    Toast.makeText(activity, "The image is already in All Images", Toast.LENGTH_SHORT).show();
                }
                else{

                    //Lấy set từ trong dictionary ra và add ảnh vô
                    Set<String> currentAlbumSet = (HashSet<String>) activity.albumsMap.get(selectedAlbum);
                    currentAlbumSet.add(img_path.get(currentViewPagerPosition));
                    Toast.makeText(activity, "Image added to "+selectedAlbum, Toast.LENGTH_SHORT).show();
                }
                addAlbumDialong.dismiss();
            }
        });
        addAlbumDialong.setContentView(bottomSheetView);
        addAlbumDialong.show();
    }

    private void onNavigation_share(){

    }

    private void onNavigation_favourite(){

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
