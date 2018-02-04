package launcher;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tseluikoartem.ening.yandexmobdevproject.R;

/**
 * Created by ening on 30.01.18.
 */

class IconHolder {

    static class GridHolder extends RecyclerView.ViewHolder {

        private final View iconColorView;
        private final TextView iconTextView;

        GridHolder(final View view) {
            super(view);
            iconColorView = view.findViewById(R.id.icon_color_view);
            iconTextView = (TextView) view.findViewById(R.id.icon_text);
        }

        public View getIconColorView() {
            return iconColorView;
        }

        public TextView getIconTextView() {
            return iconTextView;
        }
    }
}

