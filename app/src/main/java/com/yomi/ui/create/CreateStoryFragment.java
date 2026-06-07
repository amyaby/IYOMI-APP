package com.yomi.ui.create;

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
import com.yomi.databinding.FragmentCreateStoryBinding;

public class CreateStoryFragment extends Fragment {

    private FragmentCreateStoryBinding binding;
    private CreateStoryViewModel viewModel;

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

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.btnIncrement.setOnClickListener(v -> viewModel.incrementPanels());
        binding.btnDecrement.setOnClickListener(v -> viewModel.decrementPanels());

        viewModel.getPanelCount().observe(getViewLifecycleOwner(), count -> {
            binding.tvPanelCount.setText(String.valueOf(count));
        });

        binding.btnLaunch.setOnClickListener(v -> {
            String title = binding.etStoryTitle.getText().toString().trim();
            if (title.isEmpty()) {
                binding.etStoryTitle.setError("Donne un titre à ton aventure !");
                return;
            }

            viewModel.createStory(title);
            Toast.makeText(getContext(), "L'histoire est lancée ! 🚀", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigate(R.id.homeFragment);
        });

        binding.btnCopy.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Code copié !", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
