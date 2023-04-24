package com.example.sqli.activities

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqli.R
import com.example.sqli.adapters.PersonajesAdapter
import com.example.sqli.db.ManejadorBaseDatos
import com.example.sqli.interfaces.personajeInterface
import com.example.sqli.modelos.Personaje
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListadoActivity : AppCompatActivity(), personajeInterface {

    private lateinit var recyclerView: RecyclerView
    private var listaDePersonajes = ArrayList<Personaje>()
    private lateinit var fab: FloatingActionButton
    private val ORDENAR_POR_NOMBRE : String  = "Nombre"
    val columnas = arrayOf("ID", "Nombre", "Escuadron" )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado)
        inicializarVistas()
        asignarEventos()
    }
    override fun onResume() {
        super.onResume()
        traerMispersonajes()
    }
    private fun inicializarVistas(){
        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)
    }

    private fun asignarEventos(){
        fab.setOnClickListener{
            val intent = Intent(this, AgregarActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_listado, menu)
        val searchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val manejador = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(manejador.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                buscarpersonaje("%" + p0 + "%")
                Toast.makeText(applicationContext, p0, Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(TextUtils.isEmpty(p0)){
                    this.onQueryTextSubmit("");
                }
                return false
            }


        })
        return super.onCreateOptionsMenu(menu)
    }


    private fun traerMispersonajes() {
        Log.d("TRAER","trae personajes")
        val baseDatos = ManejadorBaseDatos(this)
        val cursor = baseDatos.traerTodos(columnas, ORDENAR_POR_NOMBRE)
        recorrerResultados( cursor)
        baseDatos.cerrarConexion()
    }

    @SuppressLint("Range")
    private fun buscarpersonaje(nombre: String) {
        val baseDatos = ManejadorBaseDatos(this)
        val camposATraer = arrayOf(nombre)
        val cursor = baseDatos.seleccionar(columnas,"nombre like ?", camposATraer, ORDENAR_POR_NOMBRE)
        recorrerResultados( cursor)
        baseDatos.cerrarConexion()
    }

    @SuppressLint("Range")
    fun recorrerResultados(cursor : Cursor){
        if(listaDePersonajes.size > 0)
            listaDePersonajes.clear()

        if(cursor.moveToFirst()){
            do{
                val personaje_id = cursor.getInt(cursor.getColumnIndex("ID"))
                val nombre = cursor.getString(cursor.getColumnIndex("Nombre"))
                val escuadron = cursor.getString(cursor.getColumnIndex("Escuadron"))
                val personaje: Personaje
                personaje = Personaje(personaje_id, nombre, escuadron)
                listaDePersonajes.add(personaje)
            }while(cursor.moveToNext())
        }

        val linearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

        recyclerView.layoutManager = linearLayoutManager
        val adapter = PersonajesAdapter(listaDePersonajes,  this, this)
        recyclerView.adapter = adapter

    }

    override fun personajeEliminado() {
        Log.d("PRUEBAS", "personajeEliminado")
        traerMispersonajes()
    }

    override fun editarPersonaje(personaje: Personaje) {
        Log.d("PRUEBAS", "editar personaje "+personaje.id)
        val intent = Intent(this, EditarActivity::class.java)
        intent.putExtra("id",personaje.id)
        intent.putExtra("Nombre",personaje.nombre)
        intent.putExtra("Escuadron",personaje.escuadron)
        startActivity(intent)
    }


}