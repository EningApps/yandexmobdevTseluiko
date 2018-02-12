package backgroundimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class ImageFileOperator {

    private static final Object mLock = new Object();

    private static volatile ImageFileOperator sInstance;

    private static final String DIRECTORY_NAME = "images";

    static ImageFileOperator getInstance() {
        if (null == sInstance) {
            synchronized (mLock) {
                if (null == sInstance) {
                    sInstance = new ImageFileOperator();
                }
            }
        }

        return sInstance;
    }

    private ImageFileOperator() {
    }

    void saveImage(final Context context, final Bitmap bitmap, final String fileName) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile(context, fileName));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private File createFile(final Context context, final String fileName) {
        File directory = context.getDir(DIRECTORY_NAME, Context.MODE_PRIVATE);
        return new File(directory, fileName);

    }

    Bitmap loadImage(final Context context, final String fileName) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(createFile(context, fileName));
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
        }
        return null;
    }
}
