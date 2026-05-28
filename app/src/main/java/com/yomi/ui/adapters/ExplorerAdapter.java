package com.yomi.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yomi.R;
import com.yomi.database.StoryEntity;
import java.util.ArrayList;
import java.util.List;

public class ExplorerAdapter extends RecyclerView.Adapter<ExplorerAdapter.ViewHolder> {

    private List<StoryEntity> stories = new ArrayList<>();

    public void setStories(List<StoryEntity> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Fix: Use verified item_bd_grid layout to avoid "cannot find symbol" error
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bd_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoryEntity story = stories.get(position);
        holder.tvTitle.setText(story.getTitle());
        holder.tvMeta.setText(story.getTotalPanels() + " panels");
        holder.tvReact.setText("🔥 " + (200 - position * 10));

        if (position % 2 == 0) holder.tvThumb.setText("🚀");
        else holder.tvThumb.setText("🐉");
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvThumb, tvTitle, tvMeta, tvReact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvThumb = itemView.findViewById(R.id.tvGridThumb);
            tvTitle = itemView.findViewById(R.id.tvGridTitle);
            tvMeta = itemView.findViewById(R.id.tvGridMeta);
            tvReact = itemView.findViewById(R.id.tvGridReact);
        }
    }
}
