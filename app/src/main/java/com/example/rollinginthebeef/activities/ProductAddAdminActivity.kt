package com.example.rollinginthebeef.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityProductAddAdminBinding
import com.example.rollinginthebeef.dataclass.ProductList
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.dataAPI
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductAddAdminActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityProductAddAdminBinding
    private lateinit var ImageUri : Uri
    var categoryName = ""
    var categoryId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductAddAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        binding.btnBackToAddProductPage.setOnClickListener {
            val addProductPage = Intent(this@ProductAddAdminActivity, AddProductAdminActivity::class.java)
            addProductPage.putExtra("adminData", adminData)
            startActivity(addProductPage)
        }

        binding.imgView.setOnClickListener {
            selectImage()
        }

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.category_name,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = this

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            binding.imgView.setImageURI(ImageUri)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val itemSpinner: String = parent?.getItemAtPosition(position).toString()
        categoryName = itemSpinner
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    fun addProduct(v: View){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        categoryName.forEach {
            when(categoryName){
                "Argentina Beef" -> { categoryId = 1 }
                "Argentina Tenderloin" -> { categoryId = 2 }
                "Australia Beef" -> { categoryId = 3 }
                "Japan Beef" -> { categoryId = 4 }
                "US Beef" -> { categoryId = 5 }
                "Wagyu Australia" -> { categoryId = 6 }
            }
        }

        val api: dataAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dataAPI::class.java)

        val fileName = binding.edtName.text.toString() + ".jpg"
        val storageRef = FirebaseStorage.getInstance().getReference("product/$fileName")

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading Data....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        storageRef.putFile(ImageUri).addOnSuccessListener {
            api.addProduct(
                binding.edtName.text.toString(),
                binding.edtPrice.text.toString().toInt(),
                binding.edtDetail.text.toString(),
                fileName,
                binding.edtStock.text.toString().toInt(),
                categoryId
            ).enqueue(object : Callback<ProductList> {
                override fun onResponse(call: Call<ProductList>, response: Response<ProductList>) {
                    if(response.isSuccessful){
                        val addProductMainPage = Intent(this@ProductAddAdminActivity, AddProductAdminActivity::class.java)
                        addProductMainPage.putExtra("adminData", adminData)
                        startActivity(addProductMainPage)
                    }
                }

                override fun onFailure(call: Call<ProductList>, t: Throwable) {
                    return t.printStackTrace()
                }
            })
            if(progressDialog.isShowing){progressDialog.dismiss()}
        }.addOnFailureListener{
            if(progressDialog.isShowing){progressDialog.dismiss()}
            Log.d("Exception Failure: ", "Failure Listener Image Upload")
        }
    }
}