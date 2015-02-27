package com.kemblep.hobbsutilities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

public class QuickAddFragment extends Fragment {
	
	public QuickAddFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_quick_add, container,
				false);
		
		final EditText quickAddEntry = (EditText) rootView.findViewById(R.id.quick_add_entry);
		final TextView quickAddTotal = (TextView) rootView.findViewById(R.id.quick_add_total);
		final ListView quickAddTally = (ListView) rootView.findViewById(R.id.quick_add_tally);
		
		final List<String> tally = new ArrayList<String>();
		final ArrayAdapter<String> tallyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tally);
		
		

		TextWatcher quickAddWatcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length() == 1){
					Util.setText(quickAddEntry, s.toString() + ".");
					quickAddEntry.setSelection(s.length() + 1);
				}
				
				if(s.length() == 3){
					//send it to the total
					float addEntry = Float.parseFloat(s.toString());
					float total = Float.parseFloat(quickAddTotal.getText().toString());
					
					float newTotal = total + addEntry;
					DecimalFormat finalFormat = new DecimalFormat("0.0");

					Util.setText(quickAddTotal, finalFormat.format(newTotal));
					
					String tallyAddition = finalFormat.format(addEntry);
					tally.add(0, tallyAddition);
					tallyAdapter.notifyDataSetChanged();
				}
				if(s.length() > 3){
					
					//clear and set focus of the entry
					quickAddEntry.setText(s.toString().substring(3));
					quickAddEntry.requestFocus();
					quickAddEntry.setSelection(2);
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
		
		quickAddEntry.addTextChangedListener(quickAddWatcher);
		
		Button quickAddReset = (Button) rootView.findViewById(R.id.quick_add_reset);
		OnClickListener quickAddResetClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Util.setText(quickAddEntry, "");
				if(quickAddTotal.getText().toString() != "0.0"){
					Util.setText(quickAddTotal, "0.0");
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
		
		quickAddReset.setOnClickListener(quickAddResetClick);
		
		OnItemClickListener quickAddTallyItemClick = new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Util.setText(quickAddEntry, "");
				
				float clickedTime = Float.parseFloat(quickAddTally.getItemAtPosition(position).toString());
				float total = Float.parseFloat(quickAddTotal.getText().toString());
				
				float newTotal = total - clickedTime;
				DecimalFormat finalFormat = new DecimalFormat("0.0");

				Util.setText(quickAddTotal, finalFormat.format(newTotal));
				
				tally.remove(position);
				tallyAdapter.notifyDataSetChanged();
				
			}
		};
		
		quickAddTally.setAdapter(tallyAdapter);
		quickAddTally.setOnItemClickListener(quickAddTallyItemClick);
		
		return rootView;
	}
}
