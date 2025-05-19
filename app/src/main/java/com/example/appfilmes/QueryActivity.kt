package com.example.appfilmes

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appfilmes.data.DBHelper

class QueryActivity : AppCompatActivity() {

    private lateinit var edtBuscar: EditText
    private lateinit var btnBuscar: Button
    private lateinit var txtResultados: TextView

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query)

        edtBuscar = findViewById(R.id.edtBuscar)
        btnBuscar = findViewById(R.id.btnBuscar)
        txtResultados = findViewById(R.id.txtResultados)

        dbHelper = DBHelper(this)

        btnBuscar.setOnClickListener {
            val termo = edtBuscar.text.toString().trim()
            if (termo.isNotEmpty()) {
                val resultados = dbHelper.buscarFilmesPorNome(termo)
                if (resultados.isEmpty()) {
                    txtResultados.text = "Nenhum filme encontrado"
                } else {
                    txtResultados.text = resultados.joinToString("\n") { "${it.nome} (${it.ano}) - ${it.genero}" }
                }
            } else {
                txtResultados.text = "Digite algo para buscar"
            }
        }
    }
}
