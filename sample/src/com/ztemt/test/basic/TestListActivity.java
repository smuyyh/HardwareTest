package com.ztemt.test.basic;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TestListActivity extends ListActivity implements OnCheckedChangeListener {

    private static final String TAG = "TestListActivity";
    private static final String PREFS = "test_prefs";
    private static final String KEY_SINGLE_MODE = "single_mode";

    private TestListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_list);

        setActionBarOptions();

        mAdapter = new TestListAdapter(this);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.i(TAG, "Item clicked: id -> " + id + " position -> " + position);
        Intent intent = new Intent(this, TestItemActivity.class);
        intent.putExtra("ITEM_INDEX", position);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        sp.edit().putBoolean(KEY_SINGLE_MODE, isChecked).commit();
    }

    private void setActionBarOptions() {
        View v = getLayoutInflater().inflate(R.layout.test_list_options, null);

        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        CheckBox cb = (CheckBox) v.findViewById(R.id.single_mode);
        cb.setChecked(sp.getBoolean(KEY_SINGLE_MODE, false));
        cb.setOnCheckedChangeListener(this);

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        getActionBar().setCustomView(v, lp);

        int flags = ActionBar.DISPLAY_SHOW_CUSTOM;
        int options = getActionBar().getDisplayOptions() ^ flags;
        getActionBar().setDisplayOptions(options, flags);
    }

    class TestListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public TestListAdapter(Context context) {
            // Cache the LayoutInflate to avoid asking for a new one each time
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return TestList.getCount();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.text1 = (TextView) convertView.findViewById(android.R.id.text1);
                holder.text2 = (TextView) convertView.findViewById(android.R.id.text2);

                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ViewHolder) convertView.getTag();
            }

            // Bind the data with the holder.
            setTitleView(position, holder.text1);
            setResultView(position, holder.text2);

            return convertView;
        }

        private void setTitleView(int position, TextView tv) {
            tv.setText((position + 1) + "." + TestList.get(position).getTestName());
        }

        private void setResultView(int position, TextView tv) {
            SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
            int state = sp.getInt(String.valueOf(position), -1);
            switch (state) {
            case 1:
                tv.setText(R.string.pass_text);
                tv.setTextColor(Color.GREEN);
                break;
            case 0:
                tv.setText(R.string.fail_text);
                tv.setTextColor(Color.RED);
                break;
            default:
                tv.setText("");
                break;
            }
        }

        class ViewHolder {
            TextView text1;
            TextView text2;
        }
    }
}
