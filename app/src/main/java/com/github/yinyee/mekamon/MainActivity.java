package com.github.yinyee.mekamon;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        main();
    }

    public static void main() {

        // Initialising the client
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-a746e424-b962-11e6-a856-0619f8945a4f");
        pnConfiguration.setPublishKey("pub-c-f7f5d38a-de2c-4f96-b69e-a2674cf1abc0");

        PubNub pubnub = new PubNub(pnConfiguration);

        // Adding listener and associated methods: status(), message(), presence()
        pubnub.addListener(new SubscribeCallback() {

            @Override
            public void status(PubNub pubnub, PNStatus pnStatus) {

                // Status: Disconnected
                if (pnStatus.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    System.out.println("Say something silly");
                }

                // Status: Connected
                else if (pnStatus.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    System.out.println("Connected!");

                    // Publish a message to a channel
                    /*
                    String coords = "Awesome!"; // TODO insert the coordinates as returned from Javascript Object
                    pubnub.publish().channel("mekamon").message(coords).async(new PNCallback<PNPublishResult>() {

                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status) {
                            if (!status.isError()) System.out.println("Publish successful"); // Publish is successful
                            else {
                                // Check 'category' property to find out possible issue
                                // because of which request did fail.
                                // Request can be resent using: [status retry];
                                System.out.println("Publish unsuccessful");
                            }
                        }
                    }); // Close PNCallback object
                */
                }

                // Status: Reconnect
                else if (pnStatus.getCategory() == PNStatusCategory.PNReconnectedCategory) {
                    System.out.println("Reconnected");
                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost, then regained.
                }

                // Status: Decryption error
                else if (pnStatus.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {
                    System.out.println("Decryption error :(");
                    // Handle messsage decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult pnMessageResult) {

                System.out.println(pnMessageResult.getMessage());
                // Handle new message stored in message.message
                if (pnMessageResult.getChannel() != null) {
                    // Message has been received on channel group stored in
                    // message.getChannel()
                }
                else {
                    // Message has been received on channel stored in
                    // message.getSubscription()
                }

            /*
                log the following items with your favorite logger
                    - message.getMessage()
                    - message.getSubscription()
                    - message.getTimetoken()
            */
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                System.out.println("Need to write something for presence");
            }
        });

        pubnub.subscribe().channels(Arrays.asList("mekamon")).execute();
    }

}
