package com.tseluikoartem.ening.yandexmobdevproject.activities.list;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.List;


/**
 * Created by ening on 30.01.18.
 */

public class NoteRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Integer> data;

    public NoteRecycleAdapter(List<Integer> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);
        return new NoteHolder.LinearHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        final View noteLayout = ((NoteHolder.LinearHolder) holder).getNoteLayoutView();
        noteLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar deleteSnackbar = Snackbar.make(view, "Удалить запись?", Snackbar.LENGTH_LONG)
                        .setAction("Да", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                data.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, data.size());
                            }
                        });
                deleteSnackbar.setDuration(5000);
                deleteSnackbar.show();
                deleteSnackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            Log.d("SnackBar", "Закрыт по истечении таймаута");
                        }
                        if(event == Snackbar.Callback.DISMISS_EVENT_SWIPE){
                            Log.d("SnackBar", "Swipe");
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        Log.d("SnackBar", "onShown");
                    }
                });

                return true;
            }
        });
        final View colorView = ((NoteHolder.LinearHolder) holder).getIconColorView();
        colorView.setBackgroundColor(data.get(position));
        final TextView titleTextView=(TextView) ((NoteHolder.LinearHolder) holder).getTitleTextView();
        titleTextView.setText(String.format("#%06X", 0xFFFFFF & data.get(position)));
        final TextView discrTextView=(TextView) ((NoteHolder.LinearHolder) holder).getDiscrTextView();
        discrTextView.setText("Some long long very long long text");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
