package in.ac.kuvempu.dailynews.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.ac.kuvempu.dailynews.R;
import in.ac.kuvempu.dailynews.model.ArticleItem;
import in.ac.kuvempu.dailynews.util.Constants;

/**
 * Created by raghav on 4/6/2017.
 */

public class HeadLinesAdapter extends RecyclerView.Adapter<HeadLinesAdapter.HeadLinesViewHolder> {

    private List<ArticleItem> headlines;
    private Context mContext;

    public class HeadLinesViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView headlinesLogo;
        private LinearLayout parentLayout;
        private CardView mCardView;

        public HeadLinesViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.headlinesTitle);
            headlinesLogo = (ImageView) view.findViewById(R.id.headlinesImg);
            mCardView = (CardView) view.findViewById(R.id.headlinesCardView);
        }
    }

    public HeadLinesAdapter(List<ArticleItem> headlines, Context mContext, OnNewsItemClickListener clickListener) {
        this.headlines = headlines;
        this.mContext = mContext;
        this.clickListener = clickListener;
    }

    @Override
    public HeadLinesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.headlines_list_item, parent, false);

        return new HeadLinesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HeadLinesViewHolder holder, final int position) {
        final ArticleItem headline = headlines.get(position);

        String title = headline.getTitle();
        if(title.endsWith(Constants.TOI)) {
            title = title.replace(Constants.TOI, "");
        } else if(title.endsWith(Constants.TOI1)) {
            title = title.replace(Constants.TOI1, "");
        }

        if(title.endsWith("-")){
            title = title.substring(0, title.lastIndexOf("-"));
        }
        holder.title.setText(title);

        Glide.with(mContext).load(headline.getUrlToImage()).into(holder.headlinesLogo);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onItemClick(headline);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return headlines.size();
    }

    public interface OnNewsItemClickListener {
        public void onItemClick(ArticleItem item);
    }

    private OnNewsItemClickListener clickListener;
    public void setOnNewsItemClickListener(OnNewsItemClickListener clickListener){
        this.clickListener = clickListener;
    }
}