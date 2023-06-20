package com.example.piolafinal

import androidx.appcompat.app.AppCompatActivity
import com.example.piolafinal.databinding.ActivityMainBinding
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity()
{

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ReemplazarFragment(ConsultasFragment())

        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId)
            {
                R.id.Consultas -> ReemplazarFragment(ConsultasFragment())
                R.id.Añadir -> ReemplazarFragment(AgregarFragment())
                R.id.Modificar -> ReemplazarFragment(ActualizarFragment())
                else ->{ReemplazarFragment(ConsultasFragment())}
            }//when
            true
        }//setOnItemSelectedListener

    }//onCreate

    private fun ReemplazarFragment(fragment : Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.FrameContainer,fragment)
        fragmentTransaction.commit()
    }//ReemplazarFragment

    //muestra la interfaz de eliminar
    interface EliminarDialogListener {
        fun onEliminarConfirmado(resultado: String)
    }

}//MainActivity