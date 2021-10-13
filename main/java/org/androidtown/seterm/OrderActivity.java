package org.androidtown.seterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.text.NumberFormat;

/**
 * Class that allows user to order
 * @author SeProject
 * @date 2020-06-21
 * @version 0.0.1
 */
public class OrderActivity extends AppCompatActivity implements View.OnClickListener {
    Button order;
    TextView product1;
    TextView product2;
    TextView product3;
    TextView product4;
    int quantity, quantity2, quantity3, quantity4;
    final int price1 = 2000;
    final int price2 = 3000;
    final int price3 = 3500;
    final int price4 = 1500;

    /**
     * Method that occurs when creating a screen<br/>
     * defines 'order' button and textviews that show each beverage's price.<br/>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        order = (Button) findViewById(R.id.order);
        product1 = (TextView) findViewById(R.id.americano);
        product2 = (TextView) findViewById(R.id.latte);
        product3 = (TextView) findViewById(R.id.Cappuccino);
        product4 = (TextView) findViewById(R.id.Camomile);
        order.setOnClickListener(this);
    }

    /**
     * method that occurs when click screen<br/>
     * if user clicks 'order', it goes next activity including order information .<br/>
     * arr1,arr2,arr3,arr4 each means americano, cafelatte, cappuccino, camomile's order information.<br/>
     * order information shows beverage's name, number of beverages, status of takeout, price.<br/>
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order:
                if(getQuantity1().equals("0") && getQuantity2().equals("0") && getQuantity3().equals("0") && getQuantity4().equals("0"))
                {
                    Toast.makeText(OrderActivity.this, "음료를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(this, PayActivity.class);
                    String[] arr1 = {"아메리카노", getQuantity1() + getCheck1(), getPrice1()};
                    String[] arr2 = {"카페라떼", getQuantity2() + getCheck2(), getPrice2()};
                    String[] arr3 = {"카푸치노", getQuantity3() + getCheck3(), getPrice3()};
                    String[] arr4 = {"카모마일", getQuantity4() + getCheck4(), getPrice4()};
                    intent.putExtra("americano", arr1);
                    intent.putExtra("latte", arr2);
                    intent.putExtra("capuchino", arr3);
                    intent.putExtra("camomile", arr4);
                    startActivity(intent);
                    finish();
                    break;
                }
        }
    }

    /**
     * method that increase americano's quantity
     * @param view
     */
    public void increment(View view) {
        quantity = quantity + 1;
        display(quantity);
    }

    /**
     * method that decrease americano's quantity (when the quantity is more than 0)
     * @param view
     */
    public void decrement(View view) {
        if (quantity == 0) {
            quantity = 0;
        } else {
            quantity = quantity - 1;
            display(quantity);
        }
    }

    /**
     * method that shows changed quantity and price when increasing or decreasing quantity of americano
     * * @param number 아메리카노의 개수
     */
    private void display(int number) {
        TextView quantityText = (TextView) findViewById(R.id.quantity_number);
        quantityText.setText("" + number);
        TextView priceText = (TextView) findViewById(R.id.price_number);
        priceText.setText(NumberFormat.getCurrencyInstance().format(quantity * price1));
    }

    /**
     * method that increase cafelatte's quantity
     * @param view
     */
    public void SecIncrement(View view) {
        quantity2 = quantity2 + 1;
        SecDisplay(quantity2);
    }

    /**
     * method that decrease cafelatte's quantity (when the quantity is more than 0)
     * @param view
     */
    public void SecDecrement(View view) {
        if (quantity2 == 0) {
            quantity2 = 0;
        } else {
            quantity2 = quantity2 - 1;
            SecDisplay(quantity2);
        }
    }

    /**
     * method that shows changed quantity and price when increasing or decreasing quantity of cafelatte
     * @param number quantity of cafelatte
     */
    private void SecDisplay(int number) {

        TextView SecQuantityText = (TextView) findViewById(R.id.quantity_number2);
        SecQuantityText.setText("" + number);
        TextView SecPriceText = (TextView) findViewById(R.id.price_number2);
        SecPriceText.setText(NumberFormat.getCurrencyInstance().format(quantity2 * price2));
    }


    /**
     * method that increase cappuccino's quantity
     * @param view
     */
    public void Increment3(View view) {
        quantity3 = quantity3 + 1;
        Display3(quantity3);
    }

    /**
     * method that decrease cappuccino's quantity (when the quantity is more than 0)
     * @param view
     */
    public void Decrement3(View view) {
        if (quantity3 == 0) {
            quantity3 = 0;
        } else {
            quantity3 = quantity3 - 1;
            Display3(quantity3);
        }
    }

    /**
     * method that shows changed quantity and price when increasing or decreasing quantity of cappuccino
     * @param number quantity of cappuccino
     */
    private void Display3(int number) {

        TextView ThQuantityText = (TextView) findViewById(R.id.quantity_number3);
        ThQuantityText.setText("" + number);
        TextView ThPriceText = (TextView) findViewById(R.id.price_number3);
        ThPriceText.setText(NumberFormat.getCurrencyInstance().format(quantity3 * price3));

    }

