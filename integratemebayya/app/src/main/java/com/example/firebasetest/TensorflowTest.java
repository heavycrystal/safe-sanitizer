package com.example.firebasetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.task.vision.classifier.Classifications;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;


import java.io.IOException;
import java.util.List;

public class TensorflowTest
{
    public static String infer_image(Uri filename, Context ctx)
    {

        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(200, 200, ResizeOp.ResizeMethod.BILINEAR))
                        .build();
        TensorImage tImage = new TensorImage(DataType.UINT8);
        Bitmap bitmap;

        ImageClassifier.ImageClassifierOptions options = ImageClassifier.ImageClassifierOptions.builder().setMaxResults(1).build();
        ImageClassifier imageClassifier;
        try
        {
            imageClassifier = ImageClassifier.createFromFileAndOptions(ctx, "mobilenet_v2_1.0_224_quant.tflite", options);
            bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), filename);
        }
        catch(IOException e)
        {
            Log.e("TF", "could not open tflite instance or image file.");
            return null;
        }

        tImage.load(bitmap);
        List<Classifications> results = imageClassifier.classify(tImage);
        String[] string_xplit = results.get(0).getCategories().toString().split("\"");
        return string_xplit[1];
    }
}
