package com.getbase.floatingactionbutton.sample;
/**
 * Created by Dhruv on 8/11/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
public class CustomListAdapter_friends extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] fullname;
    private final String[] fusername;
    private int flag=1,liked=0;
    public static  String clicked_on_username="";
    public static  String clicked_on_name="";
    public CustomListAdapter_friends(Activity context,String[] fullname, String[] fusername) {
        super(context, R.layout.activity_user_friend_list, fusername);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.fullname=fullname;
        this.fusername=fusername;
        flag=1;
        checkforinternet();
    }

    public View getView(final int position, final View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
            final View rowView = inflater.inflate(R.layout.friend_list, null, true);
            //Toast.makeText(getContext(),""+username[position], Toast.LENGTH_LONG).show();
            TextView title1 = (TextView) rowView.findViewById(R.id.title_friend);
            title1.setText(fullname[position]);
           ImageView abc = (ImageView) rowView.findViewById(R.id.dpImage_scapp_friend);
        RelativeLayout def=(RelativeLayout)rowView.findViewById(R.id.grgg);
        def.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked_on_username=fusername[position];
                clicked_on_name=fullname[position];
                Intent intent=new Intent("com.getbase.floatingactionbutton.sample.chat_page");
                context.startActivity(intent);
            }
        });
            //flag = 1;
        Picasso.with(context).load("http://scraap.esy.es/dp/" +fusername[position] +".png").into(abc);

        checkforinternet();

        return rowView;
    }


    private void checkforinternet() {
        ConnectivityManager cm2 = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni2 = cm2.getActiveNetworkInfo();
        if (ni2 == null) {
            //Toast.makeText(MainActivity.this, "Not Connected to Internet!!", Toast.LENGTH_LONG).show();
            flag = 0;
            return;
        }
    }


}
