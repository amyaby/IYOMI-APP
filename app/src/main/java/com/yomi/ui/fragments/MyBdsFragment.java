package com.yomi.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.yomi.R;
import com.yomi.databinding.FragmentMyBdsBinding;
import com.yomi.ui.adapters.BdGridAdapter;
import com.yomi.ui.adapters.ExploreAdapter;
import com.yomi.viewmodel.MyBdsViewModel;

public class MyBdsFragment extends Fragment {

    private FragmentMyBdsBinding binding;
    private MyBdsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyBdsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MyBdsViewModel.class);

        setupRecyclerViews();
        observeViewModel();
    }

    private void setupRecyclerViews() {
        BdGridAdapter gridAdapter = new BdGridAdapter();
        binding.rvMyBdsGrid.setAdapter(gridAdapter);
        
        gridAdapter.setOnStoryClickListener(story -> {
            Bundle args = new Bundle();
            args.putLong("storyId", story.getId());
            if ("IN_PROGRESS".equals(story.getStatus().name())) {
                Navigation.findNavController(requireView()).navigate(R.id.action_myBds_to_draw, args);
            } else {
                Navigation.findNavController(requireView()).navigate(R.id.action_myBds_to_reveal, args);
            }
        });

        ExploreAdapter suggestedAdapter = new ExploreAdapter();
        binding.rvSuggestedBds.setAdapter(suggestedAdapter);
    }

    private void observeViewModel() {
        viewModel.getAllStories().observe(getViewLifecycleOwner(), stories -> {
            if (binding.rvMyBdsGrid.getAdapter() != null && stories != null) {
                ((BdGridAdapter) binding.rvMyBdsGrid.getAdapter()).setStories(stories);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
