package dev.darshn.contacts.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.darshn.contacts.adapter.ContactListAdapter.ContactViewHolder
import dev.darshn.contacts.data.model.User
import dev.darshn.contacts.databinding.ContactViewBinding

class ContactListAdapter : RecyclerView.Adapter<ContactViewHolder>() {

    private var contactList = arrayListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = parent.context.getSystemService(LayoutInflater::class.java)
        val binding = ContactViewBinding.inflate(inflater, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.binding.tvUserName.text = contactList[position].name
        holder.binding.tvUserPhone.text = contactList[position].number
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    public fun setContact(list: ArrayList<User>) {
        contactList = list
    }

    class ContactViewHolder(val binding: ContactViewBinding) : RecyclerView.ViewHolder(binding.root)


}