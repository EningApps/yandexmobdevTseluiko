package utils;

/**
 * Created by ening on 05.02.18.
 */

public interface ApplicationConstants {

    public interface SharedPreferenciesConstants{
        public static final String SP_FOR_DB_NAME = "db_sharedpref";

        public static final String SHOW_WELCOMEPAGE_KEY = "show_welcomepage";
        public static final String IS_FIRST_LAUNCH_KEY = "firstlaunch";
        public static final String THEME_CHOICE_KEY = "theme_key";
        public static final String MAKET_TYPE_KEY = "maket_type";
        public static final String SHOW_GIT_KEY = "show_github";
        public static final String SHOW_TWITTER_KEY = "show_twitter";
        public static final String SHOW_FACEBOOK_KEY = "show_facebook";
        public static final String SHOW_VK_KEY = "show_vk";
        public static final String APPLICATION_SORT = "sort_key";

        public static final String THEME_DARK = "Тёмная тема";
        public static final String THEME_LIGHT = "Светлая тема";

    }

    public interface YandexAppMetricaConstants{
        public static final String API_KEY = "dbcd0800-42ae-4c4d-a1bb-d37917cdb6c1";



    }

    public interface BackgroundImagesConstants{
        public static final String YANDEX_FOTKI_URL = "http://api-fotki.yandex.ru/api/podhistory/poddate;";
        public static final String YANDEX_FOTKI_DATE_RULE = "T12:00:00Z/?limit=5";

        public static final int JOB_ID_LOAD_IMAGE = 181999234;
        public static final String BROADCAST_ACTION_IMAGES_LOADED = "com.tseluikoartem.yandexmobdev.IMAGES_LOADED";
        public static final String BROADCAST_PARAM_IMAGES_NAMES = "com.tseluikoartem.yandexmobdev.IMAGES_NAMES";


    }
}
