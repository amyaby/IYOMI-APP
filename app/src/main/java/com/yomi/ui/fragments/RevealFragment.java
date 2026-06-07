package com.yomi.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.yomi.R;
import com.yomi.databinding.FragmentRevealBinding;
import com.yomi.ui.adapters.PanelRevealAdapter;
import com.yomi.viewmodel.RevealViewModel;

public class RevealFragment extends Fragment {

    private FragmentRevealBinding binding;
    private RevealViewModel viewModel;
    private PanelRevealAdapter adapter;
    private long storyId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            storyId = getArguments().getLong("storyId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRevealBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RevealViewModel.class);
        adapter = new PanelRevealAdapter();

        binding.rvPanels.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPanels.setAdapter(adapter);

        setupReactionListeners();
        observeViewModel();

        binding.btnMyBds.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.myBdsFragment)
        );

        binding.btnExportPng.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Exportation PNG en cours...", Toast.LENGTH_SHORT).show();
        });

        binding.btnExportPdf.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Exportation PDF en cours...", Toast.LENGTH_SHORT).show();
        });

        binding.btnShare.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Ouverture du partage...", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupReactionListeners() {
        binding.btnReactFunny.setOnClickListener(v -> handleReaction("😂"));
        binding.btnReactHeart.setOnClickListener(v -> handleReaction("❤️"));
        binding.btnReactFire.setOnClickListener(v -> handleReaction("🔥"));
        binding.btnReactClap.setOnClickListener(v -> handleReaction("👏"));
    }

    private void handleReaction(String emoji) {
        viewModel.vote(storyId, emoji);
        Toast.makeText(getContext(), "Réaction " + emoji + " ajoutée !", Toast.LENGTH_SHORT).show();
    }

    private void observeViewModel() {
        viewModel.getStory(storyId).observe(getViewLifecycleOwner(), story -> {
            if (story != null) {
                binding.tvStoryTitle.setText(getString(R.string.story_title_format, story.getTitle()));
                binding.tvStoryMeta.setText(getString(R.string.story_meta_format, story.getTotalPanels(), story.getTotalPanels()));
            }
        });

        viewModel.getPanels(storyId).observe(getViewLifecycleOwner(), panels -> {
            if (panels != null) {
                adapter.setPanels(panels);
            }
        });

        // Add observation for reactions to update the counts in the panels
        viewModel.getReactions(storyId).observe(getViewLifecycleOwner(), reactions -> {
            if (reactions != null) {
                adapter.setReactions(reactions);
            }
        });

        // Observe total reactions for the story header
        viewModel.getTotalReactions(storyId).observe(getViewLifecycleOwner(), count -> {
            if (count != null && count > 0) {
                binding.tvTotalStoryReactions.setVisibility(View.VISIBLE);
                binding.tvTotalStoryReactions.setText("🔥 " + count);
            } else {
                binding.tvTotalStoryReactions.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
