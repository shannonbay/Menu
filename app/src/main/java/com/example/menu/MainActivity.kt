package com.example.menu

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.menu.databinding.ActivityMainBinding
import android.widget.ExpandableListView

import android.widget.ExpandableListAdapter
import android.view.MenuItem

import android.widget.Toast

import androidx.core.view.GravityCompat
import androidx.appcompat.widget.Toolbar

import com.google.android.material.floatingactionbutton.FloatingActionButton

import android.webkit.WebView
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.ExpandableListView.OnGroupClickListener


class MainActivity() : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var expandableListAdapter: ExpandableListAdapter? = null
    internal var expandableListView: ExpandableListView? = null
    var headerList: MutableList<MenuModel> = ArrayList()
    var childList: HashMap<MenuModel, MutableList<MenuModel>> = HashMap()
    internal var adapter: ExpandableListAdapter? = null
    internal var titleList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        prepareMenuData()
        populateExpandableList()

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    // NEW STUFF

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.getItemId()
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id: Int = item.getItemId()
        if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun prepareMenuData() {
        var menuModel = MenuModel(
            "Android WebView Tutorial",
            true,
            false,
            "https://www.journaldev.com/9333/android-webview-example-tutorial"
        ) //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel)
        if (!menuModel.hasChildren) {
            childList[menuModel] = mutableListOf()
        }
        menuModel = MenuModel("Java Tutorials", true, true, "") //Menu of Java Tutorials
        headerList.add(menuModel)
        var childModelsList: MutableList<MenuModel> = ArrayList<MenuModel>()
        var childModel: MenuModel = MenuModel(
            "Core Java Tutorial",
            false,
            false,
            "https://www.journaldev.com/7153/core-java-tutorial"
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            "Java FileInputStream",
            false,
            false,
            "https://www.journaldev.com/19187/java-fileinputstream"
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            "Java FileReader",
            false,
            false,
            "https://www.journaldev.com/19115/java-filereader"
        )
        childModelsList.add(childModel)
        if (menuModel.hasChildren) {
            val d = Log.d("API123", "here")
            childList[menuModel] = childModelsList
        }
        childModelsList = ArrayList()
        menuModel = MenuModel("Python Tutorials", true, true, "") //Menu of Python Tutorials
        headerList.add(menuModel)
        childModel = MenuModel(
            "Python AST – Abstract Syntax Tree",
            false,
            false,
            "https://www.journaldev.com/19243/python-ast-abstract-syntax-tree"
        )
        childModelsList.add(childModel)
        childModel = MenuModel(
            "Python Fractions",
            false,
            false,
            "https://www.journaldev.com/19226/python-fractions"
        )
        childModelsList.add(childModel)
        if (menuModel.hasChildren) {
            childList[menuModel] = childModelsList
        }
    }

    val data: HashMap<String, List<String>>
        get() {
            val listData = HashMap<String, List<String>>()

            val meals = ArrayList<String>()
            meals.add("Breakfast")
            meals.add("Lunch")
            meals.add("Dinner")

            // set multiple list to header title position
            listData["Today"] = meals
            listData["Tomorrow"] = meals

            return listData
        }

    private fun populateExpandableList() {
        expandableListView = findViewById(R.id.expandableListView)
        if (expandableListView != null) {
            val listData = data
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableListAdapter(this, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)

            expandableListView!!.setOnGroupExpandListener { groupPosition ->
                Toast.makeText(
                    applicationContext,
                    (titleList as ArrayList<String>)[groupPosition] + " List Expanded.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            expandableListView!!.setOnGroupCollapseListener { groupPosition ->
                Toast.makeText(
                    applicationContext,
                    (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                Toast.makeText(
                    applicationContext,
                    "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(
                        childPosition
                    ),
                    Toast.LENGTH_SHORT
                ).show()
                // For get child position
                Toast.makeText(applicationContext, groupPosition.toString(), Toast.LENGTH_SHORT).show()
                false
            }

            expandableListView!!.setOnGroupClickListener { parent, v, groupPosition, id ->
                Toast.makeText(
                    applicationContext,
                    "Clicked: " + (titleList as ArrayList<String>)[groupPosition],
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                Toast.makeText(
                    applicationContext,
                    "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(
                        childPosition
                    ),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
        }
    }
}