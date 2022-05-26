package com.example.myboardgames.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.myboardgames.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ExpListViewAdapterWithCheckbox extends BaseExpandableListAdapter {
    private Context context;

    private final Map<String, List<String>> mListDataChild;
    private final List<String> mListDataGroup;
    private final Map<Integer, boolean[]> mChildCheckStates;

    private String groupText;
    private String childText;

    private GroupViewHolder groupViewHolder;
    private ChildViewHolder childViewHolder;

    private List<ChangeStateEvent> observers = new ArrayList<>();

    public ExpListViewAdapterWithCheckbox(Context context, List<String> mListDataGroup, Map<String, List<String>> mListDataChild) {
        this.context = context;
        this.mListDataGroup = mListDataGroup;
        this.mListDataChild = mListDataChild;

        mChildCheckStates = new HashMap<Integer, boolean[]>();
    }

    @Override
    public int getGroupCount() {
        return mListDataGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListDataChild.get(mListDataGroup.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return mListDataGroup.get(groupPosition);
    }



    @Override
    public String getChild(int groupPosition, int childPosition) {
        return mListDataChild.get(mListDataGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        groupText = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.filter_parent, null);

            groupViewHolder = new GroupViewHolder();
            groupViewHolder.mGroupText = (TextView) convertView.findViewById(R.id.parent_name);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        groupViewHolder.mGroupText.setText(groupText);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final int mGroupPosition = groupPosition;
        final int mChildPosition = childPosition;

        childText = getChild(mGroupPosition, mChildPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.filter_child_checkbox, null);

            childViewHolder = new ChildViewHolder();
            childViewHolder.mChildText = (TextView) convertView.findViewById(R.id.child_name);
            childViewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);

            convertView.setTag(R.layout.filter_child_checkbox, childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag(R.layout.filter_child_checkbox);
        }

        childViewHolder.mChildText.setText(childText);

        //To recover from saved state
        childViewHolder.mCheckBox.setOnCheckedChangeListener(null);

        if (mChildCheckStates.containsKey(mGroupPosition)) {
            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
            childViewHolder.mCheckBox.setChecked(getChecked[mChildPosition]);
        } else {
            boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];
            mChildCheckStates.put(mGroupPosition, getChecked);
            childViewHolder.mCheckBox.setChecked(false);
        }
        //Set check listener again
        childViewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                assert getChecked[mChildPosition] != isChecked;
                getChecked[mChildPosition] = isChecked;
                mChildCheckStates.put(mGroupPosition, getChecked);

                doEventCallback(getCheckedItems());
            }
        });
        return convertView;
    }

    public void setOnChangeStateListener(ChangeStateEvent event){
        observers.add(event);
    }

    private void doEventCallback(Map<String, Set<String>> items){
        for(ChangeStateEvent event : observers){
            event.callback(items);
        }
    }

    private Map<String, Set<String>> getCheckedItems(){
        Map<String, Set<String>> res = new HashMap<>();
        for (int i = 0; i < mListDataGroup.size(); ++i) {
            Set<String> groupRes = new HashSet<>();
            String groupName = mListDataGroup.get(i);
            boolean[] checkedValues = mChildCheckStates.get(i);
            List<String> childNames = mListDataChild.get(groupName);

            //Nothing checked in group i
            if(checkedValues == null){
                continue;
            }

            for (int j = 0; j < checkedValues.length; ++j) {
                if(checkedValues[j])
                    groupRes.add(childNames.get(j));
            }

            if(!groupRes.isEmpty()){
                res.put(groupName, groupRes);
            }
        }
        return res;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public final class GroupViewHolder {
        TextView mGroupText;
    }

    public final class ChildViewHolder {
        TextView mChildText;
        CheckBox mCheckBox;
    }
}
