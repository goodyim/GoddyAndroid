package im.goody.android.screens.intro;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import im.goody.android.App;
import im.goody.android.BR;

public class IntroPage extends BaseObservable {
    @Bindable
    private Drawable image;

    public Drawable getImage() {
        return image;
    }

    public void setImage(int imageRes) {
        this.image = ContextCompat.getDrawable(App.getAppContext(), imageRes);
        notifyPropertyChanged(BR.image);
    }
}