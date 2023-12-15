package my.edu.assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EqualBreakDown extends AppCompatActivity {

    private String expenseName, currency;
    private String[] nameArray, phoneArray, paidArray;
    private double totalPayment;
    private int peopleAmount;
    private LinearLayout linearLayout;
    private SQLiteAdapter mySQLiteAdapter;
    private Button saveFriendBtn, nextBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equal_break_down);

        //get all View
        linearLayout = findViewById(R.id.linearLayout);
        saveFriendBtn = findViewById(R.id.saveFriendbtn);
        nextBtn = findViewById(R.id.nextbtn);

        //get all value
        Intent intent = getIntent();
        expenseName = intent.getStringExtra("ExpenseName");
        currency = intent.getStringExtra("Currency");
        totalPayment = intent.getDoubleExtra("TotalPayment", 0);
        peopleAmount = intent.getIntExtra("PeopleAmount", 0);
        nameArray = new String[peopleAmount];
        phoneArray = new String[peopleAmount];
        paidArray = new String[peopleAmount];


        //initialize db connection
        mySQLiteAdapter = new SQLiteAdapter(this);

        for(int i = 0; i < nameArray.length; i++) {

            //layout to fill in friends detail
            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setPadding(16,0,16,0);

            //friends count
            TextView number = new TextView(this);
            number.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            number.setGravity(Gravity.CENTER_VERTICAL);
            number.setText(String.valueOf(i+1) + ".");
            number.setTextSize(28.0f);

            // friends name
            EditText personName = new EditText(this);
            personName.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            personName.setHint("Name");
            personName.setGravity(Gravity.CENTER_VERTICAL);
            personName.setInputType(InputType.TYPE_CLASS_TEXT);

            handleTextChangesName(personName, i, nameArray);

            // friends phone no
            EditText personPhoneNo = new EditText(this);
            personPhoneNo.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            personPhoneNo.setHint("PhoneNo(optional)");
            personPhoneNo.setGravity(Gravity.CENTER_VERTICAL);
            personPhoneNo.setInputType(InputType.TYPE_CLASS_NUMBER);

            handleTextChangesPhone(personPhoneNo, i, phoneArray);

            //initialize the radio group
            RadioGroup radioGroupPaid = new RadioGroup(this);
            radioGroupPaid.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            radioGroupPaid.setGravity(Gravity.LEFT);

            // Create the "Paid" radio button
            RadioButton radioButtonPaid = new RadioButton(this);
            radioButtonPaid.setText("Paid");
            radioGroupPaid.addView(radioButtonPaid);


            // Create the "Unpaid" radio button
            RadioButton radioButtonUnpaid = new RadioButton(this);
            radioButtonUnpaid.setText("Unpaid");
            radioGroupPaid.addView(radioButtonUnpaid);

            int finalI = i;
            radioGroupPaid.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == radioButtonPaid.getId()) {
                        paidArray[finalI]="Paid";
                    } else if (checkedId == radioButtonUnpaid.getId()) {
                        paidArray[finalI]="Unpaid";
                    }
                }
            });



            LinearLayout horizontalLayout2 = new LinearLayout(this);
            horizontalLayout2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            horizontalLayout2.setOrientation(LinearLayout.HORIZONTAL);

            //add all the layout into the page
            horizontalLayout.addView(number);
            horizontalLayout.addView(personName);
            horizontalLayout.addView(personPhoneNo);
            horizontalLayout2.addView(radioGroupPaid);
            linearLayout.addView(horizontalLayout);
            linearLayout.addView(horizontalLayout2);

            saveFriendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean foundError = false;
                    for (int i = 0; i < nameArray.length; i++) {
                        if (nameArray[i] == null || nameArray[i].equals("") || phoneArray[i] == null || phoneArray[i].equals("")) {
                            foundError = true;
                            break;
                        } else {
                            for (int j = 0; j < nameArray.length; j++) {
                                if (i != j && (nameArray[i].equals(nameArray[j]) || phoneArray[i].equals(phoneArray[j]))) {
                                    foundError = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (foundError) {
                        Toast.makeText(EqualBreakDown.this,
                                "Duplicate/ Empty at phone/ name column", Toast.LENGTH_SHORT).show();
                    } else {
                        saveFriend(nameArray, phoneArray);
                    }
                }
            });
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!checkEmpty()){
                        // Navigate to equal breakdown page
                        Intent intent = new Intent(EqualBreakDown.this, EqualBreakDownPage.class);

                        intent.putExtra("PeopleAmount", peopleAmount);
                        intent.putExtra("ExpenseName", expenseName);
                        intent.putExtra("Currency", currency);
                        intent.putExtra("TotalPayment", totalPayment);
                        intent.putExtra("NameArray", nameArray);
                        intent.putExtra("PhoneArray", phoneArray);
                        intent.putExtra("PaidArray", paidArray);
                        startActivity(intent);
                    } else {
                        Toast.makeText(EqualBreakDown.this,
                                "Please ensure that you had enter all the people name " +
                                        "& tick the paid/unpaid button", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }
    }
    private void handleTextChangesName(EditText personName, final int index, String[] nameArray) {
        personName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString();
                nameArray[index] = name;
            }
        });
    }
    private void handleTextChangesPhone(EditText personPhone, final int index, String[] phoneArray) {
        personPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Retrieve the percentage value entered by the user
                String phone = editable.toString();

                // Store the percentage value in the percentArray at the appropriate index
                phoneArray[index] = "6"+phone;
            }

            // Other TextWatcher methods here...
        });
    }

    //save Friends function
    private void saveFriend(String[] nameArray, String[] phoneArray) {
        // Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Friends")
                .setMessage("Do you want to save the friends?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mySQLiteAdapter.openToWrite();
                        for (int i = 0; i < nameArray.length; i++) {
                            mySQLiteAdapter.insertFriend(nameArray[i], phoneArray[i]);
                        }
                        Toast.makeText(EqualBreakDown.this,
                                "Successfully add the friends into database", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Code to be executed when "Cancel" button is clicked
                        // (Optional: You can leave this empty if you don't need any action.)
                    }
                });

        // Show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //function to check empty column
    private boolean checkEmpty() {
        for (int i = 0; i < peopleAmount; i++) {
            if (nameArray[i] == null ||paidArray[i] == null||nameArray[i].equals("")
                    || nameArray[i].isEmpty() ||paidArray[i].equals("")||paidArray[i].isEmpty()) {
                return true;
            }
        }
        return false;
    }
}