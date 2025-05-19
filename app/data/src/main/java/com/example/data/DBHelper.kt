package com.example.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.appfilmes.Filme

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "FilmesDB"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "filmes"
        private const val COL_ID = "id"
        private const val COL_TITULO = "titulo"
        private const val COL_DIRETOR = "diretor"
        private const val COL_ANO = "ano"
        private const val COL_GENERO = "genero"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITULO TEXT NOT NULL,
                $COL_DIRETOR TEXT,
                $COL_ANO TEXT,
                $COL_GENERO TEXT
            )
        """
        db.execSQL(createTable)

        // Inserindo 2 registros iniciais
        insertInitialData(db)
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        val filmes = listOf(
            Filme(titulo = "O Poderoso Chefão", diretor = "Francis Ford Coppola", ano = "1972", genero = "Drama"),
            Filme(titulo = "Interestelar", diretor = "Christopher Nolan", ano = "2014", genero = "Ficção Científica")
        )
        for (filme in filmes) {
            val values = ContentValues().apply {
                put(COL_TITULO, filme.titulo)
                put(COL_DIRETOR, filme.diretor)
                put(COL_ANO, filme.ano)
                put(COL_GENERO, filme.genero)
            }
            db.insert(TABLE_NAME, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertFilme(filme: Filme): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TITULO, filme.titulo)
            put(COL_DIRETOR, filme.diretor)
            put(COL_ANO, filme.ano)
            put(COL_GENERO, filme.genero)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun updateFilme(filme: Filme): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TITULO, filme.titulo)
            put(COL_DIRETOR, filme.diretor)
            put(COL_ANO, filme.ano)
            put(COL_GENERO, filme.genero)
        }
        return db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf(filme.id.toString()))
    }

    fun deleteFilme(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
    }

    fun getAllFilmes(): List<Filme> {
        val filmes = mutableListOf<Filme>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                filmes.add(
                    Filme(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        titulo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITULO)),
                        diretor = cursor.getString(cursor.getColumnIndexOrThrow(COL_DIRETOR)),
                        ano = cursor.getString(cursor.getColumnIndexOrThrow(COL_ANO)),
                        genero = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENERO))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return filmes
    }

    fun searchFilmesByName(query: String): List<Filme> {
        val filmes = mutableListOf<Filme>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $COL_TITULO LIKE ?",
            arrayOf("%$query%")
        )
        if (cursor.moveToFirst()) {
            do {
                filmes.add(
                    Filme(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        titulo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITULO)),
                        diretor = cursor.getString(cursor.getColumnIndexOrThrow(COL_DIRETOR)),
                        ano = cursor.getString(cursor.getColumnIndexOrThrow(COL_ANO)),
                        genero = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENERO))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return filmes
    }
}
