package com.yomi.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yomi.R;
import com.yomi.database.StoryEntity;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<StoryEntity> stories = new ArrayList<>();

    public void setStories(List<StoryEntity> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We can reuse a simple vertical linear layout or define a new item_leaderboard.xml
        // For efficiency, I will use a simplified programmatic approach or a new layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoryEntity story = stories.get(position);
        String rankIcon = "";
        switch (position) {
            case 0: rankIcon = "🏆"; break;
            case 1: rankIcon = "🥈"; break;
            case 2: rankIcon = "🥉"; break;
            default: rankIcon = (position + 1) + "."; break;
        }
        holder.tvRank.setText(rankIcon);
        holder.tvTitle.setText(story.getTitle());
        holder.tvVotes.setText("912"); // Mocked value
        holder.pbVotes.setProgress(91 - (position * 10));
    }

    @Override
    public int getItemCount() {
        return Math.min(stories.size(), 5);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvTitle, tvVotes;
        ProgressBar pbVotes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvLeaderRank);
            tvTitle = itemView.findViewById(R.id.tvLeaderTitle);
            tvVotes = itemView.findViewById(R.id.tvLeaderVotes);
            pbVotes = itemView.findViewById(R.id.pbLeaderProgress);
        }
    }
}
