package com.example.android.advance2_lesson2_mvp.screen.searchresult;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.android.advance2_lesson2_mvp.R;
import com.example.android.advance2_lesson2_mvp.screen.BaseActivity;
import com.example.android.advance2_lesson2_mvp.screen.data.model.User;
import com.example.android.advance2_lesson2_mvp.screen.utils.Constant;
import java.util.ArrayList;

/**
 * SearchResult Screen.
 */
public class SearchResultActivity extends BaseActivity implements SearchResultContract.View {

    private SearchResultContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private SearchResultAdapter mSearchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);

        mPresenter = new SearchResultPresenter();
        mPresenter.setView(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.resultRecyclerView);

        ArrayList<User> users = getIntent().getParcelableArrayListExtra(Constant.LIST_USER_ARGS);
        mSearchResultAdapter = new SearchResultAdapter(this, users);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mSearchResultAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onStop() {
        mPresenter.onStop();
        super.onStop();
    }
}
