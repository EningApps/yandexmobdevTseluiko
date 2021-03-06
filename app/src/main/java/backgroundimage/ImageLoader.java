package backgroundimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


class ImageLoader {

    private static ImageLoader sInstance;

    public static ImageLoader getsInstance(){
        if(sInstance==null){
            sInstance = new ImageLoader();
        }
        return sInstance;
    }


    private List<String> mImageUrls = new ArrayList<>();

    private String processEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG
                    && "img".equals(parser.getName())) {
                for (int i = 1; i < parser.getAttributeCount(); i++) {
                    if ("size".equals(parser.getAttributeName(i))
                            && "XXL".equals(parser.getAttributeValue(i))) {
                        return parser.getAttributeValue(i-1);
                    }
                }
            }
        }
        return null;
    }

    List<String> getImageUrls() {
        if (mImageUrls.isEmpty()) {
            try {
                final Calendar calendar = Calendar.getInstance();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                final String formattedDate = dateFormat.format(calendar.getTime());

                final String stringUrl = "http://api-fotki.yandex.ru/api/recent/";//ApplicationConstants.BackgroundImagesConstants.YANDEX_FOTKI_URL + formattedDate + ApplicationConstants.BackgroundImagesConstants.YANDEX_FOTKI_DATE_RULE;
                final URL url = new URL(stringUrl);
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    final InputStream stream = connection.getInputStream();
                    final XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(stream, null);
                    String imgUrl;
                    while ((imgUrl = processEntry(parser)) != null) {
                        mImageUrls.add(imgUrl);
                    }
                }
            } catch (IOException | XmlPullParserException e) {
                
            }
        }

        return mImageUrls;
    }


    Bitmap loadBitmap(String srcUrl) {
        try {
            URL url = new URL(srcUrl);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[1630];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte [] bitmap = buffer.toByteArray();
            return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        } catch (IOException e) {

        }
        return null;
    }
}
