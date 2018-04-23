package com.bz.fichepresence;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bz.fichepresence.adapters.PersonneAdapter;
import com.bz.fichepresence.dbOperations.PersonOps;
import com.bz.fichepresence.entities.Personne;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

public class HomeActivity extends AppCompatActivity {

    private IntentIntegrator qrScan;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static PersonOps pOps;
    Date currentLocalTime;
    public static int code = 0;
    Calendar cal;
    static int totalPresence;
    TextView finish;
    DateFormat d;
    private static RecyclerView recyclerView;
    private PersonneAdapter adapter;
    public static List<Personne> listeP, filtredList;
    private SearchView searchView;
    TextView nom, matricule, presence, date, nbP;
    ImageView imageP, logo, qrSscan;
    CardView cardView;
    Personne pers;
    LinearLayout ll;
    ImageView save, unsave;
    static HashMap hm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        qrScan = new IntentIntegrator(this);
        qrScan.setCameraId(1);
        pOps = new PersonOps(getApplicationContext());
        pOps.open();
        cardView = (CardView) findViewById(R.id.cardView);
        logo = (ImageView) findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (code == 6) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    code = 0;
                } else code++;
            }
        });
        cardView.setVisibility(View.GONE);
        presence = (TextView) findViewById(R.id.presence);
        nbP = (TextView) findViewById(R.id.nbP);
        finish = (TextView) findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.GONE
                );
            }
        });
        calculPresence();
        nom = (TextView) findViewById(R.id.nom);
        matricule = (TextView) findViewById(R.id.matricule);
        imageP = (ImageView) findViewById(R.id.profil);
        save = (ImageView) findViewById(R.id.save);
        unsave = (ImageView) findViewById(R.id.unsave);
        date = (TextView) findViewById(R.id.date);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dd = new Date();
        date.setText(dateFormat.format(dd));
        ll = (LinearLayout) findViewById(R.id.ll);
        qrSscan = (ImageView) findViewById(R.id.qrScan);
        qrSscan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });
        listeP = new ArrayList<>();

      /*try {
            read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        updateHashMap(pOps.getAll());
        filtredList = new ArrayList<>();
        filtredList.add(new Personne());
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Matricule du participent");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                pers = (Personne) hm.get(query);

                if (null != pers) {
                    cardView.setVisibility(View.VISIBLE);
                    setP(pers);

                } else cardView.setVisibility(View.GONE);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        unsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pers.getPresent() == 1) {
                    pers.setPresent(0);
                    pers.setArrive(null);

                    pOps.updatePersonne(pers);
                    setP(pers);
                    updateHashMap(pOps.getAll());
                    calculPresence();
                }

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pers.getPresent() == 0) {
                    pers.setPresent(1);
                    cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                    currentLocalTime = cal.getTime();
                    d = new SimpleDateFormat("HH:mm a");
                    d.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
                    String localTime = d.format(currentLocalTime);
                    pers.setArrive(localTime);
                    pOps.updatePersonne(pers);
                    setP(pers);
                    updateHashMap(pOps.getAll());
                    calculPresence();
                }

            }
        });
    }

    public void read() throws FileNotFoundException {
        String fpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/listeParticipent.txt";
        ;
        File file = new File(fpath);
        Scanner scanner = new Scanner(new FileInputStream(file));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] details = line.split(",");
            String pic = details[0].trim();
            String matricule = details[1].trim();
            String nom = details[2].trim();
//            String  fonction = details[3].trim();
            Personne p = new Personne(nom, matricule);
            if (!pic.equals("---"))
                p.setImg(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + pic);
            else p.setImg("none");
            System.out.println(p.toString());
            listeP.add(p);
            pOps.addpref(p);
        }
        for (Personne p : listeP) {
            System.out.println(p.toString());
        }
        scanner.close();
    }

    private static void updateHashMap(List<Personne> list) {
        hm = new HashMap<String, Personne>();
        for (Personne p : list) {
            Log.d("+++++", p.toString());
            hm.put(p.getMatricule(), p);
            Log.d("++-----+++", hm.get(p.getMatricule()) + "");
        }
    }

    public void setP(Personne pers) {
        Log.d("++----+++", pers.toString());
        nom.setText(pers.getNom());
        matricule.setText(pers.getMatricule());
        if (pers.getPresent() == 1) {

            presence.setText("Présent à " + pers.getArrive());
            presence.setTextColor(Color.parseColor("#66bb6a"));
        } else {
            presence.setText("Absent");
            presence.setTextColor(Color.parseColor("#e64a19"));
        }
        Log.d("*****************++++++", "+++++++++++++++" + pers.getImg());
        if (!pers.getImg().equals("none")) {
            try {
                File f = new File(pers.getImg());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                if (null != b)
                    imageP.setImageBitmap(b);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else imageP.setImageResource(R.drawable.profil);
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

    private void calculPresence() {
        List<Personne> l = new ArrayList<>();
        l = pOps.getAll();
        int t = 0;
        for (Personne p : l) {
            if (p.getPresent() == 1) {
                t++;
            }
        }
        nbP.setText(t + "/" + l.size());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    date.setText(obj.getString("name"));
                  //  textViewAddress.setText(obj.getString("address"));
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}