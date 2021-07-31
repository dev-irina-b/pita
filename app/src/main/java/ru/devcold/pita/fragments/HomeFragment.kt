package ru.devcold.pita.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.devcold.pita.Order
import ru.devcold.pita.OrderAdapter
import ru.devcold.pita.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val adapter = OrderAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.orderRecycler.adapter = adapter

        getOrders()

        return binding.root
    }

    private fun getOrders() {
        lifecycleScope.launch {
            val res = Firebase.firestore.collection("orders").get().await()
            val orders = res.toObjects(Order::class.java)
            adapter.submitList(orders)
        }
    }
}