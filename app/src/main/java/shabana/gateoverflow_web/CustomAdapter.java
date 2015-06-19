package shabana.gateoverflow_web;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shabana on 6/17/15.
 */
public class CustomAdapter extends BaseAdapter {
    private ArrayList _data;
    Context _c;

    CustomAdapter (ArrayList data, Context c){
        _data = data;
        _c = c;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return _data.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return _data.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
        if (v == null)
        {
            LayoutInflater vi = (LayoutInflater)_c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item, null);
        }

        LinearLayout linlay = (LinearLayout)v.findViewById(R.id.linlayout);

        TextView nameview = (TextView)v.findViewById(R.id.titel);
        TextView descview = (TextView)v.findViewById(R.id.decription);
        TextView timeView = (TextView)v.findViewById(R.id.time);
        TextView autorView = (TextView)v.findViewById(R.id.autor);

        Details msg = (Details) _data.get(position);
        nameview.setText(msg.name);
        descview.setText(msg.desc);
        timeView.setText(msg.time);
        autorView.setText(msg.autor);

        if(position % 2 == 0){
            linlay.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else{
            linlay.setBackgroundColor(Color.parseColor("#EAEAEA"));
        }
        return v;
    }
}
