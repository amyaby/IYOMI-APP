package com.yomi.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.yomi.R;
import com.yomi.database.StoryEntity;
import java.util.ArrayList;
import java.util.List;

public class JoinableStoryAdapter extends RecyclerView.Adapter<JoinableStoryAdapter.ViewHolder> {

    private List<StoryEntity> stories = new ArrayList<>();

    public void setStories(List<StoryEntity> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoryEntity story = stories.get(position);
        holder.tvTitle.setText(story.getTitle());
        holder.tvSub.setText("2 places libres · panel 1/4"); // Mocked state
        
        if (position % 2 == 0) holder.tvThumb.setText("🌊");
        else holder.tvThumb.setText("🦋");

        holder.tvReact.setText("Rejoindre");
        holder.tvReact.setBackgroundResource(R.drawable.bg_pill_green);
        holder.tvReact.setPadding(24, 8, 24, 8);
        holder.tvReact.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.pill_g_text));
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvThumb, tvTitle, tvSub, tvReact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvThumb = itemView.findViewById(R.id.tvExploreThumb);
            tvTitle = itemView.findViewById(R.id.tvExploreTitle);
            tvSub = itemView.findViewById(R.id.tvExploreSub);
            tvReact = itemView.findViewById(R.id.tvExploreReact);
        }
    }
}
