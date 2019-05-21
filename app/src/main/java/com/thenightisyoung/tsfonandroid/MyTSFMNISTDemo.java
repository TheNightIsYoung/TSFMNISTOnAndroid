package com.thenightisyoung.tsfonandroid;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class MyTSFMNISTDemo {

    private static final String TAG = "MyTSFMNISTDemo";
    private static final String MODEL_FILE = "file:///android_asset/mnist.pb";

    // 定义数据的维度:
    private static final int IN_COL = 1;
    private static final int IN_ROW = 28*28;
    private static final int OUT_COL = 1;
    private static final int OUT_ROW = 1;

    // 模型中输入变量的名称:
    private static final String inputName = "input/input_x";
    // 模型中输出变量的名称:
    private static final String outputName = "output";

    TensorFlowInferenceInterface inferenceInterface;


    static {
        // 加载库文件:
        System.loadLibrary("tensorflow_inference");
        Log.e(TAG,"libtensorflow_inference.so loading successfully.");
    }

    MyTSFMNISTDemo(AssetManager assetManager) {
        // 接口定义:
        inferenceInterface = new TensorFlowInferenceInterface(assetManager,MODEL_FILE);
        Log.e(TAG, "Initialize TensorFlowInferenceInterface successfully.");
    }


    public int[] getPredict(Bitmap bitmap) {

        // 需要将图片缩放到 [28*28]，用于初始化样例数据：
        float[] inputdata = bitmapToFloatArray(bitmap,28, 28);

        // 将样例数据 feed 给 tensorflow model:
        inferenceInterface.feed(inputName, inputdata, IN_COL, IN_ROW);

        // 调用模型进行运算:
        String[] outputNames = new String[] {outputName};
        inferenceInterface.run(outputNames);

        // 将输出结果物存放到 outputs 中：
        int[] outputs = new int[OUT_COL*OUT_ROW];
        inferenceInterface.fetch(outputName, outputs);

        return outputs;
    }

    /**
     * Function: 将bitmap 转为（按行优先）一个 float 数组，并且每个像素点都归一化到（0~1）之间。
     * @param bitmap bitmap 格式的样例图片
     * @param rx 将图片缩放到指定的大小（列）->28
     * @param ry 将图片缩放到指定的大小（行）->28
     * @return   返回归一化后的一维 float 数组 ->28*28
     */
    public static float[] bitmapToFloatArray(Bitmap bitmap, int rx, int ry){

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        // 缩放比例
        float scaleWidth = ((float) rx) / width;
        float scaleHeight = ((float) ry) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        Log.i(TAG,"bitmap width: " + bitmap.getWidth() + ",height:" + bitmap.getHeight());
        Log.i(TAG,"bitmap.getConfig(): " + bitmap.getConfig());

        height = bitmap.getHeight();
        width = bitmap.getWidth();
        float[] result = new float[height * width];
        int k = 0;
        // 行优先
        for(int j = 0;j < height;j++){
            for (int i = 0;i < width;i++){
                int argb = bitmap.getPixel(i,j);
                int r = Color.red(argb);
                int g = Color.green(argb);
                int b = Color.blue(argb);
                int a = Color.alpha(argb);
                //由于是灰度图，所以r,g,b分量是相等的。
                assert(r==g && g==b);
                result[k++] = r / 255.0f;
            }
        }
        return result;
    }
}