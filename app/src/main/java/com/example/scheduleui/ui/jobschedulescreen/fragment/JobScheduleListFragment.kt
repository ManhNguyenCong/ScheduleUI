package com.example.scheduleui.ui.jobschedulescreen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.scheduleui.R
import com.example.scheduleui.databinding.FragmentJobScheduleListBinding

class JobScheduleListFragment : Fragment() {

    private lateinit var binding: FragmentJobScheduleListBinding

//    private val viewModel: ScheduleViewModel by activityViewModels {
//        ScheduleViewModelFactory()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJobScheduleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar) ?: return
        toolbar.menu.clear()

        Toast.makeText(context, "This task will be released in the future ...", Toast.LENGTH_SHORT)
            .show()
    }
}