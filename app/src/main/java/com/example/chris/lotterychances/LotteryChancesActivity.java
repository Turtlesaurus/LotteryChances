package com.example.chris.lotterychances;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;


public class LotteryChancesActivity extends Activity {
    //create global textviews
    TextView mResp;
    TextView pResp;
    TextView lResp;
    TextView mP;
    TextView pP;
    TextView lP;
    getLottery mGL = new getLottery(this);
    getLottery pGL = new getLottery(this);
    getLottery lGL = new getLottery(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_chances);

        mResp = (TextView) findViewById(R.id.mInfo);
        pResp = (TextView) findViewById(R.id.pInfo);
        lResp = (TextView) findViewById(R.id.lInfo);
        mP = (TextView) findViewById(R.id.mProb);
        pP = (TextView) findViewById(R.id.pProb);
        lP = (TextView) findViewById(R.id.lProb);

        //getLottery gl = new getLottery(this);
        mGL.runNumbers(2, .000000003304961888, 1.75, "https://www.lotto.net/mega-millions/numbers");
        pGL.runNumbers(2, .000000003422297813, 1.68, "https://www.lotto.net/powerball/numbers");
        lGL.runNumbers(1, .00000004911948413, .74, "https://www.lotto.net/illinois-lotto/numbers");
        //gl.execute();
    }

    //update on screen text
    public void updateTextView(){
        //update displayed jackpot after adding commas
        mResp.setText("$" + addCommas(mGL.getJackpot()));
        pResp.setText("$" + addCommas(pGL.getJackpot()));
        lResp.setText("$" + addCommas(lGL.getJackpot()));

        //update displayed ticket value to two decimal places
        mP.setText("Ticket value: $" + String.format("%.2f", mGL.getProbValue()));
        pP.setText("Ticket value: $" + String.format("%.2f", pGL.getProbValue()));
        lP.setText("Ticket value: $" + String.format("%.2f", lGL.getProbValue()));
    }

    //makes jackpot amounts pretty
    public String addCommas (int value){
        DecimalFormat formatter = new DecimalFormat("#,###,###,###");
        return formatter.format(value);
    }
}
