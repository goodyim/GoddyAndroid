package im.goody.android.screens.main;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import im.goody.android.R;
import io.reactivex.Observable;

public class MainItemMenu {
    private MainItemMenu() {}

    private boolean showEdit;
    private ChangeState state;

    enum ChangeState {
        HIDDEN, OPEN, CLOSE
    }

    public Observable<Integer> show(View anchor) {
        return Observable.create(source -> {
            PopupMenu popup = new PopupMenu(anchor.getContext(), anchor);
            popup.inflate(R.menu.item_deal);

            Menu menu = popup.getMenu();

            configureChangeStateItem(menu.findItem(R.id.action_change_event_state));
            configureEditItem(menu.findItem(R.id.action_edit_event));

            popup.setOnMenuItemClickListener(item -> {
                source.onNext(item.getItemId());
                return true;
            });

            popup.show();
        });
    }

    private void configureChangeStateItem(MenuItem item) {
        switch (state) {
            case HIDDEN:
                item.setVisible(false);
                break;
            case OPEN:
                item.setTitle(R.string.open_event);
                break;
            case CLOSE:
                item.setTitle(R.string.close_event);
        }
    }

    private void configureEditItem(MenuItem item) {
        item.setVisible(showEdit);
    }

    static class Builder {
        private MainItemMenu menu;

        Builder() {
            menu = new MainItemMenu();
        }

        Builder setShowEdit(boolean isShow) {
            menu.showEdit = isShow;
            return this;
        }

        Builder setChangeState(ChangeState state) {
            menu.state = state;
            return this;
        }

        public MainItemMenu build() {
            return menu;
        }
    }
}
