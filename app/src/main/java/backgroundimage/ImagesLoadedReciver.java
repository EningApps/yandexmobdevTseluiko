package backgroundimage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.View;

import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import static utils.ApplicationConstants.BackgroundImagesConstants.*;

public class ImagesLoadedReciver extends BroadcastReceiver {

    private static String[] mImagesUrls = new String[0];

    private static final ImagesLoadedReciver INSTANCE = new ImagesLoadedReciver();
    private static final List<View> mBackgrounds = new ArrayList<>();
    private static final List<Bitmap> mBackgroundBitmaps = new ArrayList<>();


    @Override
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();
        if (BROADCAST_ACTION_IMAGES_LOADED.equals(action)) {
            mImagesUrls = intent.getStringArrayExtra(BROADCAST_PARAM_IMAGES_NAMES);
            for(View backgroundView : mBackgrounds){
                setBackround(backgroundView,context);
            }

        }
    }

    public void registerBackground(View view){
        mBackgrounds.add(view);
        if(mImagesUrls.length!=0){
            setBackround(view, view.getContext());
        }

    }


    public void setBackround(final View view, Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        final boolean isRandomImageNeeded = sp.getBoolean(BROADCAST_IS_RANDOM_IMAGE,false);

        String eventParams = "{\"isRandomBackground\":\""+isRandomImageNeeded+"\"}";
        YandexMetrica.reportEvent("Была выбран стиль фона:", eventParams);

        final int index = isRandomImageNeeded?0:new Random().nextInt(mImagesUrls.length-1);

        final BackgroundImageAsyncChanger imageAsyncChanger = new BackgroundImageAsyncChanger(view,view.getContext());
        imageAsyncChanger.execute(mImagesUrls[index]);
    }

    public static List<Bitmap> getBackgroundBitmaps() {
        return mBackgroundBitmaps;
    }

    public static ImagesLoadedReciver getInstance() {
        return INSTANCE;
    }
}

