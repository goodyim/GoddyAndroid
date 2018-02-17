package im.goody.android.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import im.goody.android.R;
import io.reactivex.Observable;

public class EditTextDialog {
    private int titleRes;

    public EditTextDialog(@StringRes int titleRes) {
        this.titleRes = titleRes;
    }

    public Observable<String> show(Context context) {
        return Observable.create(source -> {
            String title = context.getString(titleRes);

            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.dialog_edit_text, null, false);

            new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setView(v)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                        EditText editText = v.findViewById(R.id.dialog_edit_text_value);
                        source.onNext(editText.getText().toString());
                    })
                    .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        });
    }
}
