package com.example.appfilmes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfilmes.databinding.ActivityMainBinding
import com.example.appfilmes.data.DBHelper
import com.example.appfilmes.model.Filme
import com.example.appfilmes.DetailActivity
import com.example.appfilmes.QueryActivity
import com.example.appfilmes.adapter.FilmeAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var listaFilmes: List<Filme>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        binding.btnAdicionar.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("modo", "novo")
            startActivity(intent)
        }

        binding.btnPesquisar.setOnClickListener {
            startActivity(Intent(this, QueryActivity::class.java))
        }

        carregarFilmes()
    }

    override fun onResume() {
        super.onResume()
        carregarFilmes()
    }

    private fun carregarFilmes() {
        listaFilmes = dbHelper.listarFilmes()
        binding.recyclerViewFilmes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = FilmeAdapter(listaFilmes) { filmeSelecionado ->
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("modo", "detalhe")
                intent.putExtra("filme_id", filmeSelecionado.id)
                startActivity(intent)
            }
        }
    }
}
