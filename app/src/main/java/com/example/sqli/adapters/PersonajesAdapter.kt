package com.example.sqli.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sqli.R
import com.example.sqli.db.ManejadorBaseDatos
import com.example.sqli.interfaces.personajeInterface
import com.example.sqli.modelos.Personaje


class PersonajesAdapter(
    personajes: ArrayList<Personaje>,
    contexto: Context,
    personajeInterface: personajeInterface
) :
    RecyclerView.Adapter<PersonajesAdapter.ContenedorDeVista>() {
    var innerPersonajes: ArrayList<Personaje> = personajes
    var contexto: Context = contexto
    var personajeInterface = personajeInterface

    inner class ContenedorDeVista(view: View) :
        RecyclerView.ViewHolder(view) {
        val tvTitle: TextView
        val tvContent: TextView
        val img01: ImageView
        val img02: ImageView

        init {
            tvTitle = view.findViewById(R.id.etTitle)
            tvContent = view.findViewById(R.id.tvContent)
            img01 = view.findViewById(R.id.img01)
            img02 = view.findViewById(R.id.img02)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContenedorDeVista {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_juego, parent, false)

        return ContenedorDeVista(view)
    }

    override fun getItemCount(): Int {
        return innerPersonajes.size
    }

    override fun onBindViewHolder(holder: ContenedorDeVista, position: Int) {
        val personaje: Personaje = innerPersonajes.get(position)
        holder.img02.setOnClickListener {
            Log.d("aqui", "listenborrar")
            val baseDatos = ManejadorBaseDatos(contexto!!)
            val argumentosWhere = arrayOf(personaje.id.toString())
            baseDatos.eliminar("id = ? ", argumentosWhere)
            personajeInterface.personajeEliminado()
        }

        holder.img01.setOnClickListener {
            Log.d("aqui", "listenedit")
            personajeInterface.editarPersonaje(personaje)

        }
        holder.tvTitle.text = personaje.nombre
        holder.tvContent.text = personaje.escuadron
    }
}



