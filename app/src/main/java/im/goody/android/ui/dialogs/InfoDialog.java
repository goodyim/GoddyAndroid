package im.goody.android.ui.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import im.goody.android.App;
import im.goody.android.R;
import io.reactivex.Observable;


public class InfoDialog {
    public static final int BUTTON_OK = 0;
    public static final int BUTTON_CANCEL = 1;

    private String message;
    private String title;
    private String okMessage;

    private AlertDialog dialog;

    public InfoDialog(int messageRes, int titleRes, int okMessageRes) {
        Context context = App.getAppContext();

        this.message = context.getString(messageRes);
        this.title = context.getString(titleRes);
        this.okMessage = context.getString(okMessageRes);
    }

    public InfoDialog(String message, int titleRes, int okMessageRes) {
        Context context = App.getAppContext();

        this.message = message;
        this.title = context.getString(titleRes);
        this.okMessage = context.getString(okMessageRes);
    }

    public Observable<Integer> show(Context context) {
        return Observable.create(emitter -> {
            dialog = new AlertDialog.Builder(context)
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

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
}