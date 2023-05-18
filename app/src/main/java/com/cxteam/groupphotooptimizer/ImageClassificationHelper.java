package com.cxteam.groupphotooptimizer;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.FileUtils;

import androidx.annotation.RequiresApi;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.AccessMode;
import java.util.Arrays;


class ImageClassificationHelper
{
    Context context;
    Interpreter model_interpreter;
    ImageClassificationHelper(Context context) throws IOException {
        this.context = context;
        model_interpreter = loadModelFile();
    }

    //gets the index of the max value in a float[1][n] array
    private int getMaximumIndex(float[][] array) {
        if (array.length == 0)
            throw new IllegalArgumentException("The array is empty");
        System.out.println(Arrays.deepToString(array));
        int max = 0;
        for (int i = 0; i < array[0].length; i+=1)
        {
            if (array[0][max] < array[0][i]) max = i;

        }

        return max;
    }

    private Interpreter loadModelFile() throws IOException
    {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("mnist_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        ByteBuffer mbb = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        return new Interpreter(mbb);
    }

    // Runs model inference and gets the number tipped by the model.
    public int classifyImage(int id) throws IOException
    {
        Bitmap map = BitmapFactory.decodeResource(context.getResources(), id);
        //decodeResource will resize the image depending on the screen size (78x78 in case of Pixel 3A)
        //so we need to resize it again for the input
        Bitmap scaledMap = Bitmap.createScaledBitmap(map,28,28,false);
        ByteBuffer grayscale = ByteBuffer.allocateDirect(scaledMap.getWidth()*scaledMap.getHeight()*4);
        grayscale.order(ByteOrder.nativeOrder());
        int[] pixels = new int[28 * 28];
        scaledMap.getPixels(pixels,0,scaledMap.getWidth(),0,0,scaledMap.getWidth(),scaledMap.getHeight());
        //what the hell is this
        for(int pixel : pixels)
        {
            float rChannel = (pixel >> 16) & 0xFF;
            float gChannel = (pixel >> 8) & 0xFF;
            float bChannel = (pixel) & 0xFF;
            float pixelValue = (rChannel + gChannel + bChannel) / 3 / 255.f;
            grayscale.putFloat(pixelValue);
        }
        float[][] outputs = new float[1][10];
        model_interpreter.run(grayscale,outputs);
        return getMaximumIndex(outputs);
    }

}