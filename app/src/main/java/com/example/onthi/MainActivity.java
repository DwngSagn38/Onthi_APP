package com.example.onthi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onthi.Adapter.XemayAdapter;
import com.example.onthi.Service.ApiService;
import com.example.onthi.modal.Response;
import com.example.onthi.modal.Xemay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements XemayAdapter.XeClick {

    EditText txtsearch;
    Button btnSearch;
    ImageView btnDown,btnUp;
    RecyclerView rcvSV;
    FloatingActionButton fltadd;
    XemayAdapter adapter;

    ImageView imgHinhAnh;

    List<Xemay> list = new ArrayList<>();

    private File file;

    private File createFileFormUri (Uri path, String name) {
        File _file = new File(MainActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = MainActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) >0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            Log.d("123123", "createFileFormUri: " +_file);
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getView();
        getData();

        fltadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog(new Xemay(),0);
            }
        });
    }

    public void getView(){
        btnUp = findViewById(R.id.btnUp);
        txtsearch = findViewById(R.id.txtsearch);
        btnDown = findViewById(R.id.btnDown);
        rcvSV = findViewById(R.id.rcvSV);
        fltadd = findViewById(R.id.fltadd);
    }

    public void getData() {
        Call<Response<List<Xemay>>> call = ApiService.apiservice.getData();
        call.enqueue(new Callback<Response<List<Xemay>>>() {

            @Override
            public void onResponse(Call<Response<List<Xemay>>> call, retrofit2.Response<Response<List<Xemay>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 200){
                        List<Xemay> ds = response.body().getData();
                        loadData(ds);
                        Toast.makeText(MainActivity.this, "Load data success", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<List<Xemay>>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Load data fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadData(List<Xemay> list){
        adapter = new XemayAdapter(list, this, this);
        rcvSV.setLayoutManager(new LinearLayoutManager(this));
        rcvSV.setAdapter(adapter);
    }

    public void delete(Xemay xemay){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cảnh báo");
        builder.setMessage("Bạn có muốn xóa không?");
        builder.setPositiveButton("No",null);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Response<Xemay>> call = ApiService.apiservice.deleteXe(xemay.get_id());
                call.enqueue(new Callback<Response<Xemay>>() {
                    @Override
                    public void onResponse(Call<Response<Xemay>> call, retrofit2.Response<Response<Xemay>> response) {
                        if (response.isSuccessful()){
                            if (response.body().getStatus() == 200){
                                Toast.makeText(MainActivity.this, "Delete success", Toast.LENGTH_SHORT).show();
                                getData();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<Xemay>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Delete fail", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        builder.show();
    }

    public void edit(Xemay xemay){
        opendialog(xemay,1);
    }

    private void opendialog(Xemay xe, int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add,null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText edtTenXe = view.findViewById(R.id.edtTenXe);
        EditText edtMauSac = view.findViewById(R.id.edtMauSac);
        EditText edtGiaBan = view.findViewById(R.id.edtGiaBan);
        EditText edtMoTa = view.findViewById(R.id.edtMoTa);
        imgHinhAnh = view.findViewById(R.id.imgHinhAnh);
        Button btnSave = view.findViewById(R.id.btnSave);

        imgHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        if (type == 1){
            edtTenXe.setText(xe.getTen_xe_ph42693());
            edtMauSac.setText(xe.getMau_sac_ph42693());
            edtGiaBan.setText(xe.getGia_ban_ph42693()+"");
            edtMoTa.setText(xe.getMo_ta_ph42693());
            Glide.with(imgHinhAnh)
                    .load(xe.getHinh_anh_ph42693())
                    .into(imgHinhAnh);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenxe = edtTenXe.getText().toString().trim();
                String mau = edtMauSac.getText().toString().trim();
                String gia = edtGiaBan.getText().toString().trim();
                String mota = edtMoTa.getText().toString().trim();
                if (tenxe.isEmpty()){
                    Toast.makeText(MainActivity.this, "Tên không đc để trống", Toast.LENGTH_SHORT).show();
                }else if(mau.isEmpty()){
                    Toast.makeText(MainActivity.this, "Màu không đc để trống", Toast.LENGTH_SHORT).show();
                } else if (gia.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Giá không đc để trống", Toast.LENGTH_SHORT).show();
                } else if(!gia.matches("[0-9]++")){
                    Toast.makeText(MainActivity.this, "Giá phải là số", Toast.LENGTH_SHORT).show();
                }else {
                    RequestBody _ten = RequestBody.create(MediaType.parse("multipart/form-form"), tenxe);
                    RequestBody _mau = RequestBody.create(MediaType.parse("multipart/form-form"), mau);
                    RequestBody _gia = RequestBody.create(MediaType.parse("multipart/form-form"), gia);
                    RequestBody _mota = RequestBody.create(MediaType.parse("multipart/form-form"), mota);
                    MultipartBody.Part mPart;
                    if (file != null){
                        RequestBody _hinh = RequestBody.create(MediaType.parse("image/*"),file);
                        mPart =MultipartBody.Part.createFormData("image",file.getName(),_hinh);
                    }else {
                        mPart = null;
                    }

                    Call<Response<Xemay>> call = ApiService.apiservice.addXe(_ten,_mau,_gia,_mota,mPart);
                    if(type == 1){
                        call = ApiService.apiservice.updateXe(xe.get_id(),_ten,_mau,_gia,_mota,mPart);
                    }
                    call.enqueue(new Callback<Response<Xemay>>() {
                        @Override
                        public void onResponse(Call<Response<Xemay>> call, retrofit2.Response<Response<Xemay>> response) {
                            if (response.isSuccessful()){
                                if (response.body().getStatus() == 200){
                                    Toast.makeText(MainActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                                    getData();
                                    dialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<Xemay>> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Lưu không thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void chooseImage() {
        Log.d("123123", "chooseAvatar: " +123123);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
    }
    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        Uri imageUri = data.getData();

                        Log.d("RegisterActivity", imageUri.toString());

                        Log.d("123123", "onActivityResult: "+data);

                        file = createFileFormUri(imageUri, "avatar");

                        Glide.with(imgHinhAnh)
                                .load(imageUri)
                                .centerCrop()
                                .into(imgHinhAnh);

                    }
                }
            });


}