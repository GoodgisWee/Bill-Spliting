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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CombinationBreakDown extends AppCompatActivity {

    private String expenseName, currency;
    private String[] nameArray, phoneArray, paidArray;
    private int[]percentArray;
    private double[]amountArray;
    private double totalPayment;
    private int peopleAmount;
    private LinearLayout linearLayout;
    private SQLiteAdapter mySQLiteAdapter;
    private RadioGroup radioGroupOptions;
    private RadioButton radioButtonPercentage;
    private RadioButton radioButtonAmount;
    private TextView number;
    private EditText personName,personPhoneNo;
    private Button saveFriendBtn, submitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination_break_down);

        //get all View
        linearLayout = findViewById(R.id.linearLayout);
        saveFriendBtn = findViewById(R.id.eb_savefriend);
        submitBtn = findViewById(R.id.eb_share);

        //get all value
        Intent intent = getIntent();
        expenseName = intent.getStringExtra("ExpenseName");
        currency = intent.getStringExtra("Currency");
        totalPayment = intent.getDoubleExtra("TotalPayment", 0);
        peopleAmount = intent.getIntExtra("PeopleAmount", 0);
        nameArray = new String[peopleAmount];
        phoneArray = new String[peopleAmount];
        percentArray = new int[peopleAmount];
        amountArray = new double[peopleAmount];
        paidArray = new String[peopleAmount];

        //initialize db connection
        mySQLiteAdapter = new SQLiteAdapter(this);

        for(int i = 0; i < nameArray.length; i++) {
            double total=0;

            //layout to fill in friends detail
            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setPadding(16,0,16,0);

            //friends count
            number = new TextView(this);
            number.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            number.setGravity(Gravity.CENTER_VERTICAL);
            number.setText(String.valueOf(i+1) + ".");
            number.setTextSize(28.0f);

            // friends name
            personName = new EditText(this);
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
            personPhoneNo = new EditText(this);
            personPhoneNo.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            personPhoneNo.setHint("PhoneNo(optional)");
            personPhoneNo.setGravity(Gravity.CENTER_VERTICAL);
            personPhoneNo.setInputType(InputType.TYPE_CLASS_NUMBER);

            handleTextChangesPhone(personPhoneNo, i, phoneArray);

            //add all the layout into the page
            horizontalLayout.addView(number);
            horizontalLayout.addView(personName);
            horizontalLayout.addView(personPhoneNo);

            LinearLayout horizontalLayout2 = new LinearLayout(this);
            horizontalLayout2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            horizontalLayout2.setOrientation(LinearLayout.HORIZONTAL);

            //initialize the radio group
            RadioGroup radioGroupOptions = new RadioGroup(this);
            radioGroupOptions.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            radioGroupOptions.setGravity(Gravity.LEFT);

            // Create the "Percentage" radio button
            RadioButton radioButtonPercentage = new RadioButton(this);
            radioButtonPercentage.setText("Percentage");
            radioGroupOptions.addView(radioButtonPercentage);

            // Create the "Amount" radio button
            RadioButton radioButtonAmount = new RadioButton(this);
            radioButtonAmount.setText("Amount");
            radioGroupOptions.addView(radioButtonAmount);

            //Percentage input
            EditText percentageInput = new EditText(this);
            percentageInput.setLayoutParams(new LinearLayout.LayoutParams(
                    550,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            percentageInput.setHint("Percentage");
            percentageInput.setGravity(Gravity.CENTER_VERTICAL);
            percentageInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); // Change input type to accept double
            handleTextChangesPercent(percentageInput, i, percentArray);

            //Amount input
            EditText amountInput = new EditText(this);
            amountInput.setLayoutParams(new LinearLayout.LayoutParams(
                    550,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            amountInput.setHint("Amount");
            amountInput.setGravity(Gravity.CENTER_VERTICAL);
            amountInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); // Change input type to accept double
            handleTextChangesAmount(amountInput, i, amountArray);

            //initialize the radio group
            RadioGroup radioGroupPaid = new RadioGroup(this);
            radioGroupPaid.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            radioGroupPaid.setGravity(Gravity.LEFT);
            radioGroupPaid.setPadding(250,0,0,0);

            // Create the "Paid" radio button
            RadioButton radioButtonPaid = new RadioButton(this);
            radioButtonPaid.setText("Paid");
            radioGroupPaid.addView(radioButtonPaid);

            // Create the "Unpaid" radio button
            RadioButton radioButtonUnpaid = new RadioButton(this);
            radioButtonUnpaid.setText("Unpaid");
            radioGroupPaid.addView(radioButtonUnpaid);

            // Set up a listener for the RadioGroupPaid
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

            //add all the radio group in second row
            horizontalLayout2.addView(radioGroupOptions);
            horizontalLayout2.addView(radioGroupPaid);

            LinearLayout horizontalLayout3 = new LinearLayout(this);
            horizontalLayout3.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            horizontalLayout3.setOrientation(LinearLayout.HORIZONTAL);

            // Set up a listener for the RadioGroup
            radioGroupOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == radioButtonPercentage.getId()) {
                        horizontalLayout3.removeAllViews();
                        horizontalLayout3.addView(percentageInput);
                    } else if (checkedId == radioButtonAmount.getId()) {
                        horizontalLayout3.removeAllViews();
                        horizontalLayout3.addView(amountInput);
                    }
                }
            });

            //add all the layout into the page
            linearLayout.addView(horizontalLayout);
            linearLayout.addView(horizontalLayout2);
            linearLayout.addView(horizontalLayout3);

            saveFriendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean foundError = false;
                    for (int i = 0; i < nameArray.length; i++) {
                        String checkName = nameArray[i];
                        String phoneNo = phoneArray[i];
                        if(checkName == null || checkName.equals("") ||phoneNo == null || phoneNo.equals("")){
                            foundError = true;
                            break;
                        } else{
                            for (int j = 0; j < nameArray.length; j++) {
                                if (i != j && (checkName.equals(nameArray[j]) || phoneNo.equals(phoneArray[j]))) {
                                    foundError = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (foundError) {
                        Toast.makeText(CombinationBreakDown.this,
                                "Error in input column(duplicate/empty)", Toast.LENGTH_SHORT).show();
                    } else {
                        saveFriend(nameArray, phoneArray);
                    }
                }
            });

            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!checkEmpty()){
                        // Navigate to equal breakdown page
                        Intent intent = new Intent(CombinationBreakDown.this, CombinationBreakDownResult.class);

                        intent.putExtra("PeopleAmount", peopleAmount);
                        intent.putExtra("ExpenseName", expenseName);
                        intent.putExtra("Currency", currency);
                        intent.putExtra("TotalPayment", totalPayment);
                        intent.putExtra("NameArray", nameArray);
                        intent.putExtra("PhoneArray", phoneArray);
                        intent.putExtra("PaidArray", paidArray);

                        intent.putExtra("PercentArray", percentArray);
                        intent.putExtra("AmountArray", amountArray);

                        double total =0;
                        double last=0;
                        for(int i=0;i<peopleAmount;i++){
                            if(i!=peopleAmount-1){
                                if(percentArray[i]!=0){
                                    total+=totalPayment*percentArray[i]/100;
                                } else if(amountArray[i]!=0){
                                    total+=amountArray[i];
                                }
                            } else{
                                if(percentArray[i]!=0){
                                    last+=totalPayment*percentArray[i]/100;
                                } else if(amountArray[i]!=0){
                                    last+=amountArray[i];
                                }
                            }

                        }
                        if(totalPayment!=(total+last)){
                            Toast.makeText(CombinationBreakDown.this,
                                    "Total Amount Error!  Original: "+String.format("%.2f",totalPayment)+
                                            "  User input: "+String.format("%.2f",total+last), Toast.LENGTH_SHORT).show();
                            Toast.makeText(CombinationBreakDown.this,
                                    "Consider change the last person amount into "+ String.format("%.2f",totalPayment-total)
                                    , Toast.LENGTH_LONG).show();
                        } else{
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(CombinationBreakDown.this,
                                "Please ensure that you had enter all the column", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    private void handleTextChangesName(EditText personName, final int index, String[] nameArray) {
        personName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString();

                nameArray[index] = name;
                //check is there same name occur before store inside array
            }

            // Other TextWatcher methods here...
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

    private void saveFriend(String[] nameArray, String[] phoneArray) {
        // Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Friends")
                .setMessage("Do you want to save the friends?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       /* mySQLiteAdapter.openToRead();  pending to tackle duplicate friends in db
                        String friendAll = mySQLiteAdapter.queueAllFriend_NameOnly();
                        String[] friendArray = friendAll.split(";");
                        for(int i=0;i<friendArray.length-1;i++){
                            for(String value:nameArray){
                                if(value.equals(friendArray[i])){

                                }
                            }
                        }*/
                        mySQLiteAdapter.openToWrite();
                        for (int i = 0; i < nameArray.length; i++) {
                            mySQLiteAdapter.insertFriend(nameArray[i], phoneArray[i]);
                        }
                        Toast.makeText(CombinationBreakDown.this,
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
    private void handleTextChangesPercent(EditText personPercentEditText, final int index, int[] percentArray) {
        personPercentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

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

            // Other TextWatcher methods here...
        });
    }

    private void handleTextChangesAmount(EditText personAmountEditText, final int index, double[] amountArray) {
        personAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

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

            // Other TextWatcher methods here...
        });
    }
    //function to check empty column
    private boolean checkEmpty() {
        for (int i = 0; i < peopleAmount; i++) {
            if (nameArray[i] == null || paidArray[i] == null){

            }else if(nameArray[i].equals("") ||paidArray[i].equals("") ||
                    (amountArray[i]==0&&percentArray[i]==0)){
                return true;
            }
        }
        return false;
    }
}