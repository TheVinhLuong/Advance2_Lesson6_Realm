package com.example.android.advance2_lesson2_mvp.screen.data.source.local.realm;

import android.support.annotation.NonNull;
import android.util.Log;
import com.example.android.advance2_lesson2_mvp.screen.data.model.User;
import com.example.android.advance2_lesson2_mvp.screen.data.source.UserDataSource;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.functions.BiConsumer;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.List;

/**
 * Created by VinhTL on 23/10/2017.
 */

public class UserLocalDataSource implements UserDataSource.LocalDataSource{
    private RealmApi mRealmApi;

    public UserLocalDataSource(RealmApi realmApi) {
        mRealmApi = realmApi;
    }

    @Override
    public void openTransaction() {
        if(mRealmApi == null){
            mRealmApi = new RealmApi();
        }
    }

    @Override
    public void closeTransaction() {
        mRealmApi.closeRealmMainThread();
    }
    

    @Override
    public Observable<Integer> insertUser(@NonNull final User user) {
        return mRealmApi.realmModifyTransaction(new BiConsumer<ObservableEmitter<? super 
                Integer>, Realm>() {
            @Override
            public void accept(ObservableEmitter<? super Integer> observableEmitter, Realm realm)
                    throws Exception {
                realm.insert(user);
                observableEmitter.onComplete();
            }
        });
    }

    @Override
    public Observable<Integer> insertUsers(@NonNull final List<User> users) {
        return mRealmApi.realmModifyTransaction(new BiConsumer<ObservableEmitter<? super
                Integer>, Realm>() {
            @Override
            public void accept(ObservableEmitter<? super Integer> observableEmitter, Realm realm) {
                Log.d("wtf", "insert users  accept");
                try {
                    realm.insertOrUpdate(users);
                    observableEmitter.onNext(1);
                }catch (Exception e){
                    Log.d("wtf", "insert users " + e.getMessage());
                    observableEmitter.onError(new Throwable(new Exception("duplicate primary key")));
                }
            }
        });
    }

    @Override
    public Observable<Integer> updateUser(@NonNull final User user) {
        return mRealmApi.realmModifyTransaction(new BiConsumer<ObservableEmitter<? super 
                Integer>, Realm>() {
            @Override
            public void accept(ObservableEmitter<? super Integer> observableEmitter, Realm realm)
                    throws Exception {
                realm.insertOrUpdate(user);
                observableEmitter.onComplete();
            }
        });
    }

    @Override
    public Observable<Integer> deleteUser(@NonNull final User user) {
        return mRealmApi.realmModifyTransaction(new BiConsumer<ObservableEmitter<? super 
                Integer>, Realm>() {
            @Override
            public void accept(ObservableEmitter<? super Integer> observableEmitter, Realm realm)
                    throws Exception {
                RealmObject.deleteFromRealm(user);
                observableEmitter.onComplete();
            }
        });
    }

    @Override
    public Observable<List<User>> searchUsers(final int limit, final String keyWord) {
        return mRealmApi.realmGetDataTransaction(new BiConsumer<ObservableEmitter<? super
                List<User>>, Realm>() {
            @Override
            public void accept(ObservableEmitter<? super List<User>> observableEmitter, Realm realm)
                    throws Exception {
                RealmResults<User> users = realm.where(User.class).findAll();
                observableEmitter.onNext(users.subList(0, limit));
                observableEmitter.onComplete();
            }
        });
    }

    @Override
    public List<User> searchUserSync(int limit, String keyWord) {
        return mRealmApi.searchUserSync(keyWord, limit);
    }
    
    @Override
    public Observable<List<User>> getAllUser() {
        return mRealmApi.realmGetDataTransaction(new BiConsumer<ObservableEmitter<? super 
                List<User>>, Realm>() {
            @Override
            public void accept(ObservableEmitter<? super List<User>> observableEmitter, Realm realm)
                    throws Exception {
                RealmResults<User> users = realm.where(User.class).findAll();
                observableEmitter.onNext(users);
                observableEmitter.onComplete();
            }
        });
    }
}
