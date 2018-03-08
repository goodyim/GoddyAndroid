package im.goody.android.screens.news;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import im.goody.android.R;
import io.reactivex.Observable;

public class NewsItemMenu {
    private NewsItemMenu() {}

    private boolean showEdit;
    private boolean showDelete;
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
            configureEditItem(menu.findItem(R.id.action_edit_post));
            configureDeleteItem(menu.findItem(R.id.action_delete_post));

            popup.setOnMenuItemClickListener(item -> {
                source.onNext(item.getItemId());
                return true;
            });

            popup.show();
        });
    }

    private void configureDeleteItem(MenuItem item) {
        item.setVisible(showDelete);
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
        private NewsItemMenu menu;

        Builder() {
            menu = new NewsItemMenu();
        }

        Builder setShowEdit(boolean isShow) {
            menu.showEdit = isShow;
            return this;
        }

        Builder setChangeState(ChangeState state) {
            menu.state = state;
            return this;
        }

        Builder setShowDelete(boolean showDelete) {
            menu.showDelete = showDelete;
            return this;
        }

        public NewsItemMenu build() {
            return menu;
        }
    }
}
