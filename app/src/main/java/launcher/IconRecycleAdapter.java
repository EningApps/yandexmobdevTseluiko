package launcher;

import android.support.v7.widget.RecyclerView;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        final View colorView = ((IconHolder.GridHolder) holder).getIconColorView();
        colorView.setBackgroundColor(data.get(position));
        final TextView textView=(TextView) ((IconHolder.GridHolder) holder).getIconTextView();
        textView.setText(String.format("#%06X", 0xFFFFFF & data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
