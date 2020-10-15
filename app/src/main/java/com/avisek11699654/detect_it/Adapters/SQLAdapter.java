package com.avisek11699654.detect_it.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.avisek11699654.detect_it.R;
import com.avisek11699654.detect_it.common.FruitModel;
import com.avisek11699654.detect_it.common.MySQLITE;

import java.util.ArrayList;

public class SQLAdapter extends RecyclerView.Adapter<SQLAdapter.ContactViewHolder>
        implements Filterable {
    private Context context;
    private ArrayList<FruitModel> listContacts;
    private ArrayList<FruitModel> mArrayList;
    private MySQLITE mDatabase;








    public SQLAdapter(Context context, ArrayList<FruitModel> listContacts) {
        this.context = context;
        this.listContacts = listContacts;
        this.mArrayList = listContacts;
        mDatabase = new MySQLITE(context);

    }




    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvConfidence;

        ContactViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.historyTitle);
            tvConfidence = itemView.findViewById(R.id.historyConfidence);


        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sql_adapter_list, parent, false);
        return new ContactViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        final FruitModel fruitModel = listContacts.get(position);
        holder.tvTitle.setText(fruitModel.getName());
        holder.tvConfidence.setText(fruitModel.getConfidecne());


    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listContacts = mArrayList;
                }
                else {
                    ArrayList<FruitModel> filteredList = new ArrayList<>();
                    for (FruitModel fruitModel : mArrayList) {
                        if (fruitModel.getName().toLowerCase().contains(charString)) {
                            filteredList.add(fruitModel);
                        }
                    }
                    listContacts = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listContacts;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listContacts = (ArrayList<FruitModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        return listContacts.size();
    }

}