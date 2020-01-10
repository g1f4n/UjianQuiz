package com.juaracoding.myquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.juaracoding.myquiz.quizmodel.QuizModel;
import com.juaracoding.myquiz.quizmodel.SoalQuizAndroid;
import com.juaracoding.myquiz.service.APIClient;
import com.juaracoding.myquiz.service.APIInterfacesRest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button rbA, rbB, rbC, rbD;
    TextView txtSoal, txtSkor;
    ImageView imgView;
    RadioGroup radiogroup;

    int array;
    int x = 0;
    //String hasil;
    ArrayList<String> hasil = new ArrayList<>();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rbA = findViewById(R.id.rbA);
        rbB = findViewById(R.id.rbB);
        rbC = findViewById(R.id.rbC);
        rbD = findViewById(R.id.rbD);
        txtSkor = findViewById(R.id.txtSkor);
        txtSoal = findViewById(R.id.txtSoal);
        imgView = findViewById(R.id.imgView);
        radiogroup = findViewById(R.id.radiogroup);

        rbA.setId(Integer.parseInt("1"));
        rbB.setId(Integer.parseInt("2"));
        rbC.setId(Integer.parseInt("3"));
        rbD.setId(Integer.parseInt("4"));

        callJawaban();

        rbA.setOnClickListener(this);
        rbB.setOnClickListener(this);
        rbC.setOnClickListener(this);
        rbD.setOnClickListener(this);
    }

    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;
    public void callJawaban(){

        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        Call<QuizModel> call3 = apiInterface.getQuizModel();
        call3.enqueue(new Callback<QuizModel>() {
            @Override
            public void onResponse(Call<QuizModel> call, Response<QuizModel> response) {
                progressDialog.dismiss();
                QuizModel data = response.body();
                //Toast.makeText(LoginActivity.this,userList.getToken().toString(),Toast.LENGTH_LONG).show();
                if (data != null) {
                    array = data.getData().getSoalQuizAndroid().size();
                    if (x < array) {
                        if (data.getData().getSoalQuizAndroid().get(x).getJenisPertanyaan().equalsIgnoreCase("gambar")) {
                            rbA.setText(data.getData().getSoalQuizAndroid().get(x).getA());
                            rbB.setText(data.getData().getSoalQuizAndroid().get(x).getB());
                            rbC.setText(data.getData().getSoalQuizAndroid().get(x).getC());
                            rbD.setText(data.getData().getSoalQuizAndroid().get(x).getD());
                            txtSoal.setText(data.getData().getSoalQuizAndroid().get(x).getPertanyaan());
                            txtSkor.setText(data.getData().getSoalQuizAndroid().get(x).getPoint());
                            String gambar = data.getData().getSoalQuizAndroid().get(x).getGambar();
                            Picasso.get().load(gambar).into(imgView);


                        }
                        else {
                            rbA.setText(data.getData().getSoalQuizAndroid().get(x).getA());
                            rbB.setText(data.getData().getSoalQuizAndroid().get(x).getB());
                            rbC.setText(data.getData().getSoalQuizAndroid().get(x).getC());
                            rbD.setText(data.getData().getSoalQuizAndroid().get(x).getD());
                            txtSoal.setText(data.getData().getSoalQuizAndroid().get(x).getPertanyaan());
                            txtSkor.setText(data.getData().getSoalQuizAndroid().get(x).getPoint());
                            String gambar = data.getData().getSoalQuizAndroid().get(x).getGambar();
                            Picasso.get().load(gambar).into(imgView);

                        /*if (x == array) {

                            int jawaban1 = Integer.parseInt(data.getData().getSoalQuizAndroid().get(0).getPoint());
                            int jawaban2 = Integer.parseInt(data.getData().getSoalQuizAndroid().get(1).getPoint());
                            int jawaban3 = Integer.parseInt(data.getData().getSoalQuizAndroid().get(2).getPoint());
                            hasil = String.valueOf( jawaban1 + jawaban2 + jawaban3);

                           // Toast.makeText(MainActivity.this, hasil, Toast.LENGTH_LONG).show();

                           Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                           intent.putExtra("hasil", hasil);
                           startActivity(intent);
                        }*/
                        }
                        x++;
                    }
            }else

            {

                try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(MainActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }
            @Override
            public void onFailure(Call<QuizModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Maaf koneksi bermasalah",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    @Override
    public void onClick(View v) {
        hasil.add(String.valueOf(v.getId()));
        Toast.makeText(MainActivity.this, String.valueOf(hasil),Toast.LENGTH_LONG).show();
        callJawaban();
        if (x == 3) {
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            intent.putStringArrayListExtra("hasil",hasil);
            startActivity(intent);
            finish();
        }
    }
}
