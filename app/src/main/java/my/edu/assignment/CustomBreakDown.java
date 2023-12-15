package my.edu.assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomBreakDown extends AppCompatActivity {


    private TextView cbTitle;
    private RadioButton radioPercent;
    private RadioButton radioRatio;
    private RadioButton radioAmount;
    private RadioGroup radioGroup;
    private Button nextBtn, saveFriendBtn;
    private ScrollView scrollView;
    private LinearLayout linearLayoutScroll;
    private LinearLayout linearLayoutScrollHorizontal;
    private String selectedOption = "";
    private int personCount;
    private String expenseName, currency;
    private String[] nameArray, paidArray, phoneArray;
    private double totalPayment;
    private int peopleAmount;
    private int[] percentArray, ratioArray;
    private double[] amountArray;
    private SQLiteAdapter mySQLiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_break_down);

        // Initialize views by their IDs
        cbTitle = findViewById(R.id.cb_title);
        radioPercent = findViewById(R.id.cb_radiopercent);
        radioRatio = findViewById(R.id.cb_radioratio);
        radioAmount = findViewById(R.id.cb_radioamount);
        radioGroup = findViewById(R.id.cb_radiogroup);
        nextBtn = findViewById(R.id.eb_share);
        saveFriendBtn = findViewById(R.id.eb_savefriend);
        scrollView = findViewById(R.id.cb_scrollView);
        linearLayoutScroll = findViewById(R.id.cb_llscroll);

        //get all value
        Intent intent = getIntent();
        expenseName = intent.getStringExtra("ExpenseName");
        currency = intent.getStringExtra("Currency");
        totalPayment = intent.getDoubleExtra("TotalPayment", 0);
        peopleAmount = intent.getIntExtra("PeopleAmount", 0);

        //initialize db connection
        mySQLiteAdapter = new SQLiteAdapter(this);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which radio button is selected
                if (checkedId == radioPercent.getId()) {
                    displayOption(1);
                } else if (checkedId == radioRatio.getId()) {
                    displayOption(2);
                } else if (checkedId == radioAmount.getId()) {
                    displayOption(3);
                }
            }
        });
    }

    private void handleTextChangesName(EditText personNameEditText, final int index, String[] nameArray) {
        personNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Retrieve the person name entered by the user
                String name = editable.toString();

                // Store the person name in the nameArray at the appropriate index
                nameArray[index] = name;
            }

            // Other TextWatcher methods here...
        });
    }

    private void handleTextChangesPercent(EditText personPercentEditText, final int index, int[] percentArray) {
        personPercentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                // Retrieve the percentage value entered by the user
                String percentValue = editable.toString();
                // Convert the percentage value to an int
                int percent = 0;
                try {
                    percent = Integer.parseInt(percentValue);
                } catch (NumberFormatException e) {
                    // Handle any invalid input here if needed
                }
                // Store the percentage value in the percentArray at the appropriate index
                percentArray[index] = percent;
            }
        });

    }

    private void displayOption(int choice) {
        linearLayoutScroll.removeAllViews();

        percentArray = new int[peopleAmount];
        amountArray = new double[peopleAmount];
        ratioArray = new int[peopleAmount];
        nameArray = new String[peopleAmount];
        paidArray = new String[peopleAmount];
        phoneArray = new String[peopleAmount];
        for (int i = 0; i < peopleAmount; i++) {
            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);

            //Person Count
            TextView number = new TextView(this);
            number.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            number.setGravity(Gravity.CENTER_VERTICAL);
            number.setText(String.valueOf(i + 1) + ".");
            number.setTextSize(28.0f);

            // Create a new EditText for person name
            EditText personName = new EditText(this);
            personName.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            personName.setHint("Name");
            personName.setGravity(Gravity.CENTER_VERTICAL);
            personName.setInputType(InputType.TYPE_CLASS_TEXT); // Change input type to accept text

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


            // Create a new EditText for amount
            EditText spiltType = new EditText(this);
            spiltType.setLayoutParams(new LinearLayout.LayoutParams(
                    550,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            if (choice == 1) {
                spiltType.setHint("Percent");
                spiltType.setGravity(Gravity.CENTER_VERTICAL);
                spiltType.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); // Change input type to accept double

                handleTextChangesPercent(spiltType, i, percentArray);
            } else if (choice == 2) {
                spiltType.setHint("Ratio");
                spiltType.setGravity(Gravity.CENTER_VERTICAL);
                spiltType.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); // Change input type to accept double

                handleTextChangesRatio(spiltType, i, ratioArray);
            } else if (choice == 3) {
                spiltType.setHint("Amount");
                spiltType.setGravity(Gravity.CENTER_VERTICAL);
                spiltType.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); // Change input type to accept double

                handleTextChangesAmount(spiltType, i, amountArray);
            }

            // Initialize the radio group
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
                        paidArray[finalI] = "Paid";
                    } else if (checkedId == radioButtonUnpaid.getId()) {
                        paidArray[finalI] = "Unpaid";
                    }
                }
            });

            //Empty TextView for better alignment
            TextView emptyTextVIew = new TextView(this);
            emptyTextVIew.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            emptyTextVIew.setGravity(Gravity.CENTER_VERTICAL);
            emptyTextVIew.setText("  ");
            emptyTextVIew.setTextSize(28.0f);

            LinearLayout horizontalLayout2 = new LinearLayout(this);
            horizontalLayout2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            horizontalLayout2.setOrientation(LinearLayout.HORIZONTAL);

            //add all the view into the layout
            horizontalLayout.addView(number);
            horizontalLayout.addView(personName);
            horizontalLayout.addView(personPhoneNo);
            horizontalLayout2.addView(emptyTextVIew);
            horizontalLayout2.addView(spiltType);
            horizontalLayout2.addView(radioGroupPaid);
            linearLayoutScroll.addView(horizontalLayout);
            linearLayoutScroll.addView(horizontalLayout2);
        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkEmpty(choice)) {
                    //set all intent
                    Intent intent = new Intent(CustomBreakDown.this, CustomBreakDownResult.class);
                    intent.putExtra("PeopleAmount", peopleAmount);
                    intent.putExtra("ExpenseName", expenseName);
                    intent.putExtra("Currency", currency);
                    intent.putExtra("TotalPayment", totalPayment);
                    intent.putExtra("PaidArray", paidArray);

                    //error validation
                    double total = 0;
                    if (choice == 1) {
                        for (int i = 0; i < percentArray.length; i++) {
                            total += percentArray[i];
                        }

                        if (total != 100) {
                            Toast.makeText(CustomBreakDown.this,
                                    "Percentage is not 100%, please ensure you key in the correct percentage",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            intent.putExtra("PercentArray", percentArray);
                            intent.putExtra("NameArray", nameArray);
                            startActivity(intent);
                        }
                    } else if (choice == 2) {
                        intent.putExtra("RatioArray", ratioArray);
                        intent.putExtra("NameArray", nameArray);
                        startActivity(intent);
                    } else {
                        for (int i = 0; i < amountArray.length-1; i++) {
                            total += amountArray[i];
                        }

                        if ((total+amountArray[amountArray.length - 1]) != totalPayment) {
                            Toast.makeText(CustomBreakDown.this,
                                    "Total Amount Error!  Original: "+String.format("%.2f",totalPayment)+
                                            "  User input: "+String.format("%.2f",total+amountArray[amountArray.length - 1]),
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(CustomBreakDown.this,
                                    "Consider change the last person amount into "+ String.format("%.2f",totalPayment-total)
                                    , Toast.LENGTH_LONG).show();
                        } else {
                            intent.putExtra("AmountArray", amountArray);
                            intent.putExtra("NameArray", nameArray);
                            startActivity(intent);
                        }
                    }
                } else {
                    Toast.makeText(CustomBreakDown.this,
                            "Please ensure that you had enter all the people name " +
                                    "& breakdown of each people payment", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                    Toast.makeText(CustomBreakDown.this,
                            "Duplicate/ Empty at phone/ name column", Toast.LENGTH_SHORT).show();
                } else {
                    saveFriend(nameArray, phoneArray);
                }
            }
        });
    }

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
                        Toast.makeText(CustomBreakDown.this,
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

    //function to get amount value for each person
    private void handleTextChangesAmount(EditText personAmountEditText, final int index, double[] amountArray) {
        personAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {   }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {   }
            @Override
            public void afterTextChanged(Editable editable) {
                // Retrieve the amount value entered by the user
                String amountValue = editable.toString();
                // Convert the amount value to a double
                double amount = 0;
                try {
                    amount = Double.parseDouble(amountValue);
                } catch (NumberFormatException e) {
                    // Handle any invalid input here if needed
                }

                // Store the amount value in the amountArray at the appropriate index
                amountArray[index] = amount;
            }
        });
    }

    private void handleTextChangesRatio(EditText personRatioEditText, final int index, int[] ratioArray) {
        personRatioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                // Retrieve the ratio value entered by the user
                String ratioValue = editable.toString();
                // Convert the ratio value to an int
                int ratio = 0;
                try {
                    ratio = Integer.parseInt(ratioValue);
                } catch (NumberFormatException e) {
                    // Handle any invalid input here if needed
                }
                // Store the ratio value in the ratioArray at the appropriate index
                ratioArray[index] = ratio;
            }
            });
    }

    private void handleTextChangesPhone(EditText personPhone, final int index, String[] phoneArray) {
        personPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                // Retrieve the percentage value entered by the user
                String phone = editable.toString();
                // Store the percentage value in the percentArray at the appropriate index
                phoneArray[index] = "6" + phone;
            }
        });
    }

    //function to check empty column
    private boolean checkEmpty(int choice) {
        for (int i = 0; i < peopleAmount; i++) {
            if (nameArray[i] == null || nameArray[i].isEmpty() ||
                    paidArray[i] == null||paidArray[i].isEmpty()) {
                return true;
            } else if(nameArray[i].equals("") ||paidArray[i].equals("")){
                return true;
            }
            if (choice == 1) {
                if (percentArray[i] == 0) {
                    return true;
                }
            } else if (choice == 2) {
                if (ratioArray[i] == 0) {
                    return true;
                }
            } else {
                if (amountArray[i] == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}