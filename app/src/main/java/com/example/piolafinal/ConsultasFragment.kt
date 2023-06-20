package com.example.piolafinal

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ConsultasFragment : Fragment() {
    private lateinit var radioGroupOpciones: RadioGroup
    private lateinit var edtBuscar: EditText
    private lateinit var btnConsultar: Button
    private lateinit var listView: ListView
    private lateinit var libreBDHelper: BdLibreriaHelper
    private lateinit var adapter: ArrayAdapter<String>

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
        val view = inflater.inflate(R.layout.fragment_consultas, container, false)

        //referencias a los elementos de la interfaz de usuario
        radioGroupOpciones = view.findViewById(R.id.radioGroupOpciones)
        edtBuscar = view.findViewById(R.id.edtBuscar)
        btnConsultar = view.findViewById(R.id.btnConsultar)
        listView = view.findViewById(R.id.listView)

        // instancia del helper de base de datos
        libreBDHelper = BdLibreriaHelper(requireContext())

        // listener para el botón de consulta
        btnConsultar.setOnClickListener {
            //Obtener la opción seleccionada en el RadioGroup
            val selectedRadioButtonId = radioGroupOpciones.checkedRadioButtonId
            val selectedRadioButton = view.findViewById<RadioButton>(selectedRadioButtonId)

            // Obtener el texto de búsqueda del EditText
            val searchText = edtBuscar.text.toString()
            // Obtener el tipo de búsqueda seleccionado
            val searchType = selectedRadioButton.text.toString()
            // Llamar a la función de búsqueda de libros con los parámetros correspondientes
            searchBooks(searchText, searchType)
        }
        return view
    }

    private fun searchBooks(searchText: String, searchType: String) {
        // Crear una lista mutable para almacenar los libros encontrados
        val bookList = mutableListOf<String>()

        //instancia de la base de datos legible
        val db: SQLiteDatabase = libreBDHelper.readableDatabase

        // Ejecucion de la consulta SQL según el tipo de búsqueda seleccionado
        val cursor: Cursor = when (searchType) {
            "ID" -> db.rawQuery(
                "SELECT * FROM Libreria WHERE idlibro LIKE ?",
                arrayOf("%$searchText%")
            )
            "Editorial" -> db.rawQuery(
                "SELECT * FROM Libreria WHERE Editorial LIKE ?",
                arrayOf("%$searchText%")
            )
            "Autor" -> db.rawQuery(
                "SELECT * FROM Libreria WHERE Autor LIKE ?",
                arrayOf("%$searchText%")
            )
            else -> return
        }

        // Verificar si el cursor tiene al menos una fila de resultados
        if (cursor.moveToFirst()) {
            var libroEncontrado = false
            do {
                // Extraer los datos de cada fila del cursor
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))
                val autor = cursor.getString(cursor.getColumnIndexOrThrow("Autor"))
                val editorial = cursor.getString(cursor.getColumnIndexOrThrow("Editorial"))
                val edicion = cursor.getString(cursor.getColumnIndexOrThrow("Edicion"))
                val precio = cursor.getInt(cursor.getColumnIndexOrThrow("Precio"))

                // Construir una cadena con la información del libro
                val bookInfo =
                    "Nombre: $nombre\nAutor: $autor\nEditorial: $editorial\nEdición: $edicion\nPrecio: $precio"

                // Agregar la información del libro a la listView
                bookList.add(bookInfo)
                // Marcar que se encontró al menos un libro
                libroEncontrado = true
            } while (cursor.moveToNext())

            // Cerrar el cursor
            cursor.close()

            // Crear un adaptador personalizado para el ListView
            adapter = object : ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                bookList
            ) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    // Obtener la vista del elemento de la lista
                    val view = super.getView(position, convertView, parent)
                    // Obtener la referencia al TextView dentro de la vista
                    val textView = view.findViewById<TextView>(android.R.id.text1)
                    // cambios de la apariencia del texto y color para el ListView
                    textView.setTextAppearance(android.R.style.TextAppearance_Material_Large)
                    textView.setTextColor(Color.WHITE)
                    return view
                }
            }
            // Establecer el adaptador en el ListView
            listView.adapter = adapter
        } else {
            //mensaje si no se encontraron libros para la búsqueda realizada
            val mensaje = "No se encontraron libros para la búsqueda realizada."
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConsultasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
