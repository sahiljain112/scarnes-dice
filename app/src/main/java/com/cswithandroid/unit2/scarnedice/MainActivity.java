package com.cswithandroid.unit2.scarnedice;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    int userScore = 0;
    int compScore = 0;
    int localScore = 0;
    boolean userTurn = true;

    Handler handler = new Handler();

    int diceIndex[] = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6 };

    Animation anim1,anim2;
    TextView tvPlayer,tvOpponent,tvScore,tvHeader;
    ImageView ivDice;
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
        tvScore = (TextView) findViewById(R.id.tvScore);
        ivDice = (ImageView) findViewById(R.id.ivDice);
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
                rollButton();
            }
        });

        bReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                resetButton();
            }
        });
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

        if(score >= 20){
            tvHeader.setText("Game over!");
            bHold.setClickable(false);
            bRoll.setClickable(false);

            Context context = getApplicationContext();
            CharSequence gameOver = "Game Over!";
            CharSequence newGame = "Starting new game!";

            Toast.makeText(context, gameOver, Toast.LENGTH_LONG).show();
            Toast.makeText(context, newGame, Toast.LENGTH_LONG).show();


            try {
                TimeUnit.MILLISECONDS.sleep(5000);
                resetButton();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void resetButton(){
        localScore = 0;
        userScore = 0;
        compScore = 0;

        String playerScore = "Player: " + localScore;
        tvPlayer.setText(playerScore);
        String opponentScore = "Opponent: " + localScore;
        tvOpponent.setText(opponentScore);
        String setScore = "Current_Score: " + localScore;
        tvScore.setText(setScore);
        tvHeader.setText("Get a 100 to win!");

        bHold.setClickable(true);
        bRoll.setClickable(true);

        tvPlayer.setTextColor(Color.GREEN);
        tvOpponent.setTextColor(Color.BLACK);

    }

    public void holdButton(){

        changeTextColor();
        if(userTurn){
            userScore = userScore + localScore;
            String playerScore = "Player: " + userScore;
            tvPlayer.setText(playerScore);
        }
        else{
            compScore = compScore + localScore;
            String opponentScore = "Opponent: " + compScore;
            tvOpponent.setText(opponentScore);
        }
        userTurn = !userTurn;
        localScore = 0;

        String setScore = "Current_Score: " + localScore;
        tvScore.setText(setScore);
    }

}
