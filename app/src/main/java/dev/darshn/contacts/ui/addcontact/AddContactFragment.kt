package dev.darshn.contacts.ui.addcontact

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.darshn.contacts.R
import dev.darshn.contacts.data.model.User
import dev.darshn.contacts.databinding.AddContactFragmentBinding
import dev.darshn.contacts.ui.MainViewModel

@AndroidEntryPoint
class AddContactFragment : Fragment() {


    private val viewModel: MainViewModel by viewModels()
    lateinit var binding: AddContactFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddContactFragmentBinding.inflate(inflater)
        initUI()
        return binding.root

    }

    private fun initUI() {
        binding.btSubmit.setOnClickListener {
            viewModel.saveContact(User("salman khan", "12345"))
        }


    }


}