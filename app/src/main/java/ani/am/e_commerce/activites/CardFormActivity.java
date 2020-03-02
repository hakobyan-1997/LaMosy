package ani.am.e_commerce.activites;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;

import ani.am.e_commerce.R;

public class CardFormActivity extends AppCompatActivity {
    private CardForm cardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_form);
        cardForm = findViewById(R.id.card_form);
        Button buy = findViewById(R.id.btnBuy);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(CardFormActivity.this);

        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        buy.setOnClickListener(view -> {
            if (cardForm.isValid()) {
                showDialog();
            }else {
                Toast.makeText(CardFormActivity.this, "Please complete the form", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        String msg = "Card number: " + cardForm.getCardNumber() + "\n" +
                "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                "Card CVV: " + cardForm.getCvv() + "\n" +
                "Postal code: " + cardForm.getPostalCode() + "\n" +
                "Phone number: " + cardForm.getMobileNumber();
        text.setText(msg);

        Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());

        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(CardFormActivity.this, "Thank you for purchase", Toast.LENGTH_LONG).show();
        });
        dialog.show();
    }
}
