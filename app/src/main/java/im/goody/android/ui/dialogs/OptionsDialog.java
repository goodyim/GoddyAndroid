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

    public Observable<Integer> show(Context context) {
        return Observable.create(e -> {
            String[] items = context.getResources().getStringArray(itemsId);
            String title = context.getString(titleId);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setItems(items, (dialog, which) -> e.onNext(which));
            builder.show();
        });
    }
}
