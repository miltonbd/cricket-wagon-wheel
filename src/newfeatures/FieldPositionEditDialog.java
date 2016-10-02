package newfeatures;

import java.sql.SQLException;
import java.util.List;

import wagonwheel.FieldPositionPojo;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.opengleswagonwheel.R;
import com.j256.ormlite.dao.Dao;
import com.opengleswagonwheel.MyApp;
import com.utils.DatabaseHelper;
import com.utils.MyLogger;

class FieldPositionAdapter extends ArrayAdapter<FieldPositionPojo> {

	public FieldPositionAdapter(Context context, int textViewResourceId,
			List<FieldPositionPojo> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = MyApp.activity.getLayoutInflater().inflate(
				R.layout.list_field_position, null);
		CheckBox chkBox= (CheckBox) view.findViewById(R.id.checkBox1);
		TextView tv1 = (TextView) view.findViewById(R.id.textView1);
		TextView tv2 = (TextView) view.findViewById(R.id.textView2);
		TextView tv3 = (TextView) view.findViewById(R.id.textView3);

		FieldPositionPojo item = getItem(position);
		chkBox.setSelected(item.getIsSelected());
		tv1.setText(item.getName());
		tv2.setText(String.valueOf(item.getX()));
		tv3.setText(String.valueOf(item.getY()));

		return view;
	}
}

class MyMulitpleChoiceListener implements MultiChoiceModeListener {

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		return false;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.context, menu);
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode arg0) {
		
	}

	@Override
	public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {

		return false;
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {

	}

}

public class FieldPositionEditDialog extends DialogFragment {
	private ListView listView1;
	private Dao<FieldPositionPojo, Integer> dao;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_edit_field_position,
				null);
		listView1 = (ListView) view.findViewById(R.id.listView1);
		listView1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView1.setMultiChoiceModeListener(new MyMulitpleChoiceListener());
		DatabaseHelper databaseHelper = DatabaseHelper
				.getInstance(getActivity());
		try {
			dao = databaseHelper.getDao();
			List<FieldPositionPojo> items = dao.queryForAll();
			MyLogger.log("Total Field Positions " + items.size());
			FieldPositionAdapter adapter = new FieldPositionAdapter(
					MyApp.activity.getApplicationContext(),
					R.layout.list_field_position, items);
			listView1.setAdapter(adapter);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		getDialog().setTitle("Edit Field Position.");
		return view;
	}
}
