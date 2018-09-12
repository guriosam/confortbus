package sam.gurio.confortbus.booking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sam.gurio.confortbus.R;
import sam.gurio.confortbus.booking.AbstractItem;
import sam.gurio.confortbus.booking.CenterItem;
import sam.gurio.confortbus.booking.EdgeItem;
import sam.gurio.confortbus.booking.OnSeatSelected;
import sam.gurio.confortbus.utils.AppPreferences;

public class AirplaneAdapter extends SelectableAdapter<RecyclerView.ViewHolder> {

    private OnSeatSelected mOnSeatSelected;

    AppPreferences preferences;

    public void setSeats(List<String> seats) {
        this.seats = seats;
    }

    public List<String> getSeats() {
        return seats;
    }

    private static class EdgeViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSeat;
        private final ImageView imgSeatSelected;
        private final ImageView imgSeatTaken;


        public EdgeViewHolder(View itemView) {
            super(itemView);
            imgSeat = (ImageView) itemView.findViewById(R.id.img_seat);
            imgSeatSelected = (ImageView) itemView.findViewById(R.id.img_seat_selected);
            imgSeatTaken = (ImageView) itemView.findViewById(R.id.img_seat_taken);

        }

    }

    private static class CenterViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSeat;
        private final ImageView imgSeatSelected;
        private final ImageView imgSeatTaken;

        public CenterViewHolder(View itemView) {
            super(itemView);
            imgSeat = (ImageView) itemView.findViewById(R.id.img_seat);
            imgSeatSelected = (ImageView) itemView.findViewById(R.id.img_seat_selected);
            imgSeatTaken = (ImageView) itemView.findViewById(R.id.img_seat_taken);
        }

    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }

    }

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<AbstractItem> mItems;

    private List<String> seats;

    public AirplaneAdapter(Context context, List<AbstractItem> items) {
        mOnSeatSelected = (OnSeatSelected) context;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        preferences = AppPreferences.getInstance(context);
        mItems = items;
        seats = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AbstractItem.TYPE_CENTER) {
            View itemView = mLayoutInflater.inflate(R.layout.list_item_seat, parent, false);
            return new CenterViewHolder(itemView);
        } else if (viewType == AbstractItem.TYPE_EDGE) {
            View itemView = mLayoutInflater.inflate(R.layout.list_item_seat, parent, false);
            return new EdgeViewHolder(itemView);
        } else {
            View itemView = new View(mContext);
            return new EmptyViewHolder(itemView);
        }
    }

    public List<Integer> getSelected() {
        return getSelectedItems();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        int type = mItems.get(position).getType();
        if (type == AbstractItem.TYPE_CENTER) {
            final CenterItem item = (CenterItem) mItems.get(position);
            final CenterViewHolder holder = (CenterViewHolder) viewHolder;

            if (seats.size() > position) {
                if (seats.get(position).equals("1")) {
                    holder.imgSeatTaken.setVisibility(View.VISIBLE);
                }
            }

            holder.imgSeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(preferences.isAdmin()) {

                        toggleSelection(position);

                        mOnSeatSelected.onSeatSelected(getSelectedItemCount());

                        if (holder.imgSeatTaken.getVisibility() == View.VISIBLE) {
                            holder.imgSeatTaken.setVisibility(View.INVISIBLE);
                            holder.imgSeatSelected.setVisibility(View.INVISIBLE);
                            seats.set(position, "0");
                        }
                    }
                }
            });
            if(preferences.isAdmin()) {
                holder.imgSeatSelected.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
            }
        } else if (type == AbstractItem.TYPE_EDGE) {
            final EdgeItem item = (EdgeItem) mItems.get(position);
            final EdgeViewHolder holder = (EdgeViewHolder) viewHolder;

            if (seats.size() > position) {
                if (seats.get(position).equals("1")) {
                    holder.imgSeatTaken.setVisibility(View.VISIBLE);
                }
            }

            holder.imgSeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(preferences.isAdmin()) {

                        toggleSelection(position);

                        mOnSeatSelected.onSeatSelected(getSelectedItemCount());

                        if (holder.imgSeatTaken.getVisibility() == View.VISIBLE) {
                            holder.imgSeatTaken.setVisibility(View.INVISIBLE);
                            holder.imgSeatSelected.setVisibility(View.INVISIBLE);
                            seats.set(position, "0");
                        }
                    }
                }
            });


            if(preferences.isAdmin()) {
                holder.imgSeatSelected.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
            }


        }
    }

}