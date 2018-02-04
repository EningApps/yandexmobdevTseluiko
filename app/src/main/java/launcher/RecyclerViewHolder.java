package launcher;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tseluikoartem.ening.yandexmobdevproject.R;

/**
 * Created by ening on 30.01.18.
 */

public class RecyclerViewHolder {

    public static class IconHolder extends RecyclerView.ViewHolder {

        private final View iconView;
        private final TextView titleTextView;

        IconHolder(final View view) {
            super(view);
            iconView = view.findViewById(R.id.icon_view);
            titleTextView = view.findViewById(R.id.icon_label);
        }

        public View getIconView() {
            return iconView;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }
    }



}

