package im.goody.android.core;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.bluelinelabs.conductor.Controller;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.data.IRepository;
import im.goody.android.data.network.error.StandardError;
import im.goody.android.di.components.RootComponent;
import im.goody.android.root.IRootPresenter;
import io.reactivex.disposables.Disposable;

public abstract class BaseController<V extends BaseView> extends Controller {

    @Inject
    protected IRepository repository;
    @Inject
    protected IRootPresenter rootPresenter;

    protected Disposable disposable;

    protected BaseController() {
        initDagger();
    }

    public BaseController(Bundle args) {
        super(args);
        initDagger();
    }

    protected abstract void initDaggerComponent(RootComponent parentComponent);

    @SuppressWarnings("unchecked")
    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        view().takeController(this);
        initActionBar();
        rootPresenter.launched();
    }

    protected abstract void initActionBar();

    @Override
    protected void onDetach(@NonNull View view) {
        view().dropController();
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
        super.onDetach(view);
    }

    // ======= region common methods =======

    @SuppressWarnings("unchecked")
    protected V view() {
        return (V) getView();
    }

    protected void showError(Throwable error) {
        view().showMessage(getErrorMessage(error));
    }

    protected String getErrorMessage(Throwable error) {
        StandardError errorObject = repository.getError(error, StandardError.class);
        return errorObject != null ? errorObject.getErrors() : error.getMessage();
    }

    protected void share(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(shareIntent);
    }

    // endregion

    //======= region Permissions =======

    protected boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(App.getAppContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    //endregion

    // ======= region private methods =======

    private void initDagger() {
        RootComponent rootComponent = App.getRootComponent();
        if (rootComponent != null) {
            initDaggerComponent(rootComponent);
        }
    }

    // endregion
}
