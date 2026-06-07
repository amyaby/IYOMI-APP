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
import com.yomi.database.PlayerEntity;
import com.yomi.databinding.FragmentCreateStoryBinding;
import com.yomi.ui.adapters.CollaboratorAdapter;
import com.yomi.viewmodel.CreateStoryViewModel;
import com.google.android.material.chip.Chip;
import java.util.List;

public class CreateStoryFragment extends Fragment {

    private FragmentCreateStoryBinding binding;
    private CreateStoryViewModel viewModel;
    private CollaboratorAdapter availablePlayersAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateStoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CreateStoryViewModel.class);

        setupRecyclerView();
        setupListeners();
        observeViewModel();

        // Check if we were passed an initial player to invite
        if (getArguments() != null) {
            long initialId = getArguments().getLong("initialPlayerId", -1L);
            if (initialId != -1) {
                viewModel.addPlayerById(initialId);
            }
        }
    }

    private void setupRecyclerView() {
        availablePlayersAdapter = new CollaboratorAdapter();
        binding.rvAvailablePlayers.setAdapter(availablePlayersAdapter);
        availablePlayersAdapter.setOnCollaboratorClickListener(viewModel::addPlayer);
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.btnIncrement.setOnClickListener(v -> viewModel.incrementPanels());
        binding.btnDecrement.setOnClickListener(v -> viewModel.decrementPanels());

        binding.btnAddByUsername.setOnClickListener(v -> {
            String username = binding.etInviteUsername.getText().toString().trim();
            if (!username.isEmpty()) {
                viewModel.addPlayerByUsername(username);
                binding.etInviteUsername.setText("");
            }
        });

        binding.btnLaunch.setOnClickListener(v -> {
            String title = binding.etStoryTitle.getText().toString().trim();
            if (title.isEmpty()) {
                binding.etStoryTitle.setError("Donne un titre à ton aventure !");
                return;
            }
            viewModel.createStory(title);
        });

        binding.btnCopy.setOnClickListener(v -> {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Yomi Invite Code", binding.tvInviteCode.getText());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Code copié !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeViewModel() {
        viewModel.getPanelCount().observe(getViewLifecycleOwner(), count -> binding.tvPanelCount.setText(String.valueOf(count)));
        viewModel.getInviteCode().observe(getViewLifecycleOwner(), code -> binding.tvInviteCode.setText(code));
        
        viewModel.getAllPlayers().observe(getViewLifecycleOwner(), players -> {
            if (players != null) availablePlayersAdapter.setCollaborators(players);
        });

        viewModel.getSelectedPlayers().observe(getViewLifecycleOwner(), this::updateSelectedPlayersChips);

        viewModel.getInviteStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
        });

        viewModel.getNavigateToHome().observe(getViewLifecycleOwner(), navigate -> {
            if (navigate) {
                Toast.makeText(getContext(), "L'histoire est lancée ! 🚀", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigate(R.id.action_create_to_home);
            }
        });
    }

    private void updateSelectedPlayersChips(List<PlayerEntity> players) {
        binding.cgSelectedPlayers.removeAllViews();
        for (PlayerEntity player : players) {
            Chip chip = new Chip(requireContext());
            chip.setText(player.getAvatarEmoji() + " @" + player.getName());
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> viewModel.removePlayer(player));
            binding.cgSelectedPlayers.addView(chip);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
