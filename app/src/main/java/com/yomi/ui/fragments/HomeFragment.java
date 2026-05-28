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
import com.yomi.databinding.FragmentHomeBinding;
import com.yomi.model.Story;
import com.yomi.repository.SessionManager;
import com.yomi.ui.adapters.ActiveStoryAdapter;
import com.yomi.ui.adapters.ExploreAdapter;
import com.yomi.ui.adapters.CollaboratorAdapter;
import com.yomi.viewmodel.DashboardViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DashboardViewModel viewModel;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        setupHeader();
        setupNavigation();
        observeViewModel();
    }

    private void setupHeader() {
        String username = sessionManager.getPlayerName();
        if (username != null) {
            binding.tvUsername.setText("@" + username);
            binding.tvUserAvatar.setText(username.substring(0, Math.min(username.length(), 2)).toUpperCase());
        }
    }

    private void setupNavigation() {
        View.OnClickListener startCreate = v -> 
            Navigation.findNavController(v).navigate(R.id.action_home_to_create);
            
        binding.btnAddStory.setOnClickListener(startCreate);
        binding.btnCreateFirstStory.setOnClickListener(startCreate);

        binding.layoutNotification.setOnClickListener(v -> {
            Story turnStory = viewModel.getTurnNotification().getValue();
            if (turnStory != null) {
                Bundle args = new Bundle();
                args.putLong("storyId", turnStory.getId());
                Navigation.findNavController(v).navigate(R.id.drawFragment, args);
            }
        });
    }

    private void observeViewModel() {
        // Active Stories
        ActiveStoryAdapter activeAdapter = new ActiveStoryAdapter();
        binding.rvActiveStories.setAdapter(activeAdapter);
        activeAdapter.setOnStoryClickListener(story -> {
            long currentUserId = sessionManager.getPlayerId();
            if (story.getCurrentPanelIndex() < story.getPlayerOrder().size() && 
                story.getPlayerOrder().get(story.getCurrentPanelIndex()) == currentUserId) {
                Bundle args = new Bundle();
                args.putLong("storyId", story.getId());
                Navigation.findNavController(requireView()).navigate(R.id.drawFragment, args);
            } else {
                Toast.makeText(getContext(), "En attente des autres joueurs...", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getActiveStories().observe(getViewLifecycleOwner(), stories -> {
            if (stories == null || stories.isEmpty()) {
                binding.rvActiveStories.setVisibility(View.GONE);
                binding.layoutEmptyStories.setVisibility(View.VISIBLE);
            } else {
                binding.rvActiveStories.setVisibility(View.VISIBLE);
                binding.layoutEmptyStories.setVisibility(View.GONE);
                activeAdapter.setStories(stories, sessionManager.getPlayerId());
            }
        });

        // "Your Turn" Notification
        viewModel.getTurnNotification().observe(getViewLifecycleOwner(), story -> {
            if (story != null) {
                binding.layoutNotification.setVisibility(View.VISIBLE);
                binding.tvNotificationText.setText("C'est ton tour ! \"" + story.getTitle() + "\" attend ton panel.");
            } else {
                binding.layoutNotification.setVisibility(View.GONE);
            }
        });

        // Popular BDs
        ExploreAdapter popularAdapter = new ExploreAdapter();
        binding.rvPopularStories.setAdapter(popularAdapter);
        popularAdapter.setOnStoryClickListener(story -> {
            Bundle args = new Bundle();
            args.putLong("storyId", story.getId());
            Navigation.findNavController(requireView()).navigate(R.id.revealFragment, args);
        });

        viewModel.getCompletedStories().observe(getViewLifecycleOwner(), stories -> {
            if (stories != null) {
                popularAdapter.setStories(stories);
            }
        });

        // Collaborators
        CollaboratorAdapter collabAdapter = new CollaboratorAdapter();
        binding.rvCollaborators.setAdapter(collabAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
