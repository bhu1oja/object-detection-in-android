package com.avisek11699654.detect_it.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.avisek11699654.detect_it.R;


import java.util.List;


// The adapter class which
// extends RecyclerView Adapter
public class RelatedFruit
        extends RecyclerView.Adapter<RelatedFruit.MyView> {

    // List with String type
    private List<String> listName;
    private List<String> listConfidence;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView tvConfidecne, tvTitle;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view)
        {
            super(view);

            // initialise TextView with id
            tvConfidecne = (TextView)view
                    .findViewById(R.id.tv_related_confidence);
            tvTitle = view.findViewById(R.id.tv_related_title);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public RelatedFruit(List<String> horizontalListName, List<String> horizontalListConfidence)
    {
        this.listName = horizontalListName;
        this.listConfidence = horizontalListConfidence;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType)
    {

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.related_match,
                        parent,
                        false);

        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position)
    {

        // Set the text of each item of
        // Recycler view with the list items
        holder.tvConfidecne.setText(listConfidence.get(position) + " %");
        holder.tvTitle.setText(listName.get(position));
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return listName.size();
    }
}