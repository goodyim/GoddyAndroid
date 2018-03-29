package im.goody.android.ui.dialogs;

import im.goody.android.R;

public class ErrorDialog extends InfoDialog {
    public ErrorDialog(int messageRes) {
        super(messageRes, R.string.error, R.string.retry);
    }
}
