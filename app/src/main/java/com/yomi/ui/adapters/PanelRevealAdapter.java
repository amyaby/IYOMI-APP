package com.yomi.ui.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yomi.database.PanelEntity;
import com.yomi.database.ReactionEntity;
import com.yomi.databinding.ItemRevealPanelBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PanelRevealAdapter extends RecyclerView.Adapter<PanelRevealAdapter.ViewHolder> {

    private List<PanelEntity> panels = new ArrayList<>();
    private List<ReactionEntity> allReactions = new ArrayList<>();

    public void setPanels(List<PanelEntity> panels) {
        this.panels = panels;
        notifyDataSetChanged();
    }

    public void setReactions(List<ReactionEntity> reactions) {
        this.allReactions = reactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRevealPanelBinding binding = ItemRevealPanelBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PanelEntity panel = panels.get(position);
        
        holder.binding.tvPanelNumber.setText(String.valueOf(panel.getPanelIndex() + 1));
        holder.binding.tvAuthorName.setText("@" + panel.getAuthorName());

        if (panel.getImagePath() != null) {
            File imgFile = new File(panel.getImagePath());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.binding.ivPanelImage.setImageBitmap(myBitmap);
            }
        }

        if (panel.getDialogText() != null && !panel.getDialogText().isEmpty()) {
            holder.binding.tvPanelBubble.setVisibility(View.VISIBLE);
            holder.binding.tvPanelBubble.setText(panel.getDialogText());
        } else {
            holder.binding.tvPanelBubble.setVisibility(View.GONE);
        }
        
        updateReactionSummary(holder, panel.getPanelIndex());
    }

    private void updateReactionSummary(ViewHolder holder, int panelIndex) {
        List<ReactionEntity> panelReactions = allReactions.stream()
                .filter(r -> r.getPanelIndex() == panelIndex)
                .collect(Collectors.toList());

        if (panelReactions.isEmpty()) {
            holder.binding.tvReactionSummary.setText("Pas encore de réactions");
            return;
        }

        Map<String, Integer> counts = new HashMap<>();
        for (ReactionEntity r : panelReactions) {
            counts.put(r.getEmoji(), counts.getOrDefault(r.getEmoji(), 0) + 1);
        }

        StringBuilder sb = new StringBuilder();
        if (counts.containsKey("😂")) sb.append("😂 ").append(counts.get("😂")).append("  ");
        if (counts.containsKey("❤️")) sb.append("❤️ ").append(counts.get("❤️")).append("  ");
        if (counts.containsKey("🔥")) sb.append("🔥 ").append(counts.get("🔥")).append("  ");
        if (counts.containsKey("👏")) sb.append("👏 ").append(counts.get("👏"));

        holder.binding.tvReactionSummary.setText(sb.toString().trim());
    }

    @Override
    public int getItemCount() {
        return panels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemRevealPanelBinding binding;
        ViewHolder(ItemRevealPanelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
