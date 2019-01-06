package com.myfeedback.myfeedbackprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class NewsFragment extends Fragment implements View.OnClickListener {

    public Button button;
    ArrayList<String> rssLinks = new ArrayList<>();
    View inflatedView = null;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflatedView = inflater.inflate(R.layout.fragment_news, container, false);

        Button btnRediff = inflatedView.findViewById(R.id.btnCommunity);
        Button btnCinemaBlend = inflatedView.findViewById(R.id.btnNation);

        btnRediff.setOnClickListener(this);
        btnCinemaBlend.setOnClickListener(this);

        return inflatedView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do your variables initialisations here
        rssLinks.add("http://www.thestar.com.my/rss/metro/community/");
        rssLinks.add("http://www.thestar.com.my/rss/editors-choice/nation/");
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // initialise your views
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCommunity:
                startActivity(new Intent(getActivity(), RSSFeedActivity.class).putExtra("rssLink", rssLinks.get(0)));
                break;

            case R.id.btnNation:
                startActivity(new Intent(getActivity(), RSSFeedActivity.class).putExtra("rssLink", rssLinks.get(1)));
                break;
        }
    }
}