package com.bz.fichepresence.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bz.fichepresence.MainActivity;
import com.bz.fichepresence.R;
import com.bz.fichepresence.dbOperations.PersonOps;
import com.bz.fichepresence.entities.Personne;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Rog on 11/04/2018.
 */

public class PersonneAdapter extends RecyclerView.Adapter<PersonneAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Personne item);
    }


    private final List<Personne> items;
    private final OnItemClickListener listener;
    private final View v;

    public PersonneAdapter(List<Personne> items, View v, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
        this.v = v;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageP.setImageResource(R.drawable.profil);
        holder.bind(items, position,items.get(position), listener, v);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nom,matricule,presence;
        ImageView imageP;
        LinearLayout ll;
        ImageView save,unsave;
        ViewHolder(View v) {
            super(v);
            presence = (TextView)v.findViewById(R.id.presence);
            nom = (TextView) v.findViewById(R.id.nom);
            matricule = (TextView) v.findViewById(R.id.matricule);
            imageP = (ImageView) v.findViewById(R.id.profil);
            save =(ImageView)v.findViewById(R.id.save);
            unsave =(ImageView)v.findViewById(R.id.unsave);
            ll = (LinearLayout) v.findViewById(R.id.ll);

        }

        public void bind(final List<Personne> items, final int position, final  Personne item, final OnItemClickListener listener , final View view) {
            nom.setText(item.getNom());
            matricule.setText(item.getMatricule());
            if(item.getPresent()==1){

                presence.setText("Présent à "+item.getArrive());
                presence.setTextColor(Color.parseColor("#66bb6a") );
            }
            else{
                presence.setText("Absent");
                presence.setTextColor(Color.parseColor("#e64a19") );
            }
            Log.d("*****************++++++","+++++++++++++++"+item.getImg());
            if(!item.getImg().equals("none")){
                try {
                    File f=new File(item.getImg());
                    Bitmap b  = BitmapFactory.decodeStream(new FileInputStream(f));
                    if(null!=b)
                        imageP.setImageBitmap(b);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else imageP.setImageResource(R.drawable.profil);
            unsave.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(item.getPresent()==1){
                        item.setPresent(0);
                        item.setArrive(null);
                        MainActivity.pOps.updatePersonne(item);
                        //MainActivity.listeP.removeAll(MainActivity.listeP);
                        //MainActivity.listeP=MainActivity.pOps.getAll();
                        items.set(position,item);
                        Log.d("size","+++++++++++++++"+items.size());
                        listener.onItemClick(item);
                        MainActivity.updateListFromAdapter(items,view);
                        Log.d("+++++++++++","+++++++++++++++"+item.toString());
                    }

                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(item.getPresent()==0){
                        item.setPresent(1);
                        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                        Date currentLocalTime = cal.getTime();
                        DateFormat date = new SimpleDateFormat("HH:mm a");
                        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
                        String localTime = date.format(currentLocalTime);
                        item.setArrive(localTime);
                        MainActivity.pOps.updatePersonne(item);
                        items.set(position,item);
                        Log.d("size","+++++++++++++++"+items.size());
                        listener.onItemClick(item);

                        MainActivity.updateListFromAdapter(items,view);
                        Log.d("+++++++++++","+++++++++++++++"+item.toString());
                    }

                }
            });
        }
    }
}