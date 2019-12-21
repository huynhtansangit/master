package com.example.uitest;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.zip.Inflater;

public class tab2 extends Fragment {
    private String usr_session;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        usr_session=bundle.getString("usr_ss");
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_frag, container, false);
        String[] sortTitle ={ "By price", "By number of seat" };
        ListView listView = view.findViewById(R.id.sort_listview);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,sortTitle);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sortby;
                switch (i){
                    case 0: sortby="price"; break;
                    case 1: sortby="no_seat"; break;
                    default: sortby="all"; break;
                }
                Intent intent = new Intent();
                intent.setClass(getActivity(),sorts_list.class);
                intent.putExtra("userSession",usr_session);
                intent.putExtra("sort_Type", sortby);
                startActivity(intent);
            }
        });
        return view;//inflater.inflate(R.layout.tab2_frag, container, false);
    }
}
