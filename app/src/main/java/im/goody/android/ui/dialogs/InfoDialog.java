package im.goody.android.ui.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import im.goody.android.R;
import io.reactivex.Observable;


public class InfoDialog {
    public static final int BUTTON_OK = 0;
    public static final int BUTTON_CANCEL = 1;

    private int message;
    private int title;
    private int okMessage;

    public InfoDialog(int messageRes, int titleRes, int okMessageRes) {
        this.message = message;
        this.title = title;
        this.okMessage = okMessageRes;
    }

    public Observable<Integer> show(Context context) {
        return Observable.create(emitter -> {
            new AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        emitter.onNext(BUTTON_CANCEL);
                    })
                    .setPositiveButton(okMessage, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        emitter.onNext(BUTTON_OK);
                    })
                    .setTitle(title)
                    .setMessage(message)
                    .show();
        });
    }
}
