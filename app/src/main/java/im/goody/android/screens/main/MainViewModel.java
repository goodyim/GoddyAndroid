package im.goody.android.screens.main;

import java.util.ArrayList;
import java.util.List;

import im.goody.android.data.dto.Deal;

class MainViewModel {
    private List<Deal> data;
    private int page;

    MainViewModel() {
        page = 1;
    }


    public List<Deal> getData() {
        return data == null ? null : new ArrayList<>(data);
    }

    void setData(List<Deal> data) {
        this.data = new ArrayList<>(data);
    }

    void addData(List<Deal> additionalData) {
        data.addAll(additionalData);
    }

    int incrementPageAndGet() {
        page++;
        return page;
    }

    int resetPageAndGet() {
        page = 1;
        return page;
    }
}
