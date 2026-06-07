package com.yomi.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.yomi.databinding.FragmentEditProfileBinding;
import com.yomi.repository.SessionManager;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.etUsername.setText(sessionManager.getPlayerName());
        
        binding.btnSave.setOnClickListener(v -> {
            String newName = binding.etUsername.getText().toString().trim();
            if (!newName.isEmpty()) {
                sessionManager.createSession(sessionManager.getPlayerId(), newName);
                Toast.makeText(getContext(), "Profil mis à jour", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigateUp();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
