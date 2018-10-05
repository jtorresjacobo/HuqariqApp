package com.lolisapp.gametraductor.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.lolisapp.gametraductor.bean.User;
import com.lolisapp.gametraductor.client.GetUserByCorreoClient;
import com.lolisapp.gametraductor.client.RegistrarUsuarioClient;
import com.lolisapp.gametraductor.client.SendFileClient;
import com.lolisapp.gametraductor.database.DataBaseService;
import com.lolisapp.gametraductor.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class AsyncLocal {
    Context context;

    public AsyncLocal(Context context) {
        this.context = context;
    }

    public void  syncronizeData(){

        if (isConnected(context)&& Util.isOnline()){
            DataBaseService data= DataBaseService.getInstance(context);
            ArrayList<User> listaUser=new ArrayList<>(data.getAllUser());
            for (User user:listaUser) {
                Log.d("tag","evaluate "+user.getEmail());
                isUserRegister(user,context);
            }

        }
    }


    public  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
    public void isUserRegister(final User user, final Context context){

        GetUserByCorreoClient getUserByCorreoClient=new GetUserByCorreoClient(context, new GetUserByCorreoClient.VerificarUsuarioListener() {
            @Override
            public void onSuccess(User userRegister) {
                Log.d("tag","user previous register "+user.getEmail());

                Log.d("tag","init send list file "+user.getEmail());

                sendListAudio(userRegister.getEmail());

            }

            @Override
            public void notExist() {
                registerUser(user,context);

            }

            @Override
            public void onError(String message) {

            }
        });

        getUserByCorreoClient.userByEmail(user.getEmail());


    }



    private void registerUser(User user,Context context){
        RegistrarUsuarioClient registrarUsuarioClient=new RegistrarUsuarioClient(context, new RegistrarUsuarioClient.RegistrarUsuarioCorreoListener() {
            @Override
            public void onSuccess(User user) {
                Log.d("tag","init send list file "+user.getEmail());
                sendListAudio(user.getEmail());

            }

            @Override
            public void onError(String message) {

            }
        });

        registrarUsuarioClient.insertarusuarioPorCorreo(user);


    }


    private void sendListAudio(String  correo){

        List<String> listAudio= DataBaseService.getInstance(context).getListAudioNotSend(correo);
        for (String fileAudio:listAudio) {
            Log.d("tag","init send  file "+fileAudio);

            sendIndividualAudio(fileAudio);
        }
    }

    private void sendIndividualAudio(final String  nameFile){

        SendFileClient sendFileClient=new SendFileClient(context, new SendFileClient.SendFileClientListener() {
            @Override
            public void onSuccess() {
                DataBaseService.getInstance(context).updateFileAudio(nameFile);

            }

            @Override
            public void onError(String message) {

            }
        });
        File file = new File(context.getFilesDir(), nameFile    );

        Log.d("tag","file zise"+file.getAbsolutePath());
        Log.d("tag","file zise"+file.getTotalSpace());
        Log.d("tag","file zise"+file.getName());

        sendFileClient.uploadFile(getBytes(file),nameFile);
    }



    byte[] getBytes(File file) {
        FileInputStream input = null;
        if (file.exists()) try {
            input = new FileInputStream(file);
            int len = (int) file.length();
            byte[] data = new byte[len];
            int count, total = 0;
            while ((count = input.read(data, total, len - total)) > 0) total += count;
            return data;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) try {
                input.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
