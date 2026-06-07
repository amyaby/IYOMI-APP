package com.yomi.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yomi.databinding.ItemBdGridBinding;
import com.yomi.model.Story;
import java.util.ArrayList;
import java.util.List;

public class BdGridAdapter extends RecyclerView.Adapter<BdGridAdapter.ViewHolder> {

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
        ItemBdGridBinding binding = ItemBdGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story story = stories.get(position);
        holder.binding.tvGridTitle.setText(story.getTitle());
        holder.binding.tvGridMeta.setText(story.getTotalPanels() + " panels");
        holder.binding.tvGridReact.setText("🤩 " + story.getReactionCount());
        
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
