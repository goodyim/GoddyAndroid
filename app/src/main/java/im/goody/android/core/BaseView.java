package im.goody.android.core;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.bluelinelabs.conductor.Controller;

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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()){
           onAttached();
        }
    }

    protected abstract void onAttached();

    @Override
    protected void onDetachedFromWindow() {
        if (!isInEditMode()){
           onDetached();
        }
        super.onDetachedFromWindow();
    }

    protected abstract void onDetached();

    public void dropController() {
        this.controller = null;
    }
}
