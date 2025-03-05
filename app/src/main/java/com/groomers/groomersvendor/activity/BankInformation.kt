package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.groomers.groomersvendor.databinding.ActivityBankInformationBinding

class BankInformation : AppCompatActivity() {
    private val binding by lazy { ActivityBankInformationBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnContinue.setOnClickListener {
            if (validateInputs()) {
                startActivity(Intent(this@BankInformation, Register3::class.java))
            }
        }
        binding.btnSkip.setOnClickListener {

                startActivity(Intent(this@BankInformation, Register3::class.java))

        }
    }

    private fun validateInputs(): Boolean {
        val bankName = binding.etBankName.text.toString().trim()
        val accountHolderName = binding.etAccountHolderName.text.toString().trim()
        val accountNumber = binding.etAccountNumber.text.toString().trim()
        val ifscCode = binding.etIFSC.text.toString().trim()
        val branchName = binding.etBranchName.text.toString().trim()

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

        return true
    }

}
