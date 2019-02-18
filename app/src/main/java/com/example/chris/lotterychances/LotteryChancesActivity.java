package com.example.chris.lotterychances;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.text.Html;
import java.text.DecimalFormat;


public class LotteryChancesActivity extends AppCompatActivity {
    //global declarations
    //text views for jackpot amounts
    TextView mJack, pJack, lJack;
    //text views for probability based ticket values
    TextView mProb, pProb, lProb;
    //text views for lump sums
    TextView mLump, pLump, lLump;
    //text views for taxed lump sums
    TextView mAfTax, pAfTax, lAfTax;
    //pass instance of this class to getLottery so views can be updated
    getLottery mGL = new getLottery(this);
    getLottery pGL = new getLottery(this);
    getLottery lGL = new getLottery(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_chances);

        mJack = findViewById(R.id.mInfo);
        pJack = findViewById(R.id.pInfo);
        lJack = findViewById(R.id.lInfo);
        mProb = findViewById(R.id.mProb);
        pProb = findViewById(R.id.pProb);
        lProb = findViewById(R.id.lProb);
        mLump = findViewById(R.id.mLump);
        pLump = findViewById(R.id.pLump);
        lLump = findViewById(R.id.lLump);
        mAfTax = findViewById(R.id.mAfTax);
        pAfTax = findViewById(R.id.pAfTax);
        lAfTax = findViewById(R.id.lAfTax);

        mGL.runNumbers(2, .000000003304961888, 1.75, "https://www.lotto.net/mega-millions/numbers");
        pGL.runNumbers(2, .000000003422297813, 1.68, "https://www.lotto.net/powerball/numbers");
        lGL.runNumbers(1, .00000004911948413, .74, "https://www.lotto.net/illinois-lotto/numbers");
    }

    //update on screen text
    public void updateTextView(){
        //update displayed jackpot after adding commas
        mJack.setText("$" + addCommas(mGL.getJackpot()));
        pJack.setText("$" + addCommas(pGL.getJackpot()));
        lJack.setText("$" + addCommas(lGL.getJackpot()));

        //update displayed lump sum after adding commas
        mLump.setText("Lump:\n$" + addCommas(Integer.parseInt(String.format("%.0f", mGL.getRawLumpSum()))));
        pLump.setText("Lump:\n$" + addCommas(Integer.parseInt(String.format("%.0f", pGL.getRawLumpSum()))));
        lLump.setText("Lump:\n$" + addCommas(Integer.parseInt(String.format("%.0f", lGL.getRawLumpSum()))));

        //update displayed after tax lump sum after adding commas
        mAfTax.setText("Taxed:\n$" + addCommas(Integer.parseInt(String.format("%.0f", mGL.getTaxedLumpSum()))));
        pAfTax.setText("Taxed:\n$" + addCommas(Integer.parseInt(String.format("%.0f", pGL.getTaxedLumpSum()))));
        lAfTax.setText("Taxed:\n$" + addCommas(Integer.parseInt(String.format("%.0f", lGL.getTaxedLumpSum()))));

        //set display text and color for given value
        String mText = colorText(mGL.getProbValue(), mGL.getTicketPrice());
        String pText = colorText(pGL.getProbValue(), pGL.getTicketPrice());
        String lText = colorText(lGL.getProbValue(), lGL.getTicketPrice());

        //update displayed text and ticket value to two decimal places
        mProb.setText(Html.fromHtml(mText + String.format("%.2f", mGL.getProbValue()) + "</font>", 0));
        pProb.setText(Html.fromHtml(pText + String.format("%.2f", pGL.getProbValue()) + "</font>", 0));
        lProb.setText(Html.fromHtml(lText + String.format("%.2f", lGL.getProbValue()) + "</font>", 0));
    }

    //turn ticket value pretty colors based on comparison to cost
    public String colorText(double tempValue, int tempPrice){
        if(tempValue/tempPrice >= 1){
            return "Value: $" + "<font color=\'green\'>";
        }else if(tempValue/tempPrice >= .9){
            return "Value: $" + "<font color=\'yellow\'>";
        }else if(tempValue/tempPrice >= .75){
            return "Value: $" + "<font color=\'#FFA500\'>";
        }else{
            return "Value: $" + "<font color=\'red\'>";
        }
    }

    //makes jackpot amounts pretty
    public String addCommas (int value){
        DecimalFormat formatter = new DecimalFormat("#,###,###,###");
        return formatter.format(value);
    }
}
