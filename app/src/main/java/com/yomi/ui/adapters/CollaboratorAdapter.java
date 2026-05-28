package com.yomi.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yomi.R;
import java.util.ArrayList;
import java.util.List;

public class CollaboratorAdapter extends RecyclerView.Adapter<CollaboratorAdapter.ViewHolder> {

    private List<String> collaborators = new ArrayList<>();

    public void setCollaborators(List<String> collaborators) {
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
        // Mocking data for now as per HTML
        String[] icons = {"🦊", "🌙", "🎨", "⚡"};
        String[] names = {"@foxi", "@luna", "@arty", "@zap"};
        
        if (position < icons.length) {
            holder.tvAv.setText(icons[position]);
            holder.tvName.setText(names[position]);
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Mocking 4 collaborators
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
