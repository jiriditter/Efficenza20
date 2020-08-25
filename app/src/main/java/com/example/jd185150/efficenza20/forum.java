package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class forum extends Activity {
    fetchForum fF;
    Button InsertMessage;
    Context context;
    LinearLayout cV;
    String usr="", pwd="";
    TextView newMessage;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.forum);
        this.context = this;
        cV = (LinearLayout) ((Activity) context).findViewById(R.id.vypis);
        newMessage = (TextView) ((Activity) context).findViewById(R.id.forumtext);

        Intent intent = getIntent();
        usr = intent.getStringExtra("user");
        pwd = intent.getStringExtra("pawd");

        getMessages();

        InsertMessage = (Button) findViewById(R.id.forumodeslat);
        InsertMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forumInsertMessage fIM = new forumInsertMessage(context, usr, pwd);
                fIM.execute();
                getMessages();
            }
        });

        newMessage.setSelectAllOnFocus(true);
    }

    private void getMessages() {
        cV.removeAllViewsInLayout();
        fF = new fetchForum(this, usr, pwd);
        fF.execute();
    }
}
