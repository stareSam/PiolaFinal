package com.example.piolafinal

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AgregarFragment : Fragment()
{
    //Variables
    lateinit var libreBDHelper: BdLibreriaHelper
    lateinit var btnAgregar: Button
    lateinit var edtNombre: EditText
    lateinit var edtAutor: EditText
    lateinit var edtEditorial: EditText
    lateinit var edtEdicion: EditText
    lateinit var edtPrecio: EditText

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val Vista = inflater.inflate(R.layout.fragment_agregar, container, false)

        //le damos valores a las variables enteriormente declaradas
        btnAgregar=Vista.findViewById(R.id.btnAgregar) //boton
        libreBDHelper=BdLibreriaHelper(requireContext()) //base de datos

        //input text
        edtNombre=Vista.findViewById(R.id.edtNombre)
        edtAutor=Vista.findViewById(R.id.edtAutor)
        edtEditorial=Vista.findViewById(R.id.edtEditorial)
        edtEdicion=Vista.findViewById(R.id.edtEdicion)
        edtPrecio=Vista.findViewById(R.id.edtPrecio)

        //al darle al boton manda a llamar agregar de la base de datos
        btnAgregar.setOnClickListener {
            if (edtNombre.text.isNotBlank() && edtAutor.text.isNotBlank() && edtEditorial.text.isNotBlank() && edtEdicion.text.isNotBlank() && edtPrecio.text.isNotBlank() )
            {

                //mando a llamar la funcion agregar
                libreBDHelper.agregar(
                    edtNombre.text.toString(),
                    edtAutor.text.toString(),
                    edtEditorial.text.toString(),
                    edtEdicion.text.toString(),
                    edtPrecio.text.toString().toInt())

                //vacio los campos
                edtNombre.text.clear()
                edtAutor.text.clear()
                edtEditorial.text.clear()
                edtEdicion.text.clear()
                edtPrecio.text.clear()

                Toast.makeText(context, "Guardado correctamente", Toast.LENGTH_SHORT).show()

                //Actualizar widget
                val intent = Intent(context, WidgetPiola::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                val ids: IntArray = AppWidgetManager.getInstance(getActivity()?.getApplication())
                    .getAppWidgetIds(getActivity()?.let { it1 -> ComponentName(it1.getApplication(), WidgetPiola::class.java) })
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                getActivity()?.sendBroadcast(intent)
            }
            else
            {
                Toast.makeText(context, "Revisa los datos", Toast.LENGTH_LONG).show()
            }
        }

        return Vista
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AgregarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}