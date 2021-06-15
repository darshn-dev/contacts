package dev.darshn.contacts.ui

import android.accounts.AccountManager
import android.content.ContentProviderOperation
import android.content.OperationApplicationException
import android.provider.ContactsContract

import android.provider.ContactsContract.RawContacts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.darshn.contacts.data.model.User
import dev.darshn.contacts.data.repository.ContactRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val contactRepository: ContactRepository) :
    ViewModel() {

    var userList = MutableLiveData<ArrayList<User>>()

    fun getContactList() {
        viewModelScope.launch {
            userList.postValue(contactRepository.extractContactList())
        }
    }

    fun saveContact(user: User) {
        viewModelScope.launch {
            contactRepository.saveContact(user)
        }
    }
}