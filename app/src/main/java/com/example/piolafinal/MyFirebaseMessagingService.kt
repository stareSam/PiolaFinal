package com.example.piolafinal

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val Canal_ID = "Canal_ID"
const val Canal_Nombre = "Canal_Nombre"
class MyFirebaseMessagingService : FirebaseMessagingService()
{
    fun CrearNotificacion(Titulo:String, Descripcion:String)
    {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //Hace que al presionar la notificación, abra el main activity

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT) //Esto ayuda a meterlo a una notificación

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, Canal_ID)
            .setSmallIcon(R.drawable.arannapiola)                   //Ícono de la notificación
            .setAutoCancel(true)                                    //Que se vaya cuando le aprietas
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))        //Patrón de vibración
            .setOnlyAlertOnce(true)                                 //Que no aparezcan más
            .setContentIntent(pendingIntent)                        //Acción al darle

        builder = builder.setContent(getRemoteView(Titulo, Descripcion)) //Constructor de la notificación

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val CanalNotificacion = NotificationChannel(Canal_ID, Canal_Nombre,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(CanalNotificacion)
        }

        notificationManager.notify(0, builder.build()) //Acción de notificación
    }

    override fun onMessageReceived(message: RemoteMessage)
    {
        if(message.notification!= null)
            CrearNotificacion(message.notification!!.title!!, message.notification!!.body!!)
    }

    //Prepara toda la view de la notificación
    fun getRemoteView(Titulo: String, Descripcion: String) : RemoteViews
    {
        val remoteView = RemoteViews("com.example.ejm_notificacionpush", R.layout.notif)

        remoteView.setTextViewText(R.id.txt_Titulo, Titulo)
        remoteView.setTextViewText(R.id.txt_Descripcion, Descripcion)
        remoteView.setImageViewResource(R.id.img_Logo, R.drawable.arannapiola)

        return remoteView
    }
}