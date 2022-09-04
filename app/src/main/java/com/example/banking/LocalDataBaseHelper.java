package com.example.banking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.Random;

public class LocalDataBaseHelper  {
    public SQLiteDatabase db ;
    public Context context;

    public String CREATE_USER_TABLE="CREATE TABLE IF NOT EXISTS USER_TABLE("+
        "_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
        "NAME VARCHAR,"+
        "LAST_NAME VARCHAR,"+
        "AGE VARCHAR,"+
        "GENDER VARCHAR,"+
        "BALANCE VARCHAR DEFAULT 0,"+
        "CARDNUMBER VARCHAR);";
    public LocalDataBaseHelper(Context c){
        this.context = c ;
        this.db = context.openOrCreateDatabase("mydb", Context.MODE_PRIVATE, null);
        this.db.execSQL(CREATE_USER_TABLE);
        this.db.close();
    }

    public String insertNewUser(String name, String lastName, String age, String gender){
        this.db = context.openOrCreateDatabase("mydb", Context.MODE_PRIVATE, null);
        String cardNumber = generateCardnumber();

        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("LAST_NAME", lastName);
        values.put("AGE", age);
        values.put("GENDER", gender);
        values.put("CARDNUMBER", cardNumber);

        long r = this.db.insert("USER_TABLE", "_ID", values);

        this.db.close();

        if(r!=-1){
            Toast.makeText(context,"new user inserted!",Toast.LENGTH_SHORT).show();
            return cardNumber;
        }else{
            Toast.makeText(context,"Error while inserting new user!",Toast.LENGTH_SHORT).show();
            return "";
        }





    }
    public String generateCardnumber(){
        String cardNumber;
        Random rnd = new Random();
        int number = rnd.nextInt( 999999);
        cardNumber = String.format("%06",number);

        return cardNumber;
    }

    public String login(String cardNumber){

        this.db = context.openOrCreateDatabase("mydb",Context.MODE_PRIVATE,null);

        Cursor cursor = this.db.rawQuery("SELECT * FROM USER_TABLE WHERE CARDNUMBER = "+cardNumber,null);


        if(cursor.getCount()==0){
            Toast.makeText(context,"Wrong cardnumber!",Toast.LENGTH_SHORT).show();
            return "";
        }else{
            this.db.close();
            return cardNumber;
        }


    }

    public String getUserBalance(String cardNumber){
        String balance;
        this.db = context.openOrCreateDatabase("mydb",Context.MODE_PRIVATE,null);

        Cursor cursor = this.db.rawQuery("SELECT * FROM USER_TABLE WHERE CARDNUMBER = "+cardNumber,null);
        cursor.moveToFirst();
        balance = cursor.getString(cursor.getColumnIndex("BALANCE"));

        this.db.close();
        return balance;
    }

    public boolean topUp(String cardNumber, String amount){
        boolean done = false;
        float currentAmount = Float.parseFloat(this.getUserBalance(cardNumber));
        this.db = context.openOrCreateDatabase("mydb",Context.MODE_PRIVATE,null);

        float newAmount = currentAmount + Float.parseFloat(amount);

        ContentValues values = new ContentValues();
        values.put("BALANCE",String.valueOf(newAmount));
        int r = this.db.update("USER_TABLE",values,"CARDNUMBER = ?",new String[]{cardNumber});
        if(r>0){
            done = true;
        }else{
            done = false;
            Toast.makeText(context,"Something went wrong.", Toast.LENGTH_SHORT).show();
        }
        this.db.close();
        return done;
    }
    public boolean updateUserBalance(String cardNumber, String amount){
        boolean done = false;
        this.db = context.openOrCreateDatabase("mydb",Context.MODE_PRIVATE,null);
        ContentValues values = new ContentValues();
        values.put("BALANCE",amount);
        int r = this.db.update("USER_TABLE",values,"CARDNUMBER = ?",new String[]{cardNumber});
        if(r>0){
            done = true;
        }else{
            done = false;
            Toast.makeText(context,"Something went wrong.",Toast.LENGTH_SHORT).show();
        }
        this.db.close();
        return done;
    }

    public void transferMoney(String cardNumber, String targetCardNumber, String amount){
        this.db = context.openOrCreateDatabase("mydb",Context.MODE_PRIVATE,null);
        float sourceBalance = Float.parseFloat(this.getUserBalance(cardNumber));
        float targetBalance = Float.parseFloat(this.getUserBalance(targetCardNumber));
        float transferAmount = Float.parseFloat(amount);

        if(sourceBalance>=transferAmount){
            sourceBalance = sourceBalance - transferAmount;
            targetBalance = targetBalance + transferAmount;
            if(updateUserBalance(cardNumber,String.valueOf(sourceBalance))){
                if(updateUserBalance(targetCardNumber,String.valueOf(targetBalance))){
                    Toast.makeText(context,"Transfer is done!",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context,"Error!",Toast.LENGTH_SHORT).show();

                }
            }else{
                Toast.makeText(context,"Error!",Toast.LENGTH_SHORT).show();

            }

        }
        else{
            Toast.makeText(context,"Not enough balance!",Toast.LENGTH_SHORT).show();
        }
        this.db.close();
    }
}
