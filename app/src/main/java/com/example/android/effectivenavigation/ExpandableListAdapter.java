package com.example.android.effectivenavigation;

/**
 * Created by sjaltran on 12/7/16.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
public class ExpandableListAdapter extends BaseExpandableListAdapter{
    private Activity context;
    private Map<String, List<Items>> sortedList;
    private List<String> _categoryList;

    public ExpandableListAdapter(Activity context, List<String> categoryList,
                                 Map<String, List<Items>> categorywiseItemList) {
        this.context = context;
        this.sortedList = categorywiseItemList;//hashMap containing the key(category name) and value(items undergiven category)
        this._categoryList = categoryList; //hashMap Keys
    }
    public Items getChild(int groupPosition, int childPosition) {
        List<Items> itemList_UnderCategory = sortedList.get(_categoryList.get(groupPosition));
        return itemList_UnderCategory.get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Items item = (Items) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_undercategory, null);
        }

        TextView id = (TextView) convertView.findViewById(R.id.itemId);
        TextView name = (TextView) convertView.findViewById(R.id.itemname);
        TextView barcode = (TextView) convertView.findViewById(R.id.itembarcode);
        TextView price = (TextView) convertView.findViewById(R.id.itemprice);

        id.setText(String.valueOf(item.getID()).toString().trim());
        name.setText(item.getName().toString().trim());
        barcode.setText(item.getBarcode().toString().trim());
        price.setText(item.getPrice().toString().trim());
        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<Items> itemList_UnderCategory = sortedList.get(_categoryList.get(groupPosition));
                                Items itemToRemove = itemList_UnderCategory.get(childPosition);
                                DatabaseHandler db = new DatabaseHandler(context);
                                db.deleteItem(itemToRemove);
                                itemList_UnderCategory.remove(childPosition);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        List<Items> itemList_UnderCategory = sortedList.get(_categoryList.get(groupPosition));
        return itemList_UnderCategory.size();
    }

    public Object getGroup(int groupPosition) {
        return _categoryList.get(groupPosition);
    }

    public int getGroupCount() {
        return _categoryList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String category = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.category_list,
                    null);
        }
        TextView dispCategory = (TextView) convertView.findViewById(R.id.category);
        dispCategory.setTypeface(null, Typeface.BOLD);
        dispCategory.setText(category);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}