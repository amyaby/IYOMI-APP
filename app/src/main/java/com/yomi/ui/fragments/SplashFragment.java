package com.yomi.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.yomi.R;
import com.yomi.databinding.FragmentSplashBinding;
import com.yomi.repository.SessionManager;
import com.yomi.repository.YomiRepository;

public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding;
    private SessionManager sessionManager;
    private YomiRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        repository = new YomiRepository(requireActivity().getApplication());

        binding.btnContainer.setVisibility(View.VISIBLE);
        binding.logoContainer.setAlpha(1.0f);

        binding.btnCreateAccount.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_splash_to_register)
        );

        binding.btnLogin.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_splash_to_login)
        );
        
        // Auto-login logic
        if (sessionManager.isLoggedIn()) {
            long userId = sessionManager.getPlayerId();
            // Verify if the user still exists in the database (to avoid "Ghost Sessions" after a wipe)
            repository.getPlayerById(userId, player -> {
                if (player != null) {
                    // User exists, go to home
                    requireActivity().runOnUiThread(() -> {
                        if (getView() != null) {
                            Navigation.findNavController(view).navigate(R.id.action_splash_to_home);
                        }
                    });
                } else {
                    // Database was likely wiped, clear session
                    sessionManager.logout();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
