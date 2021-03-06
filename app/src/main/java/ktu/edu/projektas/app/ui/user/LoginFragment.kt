package ktu.edu.projektas.app.ui.user

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import ktu.edu.projektas.R
import ktu.edu.projektas.databinding.FragmentLoginBinding
import androidx.appcompat.app.AppCompatActivity

// fragment class for user's login
class LoginFragment: Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private var fdb: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fdb.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.btSignup.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btLogin.setOnClickListener{
            userLogin()
        }

        return binding.root
    }

    private fun userLogin() {
        if(binding.etEmail.editText?.text.toString().trim().isEmpty()) {
            binding.etEmail.error = "Email is required!"
            binding.etEmail.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.editText?.text.toString()).matches()) {
            binding.etEmail.error = "Please provide a valid email!"
            binding.etEmail.requestFocus()
            return
        }
        if(binding.etPassword.editText?.text.toString().trim().isEmpty()) {
            binding.etPassword.error = "Password is required!"
            binding.etPassword.requestFocus()
            return
        }
        if(binding.etPassword.editText?.text.toString().length < 6){
            binding.etPassword.error = "Password is too short - it has to be at least 6 characters long!"
            binding.etPassword.requestFocus()
            return
        }

        mAuth.signInWithEmailAndPassword(binding.etEmail.editText?.text.toString(), binding.etPassword.editText?.text.toString()).addOnCompleteListener{
                task ->
            if(task.isSuccessful){
                //activity?.let { Snackbar.make(it.findViewById(R.id.drawer_layout), "User has been registered!", Snackbar.LENGTH_LONG) }
                //    ?.show()
                view?.findNavController()?.navigate(R.id.action_loginFragment_to_homeFragment)
            }
            else {
                activity?.let { Snackbar.make(it.findViewById(R.id.drawer_layout), "Failed to login, please check your credentials!", Snackbar.LENGTH_LONG) }
                    ?.show()
            }
        }
    }
}