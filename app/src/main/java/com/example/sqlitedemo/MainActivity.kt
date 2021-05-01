package com.example.sqlitedemo

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqlitedemo.databinding.ActivityMainBinding
import com.example.sqlitedemo.databinding.DialogUpdateBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListOfDataIntoRecyclerView()
        binding.lyInclude.btnAdd.setOnClickListener {
            addRecord(it)
        }

    }

    fun addRecord(view: View){
        val lyInclude = binding.lyInclude
        val name: String
        val email: String
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val status: Long
        lyInclude?.apply {
            name = etName.text.toString()
            email = etEmailId.text.toString()
            if(name.isNotEmpty() && email.isNotEmpty()){
                status = databaseHandler.addEmployee(EmpModelClass(0, name, email))
                if( status > -1 ){
                    Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_SHORT)
                    etName.text.clear()
                    etEmailId.text.clear()

                    setupListOfDataIntoRecyclerView()
                }
            }else{
                Toast.makeText(
                    applicationContext,
                    "Name or Email cannot be blank",
                    Toast.LENGTH_SHORT)
            }

        }

    }

    fun updateRecordDialog(empModelClass: EmpModelClass){
        val dialogBinding = DialogUpdateBinding.inflate(layoutInflater)
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(dialogBinding.root)

        dialogBinding.run {
            etUpdateName.setText(empModelClass.name)
            etUpdateEmailId.setText(empModelClass.email)

            btnUpdate.setOnClickListener {
                val name = etUpdateName.text.toString()
                val email = etUpdateEmailId.text.toString()

                val databaseHandler: DatabaseHandler = DatabaseHandler(this@MainActivity)
                if(name.isNotEmpty() && email.isNotEmpty()){
                    val status = databaseHandler.updateEmployee(EmpModelClass(empModelClass.id, name, email))
                    if(status > -1){
                        Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_SHORT).show()
                        setupListOfDataIntoRecyclerView()
                        updateDialog.dismiss()
                    }
                }else{
                    Toast.makeText(applicationContext, "Update Fail.", Toast.LENGTH_SHORT).show()
                }

            }
            btnCancel.setOnClickListener {
                updateDialog.dismiss()
            }
        }
        updateDialog.show()
    }

    fun deleteRecordAlertDialog(empModelClass: EmpModelClass){
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Delete Record")
        builder.setMessage("Are you SURE you wants to delete ${empModelClass.name}")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){ dialog, which ->
            val databaseHandler = DatabaseHandler(this@MainActivity)
            val status = databaseHandler.deleteEmployee(EmpModelClass(empModelClass.id, "",""))
            if(status > -1){
                Toast.makeText(applicationContext,
                "Record deleted successfully.",
                Toast.LENGTH_SHORT).show()
                setupListOfDataIntoRecyclerView()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){dialog, which ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setupListOfDataIntoRecyclerView() {
        val lyInclude = binding.lyInclude
        lyInclude.apply {
            if(getItemList().size > 0){
                    rvItemsList.visibility = View.VISIBLE
                    tvNoRecordsAvailable.visibility = View.GONE

                    rvItemsList.layoutManager = LinearLayoutManager(this@MainActivity)
                    val itemAdapter = ItemAdapter(this@MainActivity, getItemList())
                    rvItemsList.adapter = itemAdapter
            }else{
                rvItemsList.visibility = View.GONE
                tvNoRecordsAvailable.visibility = View.VISIBLE
            }
        }
    }

    private fun getItemList(): ArrayList<EmpModelClass> {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        return databaseHandler.viewEmployee()
    }
}