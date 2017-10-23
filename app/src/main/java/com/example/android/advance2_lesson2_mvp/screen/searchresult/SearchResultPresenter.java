package com.example.android.advance2_lesson2_mvp.screen.searchresult;

/**
 * Listens to user actions from the UI ({@link SearchResultActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
final class SearchResultPresenter implements SearchResultContract.Presenter {
    private static final String TAG = SearchResultPresenter.class.getName();

    private SearchResultContract.View mView;

    public SearchResultPresenter() {
    }

    @Override
    public void setView(SearchResultContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }
}
