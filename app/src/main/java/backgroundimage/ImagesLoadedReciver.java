package backgroundimage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import static utils.ApplicationConstants.BackgroundImagesConstants.*;

public class ImagesLoadedReciver extends BroadcastReceiver {

    private static String[] mImagesUrls = new String[0];

    private static ImagesLoadedReciver sInstance;

    public static ImagesLoadedReciver getsInstance(){
        if(sInstance==null){
            sInstance = new ImagesLoadedReciver();
        }
        return sInstance;
    }

    private static final List<View> mBackgrounds = new ArrayList<>();

    private static int mAllSamePicIndex ;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        mAllSamePicIndex= new Random().nextInt(40);
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

    public void setBackround(final View view, final Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        final boolean isRandomImageNeeded = sp.getBoolean(BROADCAST_IS_RANDOM_IMAGE,false);

  //      String eventParams = "{\"isRandomBackground\":\""+isRandomImageNeeded+"\"}";
//        YandexMetrica.reportEvent("Была выбран стиль фона:", eventParams);

        final int index = isRandomImageNeeded? mAllSamePicIndex : new Random().nextInt(mImagesUrls.length-1);

        final BackgroundImageAsyncChanger imageAsyncChanger = new BackgroundImageAsyncChanger(view,view.getContext());
        imageAsyncChanger.execute(mImagesUrls[index]);

    }
}

