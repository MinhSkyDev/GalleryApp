package com.example.galleryapp.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.galleryapp.Fragment.Album.AlbumAdapter;
import com.example.galleryapp.Fragment.AlbumFragment;
import com.example.galleryapp.Fragment.PhotosFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    AlbumFragment album_fragment;
    PhotosFragment photo_fragment;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        album_fragment = new AlbumFragment();
        photo_fragment = new PhotosFragment();
    }


    public void changePhotoLayoutManager(){
        photo_fragment.changeLayoutManager();
    }
    



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                // để chia Album
                return album_fragment;
            default:
                // xuất hiện toàn bộ ảnh trong máy
                return photo_fragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public AlbumAdapter getAlbumAdapter(){
        return album_fragment.getAlbumAdapter();
    }

    public void updateImageDataset(){
        photo_fragment.updateImgageDataList();
    }
}
