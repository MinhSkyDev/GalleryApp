package com.example.galleryapp.Fragment.EditPhoto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.galleryapp.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EditPhotoActivity extends AppCompatActivity {


    public ImageView preview_imageView;
    public String filepath;
    public EditPhotoActivity ctx = this;
    PreviewFilterImageAdapter adapter;
    public ViewPager editPhoto_viewPager;
    public ChipNavigationBar navigationBar;
    public EditPhotoViewPagerAdapter editPhoto_viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        filepath = bundle.getString("filepath");
        preview_imageView = (ImageView) findViewById(R.id.preview_imageView);

        File file = new File(filepath);
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        preview_imageView.setImageBitmap(myBitmap);


        preview_imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                preview_imageView.setImageBitmap(myBitmap);
                return true;
            }
        });

        ImageView tick_ok_button = (ImageView) findViewById(R.id.tick_ok_button);
        tick_ok_button.bringToFront();
        tick_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ctx.finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1010){
            Uri uri = data.getData();
            String log_str = uri.toString();
            Log.d("GetFile CUBE", log_str);
            editPhoto_viewPagerAdapter.addNewCube(uri);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        editPhoto_viewPagerAdapter = new EditPhotoViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        editPhoto_viewPagerAdapter.setFilePathAndActivity(filepath,ctx);

        editPhoto_viewPager = (ViewPager) findViewById(R.id.editPhoto_viewPager);
        editPhoto_viewPager.setAdapter(editPhoto_viewPagerAdapter);

        navigationBar = (ChipNavigationBar) findViewById(R.id.editPhoto_navigation);

        editPhoto_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        navigationBar.setItemSelected(R.id.action_cube,true);
                        break;
                    case 1:
                        navigationBar.setItemSelected(R.id.action_correction,true);
                        break;
                    case 2:
                        navigationBar.setItemSelected(R.id.action_rotate,true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch(i){
                    case R.id.action_cube:
                        editPhoto_viewPager.setCurrentItem(0);
                        break;
                    case R.id.action_correction:
                        editPhoto_viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_rotate:
                        editPhoto_viewPager.setCurrentItem(2);
                        break;
                }
            }
        });

}}