package com.example.galleryapp;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReadAlbums {

    MainActivity activity;
    public ReadAlbums(MainActivity activity){
        this.activity = activity;
    }

    public Map  readAlbums(){

        Map anotherMap = null;
        try {
            File file = new File(activity.getFilesDir() + "/map.ser");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            anotherMap = (Map) ois.readObject();
            ois.close();
        }catch (Exception e){
            Log.d("ReadAlbum","Failed");
        }
        return anotherMap;
    }

    public void writeAlbums(Map albums){
        try{
        File file = new File(activity.getFilesDir() + "/map.ser");
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(albums);
        oos.close();
        } catch (Exception e){
            Log.d("WriteAlbum","Failed");
        }
    }
    
    public ArrayList<String> getAllKey(Map albums){
        ArrayList<String> l = new ArrayList<String>(albums.keySet());
        l.add(0,"All Images");
        return l;
    }

}
