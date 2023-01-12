package com.example.galleryapp.Fragment.EditPhoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.Fragment.PhotoDialog.BitmapProcessor;
import com.example.galleryapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class PreviewFilterImageAdapter extends RecyclerView.Adapter<PreviewFilterImageAdapter.PreviewFilterImage> {


    private ArrayList<CubeDataLoader> cubes;
    private EditPhotoActivity activity;
    private RenderScript renderScript;
    private BitmapProcessor processor;
    private String filepath;
    private Bitmap srcImage;
    public Bitmap currentChooseImage;

    public PreviewFilterImageAdapter(EditPhotoActivity editPhotoActivity, String filePath ,ArrayList<CubeDataLoader> cubes , RenderScript renderScript){
        this.cubes = cubes;
        this.activity = editPhotoActivity;
        this.renderScript = renderScript;
        this.filepath = filePath;
        this.srcImage = BitmapFactory.decodeFile(filepath);
        this.processor = new BitmapProcessor(srcImage, activity, renderScript);
        this.currentChooseImage = srcImage;
    }

    @NonNull
    @Override
    public PreviewFilterImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_photo_recyclerview_adapter,parent,false);
        return new PreviewFilterImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewFilterImage holder, int position) {

        //Get the Image
        int[] lut = cubes.get(position).getData();
        Bitmap image = processor.applyLUT(cubes.get(position));
        holder.preview_filter_imageView.setImageBitmap(image);
        holder.name_filter_textView.setText(cubes.get(position).getNameFile());
        int current_position = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentChooseImage = image;
                ImageView preview_filter_imageView = activity.findViewById(R.id.preview_imageView);
                preview_filter_imageView.setImageBitmap(image);

            }
        });

    }

    @Override
    public int getItemCount() {
        return cubes.size();
    }

    class PreviewFilterImage extends RecyclerView.ViewHolder{
        public ImageView preview_filter_imageView;
        public TextView name_filter_textView;
        public View itemView;
        public PreviewFilterImage(@NonNull View itemView) {
            super(itemView);

            preview_filter_imageView = (ImageView) itemView.findViewById(R.id.preview_filter_imageView);
            name_filter_textView = (TextView) itemView.findViewById(R.id.name_filter_textView);
            this.itemView = itemView;

        }
    }


}
