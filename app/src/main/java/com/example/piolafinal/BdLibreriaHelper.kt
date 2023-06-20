package com.example.piolafinal

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

public class BdLibreriaHelper(context: Context) : SQLiteOpenHelper(context,"Libreria.db", null, 1)
{
    //Creacion de la base de datos con el nombre ingresado
    override fun onCreate(db: SQLiteDatabase?)
    {
        val sql = "CREATE TABLE Libreria (" +
                "idlibro INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nombre TEXT,"+
                "Autor TEXT,"+
                "Editorial TEXT,"+
                "Edicion TEXT,"+
                "Precio INTEGER"+
                ")"

        db!!.execSQL(sql)
    }//onCreate

    //En caso de actualizar la version de vuelve a crear la base de datos
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val sql = "DROP TABLE IF EXISTS Libreria"
        db!!.execSQL(sql)
        onCreate(db)
    }// onUpgrade

    //funcion que ageraga lo que le mandemos a la base de datos
    fun agregar(Nombre: String, Autor: String, Editorial: String, Edicion: String, Precio: Int ) {
        val datos = ContentValues()
        datos.put("Nombre", Nombre)
        datos.put("Autor", Autor)
        datos.put("Editorial", Editorial)
        datos.put("Edicion", Edicion)
        datos.put("Precio", Precio)

        val bd = this.writableDatabase
        bd.insert("Libreria", null, datos)
        bd.close()
    }//agregar

    //funcion que actualiza lo que le mandemos a la base de datos
    fun actualizar(id: Int, Nombre: String, Autor: String, Editorial: String, Edicion: String, Precio: Int ) {
        val args = arrayOf(id.toString())

        val datos = ContentValues()
        datos.put("Nombre", Nombre)
        datos.put("Autor", Autor)
        datos.put("Editorial", Editorial)
        datos.put("Edicion", Edicion)
        datos.put("Precio", Precio)

        val db = this.writableDatabase
        db.update("Libreria", datos, "idlibro = ?", args)
        db.close()
    }//actualizar

    //funcion que elimina el id indicado en la base de datos
    fun eliminar(id: Int) : Int {
        val args = arrayOf(id.toString())

        val db = this.writableDatabase
        val delete = db.delete("Libreria", "idLibro = ?", args)
        db.close()
        return delete
    }//eliminar

    public fun Count()
    {
        val A = 1;
    }
}//class