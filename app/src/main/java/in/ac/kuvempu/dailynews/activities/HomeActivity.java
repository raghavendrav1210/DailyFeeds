package in.ac.kuvempu.dailynews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.ac.kuvempu.dailynews.R;
import in.ac.kuvempu.dailynews.adapters.CategoriesAdapter;
import in.ac.kuvempu.dailynews.custom.VerticalSpaceItemDecoration;
import in.ac.kuvempu.dailynews.model.CATEGORY;
import in.ac.kuvempu.dailynews.util.Constants;

public class HomeActivity extends AppCompatActivity implements CategoriesAdapter.OnItemClickListener {

    private RecyclerView catRecyclerView;
    private String[] categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        catRecyclerView = (RecyclerView) findViewById(R.id.catRecyclerView);
        categories = getResources().getStringArray(R.array.categories);

        List<CATEGORY> categoryList = null;
        if (categories != null && categories.length > 0) {
            categoryList = new ArrayList<>();

            CATEGORY genCategory = new CATEGORY();
            genCategory.setCatName("General");
            genCategory.setImageId(getResources().getDrawable(R.drawable.general));
            categoryList.add(genCategory);

            CATEGORY ecCategory = new CATEGORY();
            ecCategory.setCatName("Business");
            ecCategory.setImageId(getResources().getDrawable(R.drawable.business));
            categoryList.add(ecCategory);

            CATEGORY techCategory = new CATEGORY();
            techCategory.setCatName("Technology");
            techCategory.setImageId(getResources().getDrawable(R.drawable.technology));
            categoryList.add(techCategory);

            CATEGORY sportsCategory = new CATEGORY();
            sportsCategory.setCatName("Sports");
            sportsCategory.setImageId(getResources().getDrawable(R.drawable.sports));
            categoryList.add(sportsCategory);

            CATEGORY myCategory = new CATEGORY();
            myCategory.setCatName("News Reported by Me");
            myCategory.setImageId(getResources().getDrawable(R.drawable.self));
            categoryList.add(myCategory);

        }

        CategoriesAdapter adapter = new CategoriesAdapter(categoryList);
        adapter.setOnItemClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        catRecyclerView.setLayoutManager(mLayoutManager);
        catRecyclerView.setItemAnimator(new DefaultItemAnimator());
        catRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(Constants.VERTICAL_ITEM_SPACE));
        catRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {

        Toast.makeText(this, categories[position], Toast.LENGTH_SHORT).show();

        switch (categories[position]) {
            case "General":
            case "Business":
            case "Technology":
            case "Sports":
            case "News Reported by Me":
                navigateToHeadlines(categories[position]);
                break;
        }
    }

    private void navigateToHeadlines(String category) {

        Intent i = new Intent(this, HeadLinesActivity.class);
        i.putExtra("CATEGORY", category);
        startActivity(i);
    }
}
