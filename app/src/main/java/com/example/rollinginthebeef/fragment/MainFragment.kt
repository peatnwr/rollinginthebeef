package com.example.rollinginthebeef.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.retrofits.ProductAPI
import com.example.rollinginthebeef.adapter.ProductAdapter
import com.example.rollinginthebeef.adapter.ProductUserAdapter
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.ProductMain
import com.example.rollinginthebeef.databinding.FragmentMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class MainFragment : Fragment() {

    private lateinit var bindingFragMain: FragmentMainBinding
    var productList = arrayListOf<ProductMain>()
    var searchList = arrayListOf<ProductMain>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingFragMain = FragmentMainBinding.inflate(layoutInflater)

        var bundle = arguments
        var account = bundle!!.getParcelable<Account?>("accountData")

        bindingFragMain.recyclerView.adapter = ProductUserAdapter(this.searchList, account, requireContext())
        bindingFragMain.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        bindingFragMain.allproduct.setOnClickListener {
            searchList.clear()
            searchList.addAll(productList)
            bindingFragMain.recyclerView.adapter!!.notifyDataSetChanged()
            bindingFragMain.recommended.text = "recommended"
        }
        bindingFragMain.c1.setOnClickListener {
            searchList.clear()
            productList.forEach {
                if (it.category_name.toLowerCase(Locale.getDefault()).contains("argentina beef")){
                    searchList.add(it)
                }
            }
            bindingFragMain.recommended.text = "Argentina Beef"
            bindingFragMain.recyclerView.adapter!!.notifyDataSetChanged()
        }
        bindingFragMain.c2.setOnClickListener {
            searchList.clear()
            productList.forEach {
                if (it.category_name.toLowerCase(Locale.getDefault()).contains("argentina tenderloin")){
                    searchList.add(it)
                }
            }
            bindingFragMain.recommended.text = "Argentina Tenderloin"
            bindingFragMain.recyclerView.adapter!!.notifyDataSetChanged()
        }
        bindingFragMain.c3.setOnClickListener {
            searchList.clear()
            productList.forEach {
                if (it.category_name.toLowerCase(Locale.getDefault()).contains("australia-beef")){
                    searchList.add(it)
                }
            }
            bindingFragMain.recommended.text = "Australia-Beef"
            bindingFragMain.recyclerView.adapter!!.notifyDataSetChanged()
        }
        bindingFragMain.c4.setOnClickListener {
            searchList.clear()
            productList.forEach {
                if (it.category_name.toLowerCase(Locale.getDefault()).contains("japan beef")){
                    searchList.add(it)
                }
            }
            bindingFragMain.recommended.text = "Japan Beef"
            bindingFragMain.recyclerView.adapter!!.notifyDataSetChanged()
        }
        bindingFragMain.c5.setOnClickListener {
            searchList.clear()
            productList.forEach {
                if (it.category_name.toLowerCase(Locale.getDefault()).contains("us beef")){
                    searchList.add(it)
                }
            }
            bindingFragMain.recommended.text = "US Beef"
            bindingFragMain.recyclerView.adapter!!.notifyDataSetChanged()
        }
        bindingFragMain.c6.setOnClickListener {
            searchList.clear()
            productList.forEach {
                if (it.category_name.toLowerCase(Locale.getDefault()).contains("wagyu australia")){
                    searchList.add(it)
                }
            }
            bindingFragMain.recommended.text = "Wagyu Australia"
            bindingFragMain.recyclerView.adapter!!.notifyDataSetChanged()
        }

        bindingFragMain.SearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchList.clear()
                val searchText = query!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    productList.forEach {
                        if (it.product_name.toLowerCase(Locale.getDefault()).contains(searchText) || it.category_name.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            searchList.add(it)
                        }
                    }
                    bindingFragMain.recommended.text = query
                    bindingFragMain.recyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    searchList.addAll(productList)
                    bindingFragMain.recyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    productList.forEach {
                        if (it.product_name.toLowerCase(Locale.getDefault()).contains(searchText) || it.category_name.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            searchList.add(it)
                        }
                    }
                    bindingFragMain.recommended.text = newText
                    bindingFragMain.recyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    searchList.addAll(productList)
                    bindingFragMain.recyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }
        })

        return bindingFragMain.root

    }

    override fun onResume() {
        super.onResume()
        var bundle = arguments
        var account = bundle!!.getParcelable<Account?>("accountData")
        callProductData(account)
    }

    fun callProductData(account: Account?){
        productList.clear()
        val serv: ProductAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductAPI::class.java)

        serv.retrieveProduct()
            .enqueue(object : Callback<List<ProductMain>>{
                override fun onResponse(call: Call<List<ProductMain>>, response: Response<List<ProductMain>>) {
                    if (response.isSuccessful){0
                        response.body()?.forEach {
                            productList.add(ProductMain(it.product_id, it.product_name, it.product_price, it.product_detail, it.product_img, it.product_category, it.product_qty,it.category_name))
                        }
                        searchList.addAll(productList)
                        bindingFragMain.recyclerView.adapter = ProductUserAdapter(searchList, account, requireContext())
                        bindingFragMain.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    }
                }

                override fun onFailure(call: Call<List<ProductMain>>, t: Throwable) {
                    return t.printStackTrace()
                }

            })
    }
}