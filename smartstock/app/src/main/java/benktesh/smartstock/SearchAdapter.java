package benktesh.smartstock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import benktesh.smartstock.Model.Stock;
import benktesh.smartstock.Utils.ColorUtils;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private static String TAG = SearchAdapter.class.getSimpleName();
    // I Create a final private ListItemClickListener called mOnClickListener
    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    final private ListItemClickListener mOnClickListener;
    private Context mContext;
    private ArrayList<Stock> mData;
    private int mViewHolderCount;
    private int mNumberItems;
    private TextView mSymbolTextView;


    // The constructor receives list of data and listern callback. It initilizes with a viewholder count of 0.
    public SearchAdapter(ArrayList<Stock> data, ListItemClickListener listener) {

        mData = data;
        mOnClickListener = listener;
        mViewHolderCount = 0;
    }

    /**
     * This method is called for each ViewHolder created when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     */
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.searchrow;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        SearchViewHolder viewHolder = new SearchViewHolder(view);

        viewHolder.viewHolderIndex.setText(mContext.getString(R.string.label_viewholder_index) + mViewHolderCount);

        //stock is up, one color, stock is down another color. Start with default color
        int backgroundColorForViewHolder = ColorUtils.getViewBackGroundColorForStock(mContext, 0);
        viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);

        mViewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + mViewHolderCount);
        return viewHolder;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    public void resetData(ArrayList<Stock> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    //  An interface called ListItemClickListener receives onClick message. Within that interface,
    // we can define a void method called onListItemClick that takes any arguement as paramter.
    public interface ListItemClickListener {
        void onListItemClick(Stock stock);
    }

    class SearchViewHolder extends ViewHolder implements View.OnClickListener {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView symbolView;
        TextView changeView;
        ImageView imageView;
        TextView summaryView;
        TextView priceView;
        // Will display which ViewHolder is displaying this data
        TextView viewHolderIndex;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         *
         * @param itemView The View that you inflated in
         *                 {@link SearchAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public SearchViewHolder(View itemView) {
            super(itemView);

            symbolView = itemView.findViewById(R.id.search_result_symbol);
            viewHolderIndex = itemView.findViewById(R.id.search_result_change);
            changeView = itemView.findViewById(R.id.search_result_change);
            //imageView = itemView.findViewById(R.id.search_result_image);
            priceView = itemView.findViewById(R.id.search_result_price);
            summaryView = itemView.findViewById(R.id.search_result_summary);

            itemView.setOnClickListener(this);
        }


        /**
         * This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         *
         * @param listIndex Position of the item in the list
         */
        void bind(int listIndex) {
            Stock stock = mData.get(listIndex);
            symbolView.setText(stock.Symbol);
            changeView.setText(stock.Change.toString());
            summaryView.setText(stock.Market);
            priceView.setText(stock.Price.toString());
            itemView.setBackgroundColor(ColorUtils.getViewBackGroundColorForStock(mContext, stock.Change));
        }

        /**
         * Called whenever a user clicks on an item in the list.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Stock stock = mData.get(clickedPosition);
            mOnClickListener.onListItemClick(stock);
        }
    }
}
