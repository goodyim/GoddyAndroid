package im.goody.android.screens.common.recyclerview_binding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewBindingAdapter {

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T> void bindRecyclerViewItems(RecyclerView view, List<T> items) {
        if (view.getAdapter() instanceof BindableAdapter) {
            if (items == null)  items = new ArrayList<>();

            ((BindableAdapter<T>) view.getAdapter()).setData(items);
        }
    }
}
