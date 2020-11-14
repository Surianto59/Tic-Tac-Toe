package com.karimun.tictactoe;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.karimun.tictactoe.databinding.ActivityGameBinding;

import java.util.ArrayList;
import java.util.Random;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class GameActivity extends AppCompatActivity {
    ActivityGameBinding binding;

    String strXWin = "X wins";
    String strOWin = "O wins";

    // Determine if it's X's move or not (for player vs player only)
    boolean isX=true;
    View[] views = new View[9];
    RelativeLayout[] relativeLayouts = new RelativeLayout[9];
    int[] cells = new int[9];
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final int mode = getIntent().getIntExtra(MainActivity.PLAY_MODE, 0);
        final boolean isPlayerX = getIntent().getBooleanExtra(MainActivity.PLAYER_SIGN, true);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        int cellLength = (width<height) ? width/4 : height/4;

        final GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(3);
        gridLayout.setRowCount(3);

        for (int i=0; i<gridLayout.getColumnCount()*gridLayout.getRowCount(); ++i) {
            final int pos = i;
            views[pos] = new View(this);
            views[pos].setVisibility(View.GONE);

            relativeLayouts[pos] = new RelativeLayout(this);
            relativeLayouts[pos].setGravity(Gravity.CENTER);
            relativeLayouts[pos].setLayoutParams(new RelativeLayout.LayoutParams(cellLength, cellLength));
            relativeLayouts[pos].setBackgroundResource(R.drawable.rectangle_border);
            relativeLayouts[pos].addView(views[i]);
            relativeLayouts[pos].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mode==0) {
                        if (isPlayerX) {
                            // Player makes a move
                            views[pos].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cross, null));
                            views[pos].setVisibility(View.VISIBLE);
                            cells[pos] = R.drawable.ic_cross;
                            relativeLayouts[pos].setClickable(false);

                            // Check if player wins
                            String evaluation = evaluateBoard(view.getContext(), cells);
                            if (evaluation != null) {
                                displayGameOverDialog(view.getContext(), evaluation);
                                return;
                            }

                            // Com makes a move
                            if (isMoveLeft(cells)) {
                                Random random = new Random();
                                ArrayList<Integer> positions = positionsLeft(cells);
                                int pos = random.nextInt(positions.size());
                                views[positions.get(pos)].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nought, null));
                                views[positions.get(pos)].setVisibility(View.VISIBLE);
                                cells[positions.get(pos)] = R.drawable.ic_nought;
                                relativeLayouts[positions.get(pos)].setClickable(false);
                            }
                            else {
                                displayGameOverDialog(view.getContext(), "Draw");
                                return;
                            }

                            // Check if com wins
                            String evaluation2 = evaluateBoard(view.getContext(), cells);
                            if (evaluation2 != null) {
                                displayGameOverDialog(view.getContext(), evaluation2);
                                return;
                            }

                            // Check if there is any move left if not it's a draw
                            if (!isMoveLeft(cells))
                                displayGameOverDialog(view.getContext(), "Draw");
                        }
                        else{
                            // Player makes a move
                            views[pos].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nought, null));
                            views[pos].setVisibility(View.VISIBLE);
                            cells[pos] = R.drawable.ic_nought;
                            relativeLayouts[pos].setClickable(false);

                            // Check if player wins
                            String evaluation = evaluateBoard(view.getContext(), cells);
                            if (evaluation != null) {
                                displayGameOverDialog(view.getContext(), evaluation);
                                return;
                            }

                            // Com makes a move
                            if (isMoveLeft(cells)) {
                                Random random = new Random();
                                ArrayList<Integer> positions = positionsLeft(cells);
                                int pos = random.nextInt(positions.size());
                                views[positions.get(pos)].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cross, null));
                                views[positions.get(pos)].setVisibility(View.VISIBLE);
                                cells[positions.get(pos)] = R.drawable.ic_cross;
                                relativeLayouts[positions.get(pos)].setClickable(false);
                            }
                            else{
                                displayGameOverDialog(view.getContext(), "Draw");
                                return;
                            }

                            // Check if com wins
                            String evaluation2 = evaluateBoard(view.getContext(), cells);
                            if (evaluation2 != null) {
                                displayGameOverDialog(view.getContext(), evaluation2);
                                return;
                            }

                            // Check if there's any move left if not it's a draw
                            if (!isMoveLeft(cells))
                                displayGameOverDialog(view.getContext(), "Draw");
                        }
                    }
                    else {
                        if (isX) {
                            views[pos].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cross, null));
                            views[pos].setVisibility(View.VISIBLE);
                            cells[pos] = R.drawable.ic_cross;
                            relativeLayouts[pos].setClickable(false);
                        }
                        else{
                            views[pos].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nought, null));
                            views[pos].setVisibility(View.VISIBLE);
                            cells[pos] = R.drawable.ic_nought;
                            relativeLayouts[pos].setClickable(false);
                        }

                        String evaluation = evaluateBoard(view.getContext(), cells);
                        if (evaluation != null) {
                            displayGameOverDialog(view.getContext(), evaluation);
                            return;
                        }

                        if (!isMoveLeft(cells))
                            displayGameOverDialog(view.getContext(), "Draw");
                        isX=!isX;
                    }
                }
            });

            gridLayout.addView(relativeLayouts[pos]);
        }

        // This runs at the beginning of the game if player chose Player vs Com and O sign
        if (mode==0) {
            if (!isPlayerX) {
                Random random = new Random();
                int position = random.nextInt(9);
                views[position].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cross, null));
                views[position].setVisibility(View.VISIBLE);
                cells[position] = R.drawable.ic_cross;
                relativeLayouts[position].setClickable(false);
            }
        }

        binding.parent.addView(gridLayout);
    }

    String evaluateBoard(Context context, int... cells) {

        // Rows
        if (cells[0]==cells[1] && cells[1]==cells[2]) {
            if (cells[0]==R.drawable.ic_cross) {
                return strXWin;
            }
            else if (cells[0]==R.drawable.ic_nought){
                return strOWin;
            }
        }
        if (cells[3]==cells[4] && cells[4]==cells[5]) {
            if (cells[3]==R.drawable.ic_cross) {
                return strXWin;
            }
            else if (cells[3]==R.drawable.ic_nought){
                return strOWin;
            }
        }
        if (cells[6]==cells[7] && cells[7]==cells[8]) {
            if (cells[6]==R.drawable.ic_cross) {
                return strXWin;
            }
            else if (cells[6]==R.drawable.ic_nought){
                return strOWin;
            }
        }

        // Columns
        if (cells[0]==cells[3] && cells[3]==cells[6]) {
            if (cells[0]==R.drawable.ic_cross) {
                return strXWin;
            }
            else if (cells[0]==R.drawable.ic_nought){
                return strOWin;
            }
        }
        if (cells[1]==cells[4] && cells[4]==cells[7]) {
            if (cells[1]==R.drawable.ic_cross) {
                return strXWin;
            }
            else if (cells[1]==R.drawable.ic_nought){
                return strOWin;
            }
        }
        if (cells[2]==cells[5] && cells[5]==cells[8]) {
            if (cells[2]==R.drawable.ic_cross) {
                return strXWin;
            }
            else if (cells[2]==R.drawable.ic_nought){
                return strOWin;
            }
        }

        // Diagonals
        if (cells[0]==cells[4] && cells[4]==cells[8]) {
            if (cells[0]==R.drawable.ic_cross) {
                return strXWin;
            }
            else if (cells[0]==R.drawable.ic_nought){
                return strOWin;
            }
        }
        if (cells[2]==cells[4] && cells[4]==cells[6]) {
            if (cells[2]==R.drawable.ic_cross) {
                return strXWin;
            }
            else if (cells[2]==R.drawable.ic_nought){
                return strOWin;
            }
        }
        return null;
    }

    ArrayList<Integer> positionsLeft(int... cells) {
        ArrayList<Integer> ints= new ArrayList<>();
        for (int i=0;i<cells.length;++i){
            if (cells[i]==0)
                ints.add(i);
        }
        return ints;
    }

    boolean isMoveLeft(int... cells) {
        for (Integer cell : cells)
            if (cell == 0)
                return true;
        return false;
    }

    void displayGameOverDialog(Context context, String title) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("Play again!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        recreate();
                    }
                })
                .setNegativeButton("Main menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }
}
