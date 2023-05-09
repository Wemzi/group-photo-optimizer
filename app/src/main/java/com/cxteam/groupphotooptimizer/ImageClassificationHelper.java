package com.cxteam.groupphotooptimizer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import com.cxteam.groupphotooptimizer.ml.ModelMnis;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;



class ImageClassificationHelper
{
    Context context;
    ImageClassificationHelper(Context context) {
        this.context = context;
    }

    ModelMnis model;

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

    public Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(28, 28, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
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
        //Load model
        model = ModelMnis.newInstance(context);
        // Creates inputs for reference.

        // Creates inputs for reference.
        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{28,28},DataType.FLOAT32);

        Drawable mnist_drawable = context.getResources().getDrawable(id);
        Bitmap mnist_image = drawableToBitmap(mnist_drawable);
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(28 * 28 * 4 );
        mnist_image.copyPixelsToBuffer(inputBuffer);
        byte[] lol = inputBuffer.array();
        float[] converted_mnist_image_floats = convert_A888_RGB_TO_FLOATS(inputBuffer.array());
        ByteBuffer inputBuffer2  = ByteBuffer.allocateDirect(28 * 28 * 4);
        for(int i=0; i<converted_mnist_image_floats.length-1;i++)
        {
            inputBuffer2.putFloat(converted_mnist_image_floats[i]);
        }
        inputFeature0.loadBuffer(inputBuffer2);
        ModelMnis.Outputs outputs = model.process(inputFeature0);
        //convert back just for fun
        ByteBuffer afterBuffer  = ByteBuffer.allocateDirect(28 * 28 * 4);
        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
        model.close();
        return getMaximumIndex(outputFeature0.getFloatArray());
    }

}