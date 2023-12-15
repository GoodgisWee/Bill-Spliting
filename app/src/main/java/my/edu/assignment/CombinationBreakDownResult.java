package my.edu.assignment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CombinationBreakDownResult extends AppCompatActivity {

    private LinearLayout linearLayoutScroll;
    private String expenseName;
    private String currency;
    private double totalPayment;
    private double[] amountArray;
    private int peopleAmount, totalRatio=0;
    private int[] percentArray, ratioArray;
    private String[] nameArray, paidArray;
    private TextView cbpTotalPayable;
    private TextView cbpPeople, cbpPayable, cbpTotal;
    private Button ebHistory;
    private TableLayout tableLayout;
    private ImageButton ebShare;

    private SQLiteAdapter mySQLiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination_break_down_result);

        // Find views by their IDs
        ebShare = findViewById(R.id.eb_share);
        ebHistory = findViewById(R.id.eb_history);
        cbpPeople = findViewById(R.id.cbp_people);
        cbpPayable = findViewById(R.id.cbp_payable);

        Intent intent = getIntent();
        expenseName = intent.getStringExtra("ExpenseName");
        currency = intent.getStringExtra("Currency");
        totalPayment = intent.getDoubleExtra("TotalPayment", 0);
        peopleAmount = intent.getIntExtra("PeopleAmount", 0);
        percentArray = intent.getIntArrayExtra("PercentArray");
        amountArray = intent.getDoubleArrayExtra("AmountArray");
        ratioArray = intent.getIntArrayExtra("RatioArray");
        nameArray = intent.getStringArrayExtra("NameArray");
        paidArray = intent.getStringArrayExtra("PaidArray");
        tableLayout = findViewById(R.id.cbp_tableLayout); // Replace 'your_table_layout_id' with the actual ID of your TableLayout

        //initialization for database connection
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();

        // Get the current time and date
        Date currentTime = new Date();
        Date currentDate = new Date();

        // Define the date format you want
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        // Convert the date to a string
        String dateString = dateFormat.format(currentDate);
        String currentTimeString = sdf.format(currentTime);

        String dateTimeString = dateString + " " + currentTimeString;



        if(ratioArray!=null){
            for(int i=0;i<peopleAmount;i++){
                totalRatio +=  ratioArray[i];
            }
        }

        cbpPeople.setText("People ("+peopleAmount+")");

        for(int i=0;i<peopleAmount; i++){
            //table row for name and payment amount
            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            //textview for name of each person
            TextView cbpPp1 = new TextView(this);
            cbpPp1.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            cbpPp1.setText(nameArray[i]);
            cbpPp1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            cbpPp1.setTextColor(Color.BLACK);
            cbpPp1.setPadding(40, 0, 0, 0);
            cbpPp1.setGravity(Gravity.LEFT);

            //textview for payment amount of each person
            TextView cbpPay1 = new TextView(this);
            cbpPay1.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            //calculation for payment amount
            if(percentArray[i]!=0){
                cbpPay1.setText(String.format("%.2f", totalPayment * percentArray[i] / 100));
            } else if(amountArray[i]!=0){
                cbpPay1.setText(String.valueOf(String.format("%.2f",amountArray[i])));
            }
            cbpPay1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            cbpPay1.setTextColor(Color.BLACK);
            cbpPay1.setGravity(Gravity.CENTER);

            //line to seperate each record
            View separator = new View(this);
            separator.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
            separator.setBackgroundColor(Color.BLACK);

            //adding all the view into the layout
            newRow.addView(cbpPp1);
            newRow.addView(cbpPay1);
            tableLayout.addView(newRow);
            tableLayout.addView(separator);

            //insert data into database
            mySQLiteAdapter.insert(expenseName, nameArray[i], currency, cbpPay1.getText().toString(),
                    String.valueOf(totalPayment), paidArray[i],dateTimeString);
        }



        TableRow newRow = new TableRow(this);
        newRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        TextView cbpTotal = new TextView(this);
        cbpTotal.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        cbpTotal.setText("total");
        cbpTotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        cbpTotal.setTextColor(Color.BLACK);
        cbpTotal.setPadding(0, 0, 40, 0);
        cbpTotal.setGravity(Gravity.RIGHT);

        TextView cbpTotalPayable = new TextView(this);
        cbpTotalPayable.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        cbpTotalPayable.setText(String.valueOf(totalPayment));
        cbpTotalPayable.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        cbpTotalPayable.setTextColor(Color.BLACK);
        cbpTotalPayable.setGravity(Gravity.CENTER);

        newRow.addView(cbpTotal);
        newRow.addView(cbpTotalPayable);

        tableLayout.addView(newRow);

        View separator = new View(this);
        separator.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
        separator.setBackgroundColor(Color.BLACK);
        tableLayout.addView(separator);

        ebShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=expenseName+"\n";
                mySQLiteAdapter.openToRead();
                String contentRead = mySQLiteAdapter.queueAll_Optional(expenseName);
                String[] contentArray = contentRead.split(";");
                for (int i = 0; i < contentArray.length-1; i++) {
                    contentArray[i] = contentArray[i].trim();
                    if (i % 2 == 0) {
                        String[] contentArray2 = new String[2];
                        for (int j = 0; j < contentArray2.length; j++) {
                            contentArray2[j] = contentArray[i + j];
                        }
                        message += contentArray2[0] + ": "+String.format("%.2f",Double.parseDouble(contentArray2[1]))+"\n";
                    }
                }

                Toast.makeText(CombinationBreakDownResult.this,
                        "Connecting to Whatsapp API...", Toast.LENGTH_SHORT).show();

                //change to all ppl phone no
                String url = "https://api.whatsapp.com/send?text="+message;

                // Create an Intent with the ACTION_VIEW to open the URL
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(CombinationBreakDownResult.this, "Error: Cannot open URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ebHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CombinationBreakDownResult.this,
                        "Succesfully insert data into the database", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CombinationBreakDownResult.this, History.class);
                startActivity(intent);
            }
        });
    }
}