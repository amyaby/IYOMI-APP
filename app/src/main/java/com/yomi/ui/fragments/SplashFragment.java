package com.yomi.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.yomi.R;
import com.yomi.databinding.FragmentSplashBinding;
import com.yomi.repository.SessionManager;
import com.yomi.viewmodel.SplashViewModel;

public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding;
    private SplashViewModel viewModel;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        sessionManager = new SessionManager(requireContext());

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (getView() != null) {
                    Navigation.findNavController(requireView()).navigate(R.id.action_splash_to_home);
                }
            }, 1500);
        } else {
            // Show buttons after a brief logo display
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (binding != null) {
                    binding.btnContainer.setVisibility(View.VISIBLE);
                }
            }, 1000);
        }

        binding.btnCreateAccount.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_splash_to_register)
        );

        binding.btnLogin.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_splash_to_login)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
