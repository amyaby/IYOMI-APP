package com.yomi.ui.fragments;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import com.yomi.R;
import com.yomi.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            setupNavigation();
        }
    }

    private void setupNavigation() {
        binding.tabHome.setOnClickListener(v -> navigateTo(R.id.homeFragment));
        binding.tabMyBds.setOnClickListener(v -> navigateTo(R.id.myBdsFragment));
        binding.tabExplore.setOnClickListener(v -> navigateTo(R.id.explorerFragment));
        binding.tabFriends.setOnClickListener(v -> navigateTo(R.id.friendsFragment));
        binding.tabProfile.setOnClickListener(v -> navigateTo(R.id.profileFragment));

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            updateTabUI(destination.getId());
            
            int id = destination.getId();
            if (id == R.id.splashFragment || 
                id == R.id.loginFragment ||
                id == R.id.registerFragment ||
                id == R.id.drawFragment ||
                id == R.id.createStoryFragment) {
                binding.tabBar.setVisibility(View.GONE);
            } else {
                binding.tabBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void navigateTo(int destinationId) {
        if (navController.getCurrentDestination() != null && 
            navController.getCurrentDestination().getId() != destinationId) {
            
            // Proper navigation for tabs: pop up to home to avoid deep stacks
            NavOptions options = new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.homeFragment, false)
                    .build();
            
            navController.navigate(destinationId, null, options);
        }
    }

    private void updateTabUI(int currentId) {
        int activeColor = getColor(R.color.primary);
        int inactiveColor = getColor(R.color.text_muted);

        binding.ivTabHome.setColorFilter(currentId == R.id.homeFragment ? activeColor : inactiveColor);
        binding.tvTabHome.setTextColor(currentId == R.id.homeFragment ? activeColor : inactiveColor);

        binding.ivTabMyBds.setColorFilter(currentId == R.id.myBdsFragment ? activeColor : inactiveColor);
        binding.tvTabMyBds.setTextColor(currentId == R.id.myBdsFragment ? activeColor : inactiveColor);

        binding.ivTabExplore.setColorFilter(currentId == R.id.explorerFragment ? activeColor : inactiveColor);
        binding.tvTabExplore.setTextColor(currentId == R.id.explorerFragment ? activeColor : inactiveColor);

        binding.ivTabFriends.setColorFilter(currentId == R.id.friendsFragment ? activeColor : inactiveColor);
        binding.tvTabFriends.setTextColor(currentId == R.id.friendsFragment ? activeColor : inactiveColor);

        binding.ivTabProfile.setColorFilter(currentId == R.id.profileFragment ? activeColor : inactiveColor);
        binding.tvTabProfile.setTextColor(currentId == R.id.profileFragment ? activeColor : inactiveColor);
    }
}
