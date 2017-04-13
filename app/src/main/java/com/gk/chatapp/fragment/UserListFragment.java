package com.gk.chatapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gk.chatapp.R;
import com.gk.chatapp.adapter.UserAdapter;
import com.gk.chatapp.entry.UserEntry;
import com.gk.chatapp.model.DrawerItem;
import com.gk.chatapp.utils.SocketIoUtils;
import com.gk.chatapp.utils.UserStatus;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "UserListFragment";

    private ListView mListView;
    private List<UserEntry> mUserEntry = new ArrayList<>();
    private SwipeRefreshLayout mItemContainer;
    private ListView userListView;

    private UserAdapter userAdapter;

    private int drawTag;

    public UserListFragment() {
        // Required empty public constructor
        drawTag = DrawerItem.DRAWER_ITEM_TAG_RECENT;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserEntry.addAll(UserStatus.getUserList(DrawerItem.DRAWER_ITEM_TAG_RECENT));
        userAdapter = new UserAdapter(getActivity(),mUserEntry);

        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recent_user,container,false);

        mItemContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.container_items);
        mItemContainer.setOnRefreshListener(this);

        userListView = (ListView) rootView.findViewById(R.id.list_view);
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userListView.setItemChecked(position,false);
            }
        });
        userListView.setAdapter(userAdapter);


        return rootView;
    }

    private void initData(){

    }

    @Override
    public void onRefresh() {
        Log.d(TAG,"refresh");
        mItemContainer.setRefreshing(true);
//        SocketIoUtils.sendMessage("updateInfo");
        updateComplete();
        mItemContainer.setRefreshing(false);
    }

    public void updateComplete(){
        mUserEntry.clear();
        mUserEntry.addAll(UserStatus.getUserList(drawTag));
        userAdapter.notifyDataSetChanged();
    }

    //如果不同 则更新
    public void setDrawTag(int drawTag) {
        if(this.drawTag !=drawTag){
            mUserEntry.clear();
            mUserEntry.addAll(UserStatus.getUserList(drawTag));
            userAdapter.notifyDataSetChanged();
            this.drawTag = drawTag;
        }
    }

    public int getDrawTag() {
        return drawTag;
    }
}
