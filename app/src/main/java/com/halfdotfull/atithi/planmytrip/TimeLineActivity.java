package com.halfdotfull.atithi.planmytrip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.halfdotfull.atithi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;

import static com.halfdotfull.atithi.planmytrip.PlanMyTrip.arrayListLocation;

public class TimeLineActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private boolean mWithLinePadding;
    private ArrayList<TimeLineModel> list1=new ArrayList<>(),list2=new ArrayList<>(),list3=new ArrayList<>(),list4=new ArrayList<>(),list5=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        makeLists();
        mRecyclerView =findViewById(R.id.recyclerTimeLine);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        initView();
    }



    private void initView() {
        //setDataListItems();
        mTimeLineAdapter = new TimeLineAdapter(mDataList, mWithLinePadding);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void setDataListItems(){

        for(String a :arrayListLocation){

            mDataList.add(new TimeLineModel(a, "", OrderStatus.INACTIVE));

        }
     /*
        mDataList.add(new TimeLineModel("Item successfully delivered", "", OrderStatus.INACTIVE));
        mDataList.add(new TimeLineModel("Courier is out to delivery your order", "2017-02-12 08:00", OrderStatus.ACTIVE));
        mDataList.add(new TimeLineModel("Item has reached courier facility at New Delhi", "2017-02-11 21:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item has been given to the courier", "2017-02-11 18:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item is packed and will dispatch soon", "2017-02-11 09:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order is being readied for dispatch", "2017-02-11 08:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order processing initiated", "2017-02-10 15:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order confirmed by seller", "2017-02-10 14:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order placed successfully", "2017-02-10 14:00", OrderStatus.COMPLETED));
*/    }


    private void makeLists() {

        list1.add(new TimeLineModel("Paharganj", "", OrderStatus.INACTIVE));
        list1.add(new TimeLineModel("Jain Coffee house", "", OrderStatus.INACTIVE));
        list1.add(new TimeLineModel("Ballimaran Bazar","",  OrderStatus.INACTIVE));
        list1.add(new TimeLineModel("Delhi War Cementry","", OrderStatus.INACTIVE));

        list2.add(new TimeLineModel("Snajat Van","",  OrderStatus.INACTIVE));
        list2.add(new TimeLineModel("Santushi Shopping Conplex", "", OrderStatus.INACTIVE));
        list2.add(new TimeLineModel("Andra Bhavan", "", OrderStatus.INACTIVE));
        list2.add(new TimeLineModel("Bhardwaj Lake", "", OrderStatus.INACTIVE));
        list2.add(new TimeLineModel("khan chaha", "", OrderStatus.INACTIVE));
        list2.add(new TimeLineModel("BTW", "", OrderStatus.INACTIVE));

        list3.add(new TimeLineModel("Bhuli Bhatayari ka Mahal", "", OrderStatus.INACTIVE));
        list3.add(new TimeLineModel("Haveli Of Mirza Ghalib", "", OrderStatus.INACTIVE));
        list3.add(new TimeLineModel("Lala Babu Chat Bhandar", "", OrderStatus.INACTIVE));
        list3.add(new TimeLineModel("Dilli Haat", "", OrderStatus.INACTIVE));

        list4.add(new TimeLineModel("Paharganj", "", OrderStatus.INACTIVE));
        list4.add(new TimeLineModel("Snajat Van","",  OrderStatus.INACTIVE));
       // list4.add(new TimeLineModel("Lala Babu Chat Bhandar", "", OrderStatus.INACTIVE));
        list4.add(new TimeLineModel("Andra Bhavan", "", OrderStatus.INACTIVE));

        list5.add(new TimeLineModel("Paharganj", "", OrderStatus.INACTIVE));
        list5.add(new TimeLineModel("Sitrarn Diwan chand", "", OrderStatus.INACTIVE));
        list5.add(new TimeLineModel("Jahaz mehal Fort", "", OrderStatus.INACTIVE));
        list5.add(new TimeLineModel("Tughlaqabad Fort", "", OrderStatus.INACTIVE));
        list5.add(new TimeLineModel("Satpura Bridge", "", OrderStatus.INACTIVE));







        Random random= new Random();

        int num=random.nextInt(5);

        switch (num){

            case 1:
                mDataList=list1;
                break;

            case 2:
                mDataList=list2;
                break;

            case 0:
                mDataList=list3;
                break;
            case 3:
                mDataList=list4;
                break;
            case 4:
                mDataList=list5;
                break;


            default:
                mDataList=list2;
                break;
        }

        //list1.add(new TimeLineModel(,"", OrderStatus.INACTIVE))

    }

}
