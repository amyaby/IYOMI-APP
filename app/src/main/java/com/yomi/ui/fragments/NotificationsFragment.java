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
import com.yomi.databinding.FragmentNotificationsBinding;
import com.yomi.viewmodel.DashboardViewModel;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private DashboardViewModel dashboardViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        
        observeViewModel();
    }

    private void observeViewModel() {
        dashboardViewModel.getNotificationMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                binding.tvEmpty.setVisibility(View.GONE);
                // For now, we display the prioritized notification message
                // In a real list, we'd use a RecyclerView, but as requested, 
                // we show the "one" current notification about the DB.
                binding.tvEmpty.setText(message);
                binding.tvEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.tvEmpty.setText("Pas de nouvelles notifications");
                binding.tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
