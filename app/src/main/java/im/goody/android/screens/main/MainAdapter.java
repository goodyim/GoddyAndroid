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
import im.goody.android.utils.AppConfig;

class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    private List<Deal> data;
    private MainItemHandler handler;

    MainAdapter(List<Deal> data, MainItemHandler handler) {
        this.data = data;
        this.handler = handler;
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

    void addData(List<Deal> items) {
        int size = getItemCount();
        data.addAll(items);
        notifyItemRangeInserted(size, items.size());
    }

    private String buildShareText(Deal deal) {
        String url = AppConfig.DEALS_URL + deal.getId();
        String tags = "#goody #гуди";
        return deal.getDescription() + "\n\n"
                + url + "\n\n"
                + tags;
    }


    interface MainItemHandler {
        void report(long id);

        void share(String text);
    }

    class MainHolder extends RecyclerView.ViewHolder {

        private ItemNewsBinding binding;

        MainHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        void bind(int position) {
            Deal deal = data.get(position);
            binding.setDeal(deal);
            binding.actionPanel.setDeal(deal);

            binding.actionPanel.panelItemShare.setOnClickListener(v -> {
                String text = buildShareText(deal);
                handler.share(text);
            });

            binding.newItemMenu.setOnClickListener(v -> MainItemMenu.show(v).subscribe(id -> {
                switch (id) {
                    case R.id.action_report:
                        handler.report(deal.getId());
                }
            }));
        }

    }
}
