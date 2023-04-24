package com.example.sqli.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ManejadorBaseDatos {

    val nombreBaseDatos = "MisPersonajes"
    val tablaJuegos = "Juegos"
    val columnaID = "ID"
    val columnaNombreJuego = "Nombre"
    val columnaEscuadron = "Escuadron"

    val versionDB = 1

    val creacionTablaJuegos = "CREATE TABLE IF NOT EXISTS "+tablaJuegos +
            "(  " + columnaID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  " + columnaNombreJuego + " TEXT NOT NULL," +
            "  " + columnaEscuadron + " TEXT)"

    var misQuerys: SQLiteDatabase

    constructor(contexto: Context){
        val baseDatos = MiDBHelper(contexto)
         misQuerys = baseDatos.writableDatabase
    }

    inner class MiDBHelper(contexto: Context): SQLiteOpenHelper(contexto, nombreBaseDatos, null, versionDB){
        override fun onCreate(p0: SQLiteDatabase?) {
            if (p0 != null) {
                p0.execSQL(creacionTablaJuegos)
            }
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0?.execSQL("DROP TABLE IF EXISTS "+tablaJuegos)
        }
    }

    fun insertar(values: ContentValues): Long{
        return misQuerys.insert(tablaJuegos, null, values)
    }

    fun actualizar(values:ContentValues, clausulaWhere: String, argumentosWhere: Array<String>): Int{
        return misQuerys.update(tablaJuegos,values,clausulaWhere, argumentosWhere )
    }

    fun eliminar( clausulaWhere: String, argumentosWhere: Array<String>): Int {
        return misQuerys.delete(tablaJuegos, clausulaWhere, argumentosWhere)
    }
    fun seleccionar(columnasATraer: Array<String>, condiciones: String, argumentos: Array<String>, ordenarPor: String ): Cursor {
        val groupBy:String? = null
        val having:String? = null

        val cursor =  misQuerys.query(tablaJuegos, columnasATraer,condiciones,argumentos, groupBy, having, ordenarPor)
        return cursor
    }

    fun traerTodos(columnasATraer: Array<String>, ordenarPor: String ): Cursor {
        val groupBy:String? = null
        val having:String? = null
        val cursor = misQuerys.query(tablaJuegos, columnasATraer, null, null, null, null, ordenarPor)
        return cursor
    }

    fun cerrarConexion(){
        misQuerys.close()
    }
}
