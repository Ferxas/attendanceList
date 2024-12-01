package com.chris.subiabre.asistencia

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "asistencia.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "asistencia"
        const val COLUMN_ID = "id"
        const val COLUMN_RUT = "rut"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_APELLIDO = "apellido"
        const val COLUMN_FECHA = "fecha"
        const val COLUMN_HORA = "hora"
        const val COLUMN_TIPO = "tipo"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_RUT TEXT NOT NULL,
                $COLUMN_NOMBRE TEXT NOT NULL,
                $COLUMN_APELLIDO TEXT NOT NULL,
                $COLUMN_FECHA TEXT NOT NULL,
                $COLUMN_HORA TEXT NOT NULL,
                $COLUMN_TIPO TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertarAsistencia(rut: String, nombre: String, apellido: String, fecha: String, hora: String, tipo: String) : Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_RUT, rut)
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_APELLIDO, apellido)
            put(COLUMN_FECHA, fecha)
            put(COLUMN_HORA, hora)
            put(COLUMN_TIPO, tipo)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun obtenerAsistencias(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_FECHA DESC, $COLUMN_HORA DESC")
        var lista = mutableListOf<Map<String, String>>()

        cursor.use {
            while (it.moveToNext()) {
                val asistencia = mapOf(
                    COLUMN_RUT to it.getString(it.getColumnIndexOrThrow(COLUMN_RUT)),
                    COLUMN_NOMBRE to it.getString(it.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                    COLUMN_APELLIDO to it.getString(it.getColumnIndexOrThrow(COLUMN_APELLIDO)),
                    COLUMN_FECHA to it.getString(it.getColumnIndexOrThrow(COLUMN_FECHA)),
                    COLUMN_HORA to it.getString(it.getColumnIndexOrThrow(COLUMN_HORA)),
                    COLUMN_TIPO to it.getString(it.getColumnIndexOrThrow(COLUMN_TIPO))
                )
                lista.add(asistencia)
            }
        }
        return lista
    }


}