package com.example.android.advance2_lesson2_mvp.screen.data.source;

import android.support.annotation.NonNull;
import com.example.android.advance2_lesson2_mvp.screen.data.model.User;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by VinhTL on 18/10/2017.
 */

public interface UserDataSource{
    interface RemoteDataSource {
        Observable<List<User>> searchUsers(int limit, String keyWord);
    }
    
    interface LocalDataSource {
        void openTransaction();

        void closeTransaction();
        
        Observable<Integer> insertUser(@NonNull User user);

        Observable<Integer> insertUsers(@NonNull List<User> users);
        
        Observable<Integer> updateUser(@NonNull User user);
        
        Observable<Integer> deleteUser(@NonNull User user);

        Observable<List<User>> searchUsers(int limit, String keyWord);
        
        List<User> searchUserSync(int limit, String keyWord);
        
        Observable<List<User>> getAllUser();
    }
}
