package ani.am.e_commerce.activities

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import ani.am.e_commerce.R
import com.braintreepayments.cardform.view.CardForm
import kotlinx.android.synthetic.main.activity_card_form.*
import kotlinx.android.synthetic.main.dialog_layout.*

class CardFormActivity : AppCompatActivity() {
    private lateinit var cardForm: CardForm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_form)
        init()
    }

    private fun init() {
        setSupportActionBar(toolbar)
        title = getString(R.string.card_data)
        cardForm = findViewById(R.id.card_form)
        val buy = findViewById<Button>(R.id.btnBuy)
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true) //.mobileNumberExplanation("SMS is required on this number")
                .setup(this@CardFormActivity)
        cardForm.getCvvEditText().inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        buy.setOnClickListener { view: View? ->
            if (cardForm.isValid()) {
                showDialog()
            } else {
                Toast.makeText(this@CardFormActivity, "Please complete the form", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this, R.style.DialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout)

        val msg = "Card number: " + cardForm.cardNumber + "\n" +
                "Card expiry date: " + cardForm.expirationDateEditText.text.toString() + "\n" +
                "Card CVV: " + cardForm.cvv + "\n" +
                "Postal code: " + cardForm.postalCode + "\n" +
                "Phone number: " + cardForm.mobileNumber
        Log.d("Tag", "msg$msg")
        dialog.text_dialog.text = msg
        dialog.btn_cancel.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.btn_ok.setOnClickListener { v: View? ->
            dialog.dismiss()
            Toast.makeText(this@CardFormActivity, "Thank you for purchase", Toast.LENGTH_LONG).show()
        }
        dialog.show()
    }
}
