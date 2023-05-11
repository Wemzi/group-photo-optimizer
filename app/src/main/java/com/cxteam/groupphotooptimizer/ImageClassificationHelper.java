package com.cxteam.groupphotooptimizer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import com.cxteam.groupphotooptimizer.ml.ModelMnis;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;



class ImageClassificationHelper
{
    private static final float[] zero = new float[] {
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    };

    Context context;
    ImageClassificationHelper(Context context)
    {
        this.context = context;
    }

    //gets the index of the max value in a float array
    private int getMaximumIndex(float[] array) {
        if (array.length <= 0)
            throw new IllegalArgumentException("The array is empty");
        int max = 0;
        for (int i = 0; i < array.length; i+=1)
        {
            if (array[max] < array[i]) max = i;
        }

        return max;
    }

    public float[] convert_A888_RGB_TO_FLOATS(byte[] input)
    {
        float[] output = new float[input.length/4];
        int j=0;
        for(int i=0; j<output.length-1; i+=4)
        {
            output[j++] = input[i] - (-127) / 255;
        }
        return output;
    }

    // Runs model inference and gets the number tipped by the model.
    public int classifyImage(int id) throws IOException
    {
        ModelMnis model = ModelMnis.newInstance(context);

        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{28,28},DataType.FLOAT32);


        Bitmap map = BitmapFactory.decodeResource(context.getResources(), R.drawable.mnist_four);

        float[] grayscale = new float[28*28];
        for (int x = 0; x < 28; x++)
        {
            for (int y = 0; y < 28; y++)
            {
                if(x < map.getWidth() && y < map.getHeight())
                {
                    int c = map.getPixel(x,y);
                    grayscale[y * 28 + x] = ((Color.red(c) + Color.blue(c) + Color.green(c)) / 3) / 256F;

                }
                else
                {
                    grayscale[y * 28 + x] = 0;
                }
            }
        }
        inputFeature0.loadArray(grayscale);

        ModelMnis.Outputs outputs = model.process(inputFeature0);

        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
        model.close();

        return getMaximumIndex(outputFeature0.getFloatArray());
    }

}