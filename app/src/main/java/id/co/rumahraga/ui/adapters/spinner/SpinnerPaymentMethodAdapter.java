package id.co.rumahraga.ui.adapters.spinner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import id.co.rumahraga.model.PaymentMethodModel;

import java.util.List;

public class SpinnerPaymentMethodAdapter extends ArrayAdapter<PaymentMethodModel> {
    public SpinnerPaymentMethodAdapter(@NonNull Context context, List<PaymentMethodModel> paymentMethodModel){
        super(context, android.R.layout.simple_spinner_item, paymentMethodModel);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(getItem(position).getPayment_name());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(getItem(position).getPayment_name());
        return view;
    }



    public String getPaymentId(int position) {
        return getItem(position).getPayment_id();
    }
}
