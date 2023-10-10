package com.example.rumahraga.ui.fragments.transaction;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentPaymentBinding;
import com.example.rumahraga.model.BookedModel;
import com.example.rumahraga.model.PaymentMethodModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.ui.adapters.booked.BookedAdapter;
import com.example.rumahraga.ui.fragments.home.HomeFragment;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;
import com.example.rumahraga.viewmodel.payment.PaymentViewModel;
import com.example.rumahraga.viewmodel.transaction.TransactionViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@AndroidEntryPoint
public class PaymentFragment extends Fragment {

    private FragmentPaymentBinding binding;
    private PaymentViewModel paymentViewModel;
    private String payment_id, paymentImage, mitraId, fieldId, userId, transactionCode;
    private int totalTransaction;
    private List<BookedModel> bookedModelList;
    private SharedPreferences sharedPreferences;
    private File file;
    private BottomSheetBehavior bottomSheetBehavior;
    private TransactionViewModel transactionViewModel;
    private Dialog dialogUplaoding;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getPaymentDetail();

        BookedAdapter bookedAdapter = new BookedAdapter(getContext(), bookedModelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvOrder.setLayoutManager(linearLayoutManager);
        binding.rvOrder.setAdapter(bookedAdapter);
        binding.btnUploadReceipt.setEnabled(false);
        binding.btnOpenBottomSheet.setEnabled(false);

        formatRupiah(binding.tvTotalPrice, totalTransaction);
        formatRupiah(binding.tvCheckOutPrice, totalTransaction);

        initBottomSheetPayment();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


    }

    private void init(){
        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        payment_id = getArguments().getString("payment_id", null);
        bookedModelList = (List<BookedModel>) getArguments().getSerializable("item_list");
        mitraId = getArguments().getString("mitra_id", null);
        fieldId = getArguments().getString("field_id", null);
        totalTransaction = getArguments().getInt("final_price", 0);
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(ConsSharedPref.USER_ID, null);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        transactionCode = getArguments().getString("transaction_code", null);




    }
    private void getPaymentDetail() {
        binding.shimmerMain.setVisibility(View.VISIBLE);
        binding.shimmerMain.startShimmer();
        binding.lrMain.setVisibility(View.GONE);
        if (payment_id != null) {
            paymentViewModel.getPaymentDetail(payment_id).observe(getViewLifecycleOwner(), new Observer<ResponseModel<PaymentMethodModel>>() {
                @Override
                public void onChanged(ResponseModel<PaymentMethodModel> paymentMethodModelResponseModel) {
                    if (paymentMethodModelResponseModel.isStatus() == true) {
                        Glide.with(getContext()).load(paymentMethodModelResponseModel.getData().getImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .skipMemoryCache(true)
                                .into(binding.ivPayment);

                        binding.tvPaymentName.setText(paymentMethodModelResponseModel.getData().getPayment_name());

                        binding.tvCredential.setText(paymentMethodModelResponseModel.getData().getCredential());
                        paymentImage = paymentMethodModelResponseModel.getData().getImage();

                        binding.btnOpenBottomSheet.setEnabled(true);

                        // hide shimmer
                        binding.shimmerMain.setVisibility(View.GONE);
                        binding.lrMain.setVisibility(View.VISIBLE);
                    }else {
                        showToast(ConsOther.TOAST_ERR, "Gagal memuat detail transaksi");
                        binding.btnOpenBottomSheet.setEnabled(false);
                    }

                }
            });
        }else {
            showToast(ConsOther.TOAST_ERR, "Gagal memuat metode pembayaran");
            binding.btnOpenBottomSheet.setEnabled(false);
        }

    }

    private void listener() {
        binding.btnBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        binding.btnClosePreview.setOnClickListener(view -> {
            binding.vOverlay.setVisibility(View.GONE);
            binding.imageView.setVisibility(View.GONE);
            binding.btnClosePreview.setVisibility(View.GONE);
        });

        binding.ivPayment.setOnClickListener(view -> {
            binding.imageView.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(paymentImage).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true)
                    .into(binding.imageView);
            binding.vOverlay.setVisibility(View.VISIBLE);
            binding.btnClosePreview.setVisibility(View.VISIBLE);
        });


        binding.ivPaymentReceipt.setOnClickListener(view -> {
            checkPermissionExternalStorage();
        });

        binding.btnOpenBottomSheet.setOnClickListener(view -> {
            showBottomSheet();
        });

        binding.btnUploadReceipt.setOnClickListener(view -> {
            transaction();
        });
    }

