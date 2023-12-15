package my.edu.assignment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainPage extends AppCompatActivity {

    //declare all the variable
    private TextView mp_expenseinput, mp_title;
    private EditText mp_pplamountinput;
    private Spinner mp_currencyinput;
    private EditText mp_payableinput;
    private Button mp_custombreakdownbtn, mp_equalbreakdownbtn, mp_history, mp_combinationbreakdownbtn;
    private LinearLayout linearLayoutScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set the content layout to main page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        //get all value from XML
        mp_title = findViewById(R.id.mp_title);
        mp_expenseinput = findViewById(R.id.mp_expenseinput);
        mp_pplamountinput = findViewById(R.id.mp_pplamountinput);
        mp_currencyinput = findViewById(R.id.mp_currencyinput);
        mp_payableinput = findViewById(R.id.mp_payableinput);
        mp_custombreakdownbtn = findViewById(R.id.mp_custombreakdownbutton);
        mp_equalbreakdownbtn = findViewById(R.id.mp_equalbreakdownbutton);
        mp_combinationbreakdownbtn = findViewById(R.id.mp_combinationbreakdownbutton);
        linearLayoutScroll = findViewById(R.id.cb_llscroll);
        mp_history = findViewById(R.id.mp_history);

        //set type for input
        mp_payableinput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mp_pplamountinput.setInputType(InputType.TYPE_CLASS_NUMBER);

        //get all value from last page
        Intent intent = getIntent();
        String userName = intent.getStringExtra("Name");

        mp_title.setText("Welcome, " + userName);

        String[] currencyOptions = {"(Pick One)","USD", "MYR", "HKD", "JPY"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, currencyOptions);
        mp_currencyinput.setAdapter(adapter);

        //button navigate to equal breakdown page
        mp_equalbreakdownbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkEmpty()) {
                    int pplAmount = Integer.parseInt(mp_pplamountinput.getText().toString());
                    double totalPayment = Double.parseDouble(mp_payableinput.getText().toString());
                    Intent intent = new Intent(MainPage.this, EqualBreakDown.class);

                    intent.putExtra("PeopleAmount", pplAmount);
                    intent.putExtra("ExpenseName", mp_expenseinput.getText().toString());
                    intent.putExtra("Currency", mp_currencyinput.getSelectedItem().toString());
                    intent.putExtra("TotalPayment", totalPayment);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainPage.this,
                            "Please fill in all the column",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //button navigate to custom breakdown page
        mp_custombreakdownbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkEmpty()) {
                    // Navigate to custom breakdown page
                    Intent intent = new Intent(MainPage.this, CustomBreakDown.class);
                    int pplAmount = Integer.parseInt(mp_pplamountinput.getText().toString());
                    double totalPayment = Double.parseDouble(mp_payableinput.getText().toString());

                    intent.putExtra("PeopleAmount", pplAmount);
                    intent.putExtra("ExpenseName", mp_expenseinput.getText().toString());
                    intent.putExtra("Currency", mp_currencyinput.getSelectedItem().toString());
                    intent.putExtra("TotalPayment", totalPayment);
                    startActivity(intent);
                } else{
                    Toast.makeText(MainPage.this,
                            "Please fill in all the column",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //button navigate to combination breadkdown page
        mp_combinationbreakdownbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkEmpty()){
                    Intent intent = new Intent(MainPage.this, CombinationBreakDown.class);
                    int pplAmount = Integer.parseInt(mp_pplamountinput.getText().toString());
                    double totalPayment = Double.parseDouble(mp_payableinput.getText().toString());

                    intent.putExtra("PeopleAmount", pplAmount);
                    intent.putExtra("ExpenseName", mp_expenseinput.getText().toString());
                    intent.putExtra("Currency", mp_currencyinput.getSelectedItem().toString());
                    intent.putExtra("TotalPayment", totalPayment);
                    startActivity(intent);
                } else{
                    Toast.makeText(MainPage.this,
                            "Please fill in all the column",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //button navigate to history page
        mp_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, History.class);
                startActivity(intent);
            }
        });
    }

    //function to check empty input
    private boolean checkEmpty(){
        if(mp_pplamountinput.getText().toString().equals("")||mp_expenseinput.getText().toString().equals("")
        ||mp_currencyinput.getSelectedItem().toString().equals("")||mp_payableinput.getText().toString().equals("")
        ||mp_currencyinput.getSelectedItem().toString().equals("(Pick One)")){
            return true;
        } else {
            return false;
        }
    }


}

