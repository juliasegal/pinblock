package com.julia.apd.enternumber.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.julia.apd.enternumber.R
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initVM()
    }

    private fun initVM() {
        viewModel.pinBlockEntry.observe(viewLifecycleOwner, Observer {
            pin_block.text = it
        })

        viewModel.progress.observe(viewLifecycleOwner, Observer {
            progress.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        viewModel.errorStringRes.observe(viewLifecycleOwner, Observer {
            if (it != 0) {
                Snackbar.make(main_layout, getString(it), Snackbar.LENGTH_LONG).show()
                pin_block.text = ""
            }
        })

        compute_button.setOnClickListener { viewModel.computeBlock(pin_entry.text.toString()) }
    }
}
