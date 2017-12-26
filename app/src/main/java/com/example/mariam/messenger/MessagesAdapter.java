package com.example.mariam.messenger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

public class MessagesAdapter extends ArrayAdapter<MessageModel>{
    private LayoutInflater inflater = null;
    private ArrayList<MessageModel> array = null;

    public MessagesAdapter(Context context, ArrayList<MessageModel> array) {
        super(context, R.layout.message_item,array);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.array = array;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.message_item,null,false);
        TextView tv = (TextView) view.findViewById(R.id.message_textView);
        tv.setText((array.get(position)).getMessage());
        if((array.get(position)).getSentByMe() == 1) {
            tv.setTextColor(view.getResources().getColor(R.color.background_out_text_color));
            tv.setBackgroundResource(R.drawable.messages_style_out);
            ((LinearLayout)view).setGravity(Gravity.RIGHT);
        } else {
            tv.setTextColor(view.getResources().getColor(R.color.background_in_text_color));
            tv.setBackgroundResource(R.drawable.messages_style_in);
            ((LinearLayout)view).setGravity(Gravity.LEFT);
        }
        return view;
    }

    public void addNewMessage(MessageModel model) {
        if (array != null) {
            array.add(model);
        }
    }

}
