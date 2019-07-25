package com.example.priyomkaipt2019


import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.WorkerThread
import com.example.priyomkaipt2019.db.AppDatabase
import com.example.priyomkaipt2019.db.RecordDetails
import kotlinx.android.synthetic.main.fragment_record.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.util.*

class NewRecordFragment : Fragment() {
    val TAG: String = "newrecord-fragment"

    lateinit var nameInput1: EditText
    lateinit var nameInput2: EditText
    lateinit var nameInput3: EditText
    lateinit var phoneInput: EditText
    lateinit var tgInput: EditText
    lateinit var addressInput: EditText
    lateinit var hasTgCheckbox: CheckBox
    lateinit var sameAddressCheckbox: CheckBox

    lateinit var proceedBtn: Button
    lateinit var clearBtn: Button

    lateinit var statusBar: LinearLayout
    var prefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefs = (context as MainActivity).getSharedPreferences(TAG, 0)
        val view = inflater.inflate(R.layout.fragment_new_record, container, false)

        initViews(view)
        hasTgCheckbox.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                setEditTextReadonly(tgInput, false, InputType.TYPE_CLASS_TEXT)
                tgInput.setText("@")
                tgInput.requestFocus()
            } else {
                setEditTextReadonly(tgInput, true)
                tgInput.setText("")
            }
            prefs?.edit()?.putBoolean("hasTgCheckbox", checked)?.commit()
        }
        sameAddressCheckbox.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                setEditTextReadonly(addressInput, true)
                addressInput.setText("")
            } else {
                setEditTextReadonly(addressInput, false, InputType.TYPE_CLASS_TEXT)
                addressInput.requestFocus()
            }
            prefs?.edit()?.putBoolean("sameAddressCheckbox", checked)?.commit()
        }
        loadFields()

        proceedBtn.setOnClickListener {
            val newrecord = RecordDetails(
                0,
                nameInput1.text.toString(),
                nameInput2.text.toString(),
                nameInput3.text.toString(),
                addressInput.text.toString(),
                phoneInput.text.toString(),
                tgInput.text.toString(),
                "{\"test\": true}",
                Calendar.getInstance().time.time
            )

            disableAllInputs()
            statusBar.visibility = View.VISIBLE
            (context as MainActivity).crScope.launch {
                withContext(Dispatchers.IO) {
                    val dao = AppDatabase.getInstance(activity)?.recordDao()
                    dao?.insert(newrecord)
                }
                (context as MainActivity).runOnUiThread { statusBar.visibility = View.INVISIBLE }
                if (activity != null) {
                    Toast.makeText(activity, "Нового ФТІшника додано", Toast.LENGTH_LONG).show()
                    activity?.getSharedPreferences(TAG, 0)?.edit()?.clear()?.apply()
                    (activity as MainActivity?)?.replaceFragment(RecordsFragment())
                }
            }
        }
        clearBtn.setOnClickListener {
            prefs?.edit()?.clear()?.apply()
            loadFields();
        }

        return view
    }

    private fun initViews(view: View) {
        nameInput3 = view.findViewById(R.id.nameInput3)
        nameInput3.addTextChangedListener(FieldWatcher("nameInput3"))
        nameInput2 = view.findViewById(R.id.nameInput2)
        nameInput2.addTextChangedListener(FieldWatcher("nameInput2"))
        nameInput1 = view.findViewById(R.id.nameInput1)
        nameInput1.addTextChangedListener(FieldWatcher("nameInput1"))
        phoneInput = view.findViewById(R.id.phoneInput)
        phoneInput.addTextChangedListener(FieldWatcher("phoneInput"))
        tgInput = view.findViewById(R.id.tgInput)
        tgInput.addTextChangedListener(FieldWatcher("tgInput"))
        setEditTextReadonly(tgInput, true)
        addressInput = view.findViewById(R.id.addressInput)
        addressInput.addTextChangedListener(FieldWatcher("addressInput"))
        setEditTextReadonly(addressInput, true)

        hasTgCheckbox = view.findViewById(R.id.hasTgCheckbox)
        hasTgCheckbox.isEnabled = false
        sameAddressCheckbox = view.findViewById(R.id.sameAddressCheckbox)
        sameAddressCheckbox.isEnabled = true

        proceedBtn = view.findViewById(R.id.newrecord_proceed_btn)
        clearBtn = view.findViewById(R.id.newrecord_clear_btn)

        statusBar = view.findViewById(R.id.newrecord_status_bar)
    }

    private fun loadFields() {
        nameInput1.setText(prefs?.getString("nameInput1", "") ?: "")
        nameInput2.setText(prefs?.getString("nameInput2", "") ?: "")
        nameInput3.setText(prefs?.getString("nameInput3", "") ?: "")
        phoneInput.setText(prefs?.getString("phoneInput", "") ?: "")
        tgInput.setText(prefs?.getString("tgInput", "") ?: "")
        addressInput.setText(prefs?.getString("addressInput", "") ?: "")

        hasTgCheckbox.isChecked = prefs?.getBoolean("hasTgCheckbox", true) ?: true
        sameAddressCheckbox.isChecked = prefs?.getBoolean("sameAddressCheckbox", true) ?: true
    }

    private fun setEditTextReadonly(edittext: EditText, readonly: Boolean, type: Int = InputType.TYPE_NULL) {
        with(edittext) {
            isFocusable = !readonly
            isFocusableInTouchMode = !readonly
            inputType = type
        }
    }

    private fun disableAllInputs() {
        setEditTextReadonly(nameInput1, true)
        setEditTextReadonly(nameInput2, true)
        setEditTextReadonly(nameInput3, true)
        setEditTextReadonly(phoneInput, true)
        setEditTextReadonly(tgInput, true)
        setEditTextReadonly(addressInput, true)

        hasTgCheckbox.isEnabled = false
        sameAddressCheckbox.isEnabled = false
    }

    inner class FieldWatcher( val name: String ) : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            prefs?.edit()?.putString(name, p0.toString())?.commit()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }
}
