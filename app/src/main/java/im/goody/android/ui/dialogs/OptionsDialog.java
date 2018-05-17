package im.goody.android.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;

import io.reactivex.Observable;

public class OptionsDialog {
    private int itemsId;
    private int titleId;

    public OptionsDialog(int title, int items) {
        this.titleId = title;
        this.itemsId = items;
    }

    public OptionsDialog(int items) {
        this.itemsId = items;
    }

    public Observable<Integer> show(Context context) {
        return Observable.create(e -> {
            String[] items = context.getResources().getStringArray(itemsId);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            if (titleId != 0) {
                builder.setTitle(context.getString(titleId));
            }

            builder.setItems(items, (dialog, which) -> e.onNext(which));
            builder.show();
        });
    }

    public Observable<Integer> show(Context context, int itemsId) {
        this.itemsId = itemsId;
        return show(context);
    }
}
