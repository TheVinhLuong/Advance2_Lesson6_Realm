package com.example.android.advance2_lesson2_mvp.screen.data;

import android.support.annotation.NonNull;
import com.example.android.advance2_lesson2_mvp.screen.data.model.User;
import io.reactivex.Observable;
import java.util.List;

public interface UserRepository {
    Observable<List<User>> searchUsersRemote(int limit, String keyWord);
    Observable<List<User>> searchUsersLocal(int limit, String keyWord);
    List<User> searchUserSync(int limit, String keyWord);
    Observable<Integer> insertUserLocal(@NonNull final User user);
    Observable<Integer> insertUsersLocal(@NonNull final List<User> users);
}