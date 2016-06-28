package com.br.skysoftmobile.util.datagrid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.br.skysoftmobile.util.datatable.DataTable;


class DataGridAdapter extends BaseAdapter {

	private Context mContext;
	private DataGrid.MemberCollection mc;

	public DataGridAdapter(Context context, DataGrid.MemberCollection mc) {
		mContext = context;
		this.mc = mc;
	}

	@Override
	public int getCount() {
		return mc.DATA_SOURCE.getRowSize();
	}

	@Override
	public Object getItem(int position) {
		return mc.DATA_SOURCE.getRow(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Make a SpeechView to hold each row.
	 * 
	 * @see android.widget.ListAdapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Item ri;

		DataTable.DataRow data = mc.DATA_SOURCE.getRow(position);

		if (convertView == null) {
			ri = new Item(mContext, mc, data);
		} else {
			ri = (Item) convertView;
			ri.populate(data);
		}

		return ri;
	}
}