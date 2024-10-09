package com.example.packup.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.packup.R;
import com.example.packup.constants.MyConstants;
import com.example.packup.database.RoomDB;
import com.example.packup.models.Items;

import java.util.List;

public class CheckListAdapter extends RecyclerView.Adapter<ChecklListViewHolder> {

    Context context;
    List<Items> itemsList;
    RoomDB database;
    String show;

    public CheckListAdapter() {
    }

    public CheckListAdapter(Context context, List<Items> itemsList, RoomDB database, String show) {
        this.context = context;
        this.itemsList = itemsList;
        this.database = database;
        this.show = show;

        if(itemsList.size() == 0)
            Toast.makeText(context.getApplicationContext(), "Nothing to show", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public ChecklListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChecklListViewHolder(LayoutInflater.from(context).inflate(R.layout.check_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklListViewHolder holder, int position) {
        holder.checkbox.setText(itemsList.get(position).getItemname());
        holder.checkbox.setChecked(itemsList.get(position).getChecked());

        if(MyConstants.FALSE_STRING.equals(show)) {
            holder.btnDel.setVisibility(View.GONE);
            holder.layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_one));
        } else {
            if(itemsList.get(position).getChecked()) {
                holder.layout.setBackgroundColor(Color.parseColor("8e546f"));
            } else {
                holder.layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_one));
            }
        }

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check = holder.checkbox.isChecked();
                database.mainDao().checkUncheck(itemsList.get(position).getID(), check);
                if(MyConstants.FALSE_STRING.equals(show)) {
                    itemsList = database.mainDao().getAllSelected(true);
                    notifyDataSetChanged();
                } else {
                    itemsList.get(position).setChecked(check);
                    notifyDataSetChanged();
                    Toast toastMsg = null;
                    if(toastMsg != null) {
                        toastMsg.cancel();
                    }
                    if(itemsList.get(position).getChecked()) {
                        toastMsg = Toast.makeText(context, "(" +holder.checkbox.getText()+") Packed", Toast.LENGTH_SHORT);
                    }
                    else {
                        toastMsg = Toast.makeText(context, "(" + holder.checkbox.getText() + ") Unpacked", Toast.LENGTH_SHORT);
                    }
                    toastMsg.show();
                }
            }
        });

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context).setTitle("Delete ( "+ itemsList.get(position).getItemname()+" )")
                        .setMessage("Are you sure you want to delete this item?").setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                database.mainDao().delete(itemsList.get(position));
                                itemsList.remove(itemsList.get(position));
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        }).setIcon(R.drawable.ic_delete_forever).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}

class ChecklListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        CheckBox checkbox;
        Button btnDel;




        public ChecklListViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.linearLayout);
            checkbox = itemView.findViewById(R.id.checkbox);
            btnDel = itemView.findViewById(R.id.btnDelete);
        }
}
