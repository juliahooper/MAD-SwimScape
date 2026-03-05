package com.swimscape

import com.google.firebase.FirebaseApp
import com.swimscape.data.firestore.FirestoreDataSource
import com.swimscape.data.room.SwimDatabase
import com.swimscape.repository.SwimRepository

class SwimScapeApplication : android.app.Application() {
    lateinit var repository: SwimRepository
        private set

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val db = SwimDatabase.getInstance(this)
        repository = SwimRepository(
            spotDao = db.spotDao(),
            favouriteDao = db.favouriteDao(),
            alertDao = db.alertDao(),
            firestoreDataSource = FirestoreDataSource()
        )
    }
}
