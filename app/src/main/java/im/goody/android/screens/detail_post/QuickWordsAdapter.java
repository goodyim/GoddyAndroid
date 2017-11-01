package im.goody.android.screens.detail_post;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import im.goody.android.App;
import im.goody.android.R;
import im.goody.android.databinding.ItemQuickWordBinding;

public class QuickWordsAdapter extends RecyclerView.Adapter<QuickWordsAdapter.QuickWordsHolder> {
    private Handler handler;
    private String[] words;

    QuickWordsAdapter(Handler handler) {
        this.handler = handler;

        words = App.getAppContext().getResources().getStringArray(R.array.quick_words);
    }

    @Override
    public QuickWordsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemQuickWordBinding binding = ItemQuickWordBinding.inflate(inflater, parent, false);
        return new QuickWordsHolder(binding);
    }

    @Override
    public void onBindViewHolder(QuickWordsHolder holder, int position) {
        holder.bind(words[position]);
    }

    @Override
    public int getItemCount() {
        return words.length;
    }

    public interface Handler {
        void handle(String word);
    }

    class QuickWordsHolder extends RecyclerView.ViewHolder {
        private ItemQuickWordBinding binding;

        QuickWordsHolder(ItemQuickWordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String name) {
            binding.setName(name);
            binding.setHandler(handler);
        }
    }
}
