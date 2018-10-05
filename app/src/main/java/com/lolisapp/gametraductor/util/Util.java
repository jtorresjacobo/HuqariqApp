package com.lolisapp.gametraductor.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import com.lolisapp.gametraductor.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Util {

    public static boolean validarCorreo(String email) {

        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();

    }


    public static boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }

    public static String getAudioName(int numberFie, String fileName, Context context){
            int index=0;
            InputStream fIn = null;
            InputStreamReader isr = null;
            BufferedReader input = null;
            try {
                fIn = context.getResources().getAssets()
                        .open(fileName, Context.MODE_WORLD_READABLE);
                isr = new InputStreamReader(fIn);
                input = new BufferedReader(isr);
                String line = "";
                while ((line = input.readLine()) != null) {
                    if (numberFie==index)return  line;
                    index++;
                }
            } catch (Exception e) {
                e.getMessage();
            } finally {
                try {
                    if (isr != null)
                        isr.close();
                    if (fIn != null)
                        fIn.close();
                    if (input != null)
                        input.close();
                } catch (Exception e2) {
                    e2.getMessage();
                }
            }
            return null;


    }

    public static String getSimpleDateToday() {
        DateTime today = new DateTime(DateTimeZone.forID(Constants.DATE_TIME_ZONE_AMERICA_LATINA));
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT_DATE, Locale.getDefault());
        return sdf.format(today.toDate());
    }

    public static void copyFileUsingStream(File source,Context context) throws IOException {
        InputStream is = null;
        FileOutputStream fos = context.openFileOutput(source.getName(), Context.MODE_PRIVATE);
        try {
            is = new FileInputStream(source);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        } finally {
            is.close();
            fos.close();
        }
    }


    public static ProgressDialog createProgressDialog(Context mContext,String mensaje) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
        dialog.setCancelable(false);
        if (mensaje!=null) dialog.setMessage(mensaje);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        return dialog;
    }

    public static String getStringDate(){
        String date = Util.getSimpleDateToday();
        String[] parts = date.split("-");
        if (parts[1].equals("01")){
            return  parts[2]+"ene".concat(parts[0]);
        }else if (parts[1].equals("02")){
            return parts[2]+"feb".concat(parts[0]);
        }else if (parts[1].equals("03")){
            return  parts[2]+"mar".concat(parts[0]);
        }else if (parts[1].equals("04")){
            return  parts[2]+"abr".concat(parts[0]);
        }else if (parts[1].equals("05")){
            return  parts[2]+"may".concat(parts[0]);
        }else if (parts[1].equals("06")){
            return  parts[2]+"jun".concat(parts[0]);
        }else if (parts[1].equals("07")){
            return  parts[2]+"jul".concat(parts[0]);
        }else if (parts[1].equals("08")){
            return  parts[2]+"ago".concat(parts[0]);
        }else if (parts[1].equals("09")){
            return  parts[2]+"set".concat(parts[0]);
        }else if (parts[1].equals("10")){
            return  parts[2]+"oct".concat(parts[0]);
        }else if (parts[1].equals("11")){
            return  parts[2]+"nov".concat(parts[0]);
        }else{
            return  parts[2]+"dec".concat(parts[0]);

        }

    }

    public static String getFileName(Integer codeDepartamento){
        return "collao";
    }


    @NotNull
    public static Integer getTotalAudio(@Nullable String fileName) {
        if (fileName.equals("collao")) return 331;
        return 229;
    }
}
