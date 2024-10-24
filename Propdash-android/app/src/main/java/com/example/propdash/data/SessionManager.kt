package com.example.propdash.data
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.propdash.data.model.Role
import com.example.propdash.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create an extension for DataStore
val Context.dataStore by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    // Keys for storing data
    companion object {
        val ID = stringPreferencesKey("id")
        val NAME = stringPreferencesKey("name")
        val EMAIL = stringPreferencesKey("email")
        val ROLE = stringPreferencesKey("role")
        val VERIFIED = booleanPreferencesKey("verified")
        val COOKIE = stringPreferencesKey("cookie")
    }

    // Save user session
    suspend fun saveUserSession(userSession: User) {
        context.dataStore.edit { preferences ->
            preferences[ID] = userSession.id
            preferences[NAME] = userSession.name
            preferences[EMAIL] = userSession.email
            preferences[ROLE] = userSession.role.toString()
            preferences[VERIFIED] = userSession.verified
            preferences[COOKIE] = userSession.cookie
        }
    }

    // Retrieve user session
    val userSession: Flow<User?> = context.dataStore.data
        .map { preferences ->
            val id = preferences[ID] ?: return@map null
            val name = preferences[NAME] ?: return@map null
            val email = preferences[EMAIL] ?: return@map null
            val role = preferences[ROLE]?.let { Role.valueOf(it) } ?: return@map null
            val verified = preferences[VERIFIED] ?: return@map null
            val cookie = preferences[COOKIE] ?: return@map null
            User(id, name, email, role, verified, cookie)
        }

    // Clear user session (for logout)
    suspend fun clearUserSession() {
        context.dataStore.edit { it.clear() }
    }
}
