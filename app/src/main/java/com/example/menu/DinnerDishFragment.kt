package com.example.menu

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class DinnerDishFragment : Fragment() {

    companion object {
        fun newInstance() = DinnerDishFragment()
    }

    private lateinit var viewModel: DinnerDishViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dinner_dish_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DinnerDishViewModel::class.java)
        // TODO: Use the ViewModel
    }

}