package com.example.android.advance2_lesson2_mvp.screen.main;

import android.util.Log;
import com.example.android.advance2_lesson2_mvp.screen.data.UserRepository;
import com.example.android.advance2_lesson2_mvp.screen.data.model.User;
import com.example.android.advance2_lesson2_mvp.screen.utils.rx.BaseSchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

final class MainPresenter implements MainContract.Presenter {
    private static final String TAG = MainPresenter.class.getSimpleName();

    private UserRepository mUserRepository;
    private MainContract.View mView;
    private BaseSchedulerProvider mSchedulerProvider;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public MainPresenter(UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    @Override
    public void setView(MainContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {

    }

    @Override
    public void setSchedulerProvider(BaseSchedulerProvider schedulerProvider) {
        mSchedulerProvider = schedulerProvider;
    }

    @Override
    public void searchUsers(final int limit, final String keyWord) {
        Log.d(TAG, "Begin searchUser ");
        final boolean[] isLoadFromLocal = { false };
        final Disposable disposable = mUserRepository.searchUsersRemote(limit, keyWord)
                .subscribeOn(Schedulers.newThread())
                .observeOn(mSchedulerProvider.ui())
                .onErrorReturn(new Function<Throwable, List<User>>() {
                    @Override
                    public List<User> apply(Throwable throwable) throws Exception {
                        Log.d(TAG, "onError thread: " + Thread.currentThread().getName());
                        Log.d(TAG, "Begin apply ");
                        isLoadFromLocal[0] = true;
                        List<User> users = mUserRepository.searchUserSync(limit, keyWord);
                        Log.d(TAG, "network error?, throw message = " + throwable.getMessage());
                        Log.d(TAG, "network error?, users = ");
                        mView.showToast(throwable.getMessage());
                        return users;
                    }
                })
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(final List<User> users) {
                        Log.d(TAG,
                                "on subscribe return thread: " + Thread.currentThread().getName());
                        if (isLoadFromLocal[0]) {
                            Log.d(TAG, "accept?");
                            mView.onSearchUsersSuccess(users);
                            mCompositeDisposable.clear();
                            return;
                        }
                        mUserRepository.insertUsersLocal(users)
                                .subscribeOn(mSchedulerProvider.ui())
                                .onErrorReturn(new Function<Throwable, Integer>() {
                                    @Override
                                    public Integer apply(Throwable throwable) throws Exception {
                                        Log.d(TAG, "insert users on error return");
                                        return 1;
                                    }
                                })
                                .subscribe(new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer integer) throws Exception {
                                        Log.d(TAG, "accept users");
                                        mView.onSearchUsersSuccess(users);
                                        mCompositeDisposable.clear();
                                    }
                                });
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
