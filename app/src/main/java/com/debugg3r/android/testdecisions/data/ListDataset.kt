package com.debugg3r.android.testdecisions.data

interface ListDataset {
    fun getCount(): Int
    fun addElement(): Int
    fun deleteElement(position: Int): Int
    fun getElement(position: Int): TextItem
    fun newItem(text: String): TextItem
    fun changeText(position: Int)
}