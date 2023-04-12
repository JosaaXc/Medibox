package com.example.medibox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.medibox.databinding.ActivitySignoOxigenacionBinding
import com.example.medibox.databinding.ActivitySignoRitmocBinding

class signoOxigenacion : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var binding: ActivitySignoOxigenacionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignoOxigenacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ritmosOxigenacion = resources.getStringArray(R.array.rg_oxigenacion)
        val adapter = ArrayAdapter(
            this, R.layout.list_item,
            ritmosOxigenacion
        )
        with(binding.autoCompleteTextView){
            setAdapter(adapter)
            onItemClickListener = this@signoOxigenacion
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //aqui obtenemos el elemento seleccionado y lo guardamos en item
        val item = parent?.getItemAtPosition(position).toString()
        Toast.makeText(this@signoOxigenacion, item, Toast.LENGTH_SHORT).show()
    }
}