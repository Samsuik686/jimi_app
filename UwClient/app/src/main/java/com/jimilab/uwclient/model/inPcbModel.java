package com.jimilab.uwclient.model;

import com.jimilab.uwclient.bean.pcb_bean.PcbPandianChoujianBean;

import java.util.ArrayList;

public interface inPcbModel {
    interface onStringLoadListener {
        void onComplete(ArrayList<PcbPandianChoujianBean> dataList );

        void onError(String throwable);
    }

    interface SendMessageListener {
        void onComplete( );

        void onError( String error_message);
    }

    interface getHasNotListener {
        void onComplete(String complete_string);

        void onError(String error_string);
    }
}
