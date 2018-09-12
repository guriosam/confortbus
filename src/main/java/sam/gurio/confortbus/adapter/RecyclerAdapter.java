package sam.gurio.confortbus.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sam.gurio.confortbus.ChooseReportActivity;
import sam.gurio.confortbus.ChooseSeatActivity;
import sam.gurio.confortbus.R;
import sam.gurio.confortbus.ReportActivity;
import sam.gurio.confortbus.objects.Bus;


/**
 * Created by Gurio on 25/03/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.BusHolder> {

    private List<Bus> mBuses;
    private int control;

    public RecyclerAdapter(List<Bus> mBuses, int control) {
        this.control = control;
        this.mBuses = mBuses;
    }


    @Override
    public RecyclerAdapter.BusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_bus, parent, false);
        return new BusHolder(inflatedView, control);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.BusHolder holder, int position) {
        if (mBuses.size() > position) {
            Bus bus = mBuses.get(position);
            holder.setPosition(position);
            holder.setBus(bus);
            holder.title.setText(bus.getLineName());
        }
    }

    public Bus getItem(int position) {
        if (mBuses.size() <= position) {
            return null;
        }
        return mBuses.get(position);
    }

    public Bus setItem(int position, Bus bus) {
        return mBuses.set(position, bus);
    }

    public List<Bus> getmBuses() {
        return mBuses;
    }

    @Override
    public int getItemCount() {
        return mBuses.size();
    }

    public static class BusHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2
        private TextView title;
        private Bus bus;
        private int position;
        private int control;

        //4
        public BusHolder(View v, int control) {
            super(v);

            this.control = control;
            title = (TextView) v.findViewById(R.id.bus_line);
            v.setOnClickListener(this);
        }

        public void setBus(Bus bus) {
            this.bus = bus;
        }


        @Override
        public void onClick(View v) {

            Context context = itemView.getContext();

            if (control == 0) {

                Intent intent = new Intent(context, ChooseSeatActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);

            } else {
                Intent intent = new Intent(context, ReportActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }

            //Intent showPhotoIntent = new Intent(context, ShowNewsActivity.class);
            //int k = AppPreferences.getInstance(context.getApplicationContext()).getKey();

            //if (k == -1) {
            //    Toast.makeText(context, "Ocorreu um erro, por favor tente novamente.", Toast.LENGTH_SHORT).show();
            //} else {
            //    AppPreferences.getInstance(context.getApplicationContext()).setCurrentNews(k - position);
            //    context.startActivity(showPhotoIntent);
            //}

        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}

