package im.goody.android.screens.main;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import im.goody.android.R;
import im.goody.android.data.dto.Deal;
import im.goody.android.databinding.ItemNewsBinding;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    private List<Deal> data;

    public MainAdapter(List<Deal> data) {
        this.data = data;
    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_news, parent, false);
        return new MainHolder(view);
    }

    @Override
    public void onBindViewHolder(MainHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }


    public class MainHolder extends RecyclerView.ViewHolder {
        private ItemNewsBinding binding;

        public MainHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        void bind(int position) {
            binding.setDeal(data.get(position));
        }
    }
}
