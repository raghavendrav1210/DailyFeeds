package in.ac.kuvempu.dailynews.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import in.ac.kuvempu.dailynews.R;
import in.ac.kuvempu.dailynews.model.ArticleItem;
import in.ac.kuvempu.dailynews.util.Constants;

public class NewsDetailsActivity extends AppCompatActivity {

    private ArticleItem SELECTED_ITEM = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        TextView newsDetailTitle = (TextView) findViewById(R.id.newsDetailsTitle);
        TextView newsDetailDesc = (TextView) findViewById(R.id.newsDetailsDesc);
        ImageView newsImg = (ImageView) findViewById(R.id.newsImg);

        if (!isConnectingToInternet(this)) {
            Toast.makeText(this, "Not able to connect to internet. Please check your data connection.", Toast.LENGTH_SHORT).show();
            return;
        }

        Object object = in.ac.kuvempu.dailynews.util.Context.getInstance().get(Constants.HEADLINE);
        if(object != null){
            ArticleItem articleItem = (ArticleItem) object;
            SELECTED_ITEM = articleItem;
            newsDetailTitle.setText(articleItem.getTitle());
            newsDetailDesc.setText(articleItem.getDescription());
            Glide.with(this).load(articleItem.getUrlToImage()).into(newsImg);

        }
    }

    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.shareNews:
                shareNews();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareNews() {
        if(SELECTED_ITEM != null){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = SELECTED_ITEM.getTitle() +"\n"+SELECTED_ITEM.getUrl() +" find more at daily news";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, SELECTED_ITEM.getTitle() + "from Daily News");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }

    }

    @Override
    public void onBackPressed() {
        in.ac.kuvempu.dailynews.util.Context.getInstance().clear();
        super.onBackPressed();
    }
}