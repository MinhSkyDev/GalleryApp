package com.example.galleryapp.Fragment.Album;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.Fragment.PhotosFragment;
import com.example.galleryapp.MainActivity;
import com.example.galleryapp.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

//Class này sẽ làm Adapter cho từng thành phần Albụm
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private ArrayList<String> names;
    private int[] bg_names;
    private MainActivity ctx;

    public AlbumAdapter(MainActivity ctx, ArrayList<String> names, int[] bg_names)
    {
        this.names = names;
        this.bg_names = bg_names;
        this.ctx = ctx;
    }


    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerViewAlbum = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_layout_adapter,parent,false);
        return new AlbumViewHolder(recyclerViewAlbum);
    }

    private Drawable scaleImage (Drawable image, float scaleFactor) {

        if ((image == null) || !(image instanceof BitmapDrawable)) {
            return image;
        }

        Bitmap b = ((BitmapDrawable)image).getBitmap();

        int sizeX = Math.round(image.getIntrinsicWidth() * scaleFactor);
        int sizeY = Math.round(image.getIntrinsicHeight() * scaleFactor);

        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);

        image = new BitmapDrawable(ctx.getResources(), bitmapResized);

        return image;

    }

    private int getRandomIndex(){
        int index = 0;
        int size_bg = bg_names.length;
        Random dice = new Random();
        index = dice.nextInt(size_bg);
        return index;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.album_textView.setText(names.get(position));

        String albumName_curent = names.get(position);

        int randomIndex = this.getRandomIndex();
        Drawable background = ctx.getResources().getDrawable(bg_names[randomIndex]);
        background = scaleImage(background , 0.1f);
        holder.album_imgView.setImageDrawable(background);
        holder.AlbumItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chuyển sang tab photo
                ctx.switchTabToPhoto();
                //Thực hiện việc lấy tất cả các hình ảnh trong Album đó và gán lại adapter cho Fragment Photo
                PhotosFragment photosFragment = ctx.viewPagerAdapter.getPhoto_fragment();
                if(albumName_curent.equals("All Images")){
                    photosFragment.updateAllImage();
                }else{
                Set<String> album = (HashSet<String>) ctx.albumsMap.get(albumName_curent);
                ArrayList<String> fileNames = new ArrayList<String>(album);
                photosFragment.updateImagePath(fileNames);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder{

        //public ImageView album_imgView;
        public TextView album_textView;
        public RoundedImageView album_imgView;
        public View AlbumItemView;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            //album_imgView = itemView.findViewById(R.id.album_imgView);
            album_textView = itemView.findViewById(R.id.album_textView);
            album_imgView = itemView.findViewById(R.id.album_imgView);
            this.AlbumItemView = itemView;
        }
    }
}
