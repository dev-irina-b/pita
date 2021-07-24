package ru.devcold.pita.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.devcold.pita.databinding.FragmentMessagesBinding

class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)


        return binding.root
    }

}