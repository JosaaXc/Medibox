package com.example.medibox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.medibox.databinding.ActivitySignoOxigenacionBinding
import com.example.medibox.databinding.ActivitySignoTemperaturaBinding

class signoTemperatura : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var binding: ActivitySignoTemperaturaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignoTemperaturaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ritmosTemperatura = resources.getStringArray(R.array.rg_temperatura)
        val adapter = ArrayAdapter(
            this, R.layout.list_item,
            ritmosTemperatura
        )
        with(binding.autoCompleteTextView){
            setAdapter(adapter)
            onItemClickListener = this@signoTemperatura
        }

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //aqui obtenemos el elemento seleccionado y lo guardamos en item
        val item = parent?.getItemAtPosition(position).toString()
        Toast.makeText(this@signoTemperatura, item, Toast.LENGTH_SHORT).show()
    }
}