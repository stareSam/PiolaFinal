package com.example.piolafinal

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ActualizarFragment : Fragment()
{
    //Variables
    lateinit var libreBDHelper: BdLibreriaHelper
    lateinit var btnActualizar: Button
    lateinit var btnEliminar: Button
    lateinit var edtNombre: EditText
    lateinit var edtAutor: EditText
    lateinit var edtEditorial: EditText
    lateinit var edtEdicion: EditText
    lateinit var edtPrecio: EditText
    lateinit var spID: Spinner


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
    ): View? {
        val Vista = inflater.inflate(R.layout.fragment_actualizar, container, false)

        //variables necesarias
        btnActualizar=Vista.findViewById(R.id.btnActualizar)
        btnEliminar=Vista.findViewById(R.id.btnEliminar)
        libreBDHelper=BdLibreriaHelper(requireContext())

        //input text
        edtNombre=Vista.findViewById(R.id.edtNombre)
        edtAutor=Vista.findViewById(R.id.edtAutor)
        edtEditorial=Vista.findViewById(R.id.edtEditorial)
        edtEdicion=Vista.findViewById(R.id.edtEdicion)
        edtPrecio=Vista.findViewById(R.id.edtPrecio)
        spID=Vista.findViewById(R.id.spID)

        //lleno el spinner con las id actuales
        camposSpinner()

        //necesario para ingresar los datos de la query
        data class DatosLibro(
            var nombre: String = "",
            var autor: String = "",
            var editorial: String = "",
            var edicion: String = "",
            var precio: Int = 0
        )

        //Al darle click a un elemento en el spinner le da esos valores a los campos
        spID.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                // Obtener el valor seleccionado del Spinner
                val idNombre = parent.getItemAtPosition(position).toString()

                if (idNombre.isNotEmpty()) {
                    //Separo el id del nombre
                    val idLibro = idNombre.substringBefore(":").trim().toInt()
                    val datosLibro = DatosLibro()

                    // Realizar la busqueda de los datos con el id especificado en el spinner e ingresa los valores en el data class
                    val db: SQLiteDatabase = libreBDHelper.readableDatabase
                    val cursor = db.rawQuery("SELECT Nombre, Autor, Editorial, Edicion, Precio FROM Libreria WHERE idlibro = $idLibro",  null)
                    if (cursor.moveToFirst()) {
                        datosLibro.nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))
                        datosLibro.autor = cursor.getString(cursor.getColumnIndexOrThrow("Autor"))
                        datosLibro.editorial = cursor.getString(cursor.getColumnIndexOrThrow("Editorial"))
                        datosLibro.edicion = cursor.getString(cursor.getColumnIndexOrThrow("Edicion"))
                        datosLibro.precio = cursor.getInt(cursor.getColumnIndexOrThrow("Precio"))
                    }
                    cursor.close()

                    // Llenar los campos de texto con los datos del ldata class
                    edtNombre.setText(datosLibro.nombre)
                    edtAutor.setText(datosLibro.autor)
                    edtEditorial.setText(datosLibro.editorial)
                    edtEdicion.setText(datosLibro.edicion)
                    edtPrecio.setText(datosLibro.precio.toString())
                }
                else if (idNombre.isBlank()) {
                    edtNombre.text.clear()
                    edtAutor.text.clear()
                    edtEditorial.text.clear()
                    edtEdicion.text.clear()
                    edtPrecio.text.clear()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hace nada si no se selecciono un elemento
            }
        }

        //Al darle click al boton manda a llamar la funcion actualizar en la base de datos
        btnActualizar.setOnClickListener {
            if (spID.selectedItemPosition != AdapterView.INVALID_POSITION && edtNombre.text.isNotBlank() && edtAutor.text.isNotBlank() && edtEditorial.text.isNotBlank() && edtEdicion.text.isNotBlank() && edtPrecio.text.isNotBlank() )
            {
                //Separo el id del nombre
                val IDSeleccionado = spID.getItemAtPosition(spID.selectedItemPosition).toString()
                val idLibro = IDSeleccionado.substringBefore(":").trim()

                //mando a llamar la funcion actualizar
                libreBDHelper.actualizar(
                    idLibro.toInt(),
                    edtNombre.text.toString(),
                    edtAutor.text.toString(),
                    edtEditorial.text.toString(),
                    edtEdicion.text.toString(),
                    edtPrecio.text.toString().toInt())

                //vacio los campos
                spID.setSelection(0)
                edtNombre.text.clear()
                edtAutor.text.clear()
                edtEditorial.text.clear()
                edtEdicion.text.clear()
                edtPrecio.text.clear()

                camposSpinner() //Actualizo la informacion del spinner

                Toast.makeText(requireContext(), "Libro actualizado",Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireContext(), "No se pudo Actualizar",Toast.LENGTH_LONG).show()
            }
        }//actualizar

        //Al darle click al boton manda a llamar la funcion actualizar en la base de datos
        btnEliminar.setOnClickListener {
            if (spID.selectedItemPosition != AdapterView.INVALID_POSITION && edtNombre.text.isNotBlank() && edtAutor.text.isNotBlank() && edtEditorial.text.isNotBlank() && edtEdicion.text.isNotBlank() && edtPrecio.text.isNotBlank() )
            {
                //Separo el id del nombre
                val IDSeleccionado = spID.getItemAtPosition(spID.selectedItemPosition).toString()
                val idLibro = IDSeleccionado.substringBefore(":").trim()

                //mando a llamar la funcion actualizar
                libreBDHelper.eliminar(idLibro.toInt())

                //vacio los campos
                spID.setSelection(0)
                edtNombre.text.clear()
                edtAutor.text.clear()
                edtEditorial.text.clear()
                edtEdicion.text.clear()
                edtPrecio.text.clear()

                camposSpinner() //Actualizo la informacion del spinner

                Toast.makeText(requireContext(), "Libro eliminado",Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireContext(), "No se pudo eliminar",Toast.LENGTH_LONG).show()
            }
        }//actualizar
        return Vista
    }

    fun camposSpinner() {
        //Creo un arreglo donde voy a guardar los id y nombres de los libros
        val spinnerItems = ArrayList<String>()
        spinnerItems.add("")

        ////Busco todos los id en la base de datos con sus nombres y los meto al arreglo
        val db: SQLiteDatabase = libreBDHelper.readableDatabase
        val cursor = db.rawQuery("SELECT idlibro, Nombre FROM Libreria", null)
        if (cursor.moveToFirst()) {
            do {
                val idLibro = cursor.getInt(cursor.getColumnIndexOrThrow("idlibro"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))
                val item = "$idLibro: $nombre"
                spinnerItems.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()

        //Creo un adapter y le paso la lista de elementos
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)

        //Paso el adapter al spinner para que muestre los elementos
        spID.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ActualizarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}