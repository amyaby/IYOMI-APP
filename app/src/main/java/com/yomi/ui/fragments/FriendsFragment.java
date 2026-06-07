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
import com.yomi.databinding.FragmentFriendsBinding;
import com.yomi.ui.adapters.FriendsAdapter;
import com.yomi.viewmodel.FriendsViewModel;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;
    private FriendsViewModel viewModel;
    private FriendsAdapter suggestionsAdapter;
    private FriendsAdapter friendsListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);

        setupRecyclerViews();
        observeViewModel();
    }

    private void setupRecyclerViews() {
        suggestionsAdapter = new FriendsAdapter(true, player -> {
            viewModel.addFriend(player.getId());
            Toast.makeText(getContext(), player.getName() + " ajouté aux amis !", Toast.LENGTH_SHORT).show();
        });
        binding.rvSuggestions.setAdapter(suggestionsAdapter);

        friendsListAdapter = new FriendsAdapter(false, player -> {
            viewModel.removeFriend(player.getId());
            Toast.makeText(getContext(), player.getName() + " retiré des amis", Toast.LENGTH_SHORT).show();
        });
        binding.rvFriendsList.setAdapter(friendsListAdapter);
    }

    private void observeViewModel() {
        viewModel.getSuggestions().observe(getViewLifecycleOwner(), suggestions -> {
            if (suggestions != null) {
                suggestionsAdapter.setPlayers(suggestions);
            }
        });

        viewModel.getFriends().observe(getViewLifecycleOwner(), friends -> {
            if (friends != null) {
                friendsListAdapter.setPlayers(friends);
                if (friends.isEmpty()) {
                    binding.layoutEmptyFriends.setVisibility(View.VISIBLE);
                    binding.rvFriendsList.setVisibility(View.GONE);
                } else {
                    binding.layoutEmptyFriends.setVisibility(View.GONE);
                    binding.rvFriendsList.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