    /**
     * method that increase camomile's quantity
     * @param view
     */
    public void Increment4(View view) {
        quantity4 = quantity4 + 1;
        Display4(quantity4);
    }

    /**
     * method that decrease camomile's quantity (when the quantity is more than 0)
     * @param view
     */
    public void Decrement4(View view) {
        if (quantity4 == 0) {
            quantity4 = 0;
        } else {
            quantity4 = quantity4 - 1;
            Display4(quantity4);
        }
    }

    /**
     * method that shows changed quantity and price when increasing or decreasing quantity of camomile
     * @param number quantity of camomile
     */
    private void Display4(int number) {

        TextView FoQuantityText = (TextView) findViewById(R.id.quantity_number4);
        FoQuantityText.setText("" + number);
        TextView FoPriceText = (TextView) findViewById(R.id.price_number4);
        FoPriceText.setText(NumberFormat.getCurrencyInstance().format(quantity4 * price4));


    }


    /**
     * method that returns total price of americano
     * @return quantity * price of a beverage
     */
    public String getPrice1() {
        return String.valueOf(quantity * price1);
    }
    /**
     * method that returns total price of cafelatte
     * @return quantity * price of a beverage
     */
    public String getPrice2() {
        return String.valueOf(quantity2 * price2);
    }
    /**
     * method that returns total price of cappucino
     * @return quantity * price of a beverage
     */
    public String getPrice3() {
        return String.valueOf(quantity3 * price3);
    }
    /**
     * method that returns total price of camomile
     * @return quantity * price of a beverage
     */
    public String getPrice4() {
        return String.valueOf(quantity4 * price4);
    }
    /**
     * method that returns quantity of americano
     * @return quantity of americano
     */
    public String getQuantity1()
    {
        TextView qu1 = (TextView) findViewById(R.id.quantity_number);
        return qu1.getText().toString();
    }
    /**
     * method that returns quantity of cafelatte
     * @return quantity of cafelatte
     */
    public String getQuantity2()
    {
        TextView qu2 = (TextView) findViewById(R.id.quantity_number2);
        return qu2.getText().toString();
    }

    /**
     * method that returns quantity of cappuccino
     * @return quantity of cappuccino
     */
    public String getQuantity3()
    {
        TextView qu3 = (TextView) findViewById(R.id.quantity_number3);
        return qu3.getText().toString();
    }

    /**
     * method that returns quantity of camomile
     * @return quantity of camomile
     */
    public String getQuantity4()
    {
        TextView qu4 = (TextView) findViewById(R.id.quantity_number4);
        return qu4.getText().toString();
    }

    /**
     * method that returns status of takeout americano
     * @return if user takeout the beverage : "takeOut"
     * @return if user doesn't takeout the beverage : ""
     */
    public String getCheck1() {
        CheckBox ck1 = (CheckBox) findViewById(R.id.check1);
        if(ck1.isChecked())
            return "TakeOut";
        else
            return "";
    }

    /**
     * method that returns status of takeout cafelatte
     * @return if user takeout the beverage : "takeOut"
     * @return if user doesn't takeout the beverage : ""
     */
    public String getCheck2() {
        CheckBox ck2 = (CheckBox) findViewById(R.id.check2);
        if(ck2.isChecked())
            return "TakeOut";
        else
            return "";
    }

    /**
     * method that returns status of takeout cappuccino
     * @return if user takeout the beverage : "takeOut"
     * @return if user doesn't takeout the beverage : ""
     */
    public String getCheck3() {
        CheckBox ck3 = (CheckBox) findViewById(R.id.check3);
        if(ck3.isChecked())
            return "TakeOut";
        else
            return "";
    }

    /**
     * method that returns status of takeout camomile
     * @return if user takeout the beverage : "takeOut"
     * @return if user doesn't takeout the beverage : ""
     */
    public String getCheck4() {
        CheckBox ck4 = (CheckBox) findViewById(R.id.check4);
        if(ck4.isChecked())
            return "TakeOut";
        else
            return "";
    }


    /**
     * method that tests 'increment' method works
     * increment method is hard to test because of parameter 'View v'
     */
    public void incrementTest() {
        quantity = quantity + 1;
    }

    /**
     * method that tests 'decrement' method works
     * decrement method is hard to test because of parameter 'View v'
     */
    public void decrementTest(){
        if (quantity == 0) {
            quantity = 0;
        } else {
            quantity = quantity - 1;
        }
    }

    /**
     * method that tests 'getQuantity' method works
     * getQuantity method is hard to test because of parameter 'View v'
     */
    public String getQuantityTest()
    {
        return String.valueOf(quantity);
}

    /**
     * method that tests 'getPrice' method works
     */
    public String getPriceTest() {
        return String.valueOf(quantity * price1);
    }

    /**
     * method that tests 'onClick' method works
     * onClick method is hard to test because of parameter 'View v'
     */
    public String[] stringTest(){
        String[] arr1 = {"아메리카노", getQuantityTest(), getPriceTest()};
        return arr1;
    }


}
