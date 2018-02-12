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
import java.util.Random;

import utils.ApplicationConstants;

class ImageLoader {

    private static volatile ImageLoader sInstance;

    private List<String> mImageUrls = new ArrayList<>();

    private String processEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG
                    && "img".equals(parser.getName())) {
                for (int i = 1; i < parser.getAttributeCount(); i++) {
                    if ("size".equals(parser.getAttributeName(i))
                            && "XXXL".equals(parser.getAttributeValue(i))) {
                        return parser.getAttributeValue(i-1);
                    }
                }
            }
        }

        return null;
    }

    Bitmap loadBitmap(String srcUrl) {
        try {
            URL url = new URL(srcUrl);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

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

    String getImageUrl() {
        final List<String> imageUrls = getImageUrls();
        if (imageUrls.isEmpty() == false) {
            final int index = new Random().nextInt(mImageUrls.size());
            return imageUrls.get(index);
        } else {
            return null;
        }
    }

    List<String> getImageUrls() {
        if (mImageUrls.isEmpty()) {
            try {
                final Calendar calendar = Calendar.getInstance();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                final String formattedDate = dateFormat.format(calendar.getTime());

                final String stringUrl = ApplicationConstants.BackgroundImagesConstants.YANDEX_FOTKI_URL + formattedDate + ApplicationConstants.BackgroundImagesConstants.YANDEX_FOTKI_DATE_RULE;
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
}
