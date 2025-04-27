package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;

public class LearnWordsActivity extends AppCompatActivity {

    private int currentLevel = 0;
    private TextView[] wordSlots;
    private LinearLayout letterContainer;
    private Button nextLevelButton;
    private TextView toastMessage;
    private ImageView recognitionImage;

    private Level[] levels = {
            new Level("CAT", R.drawable.image_cat),
            new Level("DOG", R.drawable.image_dog),
            new Level("SKY", R.drawable.image_sky),
            new Level("SUN", R.drawable.image_sun),
            new Level("HAT", R.drawable.image_hat),
            new Level("CUP", R.drawable.image_cup)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_words);

        wordSlots = new TextView[]{
                findViewById(R.id.word_slot_1),
                findViewById(R.id.word_slot_2),
                findViewById(R.id.word_slot_3)
        };

        letterContainer = findViewById(R.id.letter_container);
        nextLevelButton = findViewById(R.id.next_level_button);
        toastMessage = findViewById(R.id.toast_message);
        recognitionImage = findViewById(R.id.recognition_image);

        loadLevel();

        nextLevelButton.setOnClickListener(view -> {
            if (currentLevel < levels.length - 1) {
                currentLevel++;
                loadLevel();
                nextLevelButton.setVisibility(View.GONE);
                toastMessage.setVisibility(View.GONE);
            } else {
                toastMessage.setText("Congratulations! You completed all levels.");
                toastMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadLevel() {
        // Reset word slots
        for (TextView wordSlot : wordSlots) {
            wordSlot.setText("");
        }

        // Set the image for the level
        recognitionImage.setImageResource(levels[currentLevel].imageResId);

        // Get letters of the word and shuffle
        List<Character> letters = levels[currentLevel].getShuffledLetters();
        letterContainer.removeAllViews();

        for (char letter : letters) {
            TextView letterView = new TextView(this);
            letterView.setText(String.valueOf(letter));
            letterView.setTextSize(24);
            letterView.setPadding(16, 16, 16, 16);
            letterView.setBackgroundResource(R.drawable.letter_background);
            letterView.setOnLongClickListener(view -> {
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(null, shadowBuilder, view, 0);
                return true;
            });

            letterContainer.addView(letterView);
        }

        // Set drag and drop functionality
        for (TextView wordSlot : wordSlots) {
            wordSlot.setOnDragListener(this::onDrag);
        }
    }

    private boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DROP:
                TextView dropped = (TextView) dragEvent.getLocalState();
                TextView wordSlot = (TextView) view;
                wordSlot.setText(dropped.getText());
                checkWord();
                return true;
        }
        return true;
    }

    private void checkWord() {
        StringBuilder currentWord = new StringBuilder();
        for (TextView wordSlot : wordSlots) {
            currentWord.append(wordSlot.getText());
        }

        if (currentWord.toString().equals(levels[currentLevel].word)) {
            nextLevelButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        }
    }

    private static class Level {
        String word;
        int imageResId;

        Level(String word, int imageResId) {
            this.word = word;
            this.imageResId = imageResId;
        }

        List<Character> getShuffledLetters() {
            List<Character> letters = new java.util.ArrayList<>();
            for (char c : word.toCharArray()) {
                letters.add(c);
            }
            Collections.shuffle(letters);
            return letters;
        }
    }
}
