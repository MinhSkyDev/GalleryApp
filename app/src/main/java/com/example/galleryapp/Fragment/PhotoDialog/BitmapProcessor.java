package com.example.galleryapp.Fragment.PhotoDialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsic3DLUT;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.Type;
import android.util.Log;

import com.example.galleryapp.Fragment.EditPhoto.CubeDataLoader;

import java.io.File;
import java.util.Arrays;

public class BitmapProcessor {

    private Bitmap bmp;
    private Context ctx;
    private RenderScript renderScript;
    ScriptIntrinsic3DLUT mScriptlut;

    public static final double PI = 3.14159d;
    public static final double FULL_CIRCLE_DEGREE = 360d;
    public static final double HALF_CIRCLE_DEGREE = 180d;
    public static final double RANGE = 256d;


    public BitmapProcessor(){
        //Do Nothing
    }

    //Co hai cach dung class nay
    public BitmapProcessor(Bitmap input, Context ctx, RenderScript renderScript){
        this.bmp = input;
        this.ctx = ctx;
        this.renderScript = renderScript;
        this.mScriptlut = ScriptIntrinsic3DLUT.create(renderScript, Element.U8_4(renderScript));
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public Bitmap blur(float r){
        //Radius range (0 < r <= 25)
        if(r <= 0){
            r = 0.1f;
        }else if(r > 25){
            r = 25.0f;
        }

        Bitmap bmp_rescale = Bitmap.createScaledBitmap(bmp,
                (int) (bmp.getWidth() *0.05) ,
                (int) (bmp.getHeight() *0.05), true
                );

        Bitmap bitmap = Bitmap.createBitmap(
                bmp_rescale.getWidth(), bmp_rescale.getHeight(),
                Bitmap.Config.ARGB_8888);


        Allocation blurInput = Allocation.createFromBitmap(renderScript, bmp_rescale);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(r);
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        //renderScript.destroy();

        return bitmap;
    }

    public Bitmap blur(float r, float scale){
        //Radius range (0 < r <= 25)
        if(r <= 0){
            r = 0.1f;
        }else if(r > 25){
            r = 25.0f;
        }

        Bitmap bmp_rescale = Bitmap.createScaledBitmap(bmp,
                (int) (bmp.getWidth() *scale) ,
                (int) (bmp.getHeight() *scale), true
        );

        Bitmap bitmap = Bitmap.createBitmap(
                bmp_rescale.getWidth(), bmp_rescale.getHeight(),
                Bitmap.Config.ARGB_8888);


        Allocation blurInput = Allocation.createFromBitmap(renderScript, bmp_rescale);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(r);
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        //renderScript.destroy();

        return bitmap;
    }


    public Bitmap applyLUT(CubeDataLoader loader){
        Bitmap mBitmap = null;
        Bitmap mLutBitmap = null;
        Bitmap mOutputBitmap = null;
        Allocation mAllocIn = null;
        Allocation mAllocOut = null;
        Allocation mAllocCube;

        int redDim, greenDim, blueDim;

        if (mBitmap == null) {
            mBitmap = bmp;

            mBitmap = Bitmap.createScaledBitmap(mBitmap,
                    (int) (mBitmap.getWidth() *0.5) ,
                    (int) (mBitmap.getHeight() *0.5), true
            );

            mOutputBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap.getConfig());

            mAllocIn = Allocation.createFromBitmap(renderScript, mBitmap);
            mAllocOut = Allocation.createFromBitmap(renderScript, mOutputBitmap);
        }


        int size = loader.getSize();
        int data_size = loader.getData_size();
        int[] lut = Arrays.copyOf(loader.getData(),data_size);

        redDim = greenDim = blueDim = size;

        Type.Builder tb = new Type.Builder(renderScript, Element.U8_4(renderScript));
        tb.setX(redDim).setY(greenDim).setZ(blueDim);
        Type t = tb.create();
        mAllocCube = Allocation.createTyped(renderScript, t);
        mAllocCube.copyFromUnchecked(lut);
        mScriptlut.setLUT(mAllocCube);
        mScriptlut.forEach(mAllocIn, mAllocOut);

        mAllocOut.copyTo(mOutputBitmap);

        return mOutputBitmap;
    }

    public static Bitmap changeBrightness(Bitmap src, int value){
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // increase/decrease each channel
                R += value;
                if(R > 255) { R = 255; }
                else if(R < 0) { R = 0; }

                G += value;
                if(G > 255) { G = 255; }
                else if(G < 0) { G = 0; }

                B += value;
                if(B > 255) { B = 255; }
                else if(B < 0) { B = 0; }

                // apply new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

    public static Bitmap tintImage(Bitmap src, int degree) {

        int width = src.getWidth();
        int height = src.getHeight();

        int[] pix = new int[width * height];
        src.getPixels(pix, 0, width, 0, 0, width, height);

        int RY, GY, BY, RYY, GYY, BYY, R, G, B, Y;
        double angle = (PI * (double)degree) / HALF_CIRCLE_DEGREE;

        int S = (int)(RANGE * Math.sin(angle));
        int C = (int)(RANGE * Math.cos(angle));

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                int r = ( pix[index] >> 16 ) & 0xff;
                int g = ( pix[index] >> 8 ) & 0xff;
                int b = pix[index] & 0xff;
                RY = ( 70 * r - 59 * g - 11 * b ) / 100;
                GY = (-30 * r + 41 * g - 11 * b ) / 100;
                BY = (-30 * r - 59 * g + 89 * b ) / 100;
                Y  = ( 30 * r + 59 * g + 11 * b ) / 100;
                RYY = ( S * BY + C * RY ) / 256;
                BYY = ( C * BY - S * RY ) / 256;
                GYY = (-51 * RYY - 19 * BYY ) / 100;
                R = Y + RYY;
                R = ( R < 0 ) ? 0 : (( R > 255 ) ? 255 : R );
                G = Y + GYY;
                G = ( G < 0 ) ? 0 : (( G > 255 ) ? 255 : G );
                B = Y + BYY;
                B = ( B < 0 ) ? 0 : (( B > 255 ) ? 255 : B );
                pix[index] = 0xff000000 | (R << 16) | (G << 8 ) | B;
            }

        Bitmap outBitmap = Bitmap.createBitmap(width, height, src.getConfig());
        outBitmap.setPixels(pix, 0, width, 0, 0, width, height);

        pix = null;

        return outBitmap;
    }

    public static Bitmap boost(Bitmap src, int type, float percent) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        int A, R, G, B;
        int pixel;

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                if(type == 1) {
                    R = (int)(R * (1 + percent));
                    if(R > 255) R = 255;
                }
                else if(type == 2) {
                    G = (int)(G * (1 + percent));
                    if(G > 255) G = 255;
                }
                else if(type == 3) {
                    B = (int)(B * (1 + percent));
                    if(B > 255) B = 255;
                }
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }


    public static Bitmap rotateBitmap(Bitmap original, float degrees) {
        int x = original.getWidth();
        int y = original.getHeight();
        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);
        Bitmap rotatedBitmap = Bitmap.createBitmap(original , 0, 0, original .getWidth(), original .getHeight(), matrix, true);
        return rotatedBitmap;
    }

    public static Bitmap createFlippedBitmap(Bitmap source, boolean xFlip, boolean yFlip) {
        Matrix matrix = new Matrix();
        matrix.postScale(xFlip ? -1 : 1, yFlip ? -1 : 1, source.getWidth() / 2f, source.getHeight() / 2f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


}
