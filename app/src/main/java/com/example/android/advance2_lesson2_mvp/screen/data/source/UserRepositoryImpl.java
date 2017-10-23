package com.example.android.advance2_lesson2_mvp.screen.data.source;

import android.support.annotation.NonNull;
import com.example.android.advance2_lesson2_mvp.screen.data.UserRepository;
import com.example.android.advance2_lesson2_mvp.screen.data.model.User;
import com.example.android.advance2_lesson2_mvp.screen.data.source.local.realm.UserLocalDataSource;
import com.example.android.advance2_lesson2_mvp.screen.data.source.remote.UserRemoteDataSource;
import io.reactivex.Observable;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    
    private UserDataSource.RemoteDataSource mRemoteDataSource;
    private UserDataSource.LocalDataSource mLocalDataSource;
    
    public UserRepositoryImpl(UserRemoteDataSource remoteDataSource, UserLocalDataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    @Override
    public Observable<List<User>> searchUsersRemote(int limit, String keyWord) {
        return mRemoteDataSource.searchUsers(limit, keyWord);
    }

    @Override
    public Observable<List<User>> searchUsersLocal(int limit, String keyWord) {
        return mLocalDataSource.searchUsers(limit, keyWord);
    }

    @Override
    public List<User> searchUserSync(int limit, String keyWord) {
        return mLocalDataSource.searchUserSync(limit, keyWord);
    }

    @Override
    public Observable<Integer> insertUserLocal(@NonNull User user) {
        return mLocalDataSource.insertUser(user);
    }

    @Override
    public Observable<Integer> insertUsersLocal(@NonNull List<User> users) {
        return mLocalDataSource.insertUsers(users);
    }
}