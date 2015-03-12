package com.kemblep.hobbsutilities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class QuickAddFragment extends Fragment {
	
	public QuickAddFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View vQuickAddFragment = inflater.inflate(R.layout.fragment_quick_add, container,
				false);
		
		final EditText etQuickAddEntry = (EditText) vQuickAddFragment.findViewById(R.id.quick_add_entry);
		final TextView etQuickAddTotal = (TextView) vQuickAddFragment.findViewById(R.id.quick_add_total);
		final ListView etQuickAddTally = (ListView) vQuickAddFragment.findViewById(R.id.quick_add_tally);
		
		final List<String> tally = new ArrayList<String>();
		final ArrayAdapter<String> tallyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tally);

		TextWatcher twQuickAdd = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length() == 1){
					Util.setText(etQuickAddEntry, s.toString() + ".");
					etQuickAddEntry.setSelection(s.length() + 1);
				}
				
				if(s.length() == 3){
					//send it to the total
					float addEntry = Float.parseFloat(s.toString());
					float total = Float.parseFloat(etQuickAddTotal.getText().toString());
					
					float newTotal = total + addEntry;
					DecimalFormat finalFormat = new DecimalFormat("0.0");

					Util.setText(etQuickAddTotal, finalFormat.format(newTotal));
					
					String tallyAddition = finalFormat.format(addEntry);
					tally.add(0, tallyAddition);
					tallyAdapter.notifyDataSetChanged();
				}
				if(s.length() > 3){
					
					//clear and set focus of the entry
					etQuickAddEntry.setText(s.toString().substring(3));
					etQuickAddEntry.requestFocus();
					etQuickAddEntry.setSelection(2);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		};
		
		etQuickAddEntry.addTextChangedListener(twQuickAdd);
		
		Button btnQuickAddReset = (Button) vQuickAddFragment.findViewById(R.id.quick_add_reset);
		OnClickListener btnQuickAddResetOnClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Util.setText(etQuickAddEntry, "");
				if(etQuickAddTotal.getText().toString() != "0.0"){
					Util.setText(etQuickAddTotal, "0.0");
					if(!tally.isEmpty()){ 
						Toast.makeText(getActivity(), "Click again to clear the tally", Toast.LENGTH_SHORT).show();
					}
				} else {
					//clear the tally
					tallyAdapter.clear();
					tallyAdapter.notifyDataSetChanged();
				}
			}
		};
		
		btnQuickAddReset.setOnClickListener(btnQuickAddResetOnClickListener);
		
		OnItemClickListener etQuickAddTallyOnClickListener = new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Util.setText(etQuickAddEntry, "");
				
				float clickedTime = Float.parseFloat(etQuickAddTally.getItemAtPosition(position).toString());
				float total = Float.parseFloat(etQuickAddTotal.getText().toString());
				
				float newTotal = total - clickedTime;
				DecimalFormat finalFormat = new DecimalFormat("0.0");

				Util.setText(etQuickAddTotal, finalFormat.format(newTotal));
				
				tally.remove(position);
				tallyAdapter.notifyDataSetChanged();
				
			}
		};
		
		etQuickAddTally.setAdapter(tallyAdapter);
		etQuickAddTally.setOnItemClickListener(etQuickAddTallyOnClickListener);
		
		return vQuickAddFragment;
	}
}
