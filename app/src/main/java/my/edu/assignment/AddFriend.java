/*
package my.edu.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import my.edu.assignment.R;
import my.edu.assignment.SQLiteAdapter;

public class AddFriend extends AppCompatActivity {
    // Declare views
    private ImageButton ff_back;
    private TextView ff_title;
    private TextView ff_save;
    private EditText ff_input;
    private Button ff_inputbutton;
    private TextView ff_selectmember;
    private ScrollView scrollView;
    private LinearLayout linearLayout;

    private SQLiteAdapter mySQLiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        // Retrieve views and store them in variables
        ff_back = findViewById(R.id.ff_back);
        ff_title = findViewById(R.id.ff_title);
        ff_save = findViewById(R.id.ff_save);
        //ff_input = findViewById(R.id.ff_input);
        ff_inputbutton = findViewById(R.id.ff_Addbutton);
        ff_selectmember = findViewById(R.id.ff_selectmember);
        scrollView = findViewById(R.id.scrollView);
        linearLayout = findViewById(R.id.llscroll);

        //database initialization
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();
        mySQLiteAdapter.deleteStudentTable();


        mySQLiteAdapter.insertStudentTable("XiaoMing", "14");
        mySQLiteAdapter.insertStudentTable("Nicholas", "12");
        mySQLiteAdapter.insertStudentTable("popop", "12");
        mySQLiteAdapter.close();

        mySQLiteAdapter.openToRead();
        ArrayList<String[]> contentRead = mySQLiteAdapter.selectStudentTable_one();

        //get all value
        Intent intent = getIntent();
        String expenseName = intent.getStringExtra("ExpenseName");
        String currency = intent.getStringExtra("Currency");
        String totalPaymentStr = intent.getStringExtra("TotalPayment");
        String type = intent.getStringExtra("Type");
        String pplAmountStr = intent.getStringExtra("PeopleAmount");
        int pplAmount = Integer.parseInt(pplAmountStr);

        // Array to store the checked CheckBox values
        CheckBox[] checkBox = new CheckBox[contentRead.size()];

        String result[] = new String[contentRead.size()];

        // Check if there are any results in the contentRead
        if (contentRead != null && contentRead.size() > 0) {
            for (int i = 0; i < checkBox.length; i++) {
                // Get the first result from contentRead
                result = contentRead.get(i);
                checkBox[i] = new CheckBox(this);
                checkBox[i].setText(result[0]);

                linearLayout.addView(checkBox[i]);

               */
/* final int index = i; // Create a local final variable to use in the OnClickListener
                checkBox[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkedValues[index] = checkBox[index].getText().toString();
                    }
                });*//*

            }
        }

        //need add check checkbox number button


        ff_inputbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] checkedValues = new String[pplAmount];
                int checkValuesCount = 0;
                for(int i=0; i<checkBox.length; i++){
                    if(checkBox[i].isChecked()){
                        checkedValues[checkValuesCount++] = checkBox[i].getText().toString();
                    }
                }

                if(type =="CustomBreakDown"){
                    Intent intent = new Intent(AddFriend.this, CustomBreakDown.class);
                    intent.putExtra("PeopleAmount", pplAmountStr);
                    intent.putExtra("ExpenseName", expenseName);
                    intent.putExtra("Currency", currency);
                    intent.putExtra("TotalPayment", totalPaymentStr);
                    intent.putExtra("People Paid", checkedValues);
                    startActivity(intent);
                } else {
                    //pass to equal breakdown page
                    Intent intent = new Intent(AddFriend.this, EqualBreakDownPage.class);
                    intent.putExtra("PeopleAmount", pplAmountStr);
                    intent.putExtra("ExpenseName", expenseName);
                    intent.putExtra("Currency", currency);
                    intent.putExtra("TotalPayment", totalPaymentStr);
                    intent.putExtra("PeoplePaid", checkedValues);
                    startActivity(intent);
                }

            }
        });
    }


    */
/*//*
/ Method to print the checked values (for testing purposes)
    private void printCheckedValues() {
        for (String value : checkedValues) {
            System.out.println("Checked: " + value);
        }
    }*//*

}
*/
