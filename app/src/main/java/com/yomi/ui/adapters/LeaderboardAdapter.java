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
import com.yomi.database.StoryWithReactions;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<StoryWithReactions> stories = new ArrayList<>();

    public void setStories(List<StoryWithReactions> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoryWithReactions storyWithReactions = stories.get(position);
        StoryEntity story = storyWithReactions.story;
        
        String rankIcon = "";
        switch (position) {
            case 0: rankIcon = "🏆"; break;
            case 1: rankIcon = "🥈"; break;
            case 2: rankIcon = "🥉"; break;
            default: rankIcon = (position + 1) + "."; break;
        }
        holder.tvRank.setText(rankIcon);
        holder.tvTitle.setText(story.getTitle());
        holder.tvVotes.setText(String.valueOf(storyWithReactions.reactionCount));
        
        int maxReactions = stories.get(0).reactionCount;
        if (maxReactions > 0) {
            holder.pbVotes.setProgress((int) ((storyWithReactions.reactionCount / (float) maxReactions) * 100));
        } else {
            holder.pbVotes.setProgress(0);
        }
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
