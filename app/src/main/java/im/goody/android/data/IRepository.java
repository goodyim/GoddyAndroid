package im.goody.android.data;

import java.util.List;

import im.goody.android.data.dto.Auth;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Register;
import io.reactivex.Observable;

public interface IRepository {
    Observable<List<Deal>> getNews();
    Observable<String> login(Auth data);
    Observable<String> register(Register data);
}
