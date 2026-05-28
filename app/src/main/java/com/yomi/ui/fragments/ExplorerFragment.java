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
import com.yomi.R;
import com.yomi.databinding.FragmentExplorerBinding;
import com.yomi.ui.adapters.ExploreAdapter;
import com.yomi.ui.adapters.ExplorerAdapter;
import com.yomi.ui.adapters.LeaderboardAdapter;
import com.yomi.viewmodel.ExplorerViewModel;

public class ExplorerFragment extends Fragment {

    private FragmentExplorerBinding binding;
    private ExplorerViewModel viewModel;
    private ExplorerAdapter popularAdapter;
    private ExploreAdapter joinableAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExplorerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ExplorerViewModel.class);

        setupFilters();
        setupRecyclerViews();
        setupVoting();
        observeViewModel();
    }

    private void setupFilters() {
        binding.chipPopularity.setOnClickListener(v -> viewModel.setShowPopularity(true));
        binding.chipVotes.setOnClickListener(v -> viewModel.setShowPopularity(false));
    }

    private void setupRecyclerViews() {
        popularAdapter = new ExplorerAdapter();
        binding.rvPopularGrid.setAdapter(popularAdapter);

        joinableAdapter = new ExploreAdapter();
        binding.rvJoinableStories.setAdapter(joinableAdapter);
        joinableAdapter.setOnStoryClickListener(story -> {
            viewModel.joinStory(story.getId());
            Toast.makeText(getContext(), "Vous avez rejoint : " + story.getTitle(), Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.homeFragment);
        });

        LeaderboardAdapter leaderboardAdapter = new LeaderboardAdapter();
        binding.rvLeaderboard.setAdapter(leaderboardAdapter);
    }

    private void setupVoting() {
        binding.btnVoteFunny.setOnClickListener(v -> handleVote("😂"));
        binding.btnVoteTouching.setOnClickListener(v -> handleVote("❤️"));
        binding.btnVoteMasterpiece.setOnClickListener(v -> handleVote("🔥"));
    }

    private void handleVote(String type) {
        Toast.makeText(getContext(), "Voté : " + type, Toast.LENGTH_SHORT).show();
    }

    private void observeViewModel() {
        viewModel.getShowPopularity().observe(getViewLifecycleOwner(), popularity -> {
            if (popularity) {
                binding.sectionPopularity.setVisibility(View.VISIBLE);
                binding.sectionVotes.setVisibility(View.GONE);
            } else {
                binding.sectionPopularity.setVisibility(View.GONE);
                binding.sectionVotes.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getPopularStories().observe(getViewLifecycleOwner(), stories -> {
            if (stories != null) {
                popularAdapter.setStories(stories);
            }
        });

        viewModel.getJoinableStories().observe(getViewLifecycleOwner(), stories -> {
            if (stories != null) {
                joinableAdapter.setStories(stories);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
