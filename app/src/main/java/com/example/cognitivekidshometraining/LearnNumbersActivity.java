package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LearnNumbersActivity extends AppCompatActivity {

    private LinearLayout instructionModal, gameContainer, itemContainer, optionsContainer;
    private TextView resultTextView, selectedNumberTextView, levelCounterTextView, timeCounterTextView;
    private Button checkBtn, nextLevelBtn, restartBtn;
    private int currentLevel = 1;
    private int correctAnswer;
    private long startTime;
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private List<List<String>> levels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_numbers);

        // Initialize views
        instructionModal = findViewById(R.id.instruction_modal);
        gameContainer = findViewById(R.id.game_container);
        itemContainer = findViewById(R.id.item_container);
        optionsContainer = findViewById(R.id.options_container);
        resultTextView = findViewById(R.id.result);
        selectedNumberTextView = findViewById(R.id.selected_number);
        levelCounterTextView = findViewById(R.id.level_counter);
        timeCounterTextView = findViewById(R.id.time_counter);
        checkBtn = findViewById(R.id.check_btn);
        nextLevelBtn = findViewById(R.id.next_level_btn);
        restartBtn = findViewById(R.id.restart_btn);

        levels = new ArrayList<>();
        levels.add(Arrays.asList("🍨"));
        levels.add(Arrays.asList("🥘", "🎭", "🍛", "🎪", "🎡"));
        // Add more levels here as needed

        // Start Game Button
        findViewById(R.id.start_game_btn).setOnClickListener(view -> {
            instructionModal.setVisibility(View.GONE);
            gameContainer.setVisibility(View.VISIBLE);
            startNewLevel();
        });

        // Check Button
        checkBtn.setOnClickListener(view -> checkAnswer());

        // Next Level Button
        nextLevelBtn.setOnClickListener(view -> {
            currentLevel++;
            startNewLevel();
        });

        // Restart Button
        restartBtn.setOnClickListener(view -> {
            currentLevel = 1;
            startNewLevel();
        });
    }

    // Function to start a new level
    private void startNewLevel() {
        startTime = System.currentTimeMillis();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                timeCounterTextView.setText("Time: " + elapsedTime + "s");
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.postDelayed(timerRunnable, 1000);

        correctAnswer = levels.get(currentLevel - 1).size();
        levelCounterTextView.setText("Level: " + currentLevel);
        itemContainer.removeAllViews();

        // Add items for the current level
        for (String item : levels.get(currentLevel - 1)) {
            TextView itemView = new TextView(this);
            itemView.setText(item);
            itemContainer.addView(itemView);
        }

        // Show answer options (e.g., numbers 1-5)
        optionsContainer.removeAllViews();
        for (int i = 1; i <= 5; i++) {
            Button optionButton = new Button(this);
            optionButton.setText(String.valueOf(i));
            int finalI = i;
            optionButton.setOnClickListener(view -> selectedNumberTextView.setText("Selected: " + finalI));
            optionsContainer.addView(optionButton);
        }

        resultTextView.setText("");
        selectedNumberTextView.setText("");
        nextLevelBtn.setVisibility(View.GONE);
        restartBtn.setVisibility(View.GONE);
        checkBtn.setVisibility(View.VISIBLE);
    }

    // Function to check if the selected answer is correct
    private void checkAnswer() {
        String selectedText = selectedNumberTextView.getText().toString();
        if (!selectedText.isEmpty()) {
            int selectedNumber = Integer.parseInt(selectedText.replace("Selected: ", ""));
            if (selectedNumber == correctAnswer) {
                resultTextView.setText("Correct! 🎉");
                nextLevelBtn.setVisibility(View.VISIBLE);
                if (currentLevel == levels.size()) {
                    nextLevelBtn.setVisibility(View.GONE);
                    restartBtn.setVisibility(View.VISIBLE);
                }
            } else {
                resultTextView.setText("Wrong Answer 😢. Try Again.");
            }
            checkBtn.setVisibility(View.GONE);
        }
    }
}
