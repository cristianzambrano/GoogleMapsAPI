package uteq.solutions.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity
                                implements OnMapReadyCallback,
                                           GoogleMap.OnMapClickListener{

    GoogleMap mapa;
    int tipoVista;
    LatLng[] puntos = new LatLng[4];
    int cpuntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tipoVista=1;
        cpuntos=0;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        mapa.getUiSettings().setZoomControlsEnabled(true);

        mapa.setOnMapClickListener(this);

    }

    public void ConfigurarMapa(View v){

        mapa.setMapType(tipoVista);
        tipoVista = tipoVista<4?tipoVista+1:1;

        /*CameraUpdate camUpd1 =
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(40.41, -3.69), 10);
 
         mapa.moveCamera(camUpd1);*/
        LatLng uteq = new LatLng(-1.0128684338088096, -79.46930575553893);

        CameraPosition camPos = new CameraPosition.Builder()
                .target(uteq)
                .zoom(19)
                .bearing(45)      //noreste arriba
                .tilt(70)         //punto de vista de la cámara 70 grados
                .build();

        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);

        mapa.animateCamera(camUpd3);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Projection proj = mapa.getProjection();
        Point coord = proj.toScreenLocation(latLng);

        Toast.makeText(
                MainActivity.this,
                "Click\n" +
                        "Lat: " + latLng.latitude + "\n" +
                        "Lng: " + latLng.longitude + "\n" +
                        "X: " + coord.x + " - Y: " + coord.y,
                Toast.LENGTH_SHORT).show();


        puntos[cpuntos] = new LatLng(latLng.latitude,latLng.longitude);
        mapa.addMarker(new
                MarkerOptions().position(puntos[cpuntos])
                .title("Punto " + (cpuntos + 1))
                .snippet("Nombre del Lugar"));
        cpuntos++;

        if(cpuntos==4){
             PolylineOptions lineas = new PolylineOptions()
               .add(puntos[0])
               .add(puntos[1])
               .add(puntos[2])
               .add(puntos[3])
               .add(puntos[0]);
            lineas.width(8);
            lineas.color(Color.RED);
            mapa.addPolyline(lineas);
            cpuntos=0;
        }

    }
}