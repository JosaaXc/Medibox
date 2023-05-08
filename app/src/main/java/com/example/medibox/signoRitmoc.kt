package com.example.medibox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.medibox.databinding.ActivitySignoRitmocBinding

class signoRitmoc : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var binding: ActivitySignoRitmocBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignoRitmocBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ritmosC = resources.getStringArray(R.array.rg_ritmoc)
        val adapter = ArrayAdapter(
            this, R.layout.list_item,
            ritmosC
        )
        with(binding.autoCompleteTextView){
            setAdapter(adapter)
            onItemClickListener = this@signoRitmoc
        }
    }
    fun btnCancelar (view: View){
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
        finish()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //aqui obtenemos el elemento seleccionado y lo guardamos en item
        val item = parent?.getItemAtPosition(position).toString()
        Toast.makeText(this@signoRitmoc, item, Toast.LENGTH_SHORT).show()
    }
}