    private void formatRupiah(TextView textView, int nominal) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        textView.setText("Rp. "+decimalFormat.format(nominal));
    }

    private void initBottomSheetPayment() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetPayment);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomSheet();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void transaction() {
        dialogUplaoding = new Dialog(getContext());
        dialogUplaoding.setContentView(R.layout.dialog_uploading);
        dialogUplaoding.setCancelable(false);
        dialogUplaoding.setCanceledOnTouchOutside(false);
        dialogUplaoding.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final TextView tvMessage = dialogUplaoding.findViewById(R.id.tvMessage);
        tvMessage.setText("Sedang mengirim bukti pembayaran Anda");
        dialogUplaoding.show();

        if (transactionCode != null && userId != null && mitraId != null && payment_id !=  null && bookedModelList != null && bookedModelList.size() > 0) {
            HashMap map = new HashMap();
            map.put("transaction_code", RequestBody.create(MediaType.parse("text/plain"), transactionCode));
            map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), userId));
            map.put("field_id", RequestBody.create(MediaType.parse("text/palin"), fieldId));
            map.put("mitra_id", RequestBody.create(MediaType.parse("text/plain"), mitraId));
            map.put("payment_id", RequestBody.create(MediaType.parse("text/plain"), payment_id));
            map.put("total_price", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(totalTransaction)));

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("payment_receipt", file.getName(), requestBody);
            transactionViewModel.insertTransaction(map, filePart).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
                @Override
                public void onChanged(ResponseModel responseModel) {
                    if (responseModel.isStatus() == true) {
                       insertDetailTransaction();
                    }else {
                        showToast(ConsOther.TOAST_ERR, responseModel.getMessage());
                    }
                }
            });
        }else {
            showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
        }


    }

    private void insertDetailTransaction(){
        dialogUplaoding.dismiss();
            transactionViewModel.insertDetailTransaction(bookedModelList).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
                @Override
                public void onChanged(ResponseModel responseModel) {
                    if (responseModel.isStatus() == true) {
                        showSuccessDialog();
                    }else {
                        showFailedDialog(responseModel.getMessage());
                    }
                }
            });

    }

    private void hideBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay2.setVisibility(View.GONE);
    }


    private void showBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        binding.vOverlay2.setVisibility(View.VISIBLE);
    }
    private void showToast(String type, String message) {
        if (type.equals(ConsOther.TOAST_SUCCESS)) {
            Toasty.success(getContext(), message, Toasty.LENGTH_SHORT).show();
        }else if (type.equals(ConsOther.TOAST_NORMAL)){
            Toasty.normal(getContext(), message, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(getContext(), message, Toasty.LENGTH_SHORT).show();

        }
    }

    private void fragmentTransaction(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameMain, fragment)
                .commit();
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_success);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        final Button btnOke = dialog.findViewById(R.id.btnOke);
        btnOke.setText("Kembali ke home");
        tvMessage.setText("Pembayaran anda telah berhasil, mohon menunggu validasi pembayaran Anda");
        dialog.show();

        btnOke.setOnClickListener(view -> {
            fragmentTransaction(new HomeFragment());
            dialog.dismiss();
        });

    }

    private void showFailedDialog(String message) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_failed);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        final Button btnOke = dialog.findViewById(R.id.btnOke);
        btnOke.setText("oke");
        tvMessage.setText(message);
        dialog.show();

        btnOke.setOnClickListener(view -> {
            dialog.dismiss();
        });

    }
    private void checkPermissionExternalStorage() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            getContentLauncher.launch("image/*");
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showToast( ConsOther.TOAST_ERR,"Izin dibutuhkan untuk mengakses galeri");
            }
            // Meminta izin READ_EXTERNAL_STORAGE.
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }


    private ActivityResultLauncher<String> getContentLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null) {
                    Uri uri = result;
                    String pdfPath = getRealPathFromUri(uri);
                    file = new File(pdfPath);
                    binding.ivPaymentReceipt.setVisibility(View.VISIBLE);
                    binding.ivPaymentReceipt.setImageURI(uri);
                    binding.tvImageClick.setVisibility(View.GONE);
                    binding.btnUploadReceipt.setEnabled(true);


                } else {
                    showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
                    binding.btnUploadReceipt.setEnabled(false);
                }
            }
    );

    public String getRealPathFromUri(Uri uri) {
        String filePath = "";
        if (getContext().getContentResolver() != null) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                File file = new File(getContext().getCacheDir(), getFileName(uri));
                writeFile(inputStream, file);
                filePath = file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void writeFile(InputStream inputStream, File file) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }



}