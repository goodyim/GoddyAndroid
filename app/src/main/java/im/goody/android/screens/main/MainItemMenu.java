package im.goody.android.screens.main;

import android.view.View;
import android.widget.PopupMenu;

import im.goody.android.R;
import io.reactivex.Observable;

public class MainItemMenu {
    public static Observable<Integer> show(View anchor) {
        return Observable.create(source -> {
            PopupMenu popup = new PopupMenu(anchor.getContext(), anchor);
            popup.inflate(R.menu.main_item_popup);
            popup.setOnMenuItemClickListener(item -> {
                source.onNext(item.getItemId());
                return true;
            });
            popup.show();
        });
    }
}
