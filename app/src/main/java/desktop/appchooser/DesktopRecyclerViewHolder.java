package desktop.appchooser;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tseluikoartem.ening.yandexmobdevproject.R;


/**
 * Created by ening on 30.01.18.
 */

public class DesktopRecyclerViewHolder {

    public static class IconLinearHolder extends RecyclerView.ViewHolder {

        private final View iconView;
        private final TextView titleTextView;
        private final View iconField;

        IconLinearHolder(final View view) {
            super(view);
            iconView = view.findViewById(R.id.icon_view);
            titleTextView = view.findViewById(R.id.icon_label);
            iconField = view.findViewById(R.id.iconLinearField);
        }

        public View getIconView() {
            return iconView;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public View getIconField() {
            return iconField;
        }
    }




}

