package com.juaracoding.myquiz.service;

/**
 * Created by user on 1/10/2018.
 */

import com.juaracoding.myquiz.quizmodel.QuizModel;
import com.juaracoding.myquiz.quizmodel.SoalQuizAndroid;

import retrofit2.Call;
import retrofit2.http.GET;
/**
 * Created by anupamchugh on 09/01/17.
 */

public interface APIInterfacesRest {

 @GET("soal_quiz_android/all")
 Call<QuizModel> getQuizModel();

}
