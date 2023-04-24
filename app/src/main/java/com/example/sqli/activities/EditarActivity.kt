package com.example.sqli.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sqli.R
import com.example.sqli.db.ManejadorBaseDatos
import com.example.sqli.modelos.Personaje
import com.google.android.material.snackbar.Snackbar

class EditarActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var bnGuardar: Button
    private lateinit var etJuego: EditText
    private lateinit var spEscuadron: Spinner
    private val escuadron = arrayOf("Amanecer Dorado", "Toros negros", "Rosa azul", "Leones Carmesí", "Mantis Verde", "Pavo Real Coral","Orca Púrpura","Ciervo Aguamarino")
    private var escuadronSeleccionado: String = ""
    private lateinit var tvJuego: TextView
    var personaje: Personaje? = null
    var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar)
        getSupportActionBar()?.title = "Edición"
        getSupportActionBar()?.setHomeButtonEnabled(true);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        inicializarVistas()
        id = intent.getIntExtra("id", 0)
        buscarJuego(id)
        poblarCampos()
    }

    private fun poblarCampos() {
        etJuego.setText(personaje?.nombre)
        val position = escuadron.indexOf(personaje?.aldea)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, escuadron)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEscuadron.adapter = adapter
        spEscuadron.onItemSelectedListener = this
        if (position >= 0) {
            spEscuadron.setSelection(position)
            escuadronSeleccionado = escuadron[position]
        }
    }

    private fun inicializarVistas() {
        etJuego = findViewById(R.id.etJuego)
        bnGuardar = findViewById(R.id.bnGuardar)
        spEscuadron= findViewById(R.id.spEscuadron)
        tvJuego = findViewById(R.id.tvJuego)
        bnGuardar.setOnClickListener {
            actualizarJuego(etJuego.text.toString(), escuadronSeleccionado)
        }
    }

    val columnaNombreJuego = "Nombre"
    val columnaEscuadron = "Escuadron"

    private fun actualizarJuego(nombreJuego: String, escuadron: String) {
        if (!TextUtils.isEmpty(escuadron)) {
            val baseDatos = ManejadorBaseDatos(this)
            val contenido = ContentValues()
            contenido.put(columnaNombreJuego, nombreJuego)
            contenido.put(columnaEscuadron, escuadron)
            if ( id > 0) {
                val argumentosWhere = arrayOf(id.toString())
                val id_actualizado = baseDatos.actualizar(contenido, "id = ?", argumentosWhere)
                if (id_actualizado > 0) {
                    Snackbar.make(etJuego, "Juego actualizado", Snackbar.LENGTH_LONG).show()
                } else {
                    val alerta = AlertDialog.Builder(this)
                    alerta.setTitle("DANGER")
                        .setMessage("NO  SE PUEDE ACTUALIZAR")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar") { dialog, which ->

                        }
                        .show()
                }
            } else {
                Toast.makeText(this, "NO EXISTE ALGUN ID", Toast.LENGTH_LONG).show()
            }
            baseDatos.cerrarConexion()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("Range")
    private fun buscarJuego(idJuego: Int) {

        if (idJuego > 0) {
            val baseDatos = ManejadorBaseDatos(this)
            val columnasATraer = arrayOf("id", "nombre", "Escuadron")
            val condicion = " id = ?"
            val argumentos = arrayOf(idJuego.toString())
            val ordenarPor = "id"
            val cursor = baseDatos.seleccionar(columnasATraer, condicion, argumentos, ordenarPor)

            if (cursor.moveToFirst()) {
                do {
                    val personaje_id = cursor.getInt(cursor.getColumnIndex("ID"))
                    val nombre = cursor.getString(cursor.getColumnIndex("Nombre"))
                    val escuadron = cursor.getString(cursor.getColumnIndex("Escuadron"))
                    personaje = Personaje(personaje_id, nombre, escuadron)
                } while (cursor.moveToNext())
            }
            baseDatos.cerrarConexion()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        escuadronSeleccionado = escuadron[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}