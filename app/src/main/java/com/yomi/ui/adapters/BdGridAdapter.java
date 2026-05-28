package com.yomi.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yomi.database.StoryEntity;
import com.yomi.databinding.ItemBdGridBinding;
import java.util.ArrayList;
import java.util.List;

public class BdGridAdapter extends RecyclerView.Adapter<BdGridAdapter.ViewHolder> {

    private List<StoryEntity> stories = new ArrayList<>();
    private OnStoryClickListener listener;

    public interface OnStoryClickListener {
        void onStoryClick(StoryEntity story);
    }

    public void setOnStoryClickListener(OnStoryClickListener listener) {
        this.listener = listener;
    }

    public void setStories(List<StoryEntity> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBdGridBinding binding = ItemBdGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoryEntity story = stories.get(position);
        holder.binding.tvGridTitle.setText(story.getTitle());
        holder.binding.tvGridMeta.setText(story.getTotalPanels() + " panels");
        
        // Mocking some data for the mockup look (Image 6)
        holder.binding.tvGridReact.setText("🤩 12");
        
        if (position % 3 == 0) holder.binding.tvGridThumb.setText("👾");
        else if (position % 3 == 1) holder.binding.tvGridThumb.setText("🌙");
        else holder.binding.tvGridThumb.setText("🌿");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onStoryClick(story);
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemBdGridBinding binding;
        ViewHolder(ItemBdGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
