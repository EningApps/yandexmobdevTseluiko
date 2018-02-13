package backgroundimage;


import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;


public class BackgroundImageAsyncChanger extends AsyncTask<String,Bitmap,Drawable> {

    private View backgroundView;
    private Context context;

    public BackgroundImageAsyncChanger(View backgroundView, Context context) {
        this.backgroundView = backgroundView;
        this.context = context;
    }

    @Override
    protected Drawable doInBackground(String ... imageUrl) {

        final ImageLoader imageLoader = new ImageLoader();
        final Bitmap bitmap = imageLoader.loadBitmap(imageUrl[0]);
        final Drawable backgroundDrawable = new BitmapDrawable(context.getResources(),bitmap);
        return backgroundDrawable;
    }


    @Override
    protected void onPostExecute(Drawable backgroundImage) {
        backgroundView.setBackground(backgroundImage);
    }
}
