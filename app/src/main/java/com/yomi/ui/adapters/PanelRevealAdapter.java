package com.yomi.ui.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yomi.database.PanelEntity;
import com.yomi.databinding.ItemRevealPanelBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PanelRevealAdapter extends RecyclerView.Adapter<PanelRevealAdapter.ViewHolder> {

    private List<PanelEntity> panels = new ArrayList<>();

    public void setPanels(List<PanelEntity> panels) {
        this.panels = panels;
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

        // Load image from path
        if (panel.getImagePath() != null) {
            File imgFile = new File(panel.getImagePath());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.binding.ivPanelImage.setImageBitmap(myBitmap);
            }
        }

        // Show dialog bubble if it exists (Image 5 logic)
        if (panel.getDialogText() != null && !panel.getDialogText().isEmpty()) {
            holder.binding.tvPanelBubble.setVisibility(View.VISIBLE);
            holder.binding.tvPanelBubble.setText(panel.getDialogText());
        } else {
            holder.binding.tvPanelBubble.setVisibility(View.GONE);
        }
        
        // Mocking reaction summary
        holder.binding.tvReactionSummary.setText("😂 3  ❤️ 1  🔥 1");
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
