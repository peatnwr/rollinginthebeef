package com.example.rollinginthebeef.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rollinginthebeef.retrofits.authenticationAPI
import com.example.rollinginthebeef.activities.LoginActivity
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.FragmentProfileBinding
import com.example.rollinginthebeef.dataclass.CustomerAccounts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProfileFragment : Fragment() {

    private lateinit var profileBinding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var bundle = arguments
        var account = bundle!!.getParcelable<Account?>("accountData")
        profileBinding = FragmentProfileBinding.inflate(layoutInflater)
        profileBinding.editProfile.setOnClickListener {
            var fragment: Fragment? = null
            fragment = EditProfileFragment()
            val bundleProfile =  Bundle()
            bundleProfile.putParcelable("accountData", account)
            bundleProfile.putString("name", profileBinding.edtName.text.toString())
            bundleProfile.putString("tel", profileBinding.edtTel.text.toString())
            bundleProfile.putString("email", profileBinding.edtEmail.text.toString())
            bundleProfile.putString("address", profileBinding.edtAddress.text.toString())
            fragment.arguments = bundleProfile
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        profileBinding.btnLogout.setOnClickListener {
            var intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
            Toast.makeText(requireContext(), "Successfully Logout", Toast.LENGTH_SHORT).show()
        }
        return profileBinding.root
    }

    override fun onResume() {
        var bundle = arguments
        var account = bundle!!.getParcelable<Account?>("accountData")
        super.onResume()
        callUserData(account)
    }

    fun callUserData(account: Account?){
        val api: authenticationAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(authenticationAPI::class.java)
        api.getUserData(
            account?.username.toString()
        ).enqueue(object : Callback<CustomerAccounts>{
            override fun onResponse(
                call: Call<CustomerAccounts>,
                response: Response<CustomerAccounts>
            ) {
                if (response.isSuccessful){
                    profileBinding.textName.text = account?.username.toString()
                    profileBinding.edtName.setText(response.body()?.user_name)
                    profileBinding.edtTel.setText(response.body()?.user_tel)
                    profileBinding.edtAddress.setText(response.body()?.user_address)
                    profileBinding.edtEmail.setText(response.body()?.user_email)
                    profileBinding.edtUsername.setText(account?.username.toString())
                    profileBinding.edtUsername.isEnabled = false
                    profileBinding.edtName.isEnabled = false
                    profileBinding.edtTel.isEnabled = false
                    profileBinding.edtAddress.isEnabled = false
                    profileBinding.edtEmail.isEnabled = false
                }
            }

            override fun onFailure(call: Call<CustomerAccounts>, t: Throwable) {
                println("Failed")
            }

        })
    }
}