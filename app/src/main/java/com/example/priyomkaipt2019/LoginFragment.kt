package com.example.priyomkaipt2019


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

class LoginFragment : Fragment() {

    private var defaultName: String? = null
    private lateinit var nameInput: EditText
    private lateinit var tokenInput: EditText
    private lateinit var submitBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            defaultName = it.getString("defname")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false);

        nameInput = view.findViewById(R.id.op_name_input);
        tokenInput = view.findViewById(R.id.token_input);
        submitBtn = view.findViewById(R.id.login_btn)

        nameInput.setText(defaultName ?: "")

        submitBtn.setOnClickListener {
            val name = nameInput.text
            val token = tokenInput.text
            if (name != null && name.isNotBlank() && token!= null && token.isNotBlank()) {
                (context as MainActivity).toast("Logging in..")
                (context as MainActivity).login(name.toString(), token.toString())
            }
        }


        return view
    }


    companion object {
        val TAG = "auth-fragment"
        val PREF_OPNAME = "opname"
        val PREF_TOKEN = "token"

        @JvmStatic
        fun newInstance(defaultName: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString("defname", defaultName)
                }
            }
    }
}
