package backgroundimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;


/**
 * Created by ening on 11.02.18.
 */

public class BackgroundImageAsyncChanger extends AsyncTask<String, Void ,Drawable> {

    private int pictureNumber;
    private View backgroundView;
    private Context context;

    public BackgroundImageAsyncChanger(View backgroundView, Context context, int pictureNumber) {
        this.backgroundView = backgroundView;
        this.context = context;
        this.pictureNumber = pictureNumber;
    }

    @Override
    protected Drawable doInBackground(String ... imageFilesNames) {
        final int index = pictureNumber;
        final Bitmap bitmap = ImageFileOperator.getInstance().loadImage(context, imageFilesNames[index]);
        final Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        return drawable;
    }

    @Override
    protected void onPostExecute(Drawable backgroundImage) {
        backgroundView.setBackground(backgroundImage);
    }
}
