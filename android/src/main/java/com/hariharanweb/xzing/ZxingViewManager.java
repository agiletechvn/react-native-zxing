package com.hariharanweb.xzing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import android.graphics.BitmapFactory;

public class ZxingViewManager extends SimpleViewManager<ImageView> {
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private static final String TAG = "ZxingViewManager";
    private static final String NAME = "ZxingView";

    private String text;
    private String format;
    private int width = 200;
    private int height = 200;
    private boolean cache = false;

    private Context mApplicationContext;
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected ImageView createViewInstance(ThemedReactContext reactContext) {
        Log.d(TAG, "createViewInstance");
        ImageView iv = new ImageView(reactContext);
        mApplicationContext = reactContext.getApplicationContext();
        return iv;
    }

    @ReactProp(name = "text")
    public void setText(ImageView view, String text) {
        Log.d(TAG, "setText ********");
        this.text = text;
        setBarcode(view);
    }

    @ReactProp(name = "format")
    public void setFormat(ImageView view, String format) {
        Log.d(TAG, "setFormat " + format);
        this.format = format;
        setBarcode(view);
    }

    @ReactProp(name = "width", defaultInt = 200)
    public void setWidth(ImageView view, int width) {
        Log.d(TAG, "setWidth " + width);
        this.width = width;
        setBarcode(view);
    }

    @ReactProp(name = "height", defaultInt = 200)
    public void setHeight(ImageView view, int height) {
        Log.d(TAG, "setHeight " + height);
        this.height = height;
        setBarcode(view);
    }

    @ReactProp(name = "cache", defaultBoolean = false)
    public void setCache(ImageView view, boolean cache) {
        Log.d(TAG, "setCache " + cache);
        this.cache = cache;
        setBarcode(view);
    }

    private void setBarcode(ImageView imageView) {
        if (this.text == null || this.format == null)
            return;
        BarcodeFormat barcodeFormat = getFormat(this.format);
        try {
            Bitmap bitmap = encodeAsBitmap(this.text, barcodeFormat, (int) this.width, (int) this.height);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BarcodeFormat getFormat(String format) {
        return BarcodeFormat.valueOf(format);
    }

    private Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws Exception {
        Log.d(TAG, String.format("Creating barcode with format: %s width: %d height: %d", format, img_width, img_height));
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }

        Bitmap bitmap = null;
        File cacheFile = null;
        if(cache) {
            String cacheFilePath = mApplicationContext.getFilesDir().getAbsolutePath()
                    + "/Zxing_"
                    + text.replace("/", "_") + "_"
                    + format + "_" + width + "_" + height + ".png";
            cacheFile = new File(cacheFilePath);

            if(cacheFile.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeFile(cacheFilePath, options);
            }
        }

        if(bitmap == null) {
//            Map<EncodeHintType, Object> hints = null;
//            String encoding = guessAppropriateEncoding(contentsToEncode);
//            if (encoding != null) {
            Map<EncodeHintType, Object> hints =  hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            // we set margin at client side
            hints.put(EncodeHintType.MARGIN, 0);
//            }
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix result;
            try {
                result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
            } catch (IllegalArgumentException iae) {
                throw new Exception("Unsupported barcode format");
            }
            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }

            bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            // save file if cache is set
            if(cache) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(cacheFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    fos = null;
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                finally {
                    if (fos != null) {
                        try {
                            fos.close();
                            fos = null;
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return bitmap;
    }

//    private static String guessAppropriateEncoding(CharSequence contents) {
//        // Very crude at the moment
//        for (int i = 0; i < contents.length(); i++) {
//            if (contents.charAt(i) > 0xFF) {
//                return "UTF-8";
//            }
//        }
//        return null;
//    }
}
