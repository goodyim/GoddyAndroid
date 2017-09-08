package im.goody.android.screens.setting;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import im.goody.android.BR;

public class SettingViewModel extends BaseObservable {
    @Bindable
    private boolean some;

    public boolean isSome() {
        return some;
    }

    public void setSome(boolean some) {
        this.some = some;
        notifyPropertyChanged(BR.some);
    }
}
