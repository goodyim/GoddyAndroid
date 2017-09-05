package im.goody.android.screens.detail_post;

import im.goody.android.data.dto.Deal;

class DetailPostViewModel {
    private Deal deal;
    private int position;
    private long id;

    DetailPostViewModel() {
        position = 0;
    }

    // ======= region getters =======

    public Deal getDeal() {
        return deal;
    }

    int getPosition() {
        return position;
    }

    public long getId() {
        return id;
    }

    // endregion

    // ======= region setters =======

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    void setPosition(int position) {
        this.position = position;
    }

    public void setId(long id) {
        this.id = id;
    }

    // endregion
}
