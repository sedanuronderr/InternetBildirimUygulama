package com.info.internetbildirimuygulama

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MesajServisi :  FirebaseMessagingService() {

    override fun onMessageReceived(rm: RemoteMessage?) {
        super.onMessageReceived(rm)

        val baslik = rm?.notification?.title
        val icerik = rm?.notification?.body

        Log.e("Başlık",baslik)
        Log.e("İçerik",icerik)

        bildirimOlustur(baslik,icerik)

    }

    fun bildirimOlustur(baslik:String?,icerik:String?){

        val builder : NotificationCompat.Builder

        val bildirimYoneticisi = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this,MainActivity::class.java)

        val gidilecekIntent = PendingIntent.getActivity(this,1,intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val kanalId = "kanalId"
            val kanalAd = "kanalAd"
            val kanalTanıtım = "kanalTanıtım"
            val kanalOnceligi = NotificationManager.IMPORTANCE_HIGH

            var kanal : NotificationChannel? = bildirimYoneticisi.getNotificationChannel(kanalId)

            if(kanal == null){
                kanal = NotificationChannel(kanalId,kanalAd,kanalOnceligi)
                kanal.description = kanalTanıtım
                bildirimYoneticisi.createNotificationChannel(kanal)
            }

            builder = NotificationCompat.Builder(this,kanalId)

            builder.setContentTitle(baslik)
                .setContentText(icerik)
                .setSmallIcon(R.drawable.resim)
                .setContentIntent(gidilecekIntent)
                .setAutoCancel(true)

        }else{
            builder = NotificationCompat.Builder(this)

            builder.setContentTitle(baslik)
                .setContentText(icerik)
                .setSmallIcon(R.drawable.resim)
                .setContentIntent(gidilecekIntent)
                .setAutoCancel(true)
                .priority = Notification.PRIORITY_HIGH

        }

        bildirimYoneticisi.notify(1,builder.build())

    }

}