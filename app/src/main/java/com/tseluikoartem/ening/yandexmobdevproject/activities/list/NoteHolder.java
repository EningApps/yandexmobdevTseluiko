package com.tseluikoartem.ening.yandexmobdevproject.activities.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tseluikoartem.ening.yandexmobdevproject.R;

/**
 * Created by ening on 30.01.18.
 */

class NoteHolder{

    static class LinearHolder extends RecyclerView.ViewHolder {

        private final View iconColorView, noteLayoutView;
        private final TextView titleTextView, discrTextView;

        LinearHolder(final View view) {
            super(view);
            iconColorView = view.findViewById(R.id.color_view);
            titleTextView = (TextView) view.findViewById(R.id.textViewTitle);
            discrTextView = (TextView) view.findViewById(R.id.textViewDiscr);
            noteLayoutView =  view.findViewById(R.id.note_linearlayout);
        }

        public View getIconColorView() {
            return iconColorView;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getDiscrTextView() {
            return discrTextView;
        }

        public View getNoteLayoutView() {
            return noteLayoutView;
        }
    }
}

