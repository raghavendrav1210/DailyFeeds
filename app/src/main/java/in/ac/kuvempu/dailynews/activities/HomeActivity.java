package in.ac.kuvempu.dailynews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import in.ac.kuvempu.dailynews.R;
import in.ac.kuvempu.dailynews.adapters.CategoriesAdapter;
import in.ac.kuvempu.dailynews.custom.VerticalSpaceItemDecoration;
import in.ac.kuvempu.dailynews.model.Article;
import in.ac.kuvempu.dailynews.model.ArticleItem;
import in.ac.kuvempu.dailynews.network.NewsApiEndpointInterface;
import in.ac.kuvempu.dailynews.network.RetrofitBuilder;
import in.ac.kuvempu.dailynews.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements CategoriesAdapter.OnItemClickListener {

    private final static String API_KEY = "be33695931034bc6a4ae6c41e58ff977";
    private RecyclerView catRecyclerView;
    private static final int VERTICAL_ITEM_SPACE = 2;
    private String[] categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        catRecyclerView = (RecyclerView) findViewById(R.id.catRecyclerView);
        categories = getResources().getStringArray(R.array.categories);

        CategoriesAdapter adapter = new CategoriesAdapter(categories);
        adapter.setOnItemClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        catRecyclerView.setLayoutManager(mLayoutManager);
        catRecyclerView.setItemAnimator(new DefaultItemAnimator());
        catRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        catRecyclerView.setAdapter(adapter);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }

        NewsApiEndpointInterface apiService =
                RetrofitBuilder.getClient().create(NewsApiEndpointInterface.class);

        Call<Article> call = apiService.getNews("the-times-of-india", "top", API_KEY);
        call.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                List<ArticleItem> articles = response.body().getArticles();
                Log.d("Success", "Number of movies received: " + articles.size());
            }

            @Override
            public void onFailure(Call<Article> call, Throwable throwable) {
                Log.e("Failed", throwable.toString());
            }

        });
    }

    @Override
    public void onItemClick(int position) {

        Toast.makeText(this, categories[position], Toast.LENGTH_SHORT).show();

        switch (categories[position]) {
            case "General":
            case "Economics":
            case "Technology":
            case "Sports":
            case "News Reported by Me":
                navigateToHeadlines(categories[position]);
                break;
        }
    }

    private void navigateToHeadlines(String category){

        Intent i = new Intent(this, HeadlinesActivity.class);
        i.putExtra("CATEGORY", category);
        startActivity(i);
    }
}
