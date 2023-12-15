package my.edu.assignment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EqualBreakDownPage extends AppCompatActivity {

    // Declaration for all variable
    private TextView eb_totalamount;
    private TextView eb_totalppl;
    private TextView eb_payable;
    private TextView eb_perperson;
    private TextView eb_databasemsg;
    private Button ebHistory;
    private LinearLayout ll;
    private SQLiteAdapter mySQLiteAdapter;
    private String expenseName, currency;
    private String[] nameArray, phoneArray,paidArray;
    private int pplAmount;
    private double  totalPayment;
    private ImageButton eb_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equal_break_down_page);

        // Retrieve views and store them in variables
        eb_totalamount = findViewById(R.id.eb_totalamount);
        eb_totalppl = findViewById(R.id.eb_totalppl);
        eb_payable = findViewById(R.id.eb_payable);
        eb_perperson = findViewById(R.id.eb_perperson);
        eb_share = findViewById(R.id.eb_share);
        ebHistory = findViewById(R.id.eb_history);
         ll = findViewById(R.id.eb_ll);

        //get all value
        Intent intent = getIntent();
        expenseName = intent.getStringExtra("ExpenseName");
        currency = intent.getStringExtra("Currency");
        totalPayment = intent.getDoubleExtra("TotalPayment",0);
        pplAmount = intent.getIntExtra("PeopleAmount",0);
        nameArray = intent.getStringArrayExtra("NameArray");
        phoneArray  = intent.getStringArrayExtra("PhoneArray");
        paidArray = intent.getStringArrayExtra("PaidArray");

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

        //display all the basic info
        eb_totalamount.setText("Total Amount: "+ currency + " "+ String.format("%.2f",totalPayment));
        eb_totalppl.setText("Total People: "+pplAmount);
        Double payBalance = totalPayment / pplAmount;
        eb_payable.setText(currency+" "+String.format("%.2f",payBalance));

        //textview for congratulation
        TextView desc = new TextView(this);
        desc.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        desc.setText("Congrats");
        desc.setTextSize(20.0f);
        desc.setGravity(Gravity.CENTER);
        ll.addView(desc);

        //display name for the payment
        for(int i=0; i<nameArray.length; i++){
            TextView number = new TextView(this);  //person count
            number.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            number.setText(i+1+".");
            number.setTextSize(16.0f);
            number.setGravity(Gravity.RIGHT);

            TextView nameView = new TextView(this);  //person name
            nameView.setLayoutParams(new LinearLayout.LayoutParams(
                    180,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            nameView.setText(nameArray[i]);
            nameView.setTextSize(16.0f);
            nameView.setGravity(Gravity.LEFT);

            LinearLayout horizontalLayout = new LinearLayout(this); //layout to fill show all friends
            horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            horizontalLayout.addView(number);
            horizontalLayout.addView(nameView);
            ll.addView(horizontalLayout);

            mySQLiteAdapter.insert(expenseName, nameArray[i], currency, String.valueOf(payBalance),
                    String.valueOf(totalPayment), paidArray[i],dateTimeString);
        }

        TextView post = new TextView(this);
        post.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        post.setText("Remember to Pay the money");
        post.setTextSize(20.0f);
        post.setGravity(Gravity.CENTER);
        ll.addView(post);

        //share button connect to Whatsapp api
        eb_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=expenseName+"\n";

                //get the data from database
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

                //output loading message
                Toast.makeText(EqualBreakDownPage.this,
                        "Connecting to Whatsapp API...", Toast.LENGTH_SHORT).show();

                //change to all ppl phone no
                String url = "https://api.whatsapp.com/send?text="+message;

                // Create an Intent with the ACTION_VIEW to open the URL
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(EqualBreakDownPage.this, "Error: Cannot open URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //button to show the history
        ebHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EqualBreakDownPage.this,
                        "Successfully add the data into the database", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EqualBreakDownPage.this, History.class);
                startActivity(intent);
            }
        });
    }
}