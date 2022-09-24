package com.example.galleryapp.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.galleryapp.Fragment.AlbumFragment;
import com.example.galleryapp.Fragment.PhotosFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new AlbumFragment();
            default:
                return new PhotosFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
