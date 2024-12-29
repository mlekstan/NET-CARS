package com.example.shop_app.ui.activities;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.view.View;

import com.example.shop_app.databinding.ActivityPagemainBinding;
import com.example.shop_app.R;


public class MainPageActivity extends AppCompatActivity {
    private static final String TAG = "MainPageActivity";
    private ActivityPagemainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityPagemainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.bottomAppBar.setOnApplyWindowInsetsListener(null);
        binding.bottomAppBar.setPadding(0,0,0,0);
        binding.bottomNav.setOnApplyWindowInsetsListener(null);
        binding.bottomNav.setPadding(0,0,0,0);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.fragmentContainerView.getId());
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNav, navController);

        navController.addOnDestinationChangedListener(
                new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                        if (navDestination.getId() == R.id.offerDetailsFragment) {
                            binding.bottomAppBar.setVisibility(View.GONE);
                            binding.bottomNav.setVisibility(View.GONE);
                            binding.floatingActionButton.setVisibility(View.GONE);
                        } else {
                            binding.bottomAppBar.setVisibility(View.VISIBLE);
                            binding.bottomNav.setVisibility(View.VISIBLE);
                            binding.floatingActionButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        binding.floatingActionButton.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getApplicationContext(),R.animator.button_animation));
        binding.floatingActionButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navController.navigate(R.id.addCarActivity);
                    }
                }
        );
    }
}