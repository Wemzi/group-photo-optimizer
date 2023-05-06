package com.cxteam.groupphotooptimizer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.cxteam.groupphotooptimizer.ml.ModelMnis;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.IOException;



class ImageClassificationHelper
{
    Context context;
    ImageClassificationHelper(Context context) {
        this.context = context;
    }

    ModelMnis model;

    // Runs model inference and gets result.
    public void classifyImage() throws IOException
    {
        //Load model
        model = ModelMnis.newInstance(context);
        // Creates inputs for reference.
        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 784}, DataType.FLOAT32);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.outWidth = 28;
        options.outHeight = 28;

        Bitmap mnist_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.mnist_seven,options);
        ModelMnis.Outputs outputs = model.process(inputFeature0);
        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
        System.out.println(outputFeature0.getBuffer());
        model.close();
    }

}