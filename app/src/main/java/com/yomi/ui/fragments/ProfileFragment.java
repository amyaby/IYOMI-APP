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
import com.yomi.viewmodel.DashboardViewModel;
import com.yomi.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private DashboardViewModel dashboardViewModel;
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
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // Shared logic with dashboard for notifications
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);

        observeViewModels();

        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Navigation.findNavController(v).navigate(R.id.action_profile_to_splash);
        });

        binding.menuEditProfile.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_profile_to_edit)
        );

        binding.menuNotifications.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_profile_to_notifications)
        );

        binding.menuPrivacy.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_profile_to_privacy)
        );
    }

    private void observeViewModels() {
        profileViewModel.getUsername().observe(getViewLifecycleOwner(), name -> {
            if (name != null) {
                binding.tvProfileName.setText(name.replace("@", ""));
                updateHandle();
            }
        });

        profileViewModel.getJoinDate().observe(getViewLifecycleOwner(), date -> updateHandle());
        
        profileViewModel.getBdsCreated().observe(getViewLifecycleOwner(), val -> binding.tvStatBds.setText(val));
        profileViewModel.getPanelsDrawn().observe(getViewLifecycleOwner(), val -> binding.tvStatPanels.setText(val));
        profileViewModel.getReactionsReceived().observe(getViewLifecycleOwner(), val -> binding.tvStatReacts.setText(val));
        profileViewModel.getStreak().observe(getViewLifecycleOwner(), val -> binding.tvStreak.setText(val));
        profileViewModel.getRanking().observe(getViewLifecycleOwner(), val -> binding.tvRanking.setText(val));

        // Dynamic notification badge linked to the whole app state
        dashboardViewModel.getNotificationCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null && count > 0) {
                binding.tvNotificationBadge.setVisibility(View.VISIBLE);
                binding.tvNotificationBadge.setText(String.valueOf(count));
            } else {
                binding.tvNotificationBadge.setVisibility(View.GONE);
            }
        });
    }

    private void updateHandle() {
        String name = profileViewModel.getUsername().getValue();
        String date = profileViewModel.getJoinDate().getValue();
        if (name != null && date != null) {
            binding.tvProfileHandle.setText("@" + name + " · membre depuis " + date);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
