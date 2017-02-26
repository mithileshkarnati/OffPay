package com.example.srikarteratipally.offpay;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

/**
 * Created by Srikar Teratipally on 09-02-2017.
 */

public class database
{

    int lol;
    static public MobileServiceClient fakeClient;
    static public MobileServiceTable<DatabaseAzure> fakeTable;
    DatabaseAzure FromCustomer;
    DatabaseAzure ToCustomer;
    DatabaseAzure BalanceCustomer;
    public void database(int p){}

    public  void register(final String from, final String pin) {
        if (fakeClient == null) {
            return;
        }
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute()
            {

            }@Override
            protected Void doInBackground(Void... params) {
                try {
                    List<DatabaseAzure> results = fakeTable.execute().get();
                    lol = 1;
                    for (DatabaseAzure item : results) {
                        if (item.mobileNumber.equals(from)) {
                            lol = 0;
                            return null;
                        }
                    }

                } catch (Exception exception) {

                }
                return null;
            }@Override
            protected void onPostExecute(Void res)
            {

                // Create a new item
                if (lol == 1) {
                    final DatabaseAzure item = new DatabaseAzure();

                    item.mobileNumber = from;
                    item.password = pin;
                    fakeTable.insert(item, new TableOperationCallback<DatabaseAzure>() {
                        @Override
                        public void onCompleted(DatabaseAzure entity, Exception exception, ServiceFilterResponse response) {

                        }
                    });
                    sendSMS(from,"#rayudu\nYou are registered with the PIN " + pin + "\nbalance = Rs" + item.balance);
                } else sendSMS(from,"Dear Customer, You are already registered");


            }

        };
        runAsyncTask(task);

       }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }


    public void balance(final String mobilenumber)
    {
        if (fakeClient == null) {
        return;
        }
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute()
            {

            }@Override
            protected Void doInBackground(Void... params) {
                try {
                    List<DatabaseAzure> results = fakeTable.execute().get();
                    lol = 1;
                    for (DatabaseAzure item : results) {
                        if (item.mobileNumber.equals(mobilenumber)) {
                            lol = 0;
                            BalanceCustomer=item;
                            return null;
                        }
                    }

                } catch (Exception exception) {

                }
                return null;
            }@Override
            protected void onPostExecute(Void res)
            {


                // Create a new item
                if (lol == 1) {
                    sendSMS(mobilenumber,"Sorry, You are not registered");
                } else {
                    sendSMS(mobilenumber, "#rayudu\n" + "balance = Rs" + String.valueOf(BalanceCustomer.balance));
                }
            }

        };
        runAsyncTask(task);

    }


    public  void pay(final String from,final String receiver,final String amount,final String pin) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    List<DatabaseAzure> results = fakeTable.execute().get();
                    for (DatabaseAzure item : results) {
                        if (item.mobileNumber.equals(from)) FromCustomer = item;
                        else if (item.mobileNumber.equals(receiver)) ToCustomer = item;
                    }

                } catch (Exception exception) {

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void res) {
                if(!FromCustomer.password.equals(pin)) {sendSMS(from,"Incorrect PIN");return;}

                FromCustomer.balance = FromCustomer.balance - Integer.parseInt(amount);
                if(FromCustomer.balance < 0) {FromCustomer.balance=FromCustomer.balance + Integer.parseInt(amount);sendSMS(from,"You do not have enough funds for this transactions\nbalance = Rs"+ String.valueOf(FromCustomer.balance));return;}
                ToCustomer.balance = ToCustomer.balance + Integer.parseInt(amount);

                AsyncTask<Void, Void, Void> task3 = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {

                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            fakeTable.update(FromCustomer).get();
                            fakeTable.update(ToCustomer).get();
                        } catch (Exception e) {
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void res) {


                        sendSMS(from, "#rayudu\nSuccessfully sent Rs." + amount + " to " + receiver +"\nbalance = Rs" + String.valueOf(FromCustomer.balance));
                        sendSMS(receiver, "#rayudu\nSuccessfully received Rs." + amount + " from " + from +"\nbalance = Rs" + String.valueOf(ToCustomer.balance));

                    }

                };
                runAsyncTask(task3);


            }
        };
        runAsyncTask(task);
    }

            public void sendSMS(String phoneNo, String msg) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                    //Toast.makeText(getApplicationContext(), "Message Sent",
                    //Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
//            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
//                    Toast.LENGTH_LONG).show();
//            ex.printStackTrace();
                }
            }
        }
