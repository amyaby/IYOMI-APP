package com.yomi.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yomi.R;
import com.yomi.database.PlayerEntity;
import java.util.ArrayList;
import java.util.List;

public class CollaboratorAdapter extends RecyclerView.Adapter<CollaboratorAdapter.ViewHolder> {

    private List<PlayerEntity> collaborators = new ArrayList<>();
    private OnCollaboratorClickListener listener;

    public interface OnCollaboratorClickListener {
        void onCollaboratorClick(PlayerEntity player);
    }

    public void setOnCollaboratorClickListener(OnCollaboratorClickListener listener) {
        this.listener = listener;
    }

    public void setCollaborators(List<PlayerEntity> collaborators) {
        this.collaborators = collaborators;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collaborator, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayerEntity player = collaborators.get(position);
        holder.tvAv.setText(player.getAvatarEmoji());
        holder.tvName.setText("@" + player.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onCollaboratorClick(player);
        });
    }

    @Override
    public int getItemCount() {
        return collaborators.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAv, tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAv = itemView.findViewById(R.id.tvCollaboratorAv);
            tvName = itemView.findViewById(R.id.tvCollaboratorName);
        }
    }
}
