package im.goody.android.core;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.bluelinelabs.conductor.Controller;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.data.IRepository;
import im.goody.android.di.components.RootComponent;
import im.goody.android.root.IRootPresenter;
import io.reactivex.disposables.Disposable;

public abstract class BaseController<V extends BaseView> extends Controller {

    @Inject
    protected IRepository repository;
    @Inject
    protected IRootPresenter rootPresenter;

    protected V attachedView;

    protected Disposable disposable;

    public BaseController() {
        RootComponent rootComponent = App.getRootComponent();
        if (rootComponent != null) {
            initDaggerComponent(rootComponent);
        }
    }

    protected abstract void initDaggerComponent(RootComponent parentComponent);

    @SuppressWarnings("unchecked")
    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        attachedView = (V) view;
        attachedView.takeController(this);
        initActionBar();
    }

    protected abstract void initActionBar();

    @Override
    protected void onDetach(@NonNull View view) {
        attachedView.dropController();
        attachedView = null;

        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
        super.onDetach(view);
    }

    //======= region Permissions =======

    protected boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(App.getAppContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    //endregion
}
