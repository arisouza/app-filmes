package com.example.appfilmes.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.appfilmes.model.Filme


class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "filmes.db"
        private const val DATABASE_VERSION = 1

        const val TABELA_FILMES = "filmes"
        const val COL_ID = "id"
        const val COL_NOME = "nome"
        const val COL_GENERO = "genero"
        const val COL_DIRETOR = "diretor"
        const val COL_ANO = "ano"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val criarTabela = """
            CREATE TABLE $TABELA_FILMES (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOME TEXT NOT NULL,
                $COL_GENERO TEXT NOT NULL,
                $COL_DIRETOR TEXT NOT NULL,
                $COL_ANO INTEGER NOT NULL
            )
        """.trimIndent()
        db.execSQL(criarTabela)

        inserirFilmeInicial(db, "O Poderoso Chefão", "Drama", "Francis Ford Coppola", 1972)
        inserirFilmeInicial(db, "Star Wars: Uma Nova Esperança", "Ficção Científica", "George Lucas", 1977)
    }

    private fun inserirFilmeInicial(db: SQLiteDatabase, nome: String, genero: String, diretor: String, ano: Int) {
        val valores = ContentValues().apply {
            put(COL_NOME, nome)
            put(COL_GENERO, genero)
            put(COL_DIRETOR, diretor)
            put(COL_ANO, ano)
        }
        db.insert(TABELA_FILMES, null, valores)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABELA_FILMES")
        onCreate(db)
    }


    fun inserirFilme(filme: Filme): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nome", filme.nome)
            put("genero", filme.genero)
            put("ano", filme.ano)
            put("diretor", filme.diretor)
        }

        val resultado = db.insert("filmes", null, values)
        return resultado != -1L
    }

    fun atualizarFilme(filme: Filme): Int {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put(COL_NOME, filme.nome)
            put(COL_GENERO, filme.genero)
            put(COL_DIRETOR, filme.diretor)
            put(COL_ANO, filme.ano)
        }
        val linhasAfetadas = db.update(TABELA_FILMES, valores, "$COL_ID = ?", arrayOf(filme.id.toString()))
        db.close()
        return linhasAfetadas
    }

    fun excluirFilme(id: Int): Int {
        val db = writableDatabase
        val linhasExcluidas = db.delete(TABELA_FILMES, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()
        return linhasExcluidas
    }

    fun buscarFilmePorId(id: Int): Filme? {
        val db = readableDatabase
        val cursor = db.query(
            TABELA_FILMES,
            arrayOf(COL_ID, COL_NOME, COL_GENERO, COL_DIRETOR, COL_ANO),
            "$COL_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        var filme: Filme? = null
        if (cursor.moveToFirst()) {
            filme = Filme(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                nome = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME)),
                genero = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENERO)),
                diretor = cursor.getString(cursor.getColumnIndexOrThrow(COL_DIRETOR)),
                ano = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ANO))
            )
        }
        cursor.close()
        db.close()
        return filme
    }

    fun listarFilmes(): List<Filme> {
        val lista = mutableListOf<Filme>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABELA_FILMES", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
                val nome = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME))
                val genero = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENERO))
                val diretor = cursor.getString(cursor.getColumnIndexOrThrow(COL_DIRETOR))
                val ano = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ANO))

                val filme = Filme(id, nome, genero, diretor, ano)
                lista.add(filme)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return lista
    }

    fun buscarFilmesPorNome(nome: String): List<Filme> {
        val listaFilmes = mutableListOf<Filme>()
        val db = readableDatabase
        val cursor = db.query(
            TABELA_FILMES,
            arrayOf(COL_ID, COL_NOME, COL_GENERO, COL_DIRETOR, COL_ANO),
            "$COL_NOME LIKE ?",
            arrayOf("%$nome%"),
            null, null, "$COL_NOME"
        )

        if (cursor.moveToFirst()) {
            do {
                val filme = Filme(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    nome = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME)),
                    genero = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENERO)),
                    diretor = cursor.getString(cursor.getColumnIndexOrThrow(COL_DIRETOR)),
                    ano = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ANO))
                )
                listaFilmes.add(filme)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return listaFilmes
    }


}
