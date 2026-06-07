package com.yomi.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yomi.R;
import com.yomi.model.Story;
import java.util.ArrayList;
import java.util.List;

public class ExplorerAdapter extends RecyclerView.Adapter<ExplorerAdapter.ViewHolder> {

    private List<Story> stories = new ArrayList<>();
    private OnStoryClickListener listener;

    public interface OnStoryClickListener {
        void onStoryClick(Story story);
    }

    public void setOnStoryClickListener(OnStoryClickListener listener) {
        this.listener = listener;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bd_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story story = stories.get(position);
        
        holder.tvTitle.setText(story.getTitle());
        holder.tvMeta.setText(story.getTotalPanels() + " panels");
        holder.tvReact.setText("🔥 " + story.getReactionCount());

        if (position % 2 == 0) holder.tvThumb.setText("🚀");
        else holder.tvThumb.setText("🐉");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onStoryClick(story);
        });
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
