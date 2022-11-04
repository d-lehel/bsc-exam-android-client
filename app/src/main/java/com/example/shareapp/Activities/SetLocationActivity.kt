package com.example.shareapp.Activities

//import com.niels_ole.customtileserver.R

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.shareapp.APIClient
import com.example.shareapp.APIService
import com.example.shareapp.Helpers.Token
import com.example.shareapp.R
import com.example.shareapp.ResponseModels.Message
import com.example.shareapp.ResponseModels.Register
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_set_location.*
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class SetLocationActivity : AppCompatActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private lateinit var map : MapView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        //handle permissions first, before map is created. not depicted here
        //load/initialize the osmdroid configuration, this can be done
        // This won't work unless you have imported this: org.osmdroid.config.Configuration.*
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, if you abuse osm's
        //tile servers will get you banned based on this string.

        //inflate and create the map
        setContentView(R.layout.activity_set_location)

        val token = Token.getAccessToken(this)

        map = findViewById(R.id.map_view)
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        map.isHorizontalMapRepetitionEnabled = false
        map.isVerticalMapRepetitionEnabled = false

        val mapController = map.controller
       mapController.setZoom(2)


        val mlocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map);
        mlocationOverlay.enableMyLocation()
        map.overlays.add(mlocationOverlay)
        mapController.animateTo(mlocationOverlay.myLocation);

        set_home.setOnClickListener{
            val latitude: Double = mlocationOverlay.myLocation.latitude
            val longitude: Double = mlocationOverlay.myLocation.longitude

            val apiInterface = APIClient().getClient()?.create(APIService::class.java)
            apiInterface!!.setCoordinates("Bearer " + token, latitude, longitude).enqueue(
                object : Callback<Message> {
                    override fun onResponse(call: Call<Message>, response: Response<Message>) {
                        if(response.isSuccessful) {
                                Toast.makeText(applicationContext, "Tartozkodási hely sikeresen frissítve!", Toast.LENGTH_LONG).show()
                        } else { //todo hibakzeles specifikus
                            Toast.makeText(applicationContext, "Szerver hiba, kérem vegye fel a kapcsolatot a fejlesztővel!", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<Message>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                    }
                })
        }
    }

    override fun onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsToRequest = ArrayList<String>();
        var i = 0;
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i]);
            i++;
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, ProfileActivity::class.java))
    }
}