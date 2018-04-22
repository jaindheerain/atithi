package com.halfdotfull.atithi.planmytrip;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.halfdotfull.atithi.R;

;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineViewHolder extends RecyclerView.ViewHolder {


    TextView mDate;
    TextView mMessage;
    TimelineView mTimelineView;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);

        mTimelineView=itemView.findViewById(R.id.time_marker);
mMessage=itemView.findViewById(R.id.text_timeline_title);
        mDate=itemView.findViewById(R.id.text_timeline_date);
        mTimelineView.initLine(viewType);
    }
}
