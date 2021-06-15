package dev.darshn.contacts.ui.viewcontacts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.darshn.contacts.R
import dev.darshn.contacts.adapter.ContactListAdapter
import dev.darshn.contacts.databinding.ViewContactFragmentBinding
import dev.darshn.contacts.ui.MainViewModel

@AndroidEntryPoint
class ViewContactFragment : Fragment() {


    private val viewModel: MainViewModel by viewModels()
    lateinit var binding: ViewContactFragmentBinding
    var adapter = ContactListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewContactFragmentBinding.inflate(inflater)
        initUI()
        subscribe()
        return binding.root
    }

    private fun initUI() {
        binding.rvContactList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvContactList.adapter = adapter
        binding.fbAddContact.setOnClickListener {
            findNavController().navigate(R.id.action_viewContactFragment_to_addContactFragment)
        }
    }

    private fun subscribe() {
        viewModel.userList.observe(viewLifecycleOwner, Observer {
            adapter.setContact(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.getContactList()
    }

}