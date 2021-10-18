package com.example.rollinginthebeef.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Albums.getContentUri
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityProductEditAdminBinding
import com.example.rollinginthebeef.dataclass.ProductList
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.ProductAPI
import com.example.rollinginthebeef.retrofits.dataAPI
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.reflect.typeOf

class ProductEditAdmin : AppCompatActivity(), AdapterView.OnItemSelectedListener  {

    private lateinit var binding: ActivityProductEditAdminBinding
    private lateinit var ImageUri : Uri
    var categoryName = ""
    var categoryId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductEditAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")
        var product = intent.getStringExtra("productName")
        callProductData(product.toString())

        binding.btnBackToAddProductPage.setOnClickListener {
            val addProductPage = Intent(this@ProductEditAdmin, AddProductAdminActivity::class.java)
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

        binding.editProduct.setOnClickListener {
            addProduct()
        }

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

    fun callProductData(productName: String){
        val serv : ProductAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductAPI::class.java)
        val progressDialog = ProgressDialog(this)
        serv.editProduct(
            productName
        ).enqueue(object : Callback<ProductList>{
            override fun onResponse(call: Call<ProductList>, response: Response<ProductList>) {
                if (response.isSuccessful){
                    binding.edtName.setText(response.body()?.product_name)
                    binding.edtStock.setText(response.body()?.product_qty.toString())
                    binding.edtPrice.setText(response.body()?.product_price.toString())
                    binding.edtDetail.setText(response.body()?.product_detail)
                    binding.spinner.setSelection(response.body()?.category_id.toString().toInt() - 1)
                    val storageRef = FirebaseStorage.getInstance().reference.child("product/${response.body()?.product_img}")
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        var byte: ByteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byte)
                        var path:String = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "val", null)
                        ImageUri = Uri.parse(path)
                        binding.imgView.setImageURI(ImageUri)
                    }.addOnFailureListener {
                        Log.d("Exception Failure: ", "Failure Listener Image Receive")
                    }
                }else{
                    Log.d("Exception Failure", "Response Failure")
                }
            }
            override fun onFailure(call: Call<ProductList>, t: Throwable) {
                Log.d("Exeption Failure", "Response Failure")
            }

        })
    }

    fun addProduct(){
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
            if(progressDialog.isShowing){progressDialog.dismiss()}
            api.editProduct(
                binding.edtName.text.toString(),
                binding.edtPrice.text.toString().toInt(),
                binding.edtDetail.text.toString(),
                fileName,
                binding.edtStock.text.toString().toInt(),
                categoryId
        ).enqueue(object : Callback<ProductList> {
            override fun onResponse(call: Call<ProductList>, response: Response<ProductList>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Successfully Updated", Toast.LENGTH_SHORT).show()
                    val addProductMainPage =
                        Intent(this@ProductEditAdmin, AddProductAdminActivity::class.java)
                    addProductMainPage.putExtra("adminData", adminData)
                    startActivity(addProductMainPage)
                } else {
                    Toast.makeText(applicationContext, "Update Failed", Toast.LENGTH_SHORT).show()
                }
            }
                override fun onFailure(call: Call<ProductList>, t: Throwable) {
                    return t.printStackTrace()
                }
            })
        }.addOnFailureListener{
            if(progressDialog.isShowing){progressDialog.dismiss()}
            Log.d("Exception Failure: ", "Failure Listener Image Upload")
        }
    }

}
