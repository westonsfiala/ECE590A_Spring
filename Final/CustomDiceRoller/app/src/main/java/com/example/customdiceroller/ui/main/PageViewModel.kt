package com.example.customdiceroller.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class PageViewModel : ViewModel() {

    // Temp one for the placeholder
    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }
    fun setIndex(index: Int) {
        _index.value = index
    }

    // Save data for when the screen rotates.
    private val _numDice = MutableLiveData<Int>()
    val numDice: LiveData<Int> = Transformations.map(_numDice) {
        _numDice.value
    }
    fun setNumDice(numDice: Int) {
        _numDice.value = numDice
    }

    private val _modifier = MutableLiveData<Int>()
    val modifier: LiveData<Int> = Transformations.map(_modifier) {
        _modifier.value
    }
    fun setModifier(modifier: Int) {
        _modifier.value = modifier
    }

    private val _rollHistory = MutableLiveData<MutableList<HistoryStamp>>()
    private val _singleRollHistory = MutableLiveData<HistoryStamp>()
    val singleRollHistory: LiveData<HistoryStamp> = Transformations.map(_singleRollHistory) {
        _singleRollHistory.value
    }

    fun addRollHistory(rollData: HistoryStamp)
    {
        if(_rollHistory.value == null)
        {
            _rollHistory.value = mutableListOf()
        }

        _rollHistory.value!!.add(rollData)

        _singleRollHistory.value = rollData
    }

    fun getFullRollHistory() : MutableList<HistoryStamp>
    {
        if(_rollHistory.value == null)
        {
            _rollHistory.value = mutableListOf()
        }
        return _rollHistory.value!!
    }
}