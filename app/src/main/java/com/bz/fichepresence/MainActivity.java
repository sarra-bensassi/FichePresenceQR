package com.bz.fichepresence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toast;
import com.bz.fichepresence.adapters.PersonneAdapter;
import com.bz.fichepresence.dbOperations.PersonOps;
import com.bz.fichepresence.entities.Personne;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    public static PersonOps pOps;
    private static RecyclerView recyclerView;
    private PersonneAdapter adapter;
    public static List<Personne> listeP,filtredList;
    private SearchView searchView;
    static HashMap hm;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.listePersonne);
        recyclerView.setHasFixedSize(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listeP = new ArrayList<>();
        pOps = new PersonOps(getApplicationContext());
        pOps.open();

        listeP =pOps.getAll();
        filtredList = new ArrayList<>();
        filtredList.add(new Personne());

        updateList(listeP);
        updateHashMap(listeP);

        searchView =(SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Personne pers = (Personne) hm.get(query);

                if(null != pers){

                    Log.d("++----+++",pers.toString());
                    filtredList.set(0,pers);
                    Log.d("+++********++", filtredList.get(0)+"");
                    updateList(filtredList);

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                listeP.removeAll(listeP);
                listeP= pOps.getAll();
                updateList(listeP);
                //filtredList.removeAll(filtredList);
                return true;

                default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        pOps.open();

    }
    @Override
    protected void onPause() {
        super.onPause();
        pOps.close();
    }
    private void updateList(List<Personne> list ){
        PersonneAdapter  adapter = new PersonneAdapter(list,recyclerView,new PersonneAdapter.OnItemClickListener() {
            @Override public void onItemClick(Personne item) {
               // Toast.makeText(getApplicationContext(), "Modification enregistrée ! ", Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //  adapter.notifyAll();
        /*synchronized(adapter){
            adapter.notify();
        }*/
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();
    }

    public static void updateListFromAdapter (List<Personne> list ,View v){
        PersonneAdapter  adapter = new PersonneAdapter(list,recyclerView,new PersonneAdapter.OnItemClickListener() {
            @Override public void onItemClick(Personne item) {
                // Toast.makeText(getApplicationContext(), "Modification enregistrée ! ", Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        activity.findViewById(R.id.listePersonne).invalidate();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //  adapter.notifyAll();
        /*synchronized(adapter){
            adapter.notify();
        }*/
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();
        updateHashMap(pOps.getAll());
    }
    private static void updateHashMap(List<Personne> list){
        hm = new HashMap<String,Personne>();
        for(Personne p: list){
            Log.d("+++++",p.toString());
            hm.put(p.getMatricule(),p);
            Log.d("++-----+++",hm.get(p.getMatricule())+"");
        }
    }
}
