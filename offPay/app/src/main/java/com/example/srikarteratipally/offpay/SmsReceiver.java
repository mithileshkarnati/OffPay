package com.example.srikarteratipally.offpay;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.telephony.SmsManager;
        import android.telephony.SmsMessage;
        import android.util.Log;
        import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    private String TAG = SmsReceiver.class.getSimpleName();
    public SmsReceiver() {
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();
        Log.d("ss2","please");

        SmsMessage[] msgs = null;

        String from = "";
        String str = "";

        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received
            for (int i=0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                // Sender's phone number
                from = msgs[i].getOriginatingAddress();
                // Fetch the text message
                str += msgs[i].getMessageBody().toString();
                // Newline <img draggable="false" class="emoji" alt="🙂" src="https://s.w.org/images/core/emoji/72x72/1f642.png">
            }

            boolean parse = parser(from,str);
            // Display the entire SMS Message
            if(parse) {
                sendSMS(from,"Got the message but couldn't understand");
            }
            //else sendSMS(from, "perfect");
            Log.d(TAG, str);
        }
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

    boolean parser (String from, String given) {
        given.replace("\n", "");
        String words[] = given.split(" ");


        //Log.d("hfh1f",Integer.toString(words.length));


        if (!((words.length == 3) || (words.length == 5) || (words.length == 2))) {
            Log.d("hfhf3", "nasa");
            sendSMS(from, "lop1");
            return true;
        } else {
            //Log.d("hfhf",words[1]);
            if (!words[0].equals("#rayudu")) {
                sendSMS(from, "lop2");
                return true;
            }
            //else return false;
            database temp = new database();


            if(words.length==2) {
                if (!words[1].equals("balance")) {
                    sendSMS(from, "lop3");
                    return true;
                }
                temp.balance(from);
                return false;
            }

            else if (words[1].equals("register")) {
                Log.d("hff", "brr");
                if (words.length != 3) {
                    sendSMS(from, "lop3");
                    return true;
                }
                temp.register(from, words[2]);

                return false;
            } else if (words[1].equals("pay")) {
                if (words.length != 5) {
                    sendSMS(from, "lop4");
                    return true;
                }
                temp.pay(from, words[2], words[3], words[4]);
//            sendSMS(from,"You paid successfully to " + words[2]);
//            sendSMS(words[2], "You received amount of " + words[3] + " from " + from);
                return false;
            }
            sendSMS(from, "lop5");
            return true;
        }
    }

}