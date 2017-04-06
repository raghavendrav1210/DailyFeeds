package in.ac.kuvempu.dailynews.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.ac.kuvempu.dailynews.R;

/**
 * Created by raghav on 4/6/2017.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private String[] catList;

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        private LinearLayout parentLayout;

        public CategoriesViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.catTitle);
            parentLayout = (LinearLayout)view.findViewById(R.id.catParentLayout);
        }
    }

    public CategoriesAdapter(String[] catList) {
        this.catList = catList;
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_list_item, parent, false);

        return new CategoriesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, final int position) {
        String category = catList[position];
        holder.title.setText(category);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return catList.length;
    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

    private OnItemClickListener clickListener;
    public void setOnItemClickListener(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }
}