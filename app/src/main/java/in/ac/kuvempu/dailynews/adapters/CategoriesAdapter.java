package in.ac.kuvempu.dailynews.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.ac.kuvempu.dailynews.R;
import in.ac.kuvempu.dailynews.model.CATEGORY;

/**
 * Created by raghav on 4/6/2017.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private List<CATEGORY> catList;

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView catLogo;
//        private LinearLayout parentLayout;
//        private LinearLayout parentLayout1;
        private CardView mCardView;

        public CategoriesViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.catTitle);
            catLogo = (ImageView) view.findViewById(R.id.catLogo);
//            parentLayout = (LinearLayout)view.findViewById(R.id.catParentLayout);
//            parentLayout1 = (LinearLayout)view.findViewById(R.id.catParentLayout1);
            mCardView = (CardView) view.findViewById(R.id.catCardView);
        }
    }

    public CategoriesAdapter(List<CATEGORY> catList, OnItemClickListener clickListener) {
        this.catList = catList;
        this.clickListener = clickListener;
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_list_item, parent, false);

        return new CategoriesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoriesViewHolder holder, final int position) {
        CATEGORY category = catList.get(position);
        holder.title.setText(category.getCatName());
        holder.catLogo.setImageDrawable(category.getImageId());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });

/*        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });

        holder.parentLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

    private OnItemClickListener clickListener;
    public void setOnItemClickListener(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }
}