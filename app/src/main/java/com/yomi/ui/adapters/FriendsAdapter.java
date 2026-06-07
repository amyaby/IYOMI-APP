package com.yomi.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yomi.R;
import com.yomi.database.PlayerEntity;
import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private List<PlayerEntity> players = new ArrayList<>();
    private OnFriendActionListener listener;
    private boolean isSuggestion;

    public interface OnFriendActionListener {
        void onActionClick(PlayerEntity player);
    }

    public FriendsAdapter(boolean isSuggestion, OnFriendActionListener listener) {
        this.isSuggestion = isSuggestion;
        this.listener = listener;
    }

    public void setPlayers(List<PlayerEntity> players) {
        this.players = players;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayerEntity player = players.get(position);
        holder.tvAv.setText(player.getAvatarEmoji());
        holder.tvName.setText("@" + player.getName());
        
        if (isSuggestion) {
            holder.btnAction.setText("Ajouter");
            holder.btnAction.setVisibility(View.VISIBLE);
        } else {
            holder.btnAction.setText("Supprimer");
            // Optionally hide or change style for already friends
            holder.btnAction.setVisibility(View.VISIBLE); 
        }

        holder.btnAction.setOnClickListener(v -> {
            if (listener != null) listener.onActionClick(player);
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAv, tvName, tvStatus;
        Button btnAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAv = itemView.findViewById(R.id.tvPlayerAvatar);
            tvName = itemView.findViewById(R.id.tvPlayerName);
            tvStatus = itemView.findViewById(R.id.tvPlayerStatus);
            btnAction = itemView.findViewById(R.id.btnAction);
        }
    }
}
