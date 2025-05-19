package com.example.appfilmes

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appfilmes.R
import com.example.appfilmes.data.DBHelper
import com.example.appfilmes.model.Filme

class DetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private var filmeId: Int = -1

    private lateinit var edtNome: EditText
    private lateinit var edtGenero: EditText
    private lateinit var edtAno: EditText
    private lateinit var edtDiretor: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnExcluir: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        dbHelper = DBHelper(this)

        edtNome = findViewById(R.id.edtNome)
        edtGenero = findViewById(R.id.edtGenero)
        edtAno = findViewById(R.id.edtAno)
        edtDiretor = findViewById(R.id.edtDiretor)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnExcluir = findViewById(R.id.btnExcluir)

        filmeId = intent.getIntExtra("filme_id", -1)

        if (filmeId != -1) {
            carregarDetalhes(filmeId)
        } else {
            btnExcluir.isEnabled = false
        }

        btnSalvar.setOnClickListener {
            salvarFilme()
        }

        btnExcluir.setOnClickListener {
            excluirFilme()
        }
    }

    private fun carregarDetalhes(id: Int) {
        val filme = dbHelper.buscarFilmePorId(id)
        if (filme != null) {
            edtNome.setText(filme.nome)
            edtGenero.setText(filme.genero)
            edtAno.setText(filme.ano.toString())
            edtDiretor.setText(filme.diretor)
        }
    }

    private fun salvarFilme() {
        val nome = edtNome.text.toString().trim()
        val genero = edtGenero.text.toString().trim()
        val anoStr = edtAno.text.toString().trim()
        val diretor = edtDiretor.text.toString().trim()

        if (nome.isEmpty()) {
            Toast.makeText(this, "Nome é obrigatório", Toast.LENGTH_SHORT).show()
            return
        }

        val ano = anoStr.toIntOrNull() ?: 0

        val filme = Filme(id = filmeId, nome = nome, genero = genero, ano = ano, diretor = diretor)

        val sucesso = if (filmeId == -1) {
            dbHelper.inserirFilme(filme)
        } else {
            dbHelper.atualizarFilme(filme) > 0
        }

        if (sucesso) {
            Toast.makeText(this, "Filme salvo com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Erro ao salvar filme", Toast.LENGTH_SHORT).show()
        }
    }

    private fun excluirFilme() {
        if (filmeId == -1) return

        val sucesso = dbHelper.excluirFilme(filmeId)
        if (sucesso == 1) {
            Toast.makeText(this, "Filme excluído", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Erro ao excluir filme", Toast.LENGTH_SHORT).show()
        }
    }
}
