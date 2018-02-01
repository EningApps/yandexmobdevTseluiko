package launcher;

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

public class IconRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Integer> data;

    public IconRecycleAdapter(List<Integer> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_layout, parent, false);
        return new IconHolder.GridHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final View colorView = ((IconHolder.GridHolder) holder).getIconColorView();
        colorView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar deleteSnackbar = Snackbar.make(view, "Удалить иконку?", Snackbar.LENGTH_LONG)
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
        colorView.setBackgroundColor(data.get(position));
        final TextView textView = (TextView) ((IconHolder.GridHolder) holder).getIconTextView();
        textView.setText(String.format("#%06X", 0xFFFFFF & data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

