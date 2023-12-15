package my.edu.assignment;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class History extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdapter;
    private Button friendBtn, expenseBtn, editBtn;
    private String currentExpenseName, currentDateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //get all view
        friendBtn = findViewById(R.id.friendBtn);
        expenseBtn = findViewById(R.id.expenseBtn);
        editBtn = findViewById(R.id.editBtn);
        TextView txt = findViewById(R.id.listContent);

        //Create table view
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        //db initialization
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();

        //read all data from database
        String friend = mySQLiteAdapter.queueAllFriend();
        String contentRead = mySQLiteAdapter.queueAll();

        String[] friendArray = friend.split(";");
        String[] contentArray = contentRead.split(";");


        friendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableLayout.removeAllViews();
                //change the UI
                expenseBtn.setBackgroundColor(Color.parseColor("#2196F3"));
                expenseBtn.setTextColor(Color.WHITE);
                friendBtn.setBackgroundColor(Color.WHITE);
                friendBtn.setTextColor(Color.parseColor("#2196F3"));

                //check empty database
                if (friendArray.length == 1) {
                    TextView txt = findViewById(R.id.listContent);
                    txt.setText("The History is now empty, history record will appear once you added record");
                }

                //row for column header
                TableRow friendTitleRow = new TableRow(History.this);

                //padding for friend count column
                int paddingInDp = 16;
                int paddingInPixels = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, paddingInDp, getResources().getDisplayMetrics()
                );

                //title column: id
                TextView friendIdTitleView = new TextView(History.this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, paddingInPixels, paddingInPixels);
                friendIdTitleView.setLayoutParams(params);
                friendIdTitleView.setText("ID");
                friendIdTitleView.setGravity(Gravity.CENTER);
                friendIdTitleView.setTypeface(null, Typeface.BOLD);

                //title column: name
                TextView friendNameTitleView = new TextView(History.this);
                friendNameTitleView.setLayoutParams(new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1
                ));
                friendNameTitleView.setText("Name");
                friendNameTitleView.setGravity(Gravity.LEFT);
                friendNameTitleView.setTypeface(null, Typeface.BOLD);

                //title column: phoneNo
                TextView friendPhoneNoTitleView = new TextView(History.this);
                friendPhoneNoTitleView.setLayoutParams(new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1
                ));
                friendPhoneNoTitleView.setText("Phone No");
                friendPhoneNoTitleView.setGravity(Gravity.LEFT);
                friendPhoneNoTitleView.setTypeface(null, Typeface.BOLD);

                //add all view into each record row
                friendTitleRow.addView(friendIdTitleView);
                friendTitleRow.addView(friendNameTitleView);
                friendTitleRow.addView(friendPhoneNoTitleView);
                tableLayout.addView(friendTitleRow);

                //start showing all the record
                for (int i = 0; i < friendArray.length - 1; i = i + 2) {
                    TableRow friendRow = new TableRow(History.this);

                    //Show the Count
                    TextView friendCountView = new TextView(History.this);
                    params.setMargins(0, 0, paddingInPixels, paddingInPixels);
                    friendCountView.setLayoutParams(params);
                    friendCountView.setText(String.valueOf(i / 2 + 1) + "."); //Date
                    friendCountView.setGravity(Gravity.LEFT);

                    //Show the Name
                    TextView friendNameView = new TextView(History.this);
                    friendNameView.setLayoutParams(new TableRow.LayoutParams(
                            0,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1
                    ));
                    friendNameView.setText(friendArray[i]);
                    friendNameView.setGravity(Gravity.LEFT);

                    //Show the Phone
                    TextView friendPhoneNoView = new TextView(History.this);
                    friendPhoneNoView.setLayoutParams(new TableRow.LayoutParams(
                            0,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1
                    ));
                    friendPhoneNoView.setText(friendArray[i + 1]);
                    friendPhoneNoView.setGravity(Gravity.LEFT);

                    //add all view into the layout
                    friendRow.addView(friendCountView);
                    friendRow.addView(friendNameView);
                    friendRow.addView(friendPhoneNoView);
                    tableLayout.addView(friendRow);
                }
            }
        });

        //expense button to show expense history
        expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change the UI
                expenseBtn.setBackgroundColor(Color.WHITE);
                expenseBtn.setTextColor(Color.parseColor("#2196F3"));
                friendBtn.setBackgroundColor(Color.parseColor("#2196F3"));
                friendBtn.setTextColor(Color.WHITE);

                //refresh all the value
                txt.setText(null);
                tableLayout.removeAllViews();
                currentExpenseName = "";
                currentDateStr="";

                //check if the db is empty
                if (contentArray.length == 1) {
                    txt.setText("The History is now empty, history record will appear once you added record");
                }

                //initialize for all variable for expense database
                int count=0;

                //output all the expense details
                for (int i = 0; i < contentArray.length - 1; i++) {
                    contentArray[i] = contentArray[i].trim();
                    if (i % 7 == 0) {
                        String[] contentArray2 = new String[7];
                        for (int j = 0; j < contentArray2.length; j++) {
                            contentArray2[j] = contentArray[i + j];
                        }

                        //to seperate head and body content
                        if (!currentExpenseName.equals(contentArray2[0])|| !currentDateStr.equals(contentArray2[6])) {
                            count = 1;
                            if (i != 0) {
                                //create empty row to better align the result
                                TableRow emptyRow = new TableRow(History.this);
                                TextView emptyTextView = new TextView(History.this);
                                emptyRow.addView(emptyTextView);
                                tableLayout.addView(emptyRow);

                                //creating a line to seperate all table
                                View newView = new View(History.this);
                                newView.setBackgroundColor(getResources().getColor(android.R.color.black));
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        dpToPx(2)
                                );
                                newView.setLayoutParams(layoutParams);
                                tableLayout.addView(newView);
                            }
                            //Show the bill Name and total in one row
                            TableRow billNameRow = new TableRow((History.this));   //bill name
                            TextView billNameView = new TextView(History.this);
                            TableRow.LayoutParams billLayoutParams = new TableRow.LayoutParams(
                                    0,
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    1
                            );
                            int leftMarginInPixels = dpToPx(20);
                            billLayoutParams.setMargins(leftMarginInPixels, 0, 0, 0);
                            billNameView.setLayoutParams(billLayoutParams);
                            billNameView.setText("Bill Name: " + contentArray2[0]);
                            billNameView.setGravity(Gravity.LEFT);

                            TextView totalView = new TextView(History.this);  //total
                            totalView.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT
                            ));
                            totalView.setText("Total: " + String.format("%.2f", Double.parseDouble(contentArray2[4])));
                            totalView.setGravity(Gravity.LEFT);

                            billNameRow.addView(billNameView);
                            billNameRow.addView(totalView);

                            //row to show date and currency
                            TableRow dateRow = new TableRow(History.this);
                            TextView dateView = new TextView(History.this); //Date
                            dateView.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT
                            ));
                            dateView.setLayoutParams(billLayoutParams);
                            dateView.setText("Date: " + contentArray2[6]); //Date
                            dateView.setGravity(Gravity.LEFT);
                            TextView currencyView = new TextView(History.this);  //currency
                            currencyView.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT
                            ));
                            currencyView.setText("Currency: "+contentArray2[2]);
                            currencyView.setGravity(Gravity.CENTER);

                            dateRow.addView(dateView);
                            dateRow.addView(currencyView);

                            //row for column header
                            TableRow titleRow = new TableRow(History.this);
                            TextView IdTitleView = new TextView(History.this);  //id
                            TableRow.LayoutParams params = new TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT
                            );
                            IdTitleView.setLayoutParams(params);
                            IdTitleView.setText("ID");
                            IdTitleView.setGravity(Gravity.CENTER);
                            IdTitleView.setTypeface(null, Typeface.BOLD);

                            TextView nameView = new TextView(History.this);  //name
                            nameView.setLayoutParams(new TableRow.LayoutParams(
                                    0,
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    1
                            ));
                            nameView.setText("Name");
                            nameView.setGravity(Gravity.CENTER);
                            nameView.setTypeface(null, Typeface.BOLD);

                            TextView paymentView = new TextView(History.this);  //payment
                            paymentView.setLayoutParams(new TableRow.LayoutParams(
                                    0,
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    1
                            ));
                            paymentView.setText("Payment");
                            paymentView.setGravity(Gravity.CENTER);
                            paymentView.setTypeface(null, Typeface.BOLD);

                            TextView paymentStatusView = new TextView(History.this); //payment status
                            paymentView.setLayoutParams(new TableRow.LayoutParams(
                                    0,
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    1
                            ));
                            paymentStatusView.setText("Payment Status");
                            paymentStatusView.setGravity(Gravity.CENTER);
                            paymentStatusView.setTypeface(null, Typeface.BOLD);

                            titleRow.addView(IdTitleView);
                            titleRow.addView(nameView);
                            titleRow.addView(paymentView);
                            titleRow.addView(paymentStatusView);

                            // Create the first row (header row)
                            TableRow firstRow = new TableRow(History.this);
                            for (int k = 0; k < contentArray2.length; k++) {
                                if(k==0) {
                                    TextView headerTextView1 = new TextView(History.this);
                                    headerTextView1.setLayoutParams(new TableRow.LayoutParams(
                                            TableRow.LayoutParams.WRAP_CONTENT,
                                            TableRow.LayoutParams.WRAP_CONTENT
                                    ));
                                    headerTextView1.setText(String.valueOf(count)+".");
                                    firstRow.addView(headerTextView1);
                                }
                                //exclude billname, total, date, currency
                                if (k != 0 && k != 4 && k != 6 && k!=2) {
                                    TextView headerTextView = new TextView(History.this);
                                    headerTextView.setLayoutParams(new TableRow.LayoutParams(
                                            0,
                                            TableRow.LayoutParams.WRAP_CONTENT,
                                            1
                                    ));
                                    if (k == 3) {  //payment
                                        headerTextView.setText(String.format("%.2f", Double.parseDouble(contentArray2[k])));
                                    } else {
                                        headerTextView.setText(contentArray2[k]);
                                    }
                                    headerTextView.setGravity(Gravity.CENTER);
                                    firstRow.addView(headerTextView);
                                }
                            }

                            //create a empty Row
                            TableRow emptyRow = new TableRow(History.this);
                            TextView emptyTextView = new TextView(History.this);

                            emptyRow.addView(emptyTextView);

                            //add all table row
                            tableLayout.addView(billNameRow);
                            tableLayout.addView(dateRow);
                            tableLayout.addView(emptyRow);
                            tableLayout.addView(titleRow);
                            tableLayout.addView(firstRow);
                        } else {
                            //here is the record Row
                            count++;
                            // Create the first row (header row)
                            TableRow firstRow = new TableRow(History.this);
                            for (int k = 0; k < contentArray2.length; k++) {
                                if(k==0) {
                                    TextView headerTextView1 = new TextView(History.this);
                                    headerTextView1.setLayoutParams(new TableRow.LayoutParams(
                                            TableRow.LayoutParams.WRAP_CONTENT,
                                            TableRow.LayoutParams.WRAP_CONTENT
                                    ));
                                    headerTextView1.setText(String.valueOf(count) + ".");
                                    firstRow.addView(headerTextView1);
                                }
                                //exclude billname, total, date, currency
                                if (k != 0 && k != 4 && k != 6 && k!=2) {
                                    TextView headerTextView = new TextView(History.this);
                                    headerTextView.setLayoutParams(new TableRow.LayoutParams(
                                            0,
                                            TableRow.LayoutParams.WRAP_CONTENT,
                                            1
                                    ));
                                    if (k == 3) {
                                        headerTextView.setText(String.format("%.2f", Double.parseDouble(contentArray2[k])));
                                    } else {
                                        headerTextView.setText(contentArray2[k]);
                                    }
                                    headerTextView.setGravity(Gravity.CENTER); // Align text to center
                                    firstRow.addView(headerTextView);
                                }
                            }

                            tableLayout.addView(firstRow);

                            editBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        }
                        //refresh all video to make sure same bill record under same section
                        currentExpenseName = contentArray2[0];
                        currentDateStr=contentArray2[6];
                    }
                }

            }
        });
    }
    // Convert dp to pixels
    private int dpToPx(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}