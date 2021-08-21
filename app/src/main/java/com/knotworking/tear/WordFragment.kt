package com.knotworking.tear

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.knotworking.tear.databinding.WordFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

class WordFragment : Fragment() {

    private val wordViewModel: WordViewModel by viewModel()
    private lateinit var wordFragmentBinding: WordFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        wordFragmentBinding = WordFragmentBinding.inflate(inflater).apply {
            viewmodel = wordViewModel
            state = wordViewModel.wordViewState.value
        }
        // Set the lifecycle owner to the lifecycle of the view
        wordFragmentBinding.lifecycleOwner = this.viewLifecycleOwner
        return wordFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        wordViewModel.requestNewWord()
    }

}