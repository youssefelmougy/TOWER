package com.example.tower;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import java.util.ArrayList;

public class TextbookAdapter extends BaseAdapter {

    private final Context mContext;
    private ArrayList<Textbook> textbooks;
    private String originalClass;
    String searchQuery;

    public TextbookAdapter(Context mContext, ArrayList<Textbook> textbooks, String originalClass) {
        this.mContext = mContext;
        this.textbooks = textbooks;
        this.originalClass = originalClass;
    }

    public TextbookAdapter(Context mContext, ArrayList<Textbook> textbooks, String originalClass, String searchQuery) {
        this.mContext = mContext;
        this.textbooks = textbooks;
        this.originalClass = originalClass;
        this.searchQuery = searchQuery;
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

            LinearLayout layout = convertView.findViewById(R.id.textbook_layout);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendSpecificBook(book);
                }
            });
        }
        else {
            Log.d("Mike", "No I'm actually over here!!!");
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_book_name);
        final TextView authorTextView = (TextView)convertView.findViewById(R.id.textview_book_author);
        final TextView sellerTextView = (TextView)convertView.findViewById(R.id.textview_book_seller);
        final TextView priceTextView = (TextView)convertView.findViewById(R.id.textview_book_price);

        // 4
        Picasso.get().load(book.getImageUrl()).placeholder(R.drawable.textbook).fit().into(imageView);

        nameTextView.setText(book.getTitle());
        authorTextView.setText(book.getAuthor());

        if (book.getSeller() == 0) {
            sellerTextView.setText("");
            priceTextView.setText("");
        }
        else {
            sellerTextView.setText("" + book.getSeller());

            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            double amt = book.getPrice();
            priceTextView.setText("" + formatter.format(amt));
        }

        // 3


        return convertView;
    }

    private void sendSpecificBook(Textbook textbook) {
        Intent intent = new Intent(mContext, SpecificTextbook.class);
        Bundle bundle = new Bundle();
        bundle.putString("BOOK_TITLE", textbook.getTitle());
        bundle.putString("BOOK_AUTHOR", textbook.getAuthor());
        bundle.putLong("BOOK_SELLER", textbook.getSeller());
        bundle.putDouble("BOOK_PRICE", textbook.getPrice());
        bundle.putString("CLASS_FROM", "" + originalClass);
        bundle.putString("UNIQUE_ID", textbook.getUniqueID());
        bundle.putString("IMAGE_URL", textbook.getImageUrl());
        bundle.putString("BOOK_DESCRIPTION", textbook.getDescription());
        bundle.putString("BOOK_ISBN", textbook.getIsbn13());
        if(searchQuery != null) {
            bundle.putString("SEARCH_QUERY", searchQuery);


        }
        intent.putExtras(bundle);
        mContext.startActivity(intent);

    }
}
