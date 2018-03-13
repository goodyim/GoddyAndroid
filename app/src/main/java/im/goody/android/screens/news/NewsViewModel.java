package im.goody.android.screens.news;

import java.util.ArrayList;
import java.util.List;

class NewsViewModel {
    private List<NewsItemViewModel> data;
    private int page;
    private int position;

    NewsViewModel() {
        page = 1;
        position = 0;
    }


    public List<NewsItemViewModel> getData() {
        return data == null ? null : new ArrayList<>(data);
    }

    void setData(List<NewsItemViewModel> data) {
        this.data = new ArrayList<>(data);
    }

    void addData(List<NewsItemViewModel> additionalData) {
        data.addAll(additionalData);
    }

    int incrementPageAndGet() {
        page++;
        return page;
    }

    void decrementPage() {
        page--;
    }

    int resetPageAndGet() {
        page = 1;
        return page;
    }

    int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }
}
