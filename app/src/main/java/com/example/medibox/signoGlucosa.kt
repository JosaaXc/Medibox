package com.example.medibox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.medibox.databinding.ActivitySignoGlucosaBinding
import com.example.medibox.databinding.ActivitySignoRitmocBinding

class signoGlucosa : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var binding: ActivitySignoGlucosaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignoGlucosaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ritmosGlucosa = resources.getStringArray(R.array.rg_glucosa)
        val adapter = ArrayAdapter(
            this, R.layout.list_item,
            ritmosGlucosa
        )
        with(binding.autoCompleteTextView){
            setAdapter(adapter)
            onItemClickListener = this@signoGlucosa
        }
    }
    fun btnCancelar (view: View){
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
        finish()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        Toast.makeText(this@signoGlucosa, item, Toast.LENGTH_SHORT).show()
    }
}