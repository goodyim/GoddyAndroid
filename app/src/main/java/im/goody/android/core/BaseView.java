package im.goody.android.core;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;

import com.bluelinelabs.conductor.Controller;

import im.goody.android.R;

public abstract class BaseView<C extends Controller, B extends ViewDataBinding> extends ConstraintLayout {

    protected C controller;
    protected B binding;

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()){
           binding = DataBindingUtil.bind(this);
        }
    }

    public void takeController(C controller) {
        this.controller = controller;
    }

    public void dropController() {
        this.controller = null;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()){
           onAttached();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (!isInEditMode()){
           onDetached();
        }
        super.onDetachedFromWindow();
    }

    protected abstract void onAttached();

    protected abstract void onDetached();

    public void showErrorWithRetry(String message, OnClickListener listener) {
        if (message == null) message = getResources().getString(R.string.unknown_error);

        Snackbar snackbar = getSnackBarWithMessage(message);
        snackbar.setAction(R.string.retry, listener);
        snackbar.show();
    }

    public void showMessage(@StringRes int stringRes) {
       getSnackBarWithMessage(getResources().getString(stringRes)).show();
    }

    public void showMessage(String text) {
        getSnackBarWithMessage(text).show();
    }

    protected Snackbar getSnackBarWithMessage(String message) {
        return Snackbar.make(this, message, Snackbar.LENGTH_LONG);
    }
}
