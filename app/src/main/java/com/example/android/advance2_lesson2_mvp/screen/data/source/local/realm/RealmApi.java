package com.example.android.advance2_lesson2_mvp.screen.data.source.local.realm;

import android.util.Log;
import com.example.android.advance2_lesson2_mvp.screen.data.model.User;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiConsumer;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.List;

/**
 * Created by VinhTL on 23/10/2017.
 */

public class RealmApi {
    private Realm mRealm;

    public RealmApi() {
        mRealm = Realm.getInstance(Realm.getDefaultConfiguration());
    }
    
    public <T> Observable<T> realmModifyTransaction(
            final BiConsumer<ObservableEmitter<? super T>, Realm> biConsumer) {
        mRealm = Realm.getDefaultInstance();
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(final ObservableEmitter<T> emitter) throws Exception {
                mRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        try {
                            biConsumer.accept(emitter, realm);
                        } catch (Exception e1) {
                            emitter.onError(e1);
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        emitter.onComplete();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        emitter.onError(error);
                    }
                });
            }
        });
    }
    
    public <T> Observable<T> realmGetDataTransaction(final BiConsumer<ObservableEmitter<? super T>, Realm> biConsumer){
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                biConsumer.accept(emitter, mRealm);
                emitter.onComplete();
            }
        });
    }
    
    public void closeRealmMainThread(){
        mRealm = Realm.getDefaultInstance();
        if(mRealm != null && !mRealm.isClosed()){
            mRealm.close();
            mRealm = null;
        }
    }
    
    public List<User> searchUserSync(String keyWord, int limit){
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            RealmResults<User> results = mRealm.where(User.class).findAll();
            mRealm.commitTransaction();
            return results;
        }catch (Exception e){
            Log.d("wtf", e.getMessage());
        }
        return null;
    }
}
