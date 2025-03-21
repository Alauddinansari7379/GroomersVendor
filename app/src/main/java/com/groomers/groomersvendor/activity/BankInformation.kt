package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.databinding.ActivityBankInformationBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.helper.Toastic
import com.groomers.groomersvendor.viewmodel.AddBankViewModel
import com.groomers.groomersvendor.viewmodel.GetVendorViewModel
import com.groomers.groomersvendor.viewmodel.MyApplication
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankInformation : AppCompatActivity() {
    private val binding by lazy { ActivityBankInformationBinding.inflate(layoutInflater) }
    private val viewModel: AddBankViewModel by viewModels()
    private var isFromFinance: Boolean = false
    private val viewModelRegister by lazy {
        (application as MyApplication).registerViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        isFromFinance = intent.getBooleanExtra("IS_FROM_FINANCE", false)
        if (isFromFinance){
            binding.progressBar.visibility = View.GONE
            binding.btnSkip.visibility = View.GONE
        }
        binding.btnContinue.setOnClickListener {
            if (validateInputs()) {
                startActivity(Intent(this@BankInformation, Register3::class.java))
            }
        }
        binding.btnSkip.setOnClickListener {

                startActivity(Intent(this@BankInformation, Register3::class.java))

        }
        observer()
    }

    private fun observer() {

        // Observe isLoading to show/hide progress
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(this)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        }

        // Observe the result of the login attempt
        viewModel.modelAddBank.observe(this) { modelLogin ->
            modelLogin?.let {
                if (it.status == 1){
                    Toastic.toastic(
                        context = this@BankInformation,
                        message = "Bank details added successful",
                        duration = Toastic.LENGTH_SHORT,
                        type = Toastic.SUCCESS,
                        isIconAnimated = true,
                        textColor = if (false) Color.BLUE else null,
                    ).show()
                    finish()
                }
            }
        }

        // Observe error message if login fails
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                if (errorMessage.isNotEmpty()) {
                    Toastic.toastic(
                        context = this@BankInformation,
                        message = errorMessage,
                        duration = Toastic.LENGTH_SHORT,
                        type = Toastic.ERROR,
                        isIconAnimated = true,
                        textColor = if (false) Color.BLUE else null,
                    ).show()

                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val bankName = binding.etBankName.text.toString().trim()
        val accountHolderName = binding.etAccountHolderName.text.toString().trim()
        val accountNumber = binding.etAccountNumber.text.toString().trim()
        val ifscCode = binding.etIFSC.text.toString().trim()
        val branchName = binding.etBranchName.text.toString().trim()
        viewModelRegister.accountName = accountHolderName
        viewModelRegister.accountNo = accountNumber
        viewModelRegister.bankName = bankName
        viewModelRegister.ifsc = ifscCode
        viewModelRegister.branchName = branchName

        // Regex pattern for IFSC Code (4 letters + 7 numbers)
        val ifscPattern = Regex("^[A-Z]{4}0[A-Z0-9]{6}$")

        if (bankName.isEmpty()) {
            binding.etBankName.error = "Bank name cannot be empty"
            binding.etBankName.requestFocus()
            return false
        }

        if (accountHolderName.isEmpty()) {
            binding.etAccountHolderName.error = "Account holder name cannot be empty"
            binding.etAccountHolderName.requestFocus()
            return false
        }

        if (accountNumber.isEmpty()) {
            binding.etAccountNumber.error = "Account number cannot be empty"
            binding.etAccountNumber.requestFocus()
            return false
        } else if (accountNumber.length < 8 || accountNumber.length > 18) {
            binding.etAccountNumber.error = "Enter a valid account number (8-18 digits)"
            binding.etAccountNumber.requestFocus()
            return false
        }

        if (ifscCode.isEmpty()) {
            binding.etIFSC.error = "IFSC code cannot be empty"
            binding.etIFSC.requestFocus()
            return false
        }/* else if (!ifscPattern.matches(ifscCode)) {
            binding.etIFSC.error = "Enter a valid IFSC code (e.g., SBIN0001234)"
            binding.etIFSC.requestFocus()
            return false
        }*/

        if (branchName.isEmpty()) {
            binding.etBranchName.error = "Branch name cannot be empty"
            binding.etBranchName.requestFocus()
            return false
        }
        if (isFromFinance) {
            viewModel.addBank(accountHolderName, accountNumber, bankName, ifscCode, branchName)
            return false
        }
        return true

    }

}
