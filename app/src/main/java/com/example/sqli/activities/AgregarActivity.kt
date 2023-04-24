package com.example.sqli.activities

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.sqli.R
import com.example.sqli.db.ManejadorBaseDatos
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class AgregarActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var fabAgregar: FloatingActionButton
    private lateinit var etJuego: EditText
    private lateinit var spEscuadron: Spinner
    private val escuadron = arrayOf(
        "Amanecer Dorado",
        "Toros negros",
        "Rosa azul",
        "Leones Carmesí",
        "Mantis Verde",
        "Pavo Real Coral",
        "Orca Púrpura",
        "Ciervo Aguamarino"
    )
    private var escuadronSeleccionado: String = ""
    private lateinit var tvJuego: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
        inicializarVistas()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, escuadron)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spEscuadron.adapter = adapter
        spEscuadron.onItemSelectedListener = this
        fabAgregar.setOnClickListener {
            insertarJuego(etJuego.text.toString(), escuadronSeleccionado)
        }
    }

    val columnaID = "id"
    val columnaNombreJuego = "Nombre"
    val columnaEscuadron = "Escuadron"
    var id: Int = 0
    private fun insertarJuego(nombreJuego: String, escuadron: String) {
        if (!TextUtils.isEmpty(escuadron)) {
            val baseDatos = ManejadorBaseDatos(this)
            val contenido = ContentValues()
            contenido.put(columnaNombreJuego, nombreJuego)
            contenido.put(columnaEscuadron, escuadron)
            id = baseDatos.insertar(contenido).toInt()
            if (id > 0) {
                Toast.makeText(this, "Juego " + nombreJuego + " agregado", Toast.LENGTH_LONG).show()
                finish()
            } else
                Toast.makeText(this, "The save isnt possible", Toast.LENGTH_LONG).show()
            baseDatos.cerrarConexion()
        } else {
            Snackbar.make(tvJuego, "Seleccione una consola", 0).show()
        }
    }

    private fun inicializarVistas() {
        etJuego = findViewById(R.id.etJuego)
        fabAgregar = findViewById(R.id.fabAgregar)
        spEscuadron = findViewById(R.id.spEscuadron)
        tvJuego = findViewById(R.id.tvJuego)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        escuadronSeleccionado = escuadron[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}