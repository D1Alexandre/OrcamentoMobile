package com.br.skysoftmobile.util.datagrid;

import com.br.skysoftmobile.util.datatable.*;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

class Item extends LinearLayout {

	private TextView[] artTextView;
	private Context mContext;
	private Spliter2 mTxtContent;
	private DataGrid.MemberCollection mc;

	public Item(Context context, DataGrid.MemberCollection mc,
			DataTable.DataRow data) {
		super(context);
		mContext = context;
		this.mc = mc;

		setOrientation(HORIZONTAL);

		artTextView = new TextView[data.getColumnSize()];
		int intCellSpliter = 0;

		for (int i = 0; i < mc.COLUMN_STYLE.size(); i++) {
			artTextView[i] = new TextView(mContext);
			artTextView[i].setTextSize(DataGridStyle.DataCell.FontSize);
			artTextView[i].setPadding(DataGridStyle.DataCell.LPadding, DataGridStyle.DataCell.TPadding,
					DataGridStyle.DataCell.RPadding, DataGridStyle.DataCell.BPadding);
			artTextView[i].setBackgroundColor(DataGridStyle.DataCell.BackgroundColor);
			artTextView[i].setText(data.get(mc.COLUMN_STYLE.get(i)
					.getFieldName()));
			artTextView[i].setTextColor(DataGridStyle.DataCell.TextColor);
			artTextView[i].setWidth(mc.COLUMN_STYLE.get(i).getWitdh());
			artTextView[i].setSingleLine(true);
			artTextView[i].setGravity(DataGridStyle.DataCell.Gravity);

			mc.ITEM_VIEW.get(i).add(artTextView[i]);

			addView(artTextView[i], new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					DataGridStyle.DataCell.Height));

			if (intCellSpliter < mc.COLUMN_STYLE.size()) {
				mTxtContent = new Spliter2(getContext(), mc, i);
				mc.SPLITER_VIEW.get(i).add(mTxtContent);
				addView(mTxtContent, new LinearLayout.LayoutParams(
						DataGridStyle.HeaderCell.Width, DataGridStyle.DataCell.Height));
				intCellSpliter++;
			}
		}
	}

	public void populate(DataTable.DataRow data) {
		for (int i = 0; i < mc.COLUMN_STYLE.size(); i++) {
			artTextView[i].setWidth(mc.COLUMN_STYLE.get(i).getWitdh());
			artTextView[i].setText(data.get(mc.COLUMN_STYLE.get(i)
					.getFieldName()));
		}

	}
}