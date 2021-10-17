package com.example.rollinginthebeef.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rollinginthebeef.retrofits.authenticationAPI
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.LoginUser
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.FragmentEditProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest


class EditProfileFragment : Fragment() {

    private lateinit var editProfileBinding: FragmentEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editProfileBinding = FragmentEditProfileBinding.inflate(layoutInflater)
        var bundle = arguments
        var account = bundle!!.getParcelable<Account?>("accountData")
        editProfileBinding.textName.text = account?.username.toString()
        editProfileBinding.edtName.setText(bundle!!.getString("name"))
        editProfileBinding.edtTel.setText(bundle!!.getString("tel"))
        editProfileBinding.edtEmail.setText(bundle!!.getString("email"))
        editProfileBinding.edtAddress.setText(bundle!!.getString("address"))
        editProfileBinding.edtUsername.isEnabled = false
        editProfileBinding.btnSave.setOnClickListener {
            saveEditProfile()
        }
        return editProfileBinding.root
    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }


    fun saveEditProfile(){
        var bundle = arguments
        var account = bundle!!.getParcelable<Account?>("accountData")
        val api: authenticationAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(authenticationAPI::class.java)
        if (editProfileBinding.password.text.trim().isNotEmpty() && editProfileBinding.confirmPassword.text.trim().isNotEmpty() &&
            editProfileBinding.password.text.toString().equals(editProfileBinding.confirmPassword.text.toString())) {
            api.editUserProfile(
                account?.username.toString(),
                editProfileBinding.edtName.text.toString(),
                editProfileBinding.edtTel.text.toString(),
                editProfileBinding.edtEmail.text.toString(),
                editProfileBinding.edtAddress.text.toString(),
                md5(editProfileBinding.password.text.toString()),
                md5(editProfileBinding.confirmPassword.text.toString())
            ).enqueue(object : Callback<LoginUser>{
                override fun onResponse(
                    call: Call<LoginUser>,
                    response: Response<LoginUser>
                ) {
                    Toast.makeText(context, "Successfully Update Profile", Toast.LENGTH_SHORT).show()
                    var fragment: Fragment? = null
                    fragment = ProfileFragment()
                    val bundleEdit =  Bundle()
                    bundleEdit.putParcelable("accountData", account)
                    fragment.arguments = bundleEdit
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayout, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }

                override fun onFailure(call: Call<LoginUser>, t: Throwable) {
                    Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show()
                }

            })
        }else{
            Toast.makeText(context, "Password please !!!", Toast.LENGTH_SHORT).show()
        }

    }

}