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
import com.yomi.databinding.FragmentProfileBinding;
import com.yomi.repository.SessionManager;
import com.yomi.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        observeViewModel();

        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.logout(); // Clear the session
            Navigation.findNavController(v).navigate(R.id.action_profile_to_splash);
        });

        binding.btnSettings.setOnClickListener(v -> {
            // Settings logic
        });
    }

    private void observeViewModel() {
        viewModel.getUsername().observe(getViewLifecycleOwner(), name -> {
            if (name != null) {
                binding.tvProfileName.setText(name.replace("@", ""));
                updateHandle();
            }
        });

        viewModel.getJoinDate().observe(getViewLifecycleOwner(), date -> updateHandle());
        
        viewModel.getBdsCreated().observe(getViewLifecycleOwner(), val -> binding.tvStatBds.setText(val));
        viewModel.getPanelsDrawn().observe(getViewLifecycleOwner(), val -> binding.tvStatPanels.setText(val));
        viewModel.getReactionsReceived().observe(getViewLifecycleOwner(), val -> binding.tvStatReacts.setText(val));
        viewModel.getStreak().observe(getViewLifecycleOwner(), val -> binding.tvStreak.setText(val));
        viewModel.getRanking().observe(getViewLifecycleOwner(), val -> binding.tvRanking.setText(val));
    }

    private void updateHandle() {
        String name = viewModel.getUsername().getValue();
        String date = viewModel.getJoinDate().getValue();
        if (name != null && date != null) {
            binding.tvProfileHandle.setText(name + " · membre depuis " + date);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
