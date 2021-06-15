package dev.darshn.contacts.data.repository

import android.Manifest.permission.READ_CONTACTS
import android.Manifest.permission.WRITE_CONTACTS
import android.accounts.AccountManager
import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.Context
import android.content.OperationApplicationException
import android.content.pm.PackageManager
import android.os.Build
import android.os.RemoteException
import android.provider.ContactsContract
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

import dev.darshn.contacts.data.model.User
import javax.inject.Inject

class ContactRepository @Inject constructor(
    var context: Context
) {


    @RequiresApi(Build.VERSION_CODES.O)
    public suspend fun extractContactList(): ArrayList<User> {
        var contactList = arrayListOf<User>()
        var phoneNumValue = ""
        var contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null
        )

        if (cursor?.count!! > 0) {
            while (cursor?.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                //check if number exists
                if (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        .toInt() > 0
                ) {
                    var curPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        arrayOf(id),
                        null
                    )
                    if (curPhone?.count!! > 0) {
                        while (curPhone?.moveToNext()) {
                            phoneNumValue =
                                curPhone.getString(curPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            break; //ending loop with one phone number
                        }
                    }

                    curPhone?.close()
                }
                val user = User(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                    phoneNumValue
                )
                contactList.add(user)
            }
        }
        //    var userList = MutableLiveData<ArrayList<User>>()
        //  userList.value= contactList
        cursor?.close()
        return contactList
    }


    public suspend fun saveContact(user: User) {
        val ops = ArrayList<ContentProviderOperation>()
        val rawContactInsertIndex: Int = ops.size


        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(
                    ContactsContract.RawContacts.ACCOUNT_TYPE,
                    AccountManager.KEY_ACCOUNT_TYPE
                )
                .withValue(
                    ContactsContract.RawContacts.ACCOUNT_NAME,
                    AccountManager.KEY_ACCOUNT_NAME
                )
                .build()
        )

        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, user.name)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.IN_VISIBLE_GROUP, true)
                .build()
        )

        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, user.number)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, "4343")
                .build()
        )

        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, "")
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, "")
                .build()
        )

        //Log.i("Line43", Data.CONTENT_URI.toString()+" - "+rawContactInsertIndex);


        //Log.i("Line43", Data.CONTENT_URI.toString()+" - "+rawContactInsertIndex);
        try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    WRITE_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            }
        } catch (e: RemoteException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: OperationApplicationException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }
}