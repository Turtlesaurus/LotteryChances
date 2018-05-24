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

        getLottery gl = new getLottery(this);
        gl.execute();
    }

    //update on screen text
    public void updateTextView(int mJack, int pJack, int lJack, double megaMillions, double powerball, double lotto){
        //update displayed jackpot after adding commas
        mResp.setText("$" + addCommas(mJack));
        pResp.setText("$" + addCommas(pJack));
        lResp.setText("$" + addCommas(lJack));

        //update displayed ticket value to two decimal places
        mP.setText("Ticket value: $" + String.format("%.2f", megaMillions));
        pP.setText("Ticket value: $" + String.format("%.2f", powerball));
        lP.setText("Ticket value: $" + String.format("%.2f", lotto));
    }

    //makes jackpot amounts pretty
    public String addCommas (int value){
        DecimalFormat formatter = new DecimalFormat("#,###,###,###");
        return formatter.format(value);
    }

    private class getLottery extends AsyncTask<URL, Integer, Double> {

        private LotteryChancesActivity lca;
        int pJackpot = 0;
        int mJackpot = 0;
        int lJackpot = 0;
        double powerball = 0;
        double megaMillions = 0;
        double lotto = 0;

        public getLottery(LotteryChancesActivity pass){
            lca = pass;
        }

        protected Double doInBackground(URL... urls) {

            try {
                pJackpot = readFromWeb("https://www.lotto.net/powerball/numbers");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                mJackpot = readFromWeb("https://www.lotto.net/mega-millions/numbers");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                lJackpot = readFromWeb("https://www.lotto.net/illinois-lotto/numbers");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //calculate ticket value based on jackpot, odds, and ticket cost
            powerball = (((pJackpot - 2) * (.000000003422297813)) - 1.68) + 2;
            megaMillions = (((mJackpot - 2) * (.000000003304961888)) - 1.75) + 2;
            lotto = (((lJackpot - 1) * (.00000004911948413)) - .74) + 1;

            return null;
        }

        //send jackpot and ticket values back to view for update
        protected void onPostExecute(Double nothing){
            lca.updateTextView(mJackpot, pJackpot, lJackpot, megaMillions, powerball, lotto);
        }
    }

    //pulls jackpot amount from given lotto.net game page
    public static int readFromWeb(String webURL) throws IOException {

        URL url = new URL(webURL);
        InputStream is =  url.openStream();
        String jackpotValue = "";
        String jackpotString = " 0,";

        try( BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Next Estimated Jackpot")) {
                    jackpotString = line.substring((line.indexOf("$") + 1), line.indexOf("</span>"));
                }
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new MalformedURLException("URL is malformed!!");
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }

        for(int i=0; i<jackpotString.length(); i++) {
            if(Character.isDigit(jackpotString.charAt(i))){
                jackpotValue += jackpotString.charAt(i);
            }else if(jackpotString.charAt(i) == 'M'){
                i = jackpotString.length();
                jackpotValue += "000000";
            }
        }
        return Integer.parseInt(jackpotValue);
    }
}
