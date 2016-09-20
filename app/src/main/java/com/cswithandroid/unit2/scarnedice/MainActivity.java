package com.cswithandroid.unit2.scarnedice;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int userScore = 0;
    int compScore = 0;
    int localScore = 0;
    boolean userTurn = true;
    boolean rollAgain = false;
    String ROLL_AGAIN = "Double rolled. Roll again!",
           ONE_ROLLED = "Lost current turn's point!",
           DOUBLE_ONE_ROLLED = "Lost all points! Tough luck!",
           SCORE = "Scored, Nice one!";


    Handler handler = new Handler();

    int diceIndex[] = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6 };

    Animation anim1,anim2;
    TextView tvPlayer,tvOpponent,tvScore,tvHeader, tvFeedback;
    ImageView ivDice, ivDice2;
    Button bRoll;
    Button bReset;
    Button bHold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHeader = (TextView) findViewById(R.id.tvHeader);
        tvPlayer = (TextView) findViewById(R.id.tvPlayer);
        tvOpponent = (TextView) findViewById(R.id.tvOpponent);
        tvFeedback = (TextView) findViewById(R.id.tvFeedback);
        tvScore = (TextView) findViewById(R.id.tvScore);
        ivDice = (ImageView) findViewById(R.id.ivDice);
        ivDice2 = (ImageView) findViewById(R.id.ivDice2);
        bRoll = (Button) findViewById(R.id.bRoll);
        bReset = (Button) findViewById(R.id.bReset);
        bHold = (Button) findViewById(R.id.bHold);

        anim1 = new TranslateAnimation(0.0f,200.0f,0.0f,0.0f);
        anim2 = new TranslateAnimation(0.0f,-200.0f,0.0f,0.0f);

        anim1.setDuration(200);
        anim2.setDuration(200);

         tvPlayer.setTextColor(Color.GREEN);
         tvOpponent.setTextColor(Color.BLACK);
         tvScore.setTextColor(Color.BLACK);

//        for(int i = 1; i < 7; i++){
//            String dice = "dice" + i;
//            diceIndex[i-1] = R.drawable[dice];
//         }

        bHold.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                holdButton();
            }
        });

        bRoll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
               // rollButton();
                 rollDice();
            }
        });

        bReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                resetButton();
            }
        });
    }

    public void rollDice() {

        Random r = new Random();
        int dice = r.nextInt(6) + 1;
        int dice2 = r.nextInt(6) + 1;

        ivDice.setImageResource(diceIndex[dice - 1]);
        ivDice2.setImageResource(diceIndex[dice2 - 1]);

        if(rollAgain){
            rollAgain = false;
            bHold.setClickable(true);
        }

        if(dice == 1 || dice2 == 1){

            Log.d("Condition", "Entered 1's dice condition");
            changeTextColor();

                if(dice == 1 && dice2 == 1) {
                    resetScore();
                    showFeedback(DOUBLE_ONE_ROLLED);
                }
                else
                    showFeedback(ONE_ROLLED);

            userTurn = !userTurn;
            localScore = 0;
        }
        else if (dice == dice2){

            // Roll again logic
            Log.d("Condition", "Entered equal dice condition");
            localScore = localScore + dice + dice2;
            rollAgain = true;
            bHold.setClickable(false);
            showFeedback(ROLL_AGAIN);
        }
        else {
            Log.d("Condition", "Entered unequal dice condition");
            localScore = localScore + dice + dice2;
            showFeedback(SCORE);
        }

        setScores();
        checkWinCase();

        Log.d("Roll", "Dice rolled successfully");
    }

    public void resetScore() {
        // Reset entire score here
        if(userTurn)
            userScore = 0;
        else
            compScore = 0;

    }

    public void setScores() {

        String setScore = "Current_Score: " + localScore;
        tvScore.setText(setScore);

        String playerScore = "Player: " + userScore;
        tvPlayer.setText(playerScore);

        String opponentScore = "Opponent: " + compScore;
        tvOpponent.setText(opponentScore);
    }

    public void rollButton(){

        Random r = new Random();
        int dice = r.nextInt(6) + 1;
      //  String imagePath = "@drawable/dice" + dice;
        ivDice.setImageResource(diceIndex[dice - 1]);
        ivDice.setAnimation(anim1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivDice.setAnimation(null);
            }
        }, 200);

//        ivDice.setAnimation(anim2);
//        ivDice.setAnimation(anim2);
//        ivDice.setAnimation(anim1);

        if(dice == 1){
            changeTextColor();
            userTurn = !userTurn;
            localScore = 0;
        }
        else{
            localScore = localScore + dice;
        }
        String setScore = "Current_Score: " + localScore;
        tvScore.setText(setScore);
        checkWinCase();

    }

    public void changeTextColor(){

        if(userTurn){
            tvOpponent.setTextColor(Color.GREEN);
            tvPlayer.setTextColor(Color.BLACK);
        }
        else{
            tvPlayer.setTextColor(Color.GREEN);
            tvOpponent.setTextColor(Color.BLACK);
        }
    }
    public void checkWinCase() {
        int score = 0;
        if(userTurn){
            score = userScore + localScore;
        }
        else{
           score = compScore + localScore;
        }

        if(score >= 100){
            tvHeader.setText("Game over!");
            bHold.setClickable(false);
            bRoll.setClickable(false);

            Context context = getApplicationContext();
            CharSequence gameOver = "Game Over!";
            CharSequence newGame = "Starting new game!";

            Toast.makeText(context, gameOver, Toast.LENGTH_SHORT).show();
            Toast.makeText(context, newGame, Toast.LENGTH_LONG).show();

            try {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetButton();
                    }
                }, 4000);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void showFeedback(String feedback) {
        tvFeedback.setText(feedback);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvFeedback.setText("Feedback!");
            }
        },1500);
    }

    public void resetButton(){
        localScore = 0;
        userScore = 0;
        compScore = 0;

        setScores();
        tvHeader.setText("Get a 100 to win!");

        bHold.setClickable(true);
        bRoll.setClickable(true);

        tvPlayer.setTextColor(Color.GREEN);
        tvOpponent.setTextColor(Color.BLACK);

    }

    public void addScores() {
        if(userTurn){
            userScore = userScore + localScore;
        }
        else{
            compScore = compScore + localScore;
        }
    }

    public void holdButton(){

        changeTextColor();
        addScores();

        userTurn = !userTurn;
        localScore = 0;

        setScores();
    }

}
