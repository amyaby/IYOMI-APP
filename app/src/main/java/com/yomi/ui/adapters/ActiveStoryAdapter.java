package com.yomi.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;
import com.yomi.R;
import com.yomi.databinding.ItemStoryBinding;
import com.yomi.model.Story;
import com.yomi.model.StoryStatus;
import java.util.ArrayList;
import java.util.List;

public class ActiveStoryAdapter extends RecyclerView.Adapter<ActiveStoryAdapter.ViewHolder> {

    private List<Story> stories = new ArrayList<>();
    private long currentUserId;
    private OnStoryClickListener onStoryClickListener;

    public interface OnStoryClickListener {
        void onStoryClick(Story story);
    }

    public void setOnStoryClickListener(OnStoryClickListener listener) {
        this.onStoryClickListener = listener;
    }

    public void setStories(List<Story> stories, long currentUserId) {
        this.stories = stories;
        this.currentUserId = currentUserId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStoryBinding binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story story = stories.get(position);
        holder.binding.tvStoryTitle.setText(story.getTitle());
        
        String meta = holder.itemView.getContext().getString(R.string.story_meta_format, 
                story.getTotalPanels(), story.getPlayerOrder().size());
        holder.binding.tvStoryMeta.setText(meta);

        boolean isMyTurn = false;
        List<Long> order = story.getPlayerOrder();
        int currentIndex = story.getCurrentPanelIndex();
        
        if (story.getStatus() == StoryStatus.IN_PROGRESS && currentIndex < order.size()) {
            if (order.get(currentIndex) == currentUserId) {
                isMyTurn = true;
            }
        }

        if (isMyTurn) {
            holder.binding.tvStatusPill.setText("Ton tour");
            holder.binding.tvStatusPill.setBackgroundResource(R.drawable.bg_pill_pink);
            holder.binding.tvStatusPill.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.pill_p_text));
            holder.binding.tvStatusPill.setAlpha(1.0f);
        } else {
            holder.binding.tvStatusPill.setText("En attente");
            holder.binding.tvStatusPill.setBackgroundResource(R.drawable.bg_pill_purple);
            holder.binding.tvStatusPill.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.pill_v_text));
            holder.binding.tvStatusPill.setAlpha(0.6f);
        }

        setupPanelPreview(holder.binding.panelPreviewContainer, story, isMyTurn);

        holder.itemView.setOnClickListener(v -> {
            if (onStoryClickListener != null) onStoryClickListener.onStoryClick(story);
        });
    }

    private void setupPanelPreview(LinearLayout container, Story story, boolean isMyTurn) {
        container.removeAllViews();
        int total = story.getTotalPanels();
        int current = story.getCurrentPanelIndex();
        
        for (int i = 0; i < total; i++) {
            View panelView = new View(container.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 48);
            lp.weight = 1;
            if (i < total - 1) lp.setMarginEnd(8);
            panelView.setLayoutParams(lp);
            
            panelView.setBackgroundResource(R.drawable.bg_nav_btn);
            
            if (i < current) {
                panelView.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    ContextCompat.getColor(container.getContext(), R.color.pill_v_bg)));
            } else if (i == current) {
                int colorRes = isMyTurn ? R.color.pill_p_bg : R.color.pill_v_bg;
                panelView.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    ContextCompat.getColor(container.getContext(), colorRes)));
                if (isMyTurn) panelView.setAlpha(1.0f);
                else panelView.setAlpha(0.5f);
            } else {
                panelView.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    ColorUtils.setAlphaComponent(ContextCompat.getColor(container.getContext(), R.color.text_muted), 40)));
            }
            
            container.addView(panelView);
        }
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemStoryBinding binding;
        
        ViewHolder(ItemStoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
