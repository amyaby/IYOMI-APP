package com.yomi.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.yomi.R;
import com.yomi.databinding.FragmentDrawBinding;
import com.yomi.repository.SessionManager;
import com.yomi.viewmodel.DrawViewModel;

public class DrawFragment extends Fragment {

    private FragmentDrawBinding binding;
    private DrawViewModel viewModel;
    private SessionManager sessionManager;
    private long storyId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            storyId = getArguments().getLong("storyId");
        }
        sessionManager = new SessionManager(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDrawBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DrawViewModel.class);

        setupTools();
        setupColors();
        observeViewModel();

        viewModel.loadPreviousPanelOnly(storyId); 

        binding.btnTogglePrevious.setOnClickListener(v -> {
            boolean isVisible = binding.previousPanelPreview.getVisibility() == View.VISIBLE;
            binding.previousPanelPreview.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        });

        binding.btnPlace.setOnClickListener(v -> {
            String text = binding.etBubbleText.getText().toString().trim();
            if (!text.isEmpty()) {
                addBubble(text);
                binding.etBubbleText.setText("");
            }
        });

        binding.btnSubmit.setOnClickListener(v -> {
            // Capture the whole drawing area including bubbles
            View container = binding.currentDrawingContainer;
            Bitmap drawing = Bitmap.createBitmap(container.getWidth(), container.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(drawing);
            canvas.drawColor(Color.WHITE); // Background white
            container.draw(canvas);
            
            StringBuilder allText = new StringBuilder();
            for (int i = 0; i < binding.currentDrawingContainer.getChildCount(); i++) {
                View child = binding.currentDrawingContainer.getChildAt(i);
                if (child instanceof TextView) {
                     if (allText.length() > 0) allText.append("\n");
                     allText.append(((TextView)child).getText().toString());
                }
            }

            viewModel.submitTurn(storyId, sessionManager.getPlayerId(), drawing, allText.toString());
            
            Toast.makeText(getContext(), "Panel soumis ! ✨", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigate(R.id.action_draw_to_home);
        });
    }

    private void addBubble(String text) {
        TextView bubble = new TextView(requireContext());
        bubble.setText(text);
        bubble.setBackgroundResource(R.drawable.bg_edittext);
        bubble.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.WHITE));
        bubble.setPadding(24, 12, 24, 12);
        bubble.setTextColor(Color.BLACK);
        bubble.setTextSize(14);
        bubble.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
        
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 50, 50, 50);
        bubble.setLayoutParams(lp);
        
        makeDraggable(bubble);
        
        // Double tap or Long click to remove
        bubble.setOnLongClickListener(v -> {
            binding.currentDrawingContainer.removeView(v);
            Toast.makeText(getContext(), "Bulle supprimée", Toast.LENGTH_SHORT).show();
            return true;
        });
        
        binding.currentDrawingContainer.addView(bubble);
        Toast.makeText(getContext(), "Bulle ajoutée ! Déplacez-la !", Toast.LENGTH_SHORT).show();
    }

    private void makeDraggable(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        v.setX(event.getRawX() + dX);
                        v.setY(event.getRawY() + dY);
                        return true;
                }
                return false;
            }
        });
    }

    private void setupTools() {
        binding.btnPencil.setOnClickListener(v -> {
            binding.canvasView.setEraserMode(false);
            updateToolUI(binding.btnPencil);
        });
        binding.btnEraser.setOnClickListener(v -> {
            binding.canvasView.setEraserMode(true);
            updateToolUI(binding.btnEraser);
        });
        binding.btnUndo.setOnClickListener(v -> binding.canvasView.undo());
        binding.btnRedo.setOnClickListener(v -> binding.canvasView.redo());
        binding.btnTrash.setOnClickListener(v -> binding.canvasView.clear());
        
        binding.sbStroke.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                binding.canvasView.setStrokeWidth(progress + 5);
            }
            @Override public void onStartTrackingTouch(android.widget.SeekBar s) {}
            @Override public void onStopTrackingTouch(android.widget.SeekBar s) {}
        });
    }

    private void updateToolUI(View active) {
        binding.btnPencil.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
        binding.btnEraser.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
        active.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.pill_v_bg));
    }

    private void setupColors() {
        int[] palette = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA};
        binding.colorsRow.removeAllViews();
        for (int color : palette) {
            View dot = new View(requireContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(60, 60);
            lp.setMargins(12, 0, 12, 0);
            dot.setLayoutParams(lp);
            dot.setBackgroundResource(R.drawable.bg_color_dot);
            dot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
            dot.setOnClickListener(v -> binding.canvasView.setPaintColor(color));
            binding.colorsRow.addView(dot);
        }
    }

    private void observeViewModel() {
        viewModel.getPreviousPanelData().observe(getViewLifecycleOwner(), panel -> {
            if (panel != null && panel.getImagePath() != null) {
                binding.ivPreviousPanel.setImageBitmap(BitmapFactory.decodeFile(panel.getImagePath()));
                binding.tvPreviousBubble.setText(panel.getDialogText());
                binding.tvPreviousBubble.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getTimerValue().observe(getViewLifecycleOwner(), time -> binding.tvTimer.setText(time));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
