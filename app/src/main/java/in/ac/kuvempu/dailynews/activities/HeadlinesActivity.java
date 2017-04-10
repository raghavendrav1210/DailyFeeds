package in.ac.kuvempu.dailynews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.ac.kuvempu.dailynews.R;
import in.ac.kuvempu.dailynews.adapters.HeadLinesAdapter;
import in.ac.kuvempu.dailynews.custom.VerticalSpaceItemDecoration;
import in.ac.kuvempu.dailynews.model.Article;
import in.ac.kuvempu.dailynews.model.ArticleItem;
import in.ac.kuvempu.dailynews.network.NewsApiEndpointInterface;
import in.ac.kuvempu.dailynews.network.RetrofitBuilder;
import in.ac.kuvempu.dailynews.util.Constants;
import in.ac.kuvempu.dailynews.util.Context;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeadLinesActivity extends AppCompatActivity implements HeadLinesAdapter.OnNewsItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private String category;
    private final static String API_KEY = "be33695931034bc6a4ae6c41e58ff977";
    private RecyclerView headlinesRView;
    private HeadLinesAdapter headLinesAdapter;
    private String[] sources = new String[2];
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headlines);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = bundle.getString("CATEGORY");
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle((category != null && !category.trim().equals("")) ? category : "Headlines");
        }

        headlinesRView = (RecyclerView) findViewById(R.id.headlinesRView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        switch (category) {
            case "General":
                sources[0] = Constants.GENERAL;
                sources[1] = Constants.GENERAL1;
                break;
            case "Business":
                sources[0] = Constants.BUSINESS;
                sources[1] = Constants.BUSINESS1;
                break;
            case "Technology":
                sources[0] = Constants.TECHNOLOGY;
                sources[1] = Constants.TECHNOLOGY1;
                break;
            case "Sports":
                sources[0] = Constants.SPORTS;
                sources[1] = Constants.SPORTS1;
                break;
            case "News Reported by Me":
                break;
        }

        prepareData();
    }

    private List<ArticleItem> articles = new ArrayList<>();
    private void prepareData() {

        if(sources != null && sources.length > 0) {
            articles = null;
            articles = new ArrayList<>();
            getArticles(sources[0], 0);
        }
    }

    private void loadHeadlines(List<ArticleItem> articles) {

        if (articles != null && !articles.isEmpty()) {
            if(mSwipeRefreshLayout != null){
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "Refreshed...", Toast.LENGTH_SHORT).show();
            }
            headLinesAdapter = new HeadLinesAdapter(articles, this, this);
//            headLinesAdapter.setOnNewsItemClickListener(this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            headlinesRView.setLayoutManager(mLayoutManager);
            headlinesRView.setItemAnimator(new DefaultItemAnimator());
            headlinesRView.addItemDecoration(new VerticalSpaceItemDecoration(Constants.VERTICAL_ITEM_SPACE));
            headlinesRView.setAdapter(headLinesAdapter);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.headlines_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.reloadNews:
                prepareData();
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(ArticleItem item) {
        Context.getInstance().add(Constants.HEADLINE, item);
        navigteToDetailScreen();
    }

    private void navigteToDetailScreen(){

        Intent i = new Intent(this, NewsDetailsActivity.class);
        startActivity(i);
    }

    private void getArticles(String source, final int count){

        NewsApiEndpointInterface apiService =
                RetrofitBuilder.getClient().create(NewsApiEndpointInterface.class);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }
        Call<Article> call = apiService.getNews(source, "top", API_KEY);
        call.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                List<ArticleItem> localArticles = response.body().getArticles();
                Log.d("Success", "Number of movies received: " + articles.size());
                articles.addAll(localArticles);
                if(count == 0) {
                    getArticles(sources[1], 1);
                } else {
                    loadHeadlines(articles);
                }
            }

            @Override
            public void onFailure(Call<Article> call, Throwable throwable) {
                Log.e("Failed", throwable.toString());
            }

        });
    }

    @Override
    public void onRefresh() {
        prepareData();
    }
}
