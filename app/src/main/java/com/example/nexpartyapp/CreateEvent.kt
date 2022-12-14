package com.example.nexpartyapp

import Database.AppDatabase
import Interface.EventDao
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import Beans.Event
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.widget.DatePicker

class CreateEvent : AppCompatActivity() {

    var txtEventName: EditText? = null
    var txtEventDate: EditText? = null
    var txtRes: TextView? = null

    var listEvents: List<Event>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        txtEventName = findViewById(R.id.txtEventName)
        txtEventDate = findViewById(R.id.txtEventDate)
        val btnCreateEvent = findViewById<Button>(R.id.btnCreateEvent)
        txtRes = findViewById(R.id.txtRes)
        val btnShowCalendar = findViewById<Button>(R.id.btnShowCalendar)
        val db:AppDatabase? = AppDatabase.getInstance(this.applicationContext)
        val dao:EventDao? = db!!.eventsDao()

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        btnShowCalendar.setOnClickListener(){
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->

                txtEventDate!!.setText(""+ mdayOfMonth +"/"+ mmonth +"/"+ myear)
            }, year, month, day)
            datePickerDialog.show()
        }

        btnCreateEvent.setOnClickListener {
            //creamos el evento
            val event = Event(txtEventName!!.text.toString(),txtEventDate!!.text.toString())

            if (dao != null){
                dao.insert(event)
            }

            txtEventName!!.setText(null)
            txtEventDate!!.setText(null)

            //mostramos solo para verificar q se cree

            listEvents = dao!!.eventList() as List<Event>?
            val total = listEvents!!.size
            txtRes!!.setText("total events: $total\n")
            for(i in listEvents!!.indices){
                val e = listEvents!![i]
                txtRes!!.append(
                    """
                        Event name: ${e!!.name}
                        Event date: ${e!!.eventDate}

                    """.trimIndent()
                )
            }


            //pasamos datos al event layout
            val intent = Intent(this, com.example.nexpartyapp.Event::class.java)
            intent.putExtra("eventName" , txtEventName!!.text.toString())
            intent.putExtra("eventDate" , txtEventDate!!.text.toString())
            startActivity(intent)

            // creamos la conexion con el event layout
            val activate = Intent(this, com.example.nexpartyapp.Event::class.java)
            startActivity(activate)

        }
    }
}