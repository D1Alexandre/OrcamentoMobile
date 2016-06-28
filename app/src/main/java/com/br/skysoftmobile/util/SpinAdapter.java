package com.br.skysoftmobile.util;

import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinAdapter extends ArrayAdapter<String[]>{

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<String[]> values;

    public SpinAdapter(Context context, int textViewResourceId, List<String[]> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
       return values.size();
    }

    public String getItemValue(int position){
    	String[] str = values.get(position);
    	return str[0];
    }
    
    public String getItemLabel(int position){
    	String[] str = values.get(position);
    	return str[1];
    }
    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
    	
    	TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        String[] str = values.get(position);
        label.setText(str[1]);
        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }
    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
    	//RadioButton rb = new RadioButton(context);
    	
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        String[] str = values.get(position);
        label.setText(str[1]);
        label.setTextSize(20);
        label.setGravity(Gravity.CENTER_VERTICAL);
        label.setHeight(70);
        label.setPadding(40, 10, 10, 10);
        return label;
    }
}