package in.ac.kuvempu.dailynews.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.ac.kuvempu.dailynews.R;
import in.ac.kuvempu.dailynews.adapters.HeadLinesAdapter;
import in.ac.kuvempu.dailynews.custom.VerticalSpaceItemDecoration;
import in.ac.kuvempu.dailynews.database.NewsDBHelper;
import in.ac.kuvempu.dailynews.model.Article;
import in.ac.kuvempu.dailynews.model.ArticleItem;
import in.ac.kuvempu.dailynews.network.NewsApiEndpointInterface;
import in.ac.kuvempu.dailynews.network.RetrofitBuilder;
import in.ac.kuvempu.dailynews.util.Constants;
import in.ac.kuvempu.dailynews.util.Context;
import in.ac.kuvempu.dailynews.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeadLinesActivity extends AppCompatActivity implements HeadLinesAdapter.OnNewsItemClickListener, HeadLinesAdapter.OnNewsItemLongClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private String category;
    private final static String API_KEY = "be33695931034bc6a4ae6c41e58ff977";
    private RecyclerView headlinesRView;
    private HeadLinesAdapter headLinesAdapter;
    private String[] sources = new String[2];
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NewsDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headlines);

        if (!Utils.isConnectingToInternet(this)) {
            Toast.makeText(this, "Not able to connect to internet. Please check your data connection.", Toast.LENGTH_SHORT).show();
        }
        dbHelper = new NewsDBHelper(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = bundle.getString("CATEGORY");
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle((category != null && !category.trim().equals("")) ? category : "Headlines");
        }

        headlinesRView = (RecyclerView) findViewById(R.id.headlinesRView);
        registerForContextMenu(headlinesRView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        articles = dbHelper.getAllNews(category);
        loadHeadlines(articles);


        boolean selfNews = false;
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
                selfNews = true;
                break;
        }

        prepareData(selfNews);
    }

    private List<ArticleItem> articles = new ArrayList<>();
    private SweetAlertDialog pDialog;

    private void prepareData(boolean selfNews) {
        //Show progressdialog dismiss at the end
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        if (!isFinishing())
            pDialog.show();


        if (selfNews) {
            loadNewsFromDB();
        } else if (sources != null && sources.length > 0) {
            articles = null;
            articles = new ArrayList<>();
            getArticles(sources[0], 0);
        }
    }

    private void loadNewsFromDB() {

        if (category != null && !category.trim().equals("")) {
            List<ArticleItem> articleItems = dbHelper.getAllNews(category);
            articles.clear();
            if (articleItems != null) {

                articles.addAll(articleItems);
                loadHeadlines(articleItems);
            } else {

            }
        }

    }

    private void loadHeadlines(List<ArticleItem> articles) {
        if(pDialog != null && pDialog.isShowing()) {
            pDialog.dismissWithAnimation();
        }


        if (articles != null && !articles.isEmpty()) {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
//                Toast.makeText(this, "Refreshed...", Toast.LENGTH_SHORT).show();
            }
            if (headlinesRView != null) {
                headlinesRView.setAdapter(null);
                headLinesAdapter = new HeadLinesAdapter(articles, this, this, this, category);
//            headLinesAdapter.setOnNewsItemClickListener(this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                headlinesRView.setLayoutManager(mLayoutManager);
                headlinesRView.setItemAnimator(new DefaultItemAnimator());
                headlinesRView.addItemDecoration(new VerticalSpaceItemDecoration(Constants.VERTICAL_ITEM_SPACE));
                headlinesRView.setAdapter(headLinesAdapter);
                headLinesAdapter.notifyDataSetChanged();
            }

        } else {
            if (headlinesRView != null) {
                headlinesRView.setAdapter(null);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean selfNews = ((category != null && category.equalsIgnoreCase("News Reported by Me")) ? true : false);
        if (selfNews) {
            loadNewsFromDB();
        }

    }

    private MenuItem addNews = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.headlines_menu, menu);
        addNews = menu.findItem(R.id.submitNews);
        boolean selfNews = ((category != null && category.equalsIgnoreCase("News Reported by Me")) ? true : false);
        addNews.setVisible(selfNews);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.reloadNews:
                boolean selfNews = ((category != null && category.equalsIgnoreCase("News Reported by Me")) ? true : false);
                prepareData(selfNews);
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.submitNews:
                navigateToMyNewsActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateToMyNewsActivity() {
        Intent i = new Intent(this, MyNewsActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(ArticleItem item) {
        Context.getInstance().add(Constants.HEADLINE, item);
        navigteToDetailScreen();
    }

    private void navigteToDetailScreen() {

        Intent i = new Intent(this, NewsDetailsActivity.class);
        startActivity(i);
    }

    private void getArticles(String source, final int count) {

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
                if (response != null && response.body() != null) {
                    List<ArticleItem> localArticles = response.body().getArticles();
                    if (articles != null) {
                        articles.clear();
                        articles.addAll(localArticles);
                        if (dbHelper != null) {
                            dbHelper.deleteAllNews(category);
                            dbHelper.insertAllNews(articles, category);
                        }
                    }
                    if (count == 0) {
                        getArticles(sources[1], 1);
                    } else {
                        loadHeadlines(articles);
                    }
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
        boolean selfNews = ((category != null && category.equalsIgnoreCase("News Reported by Me")) ? true : false);
        prepareData(selfNews);
    }

    @Override
    public void onItemLongClick(final ArticleItem item, final int position) {

        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Do you want to delete this news ?")
                .setConfirmText("Yes,delete it!")
                .setCancelText("No,cancel!")
                .showCancelButton(true);
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismissWithAnimation();
                if (articles != null && articles.size() == 0) {
                    dbHelper.deleteNews(item.getId());
                    articles.remove(position);
                    boolean selfNews = category != null && category.equalsIgnoreCase("News Reported by Me");
                    if (selfNews) {
                        loadNewsFromDB();
                    }
                } else if (articles != null && !articles.isEmpty()) {
                    dbHelper.deleteNews(item.getId());
                    articles.remove(position);
                    boolean selfNews = category != null && category.equalsIgnoreCase("News Reported by Me");
                    if (selfNews) {
                        loadNewsFromDB();
                    }
                }
            }
        });
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismissWithAnimation();
            }
        });

        if (!isFinishing())
            dialog.show();

    }
}