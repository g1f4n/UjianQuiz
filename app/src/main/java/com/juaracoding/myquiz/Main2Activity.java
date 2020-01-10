package com.juaracoding.myquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.juaracoding.myquiz.quizmodel.QuizModel;
import com.juaracoding.myquiz.service.APIClient;
import com.juaracoding.myquiz.service.APIInterfacesRest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity {

    TextView txtSkor;
    Button btnselesai, btnulang;

    ArrayList<String> hasil = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnselesai = findViewById(R.id.btnselesai);
        btnulang = findViewById(R.id.btnulang);
        txtSkor = findViewById(R.id.txtSkor);

        Intent arr = getIntent();
        hasil = arr.getStringArrayListExtra("hasil");
        callSoal();

        /*String txthasil = getIntent().getStringExtra("hasil");
        txtSkor.setText(txthasil);

         */
        btnulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnselesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

        APIInterfacesRest apiInterface;
        ProgressDialog progressDialog;
        public void callSoal(){

            apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
            progressDialog = new ProgressDialog(Main2Activity.this);
            progressDialog.setTitle("Loading");
            progressDialog.show();
            Call<QuizModel> call3 = apiInterface.getQuizModel();
            call3.enqueue(new Callback<QuizModel>() {
                @Override
                public void onResponse(Call<QuizModel> call, Response<QuizModel> response) {
                    progressDialog.dismiss();
                    QuizModel data = response.body();
                    //Toast.makeText(LoginActivity.this,userList.getToken().toString(),Toast.LENGTH_LONG).show();
                    if (data !=null) {

                        Integer point=0;
                        Integer nilai=0;

                        for (int x = 0;x<data.getData().getSoalQuizAndroid().size();x++){
                            ArrayList<String> Add = new ArrayList<>(3);
                            Add.add("4");
                            Add.add("1");
                            Add.add("1");

                            if(hasil.get(x).equalsIgnoreCase(Add.get(x))){
                                point = Integer.parseInt(data.getData().getSoalQuizAndroid().get(x).getPoint());
                                nilai += point;
                            }
                            txtSkor.setText(String.valueOf(nilai));
                        }

                    }else{

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(Main2Activity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(Main2Activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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


    }

