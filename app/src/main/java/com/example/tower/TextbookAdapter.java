package com.example.tower;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.NumberFormat;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TextbookAdapter extends BaseAdapter {

    private final Context mContext;
    private ArrayList<Textbook> textbooks;

    public TextbookAdapter(Context mContext, ArrayList<Textbook> textbooks) {
        this.mContext = mContext;
        this.textbooks = textbooks;
    }

    @Override
    public int getCount() {
        return textbooks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1
        final Textbook book = textbooks.get(position);

        // 2
        if (convertView == null) {
            Log.d("Mike", "Hey I'm here!!!");
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.textbook, parent, false);
            final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
            final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_book_name);
            final TextView authorTextView = (TextView)convertView.findViewById(R.id.textview_book_author);
            final TextView sellerTextView = (TextView)convertView.findViewById(R.id.textview_book_seller);
            final TextView priceTextView = (TextView)convertView.findViewById(R.id.textview_book_price);

            // 4
            imageView.setImageResource(R.drawable.textbook);
            nameTextView.setText(book.getTitle());
            authorTextView.setText(book.getAuthor());
            sellerTextView.setText("" + book.getSeller());

            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            double amt = book.getPrice();
            priceTextView.setText("" + formatter.format(amt));
        }
        else {
            Log.d("Mike", "No I'm actually over here!!!");

        }

        // 3


        return convertView;
    }
}
