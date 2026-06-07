package com.yomi.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yomi.databinding.ItemExploreBinding;
import com.yomi.model.Story;
import java.util.ArrayList;
import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ViewHolder> {

    private List<Story> stories = new ArrayList<>();
    private OnStoryClickListener listener;
    private OnReactionClickListener reactionListener;

    public interface OnStoryClickListener {
        void onStoryClick(Story story);
    }

    public interface OnReactionClickListener {
        void onReactionClick(long storyId);
    }

    public void setOnStoryClickListener(OnStoryClickListener listener) {
        this.listener = listener;
    }

    public void setOnReactionClickListener(OnReactionClickListener listener) {
        this.reactionListener = listener;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExploreBinding binding = ItemExploreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story story = stories.get(position);
        
        holder.binding.tvExploreTitle.setText(story.getTitle());
        holder.binding.tvExploreSub.setText(story.getTotalPanels() + " panels");
        
        // Visual variety
        if (story.getId() % 3 == 0) holder.binding.tvExploreThumb.setText("🚀");
        else if (story.getId() % 3 == 1) holder.binding.tvExploreThumb.setText("🐉");
        else holder.binding.tvExploreThumb.setText("🐙");
        
        holder.binding.tvExploreReact.setText("🔥 " + story.getReactionCount());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onStoryClick(story);
        });

        holder.binding.tvExploreReact.setOnClickListener(v -> {
            if (reactionListener != null) reactionListener.onReactionClick(story.getId());
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemExploreBinding binding;
        ViewHolder(ItemExploreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
