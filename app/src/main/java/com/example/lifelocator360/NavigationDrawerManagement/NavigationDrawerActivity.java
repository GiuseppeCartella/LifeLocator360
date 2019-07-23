package com.example.lifelocator360.NavigationDrawerManagement;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.lifelocator360.FragmentManagement.CalendarFragment;
import com.example.lifelocator360.FragmentManagement.ContactsFragment;
import com.example.lifelocator360.FragmentManagement.InstagramFragment;
import com.example.lifelocator360.FragmentManagement.NotesFragment;
import com.example.lifelocator360.FragmentManagement.PhotoFragment;
import com.example.lifelocator360.FragmentManagement.SettingsFragment;
import com.example.lifelocator360.MapManagement.MapsFragment;
import com.example.lifelocator360.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String currentFragment = "maps";
    GoogleApiClient client;
    MapsFragment mapsFragment;

    public int getNavigationDrawerSize() {
        return navigationDrawerSize;
    }

    public void setNavigationDrawerSize() {
        this.navigationDrawerSize = navigationView.getMenu().size();
    }

    private int navigationDrawerSize;

    public void uncheckAllNavigationItems() {
        setNavigationDrawerSize();

        for (int i = 0; i < getNavigationDrawerSize(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    private void returnToMap() {
        currentFragment = "maps";
        uncheckAllNavigationItems();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home: {
                if (!currentFragment.equals("maps")) {
                    returnToMap();
                }
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
/*
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapsFragment()).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_photo:
                if (!currentFragment.equals("maps")) {
                    getSupportFragmentManager().popBackStack();
                }

                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new PhotoFragment()).addToBackStack("stack1").commit();
                currentFragment = "photo";
                break;

            case R.id.nav_calendar:
                if (!currentFragment.equals("maps")) {
                    getSupportFragmentManager().popBackStack();
                }

                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new CalendarFragment()).addToBackStack("stack1").commit();
                currentFragment = "calendar";
                break;

            case R.id.nav_contacts:
                if (!currentFragment.equals("maps")) {
                    getSupportFragmentManager().popBackStack();
                }

                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ContactsFragment()).addToBackStack("stack1").commit();
                currentFragment = "contacts";
                break;

            case R.id.nav_notes:
                if (!currentFragment.equals("maps")) {
                    getSupportFragmentManager().popBackStack();
                }

                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new NotesFragment()).addToBackStack("stack1").commit();
                currentFragment = "notes";
                break;

            case R.id.nav_instagram:
                if (!currentFragment.equals("maps")) {
                    getSupportFragmentManager().popBackStack();
                }

                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new InstagramFragment()).addToBackStack("stack1").commit();
                currentFragment = "instagram";
                break;

            case R.id.nav_settings:
                if (!currentFragment.equals("maps")) {
                    getSupportFragmentManager().popBackStack();
                }
                navigationView.getMenu().getItem(5).setChecked(true);

                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new SettingsFragment()).addToBackStack("stack1").commit();
                currentFragment = "settings";
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!currentFragment.equals("maps"))
                returnToMap();
            else
                super.onBackPressed();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ciao", "Sono nell'on activity result");
        MapsFragment m1 = new MapsFragment();

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                m1.setGPSActive(true); // flag maintain before get location

                getSupportFragmentManager().beginTransaction().detach(new MapsFragment()).attach(new MapsFragment()).commit();

                m1.getDeviceLocation();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                m1.mMap.setMyLocationEnabled(true);
            }
        }
    }
}