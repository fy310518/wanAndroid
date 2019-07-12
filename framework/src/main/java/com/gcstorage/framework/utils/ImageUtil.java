package com.gcstorage.framework.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by Administrator on 2018/9/19 0019.
 */

public class ImageUtil {
    private static final String TAG = "ImageUtil";



    /**
     * @param path 主要是为了判断图片的方向而已
     * @return Android开发中，在对图片进行展示、编辑、发送等操作时经常会涉及Exif的操作，Android中操作Exif主要是通过ExifInterface，ExifInterface看上去是一个接口，其实是一个类，位于Android.media.ExifInterface的位置。进入ExifInterface类，发现方法很少，主要就是三个方面：读取、写入、缩略图。
     * orientation获取图片的方向
     */
    public static Bitmap decodeImage(String path) {
        Bitmap res;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inSampleSize = 1;
            op.inJustDecodeBounds = false;
            //op.inMutable = true;
            res = BitmapFactory.decodeFile(path, op);
            //rotate and scale.
            Matrix matrix = new Matrix();
            //旋转角度
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }

            Bitmap temp = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, true);
            Log.d("com.arcsoft", "check target Image:" + temp.getWidth() + "X" + temp.getHeight());

            if (!temp.equals(res)) {
                res.recycle();
            }
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * bitmap 转 byte[] 数组
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            baos.flush();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }


    /**
     * byte[]转换成Bitmap
     *
     * @param b
     * @return
     */

    public static Bitmap bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }


    /**
     * 设置图片圆角
     *
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 压缩图片
     *
     * @param path 图片地址
     */
    public static Bitmap revitionImageSize(String path) {

        int degree = readPictureDegree(path);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        } else if (w == h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(path, newOpts);
        return rotateBitmap(degree, bitmap);
    }

    /**
     * 压缩图片
     *
     * @param resId 图片地址
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > 800f || width > 480f) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / 800f);
            final int widthRatio = Math.round((float) width / 480f);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * bitmap转字节数组（图片进行质量压缩）
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        while (baos.toByteArray().length / 1024 > 150) {   //循环判断如果压缩后图片是否大于150kb,大于继续压缩
            baos.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        byte[] bytes = baos.toByteArray();//把压缩后的数据baos存放到字节数组中
        return bytes;
    }

/**
     * 放大缩小图片  ,指定图片的宽高(分辨率)
     *
     * @param bitmap 位图
     * @param w      新的宽度
     * @param h      新的高度
     * @return Bitmap
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
    public static InputStream bitmapToInputStream(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
        while (baos.toByteArray().length / 1024 > 200) {
            baos.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        InputStream isBitmap = new ByteArrayInputStream(baos.toByteArray());
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ImageUtil", "ImageUtil压缩图片异常");
        }

        if (bitmap != null) {
            bitmap.recycle();
        }
        return isBitmap;
    }


    /**
     * 图片质量压缩
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToCompress(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
        while (baos.toByteArray().length / 1024 > 200) {
            baos.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        byte[] bytes = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ImageUtil", "ImageUtil压缩图片异常");
        }

        if (bitmap != null) {
            bitmap.recycle();
        }
        return bytes;
    }


    public static String parseUrl(String url) {
        if (!TextUtils.isEmpty(url) ) {
            if (url.contains("220.249.118.115")) {
                return url.replace("220.249.118.115", "113.57.174.98");
            }
        }
        return url;
    }
    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 根据旋转角度将图片旋转
     *
     * @param img
     * @return
     */
    public static Bitmap rotateBitmap(int angle, Bitmap img) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle); /*翻转90度*/
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }


    /**
     * 将View对象生成Bitmap并返回
     * <p>
     * Bitmap.Config ARGB_4444 16 每个像素 占四位
     * Bitmap.Config ARGB_8888 32 每个像素 占八位
     * Bitmap.Config RGB_565 16 R占5位 G占6位 B占5位 没有透明度（A）
     */
    public static Bitmap loadBitmapFromView(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(90, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(90, View.MeasureSpec.EXACTLY));
        // 这个方法也非常重要，设置布局的尺寸和位置
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        // 生成bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        // 利用bitmap生成画布
        Canvas canvas = new Canvas(bitmap);
        // 把view中的内容绘制在画布上
        view.draw(canvas);
        return bitmap;
    }

    /*
     * 设置Bitmap圆角半径
     */
    public static Bitmap setRoundRect(Bitmap bitmap, float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 获取网落图片资源
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) throws IOException {
        URL myFileURL;
        Bitmap bitmap = null;

        myFileURL = new URL(url);
        //获得连接
        HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
        //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
        conn.setConnectTimeout(6000);
        //连接设置获得数据流
        conn.setDoInput(true);
        //不使用缓存
        conn.setUseCaches(false);
        //这句可有可无，没有影响
        //conn.connect();
        //得到数据流
        InputStream is = conn.getInputStream();
        //解析得到图片
        bitmap = BitmapFactory.decodeStream(is);
        //关闭数据流
        is.close();

        return bitmap;
    }

    /**
     * 保存Bitmap到本地SD卡上
     */
    public static void saveBitmap(Bitmap bitmap, String picName, String dir) throws IOException {
        File file = new File(dir, picName);
        if (!file.getParentFile().exists()) { //如果目标文件所在的目录不存在，则创建父目录
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) { //判断目标文件所在的目录是否存在
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        out.flush();
        out.close();
    }

    /**
     * 通过File对象获取Bitmap对象
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static Bitmap getBitmapByFile(File file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        return BitmapFactory.decodeStream(fis);
    }


    public static Bitmap drawTextToBitmap(Context gContext, Bitmap bitmap,
                                          int res, String gText, String fileSize) {

        int fontSize = 45;//字体大小
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Typeface font = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(font);
        paint.setTextSize(fontSize);
        paint.setDither(true); //获取跟清晰的图像采样
        paint.setFilterBitmap(true);//过滤一些
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int margin = 45;
        int width = bitmap.getWidth();
        float height = bitmap.getHeight();
        canvas.drawText(gText, margin, height - 2 * margin, paint);
        canvas.drawText(fileSize, width - 2 * margin - fileSize.length() * fontSize / 2, height - 2 * margin, paint);

        Bitmap bmp = BitmapFactory.decodeResource(gContext.getResources(), res);
        Matrix matrix = new Matrix();//缩小播放按钮到1/3
        float scaleHeight = height / (bmp.getHeight() * 3);
        matrix.postScale(scaleHeight, scaleHeight); //长和宽放大缩小的比例
        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

        canvas.drawBitmap(bmp, (width - bmp.getWidth()) / 2,
                (height - bmp.getHeight()) / 2, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }












    //======================图片压缩(万紫辉)=================begin

    /**
     * 压缩图片(方法1)
     *
     * @param filename
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String filename,
                                                     int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);
    }

    /**
     * 压缩图片(方法2)
     *
     * @param is
     * @return
     */
    public static Bitmap decodeBitmapFromInputStream(InputStream is) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //2倍压缩(固定值)
        options.inSampleSize = 2;

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * 压缩图片(方法3)
     *
     * @param bytes
     * @return
     */
    public static Bitmap decodeBitmapFromBytes(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        //2倍压缩(固定值)
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    /**
     * 压缩图片(方法4)
     *
     * @param bitmap
     * @return
     */
    public static Bitmap decodeBitmapFromBitmap(Bitmap bitmap) {
        //Bitmap转byte[]
        byte[] bytes = ImageUtil.bitmapToBytes(bitmap);
        Bitmap result = ImageUtil.decodeBitmapFromBytes(bytes);
        return result;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    //======================图片压缩=================end

    /**
     * bitmap转字节数组
     *
     * @return
     * @auther 万紫辉
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        //初始化一个流对象
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        //把bitmap100%高质量压缩 到 output对象里
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        //自由选择是否进行回收
//        bitmap.recycle();
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param bmp
     * @param overlay
     * @return
     */
    public static Bitmap drawBitmap(Context context, Bitmap bmp, Bitmap overlay) {
        long start = System.currentTimeMillis();
        int dwidth = bmp.getWidth();
        int dheight = bmp.getHeight();
        int width = overlay.getWidth();
        int height = overlay.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        Matrix mDrawMatrix = new Matrix();

        float scale;
        float dx;
        float dy;

        if (dwidth <= width && dheight <= height) {
            scale = 1.0f;
        } else {
            scale = Math.min((float) width / (float) dwidth,
                    (float) height / (float) dheight);
        }

        dx = Math.round((width - DipPxUtil.dip2px(context, 8) - dwidth * scale) * 0.5f);
        dy = Math.round((height - DipPxUtil.dip2px(context, 10) - dheight * scale) * 0.5f);

        mDrawMatrix.setScale(scale, scale);
        mDrawMatrix.postTranslate(dx, dy);

        // 在原始位置0，0插入原图
        canvas.drawBitmap(bmp, mDrawMatrix, null);
        canvas.drawBitmap(overlay, 0, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bitmap;
    }

    /**
     * 根据bitmap对图片进行比例压缩
     *
     * @param image
     * @return
     */
    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 200) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 根据bitmap对图片进行质量压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }



    public static final Bitmap createBitmapFromPath(String path, Context context, int maxResolutionX, int maxResolutionY) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = null;
        if (path.endsWith(".3gp")) {
            return ThumbnailUtils.createVideoThumbnail(path, 1);
        } else {
            try {
                options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                int e = options.outWidth;
                int height = options.outHeight;
                options.inSampleSize = computeBitmapSimple(e * height, maxResolutionX * maxResolutionY);
                options.inPurgeable = true;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inDither = false;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(path, options);
                return rotateBitmapByExif(bitmap, path, true);
            } catch (OutOfMemoryError var8) {
                options.inSampleSize *= 2;
                bitmap = BitmapFactory.decodeFile(path, options);
                return rotateBitmapByExif(bitmap, path, true);
            } catch (Exception var9) {
                var9.printStackTrace();
                return null;
            }
        }
    }


    public static int computeBitmapSimple(int realPixels, int maxPixels) {
        try {
            if (realPixels <= maxPixels) {
                return 1;
            } else {
                int e;
                for (e = 2; realPixels / (e * e) > maxPixels; e *= 2) {
                }

                return e;
            }
        } catch (Exception var3) {
            return 1;
        }
    }


    public static Bitmap rotateBitmapByExif(Bitmap bitmap, String path, boolean isRecycle) {
        int digree = getBitmapExifRotate(path);
        if (digree != 0) {
            bitmap = rotate((Context) null, bitmap, digree, isRecycle);
        }

        return bitmap;
    }

    public static int getBitmapExifRotate(String path) {
        short digree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(path);
        } catch (IOException var4) {
            var4.printStackTrace();
            return 0;
        }

        if (exif != null) {
            int ori = exif.getAttributeInt("Orientation", 0);
            switch (ori) {
                case 3:
                    digree = 180;
                    break;
                case 6:
                    digree = 90;
                    break;
                case 8:
                    digree = 270;
                    break;
                default:
                    digree = 0;
            }
        }

        return digree;
    }

    public static Bitmap rotate(Context context, Bitmap bitmap, int degree, boolean isRecycle) {
        Matrix m = new Matrix();
        m.setRotate((float) degree, (float) bitmap.getWidth() / 2.0F, (float) bitmap.getHeight() / 2.0F);

        try {
            Bitmap ex = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            if (isRecycle) {
                bitmap.recycle();
            }

            return ex;
        } catch (OutOfMemoryError var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static Uri addImage(ContentResolver cr, String path) {
        File file = new File(path);
        String name = file.getName();
        int i = name.lastIndexOf(".");
        String title = name.substring(0, i);
        String filename = title + name.substring(i);
        int[] degree = new int[1];
        return addImage(cr, title, System.currentTimeMillis(), (Location) null, file.getParent(), filename, degree);
    }

    private static Uri STORAGE_URI;

    private static Uri addImage(ContentResolver cr, String title, long dateTaken, Location location, String directory, String filename, int[] degree) {
        File file = new File(directory, filename);
        long size = file.length();
        ContentValues values = new ContentValues(9);
        values.put("title", title);
        values.put("_display_name", filename);
        values.put("datetaken", Long.valueOf(dateTaken));
        values.put("mime_type", "image/jpeg");
        values.put("orientation", Integer.valueOf(degree[0]));
        values.put("_data", file.getAbsolutePath());
        values.put("_size", Long.valueOf(size));
        if (location != null) {
            values.put("latitude", Double.valueOf(location.getLatitude()));
            values.put("longitude", Double.valueOf(location.getLongitude()));
        }

        return cr.insert(STORAGE_URI, values);
    }


    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            try {
                Bitmap e;
                if (drawable instanceof ColorDrawable) {
                    e = Bitmap.createBitmap(2, 2, BITMAP_CONFIG);
                } else {
                    e = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
                }

                Canvas canvas = new Canvas(e);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return e;
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    private static final Bitmap.Config BITMAP_CONFIG;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    static {
        BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
        STORAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }


    /**
     * 调用系统的裁剪页面
     *
     * @param activity
     * @param uri
     * @param requestCode
     */
    public static void startPhotoZoom(Activity activity, Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);

//        intent.putExtra("output", Uri.fromFile(cropFile)); //返回存储路径
//        intent.putExtra("outputFormat", "jpg");//返回格式
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }

}


