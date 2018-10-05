package com.lolisapp.gametraductor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lolisapp.gametraductor.bean.Ubigeo;
import com.lolisapp.gametraductor.bean.User;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class DataBaseService extends SQLiteOpenHelper {

    private static DataBaseService dbHelper;
    private static final int VERSION_BASEDATOS = 3;
    private static final String NOMBRE_BASEDATOS = "CATOLICA_GAME";
    private static final String TABLE_UBIGEO = "TABLE_UBIGEO";


    private static final String TABLE_USER = "TABLE_USER";
    private static final String TABLE_AUDIOS = "TABLE_AUDIOS";




    private static String sqlCreateTableUser = "CREATE TABLE ".concat(TABLE_USER).concat(" (ID_USER_LOCAL INTEGER  primary key autoincrement," +
            "ID_USER_EXTERN INTEGER,EMAIL TEXT,PHONE TEXT,DNI TEXT,PASSWORD TEXT , NAME TEXT,LASTNAME TEXT," +
            "CODE_DEPARTAMENTO INTEGER,CODE_PROVINCIA INTEGER," +
            "CODE_DISTRITO INTEGER,AVANCE INTEGER,INSTITUCION TEXT,REGION TEXT,ISMEMBER INTEGER ) ");

    private static String sqlCreateTableAudio = "CREATE TABLE ".concat(TABLE_AUDIOS).concat(" (ID INTEGER  primary key autoincrement," +
            "EMAIL_USER  TEXT,NAME_FILE TEXT,IS_SEND INTEGER ,ORDER_FILE INTEGER ) ");

    private static String sqlCreateTableUbigeo = "CREATE TABLE ".concat(TABLE_UBIGEO).concat(" (CODE_DEPARTAMENTO INTEGER," +
            "CODE_PROVINCIA INTEGER,CODE_DISTRITO INTEGER,NOMBRE VARCHAR(50) ) ");



    public static  DataBaseService getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DataBaseService(context);
        }
        return dbHelper;
    }
    private DataBaseService(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("drop table "+ TABLE_USER);
        db.execSQL("drop table "+TABLE_UBIGEO);
        db.execSQL("drop table "+TABLE_AUDIOS);


        onCreate(db);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sqlCreateTableUser);
        db.execSQL(sqlCreateTableUbigeo);
        db.execSQL(sqlCreateTableAudio);
        db.execSQL(instructionUbigeosOne());
        db.execSQL(instructionUbigeosTwo());
        db.execSQL(instructionUbigeosThree());
        db.execSQL(instructionUbigeosFor());
        db.execSQL(instructionUbigeosTSeven());
        db.execSQL(instructionUbigeosEight());

    }











    public User getUser(String correo) throws ParseException {
        SQLiteDatabase db = getWritableDatabase();;
        String sql=("select ID_USER_LOCAL,ID_USER_EXTERN ,EMAIL ,PHONE, DNI," +
                "PASSWORD,NAME,LASTNAME,CODE_DEPARTAMENTO,CODE_PROVINCIA," +
                "CODE_DISTRITO,AVANCE,REGION,INSTITUCION,ISMEMBER FROM ") .
                concat(TABLE_USER).concat(" where EMAIL='").concat(correo.toString()).concat("'");
        Cursor cursor = db.rawQuery(sql, null);
        User user = null;
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setUserLocalId(cursor.getLong(0));
                user.setUserExternId(cursor.getLong(1));

                user.setEmail(cursor.getString(2));
                user.setPhone(cursor.getString(3));
                user.setDni(cursor.getString(4));
                user.setPassword(cursor.getString(5));
                user.setFirstName(cursor.getString(6));
                user.setLastName(cursor.getString(7));
                user.setCodeDepartamento(cursor.getInt(8));
                user.setCodeProvincia(cursor.getInt(9));
                user.setCodeDistrito(cursor.getInt(10));
                user.setAvance(cursor.getInt(11));
                user.setInstitution(cursor.getString(13));
                user.setRegion(cursor.getString(12));
                user.setIsMember(cursor.getInt(14));
                return user;

            } while (cursor.moveToNext());
        }
        cursor.close();


        return user;

    }


    public List<String> getListAudioNotSend(String correo){

        SQLiteDatabase db = getWritableDatabase();;
        String sql=("select NAME_FILE FROM ") .concat(TABLE_AUDIOS).concat(" where EMAIL_USER='")
                .concat(correo.trim()).concat("'")
                .concat(" and IS_SEND=0 group by NAME_FILE");
        Cursor cursor = db.rawQuery(sql, null);
        List<String> listAudio=new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                listAudio.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        cursor.close();


        return listAudio;

    }

    public void updateFileAudio(String file){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("IS_SEND",1);
        db.update(TABLE_AUDIOS, valores, "NAME_FILE='".concat(file+"'"), null);


    }



    public User getUserByPassEmail(String correo,String pass) throws ParseException {
        SQLiteDatabase db = getWritableDatabase();;
        String sql=("select ID_USER_LOCAL,ID_USER_EXTERN ,EMAIL ,PHONE, DNI," +
                "PASSWORD,NAME,LASTNAME,CODE_DEPARTAMENTO,CODE_PROVINCIA," +
                "CODE_DISTRITO,AVANCE FROM ") .
                concat(TABLE_USER).concat(" where EMAIL='").concat(correo.trim()).concat("'")
                .concat(" and PASSWORD='").concat(pass.trim()).concat("'");
        Cursor cursor = db.rawQuery(sql, null);
        User user = null;
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setUserLocalId(cursor.getLong(0));
                user.setUserExternId(cursor.getLong(1));

                user.setEmail(cursor.getString(2));
                user.setPhone(cursor.getString(3));
                user.setDni(cursor.getString(4));
                user.setPassword(cursor.getString(5));
                user.setFirstName(cursor.getString(6));
                user.setLastName(cursor.getString(7));
                user.setCodeDepartamento(cursor.getInt(8));
                user.setCodeProvincia(cursor.getInt(9));
                user.setCodeDistrito(cursor.getInt(10));
                user.setAvance(cursor.getInt(11));
                return user;

            } while (cursor.moveToNext());
        }
        cursor.close();







        return user;

    }


    public List<User> getAllUser() {
        List<User> listaUser=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();;
        String sql=("select ID_USER_LOCAL,ID_USER_EXTERN ,EMAIL ,PHONE, DNI," +
                "PASSWORD,NAME,LASTNAME,CODE_DEPARTAMENTO,CODE_PROVINCIA," +
                "CODE_DISTRITO,AVANCE FROM ") .
                concat(TABLE_USER);
        Cursor cursor = db.rawQuery(sql, null);
        User user = null;
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setUserLocalId(cursor.getLong(0));
                user.setUserExternId(cursor.getLong(1));

                user.setEmail(cursor.getString(2));
                user.setPhone(cursor.getString(3));
                user.setDni(cursor.getString(4));
                user.setPassword(cursor.getString(5));
                user.setFirstName(cursor.getString(6));
                user.setLastName(cursor.getString(7));
                user.setCodeDepartamento(cursor.getInt(8));
                user.setCodeProvincia(cursor.getInt(9));
                user.setCodeDistrito(cursor.getInt(10));
                user.setAvance(cursor.getInt(11));
                listaUser.add(user);

            } while (cursor.moveToNext());
        }
        cursor.close();


        return listaUser;

    }





    public void insertUser(User user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("ID_USER_EXTERN", user.getUserLocalId());
        nuevoRegistro.put("EMAIL",user.getEmail() );
        nuevoRegistro.put("PHONE",user.getPhone() );
        nuevoRegistro.put("DNI",user.getDni() );
        nuevoRegistro.put("PASSWORD",user.getPassword());
        nuevoRegistro.put("NAME",user.getFirstName());
        nuevoRegistro.put("LASTNAME",user.getLastName());
        nuevoRegistro.put("CODE_DEPARTAMENTO",user.getCodeDepartamento());
        nuevoRegistro.put("CODE_PROVINCIA",user.getCodeProvincia());
        nuevoRegistro.put("CODE_DISTRITO",user.getCodeDistrito());
        nuevoRegistro.put("AVANCE",user.getAvance());


        db.insert(TABLE_USER, null, nuevoRegistro);


    }

    public void editAvance(Integer avance,String email){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("AVANCE",avance);
        db.update(TABLE_USER, valores, "EMAIL='".concat(email+"'"), null);

    }

    public void editMember(String instituion,String region,Integer isMember,String email){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("REGION",region);
        valores.put("INSTITUCION",instituion);
        valores.put("ISMEMBER",isMember);
        db.update(TABLE_USER, valores, "EMAIL='".concat(email+"'"), null);

    }




    public void editExtraInfo(String instution,String region,Integer isMember ,Integer id){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("INSTITUCION",instution);
        valores.put("REGION",region);
        valores.put("ISMEMBER",isMember);

        db.update(TABLE_USER, valores, "ID_USER_LOCAL=".concat(id.toString()), null);

    }



    public void insertAudio(String email,Integer order,String  filename){
        Log.d("tag","insert audio");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("EMAIL_USER", email);
        nuevoRegistro.put("ORDER_FILE",order );
        nuevoRegistro.put("IS_SEND",0 );
        nuevoRegistro.put("NAME_FILE",filename );



        db.insert(TABLE_AUDIOS, null, nuevoRegistro);


    }

    public void deleteAudio(String email,Integer order){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_AUDIOS, "EMAIL_USER" + "='" + email+"' AND ORDER_FILE="+order, null);



    }










    public ArrayList<Ubigeo> getListDepartamento() throws ParseException {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Ubigeo> lista = new ArrayList<>();
        String sql="select CODE_DEPARTAMENTO,CODE_PROVINCIA,CODE_DISTRITO,NOMBRE from ".
                concat(TABLE_UBIGEO).concat(" where CODE_PROVINCIA=0 AND CODE_DISTRITO=0");
        Cursor cursor = db.rawQuery(sql
                , null);
        if (cursor.moveToFirst()) {
            do {
                Ubigeo ubigeo=new Ubigeo();
                ubigeo.setIdDepartamento(cursor.getInt(0));
                ubigeo.setIdProvincia(cursor.getInt(1));
                ubigeo.setIdDistrito(cursor.getInt(2));
                ubigeo.setNombre(cursor.getString(3));

                lista.add(ubigeo);

            } while (cursor.moveToNext());
        }


        return lista;

    }


    public ArrayList<Ubigeo> getListProvincia(Integer idDepartamento) throws ParseException {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Ubigeo> lista = new ArrayList<>();
        String sql="select CODE_DEPARTAMENTO,CODE_PROVINCIA,CODE_DISTRITO,NOMBRE from ".
                concat(TABLE_UBIGEO).concat(" where CODE_DEPARTAMENTO="+idDepartamento.toString()+ " AND " +
                "CODE_DISTRITO=0 AND CODE_PROVINCIA!=0");
        Cursor cursor = db.rawQuery(sql
                , null);
        if (cursor.moveToFirst()) {
            do {
                Ubigeo ubigeo=new Ubigeo();
                ubigeo.setIdDepartamento(cursor.getInt(0));
                ubigeo.setIdProvincia(cursor.getInt(1));
                ubigeo.setIdDistrito(cursor.getInt(2));
                ubigeo.setNombre(cursor.getString(3));

                lista.add(ubigeo);

            } while (cursor.moveToNext());
        }


        return lista;

    }

    public ArrayList<Ubigeo> getListDistrito(Integer idDepartamento,Integer idProvincia) throws ParseException {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Ubigeo> lista = new ArrayList<>();
        String sql="select CODE_DEPARTAMENTO,CODE_PROVINCIA,CODE_DISTRITO,NOMBRE from ".
                concat(TABLE_UBIGEO).concat(" where CODE_DEPARTAMENTO="+idDepartamento.toString() +
                " AND  CODE_DISTRITO!=0 AND CODE_PROVINCIA="+idProvincia.toString());
        Cursor cursor = db.rawQuery(sql
                , null);
        if (cursor.moveToFirst()) {
            do {
                Ubigeo ubigeo=new Ubigeo();
                ubigeo.setIdDepartamento(cursor.getInt(0));
                ubigeo.setIdProvincia(cursor.getInt(1));
                ubigeo.setIdDistrito(cursor.getInt(2));
                ubigeo.setNombre(cursor.getString(3));

                lista.add(ubigeo);

            } while (cursor.moveToNext());
        }


        return lista;

    }







    @Override
    public synchronized void close() {
        if (dbHelper != null)
            dbHelper.close();
        super.close();
    }








    private static String instructionUbigeosOne() {

        return "INSERT INTO ".concat(TABLE_UBIGEO) + " (CODE_DEPARTAMENTO, CODE_PROVINCIA, CODE_DISTRITO, NOMBRE) VALUES" +
                "(15, 0, 0, 'Lima')," +
                "(1, 1, 0, 'Chachapoyas')," +
                "(1, 1, 1, 'Chachapoyas')," +
                "(1, 1, 2, 'Asuncion')," +
                "(1, 1, 3, 'Balsas')," +
                "(1, 1, 4, 'Cheto')," +
                "(1, 1, 5, 'Chiliquin')," +
                "(1, 1, 6, 'Chuquibamba')," +
                "(1, 1, 7, 'Granada')," +
                "(1, 1, 8, 'Huancas')," +
                "(1, 1, 9, 'La Jalca')," +
                "(1, 1, 10, 'Leimebamba')," +
                "(1, 1, 11, 'Levanto')," +
                "(1, 1, 12, 'Magdalena')," +
                "(1, 1, 13, 'Mariscal Castilla')," +
                "(1, 1, 14, 'Molinopampa')," +
                "(1, 1, 15, 'Montevideo')," +
                "(1, 1, 16, 'Olleros')," +
                "(1, 1, 17, 'Quinjalca')," +
                "(1, 1, 18, 'San Francisco de Daguas')," +
                "(1, 1, 19, 'San Isidro de Maino')," +
                "(1, 1, 20, 'Soloco')," +
                "(1, 1, 21, 'Sonche')," +
                "(1, 2, 0, 'Bagua')," +
                "(1, 2, 1, 'Bagua')," +
                "(1, 2, 2, 'Aramango')," +
                "(1, 2, 3, 'Copallin')," +
                "(1, 2, 4, 'El Parco')," +
                "(1, 2, 5, 'Imaza')," +
                "(1, 2, 6, 'La Peca')," +
                "(1, 3, 0, 'Bongara')," +
                "(1, 3, 1, 'Jumbilla')," +
                "(1, 3, 2, 'Chisquilla')," +
                "(1, 3, 3, 'Churuja')," +
                "(1, 3, 4, 'Corosha')," +
                "(1, 3, 5, 'Cuispes')," +
                "(1, 3, 6, 'Florida')," +
                "(1, 3, 7, 'Jazán')," +
                "(1, 3, 8, 'Recta')," +
                "(1, 3, 9, 'San Carlos')," +
                "(1, 3, 10, 'Shipasbamba')," +
                "(1, 3, 11, 'Valera')," +
                "(1, 3, 12, 'Yambrasbamba')," +
                "(1, 4, 0, 'Condorcanqui')," +
                "(1, 4, 1, 'Nieva')," +
                "(1, 4, 2, 'El Cenepa')," +
                "(1, 4, 3, 'Rio Santiago')," +
                "(1, 5, 0, 'Luya')," +
                "(1, 5, 1, 'Lamud')," +
                "(1, 5, 2, 'Camporredondo')," +
                "(1, 5, 3, 'Cocabamba')," +
                "(1, 5, 4, 'Colcamar')," +
                "(1, 5, 5, 'Conila')," +
                "(1, 5, 6, 'Inguilpata')," +
                "(1, 5, 7, 'Longuita')," +
                "(1, 5, 8, 'Lonya Chico')," +
                "(1, 5, 9, 'Luya')," +
                "(1, 5, 10, 'Luya Viejo')," +
                "(1, 5, 11, 'Maria')," +
                "(1, 5, 12, 'Ocalli')," +
                "(1, 5, 13, 'Ocumal')," +
                "(1, 5, 14, 'Pisuquia')," +
                "(1, 5, 15, 'Providencia')," +
                "(1, 5, 16, 'San Cristobal')," +
                "(1, 5, 17, 'San Francisco del Yeso')," +
                "(1, 5, 18, 'San Jeronimo')," +
                "(1, 5, 19, 'San Juan de Lopecancha')," +
                "(1, 5, 20, 'Santa Catalina')," +
                "(1, 5, 21, 'Santo Tomas')," +
                "(1, 5, 22, 'Tingo')," +
                "(1, 5, 23, 'Trita')," +
                "(1, 6, 0, 'Rodriguez de Mendoza')," +
                "(1, 6, 1, 'San Nicolas')," +
                "(1, 6, 2, 'Chirimoto')," +
                "(1, 6, 3, 'Cochamal')," +
                "(1, 6, 4, 'Huambo')," +
                "(1, 6, 5, 'Limabamba')," +
                "(1, 6, 6, 'Longar')," +
                "(1, 6, 7, 'Mariscal Benavides')," +
                "(1, 6, 8, 'Milpuc')," +
                "(1, 6, 9, 'Omia')," +
                "(1, 6, 10, 'Santa Rosa')," +
                "(1, 6, 11, 'Totora')," +
                "(1, 6, 12, 'Vista Alegre')," +
                "(1, 7, 0, 'Utcubamba')," +
                "(1, 7, 1, 'Bagua Grande')," +
                "(1, 7, 2, 'Cajaruro')," +
                "(1, 7, 3, 'Cumba')," +
                "(1, 7, 4, 'El Milagro')," +
                "(1, 7, 5, 'Jamalca')," +
                "(1, 7, 6, 'Lonya Grande')," +
                "(1, 7, 7, 'Yamon')," +
                "(2, 0, 0, 'Ancash')," +
                "(2, 1, 0, 'Huaraz')," +
                "(2, 1, 1, 'Huaraz')," +
                "(2, 1, 2, 'Cochabamba')," +
                "(2, 1, 3, 'Colcabamba')," +
                "(2, 1, 4, 'Huanchay')," +
                "(2, 1, 5, 'Independencia')," +
                "(2, 1, 6, 'Jangas')," +
                "(2, 1, 7, 'La Libertad')," +
                "(2, 1, 8, 'Olleros')," +
                "(2, 1, 9, 'Pampas')," +
                "(2, 1, 10, 'Pariacoto')," +
                "(2, 1, 11, 'Pira')," +
                "(2, 1, 12, 'Tarica')," +
                "(2, 2, 0, 'Aija')," +
                "(2, 2, 1, 'Aija')," +
                "(2, 2, 2, 'Coris')," +
                "(2, 2, 3, 'Huacllan')," +
                "(2, 2, 4, 'La Merced')," +
                "(2, 2, 5, 'Succha')," +
                "(2, 3, 0, 'Antonio Raymondi')," +
                "(2, 3, 1, 'Llamellin')," +
                "(2, 3, 2, 'Aczo')," +
                "(2, 3, 3, 'Chaccho')," +
                "(2, 3, 4, 'Chingas')," +
                "(2, 3, 5, 'Mirgas')," +
                "(2, 3, 6, 'San Juan de Rontoy')," +
                "(2, 4, 0, 'Asuncion')," +
                "(2, 4, 1, 'Chacas')," +
                "(2, 4, 2, 'Acochaca')," +
                "(2, 5, 0, 'Bolognesi')," +
                "(2, 5, 1, 'Chiquian')," +
                "(2, 5, 2, 'Abelardo Pardo Lezameta')," +
                "(2, 5, 3, 'Antonio Raymondi')," +
                "(2, 5, 4, 'Aquia')," +
                "(2, 5, 5, 'Cajacay')," +
                "(2, 5, 6, 'Canis')," +
                "(2, 5, 7, 'Colquioc')," +
                "(2, 5, 8, 'Huallanca')," +
                "(2, 5, 9, 'Huasta')," +
                "(2, 5, 10, 'Huayllacayan')," +
                "(2, 5, 11, 'La Primavera')," +
                "(2, 5, 12, 'Mangas')," +
                "(2, 5, 13, 'Pacllon')," +
                "(2, 5, 14, 'San Miguel de Corpanqui')," +
                "(2, 5, 15, 'Ticllos')," +
                "(2, 6, 0, 'Carhuaz')," +
                "(2, 6, 1, 'Carhuaz')," +
                "(2, 6, 2, 'Acopampa')," +
                "(2, 6, 3, 'Amashca')," +
                "(2, 6, 4, 'Anta')," +
                "(2, 6, 5, 'Ataquero')," +
                "(2, 6, 6, 'Marcara')," +
                "(2, 6, 7, 'Pariahuanca')," +
                "(2, 6, 8, 'San Miguel de Aco')," +
                "(2, 6, 9, 'Shilla')," +
                "(2, 6, 10, 'Tinco')," +
                "(2, 6, 11, 'Yungar')," +
                "(2, 7, 0, 'Carlos Fermin Fitzcarrald')," +
                "(2, 7, 1, 'San Luis')," +
                "(2, 7, 2, 'San Nicolas')," +
                "(2, 7, 3, 'Yauya')," +
                "(2, 8, 0, 'Casma')," +
                "(2, 8, 1, 'Casma')," +
                "(2, 8, 2, 'Buena Vista Alta')," +
                "(2, 8, 3, 'Comandante Noel')," +
                "(2, 8, 4, 'Yautan')," +
                "(2, 9, 0, 'Corongo')," +
                "(2, 9, 1, 'Corongo')," +
                "(2, 9, 2, 'Aco')," +
                "(2, 9, 3, 'Bambas')," +
                "(2, 9, 4, 'Cusca')," +
                "(2, 9, 5, 'La Pampa')," +
                "(2, 9, 6, 'Yanac')," +
                "(2, 9, 7, 'Yupan')," +
                "(2, 10, 0, 'Huari')," +
                "(2, 10, 1, 'Huari')," +
                "(2, 10, 2, 'Anra')," +
                "(2, 10, 3, 'Cajay')," +
                "(2, 10, 4, 'Chavin de Huantar')," +
                "(2, 10, 5, 'Huacachi')," +
                "(2, 10, 6, 'Huacchis')," +
                "(2, 10, 7, 'Huachis')," +
                "(2, 10, 8, 'Huantar')," +
                "(2, 10, 9, 'Masin')," +
                "(2, 10, 10, 'Paucas')," +
                "(2, 10, 11, 'Ponto')," +
                "(2, 10, 12, 'Rahuapampa')," +
                "(2, 10, 13, 'Rapayan')," +
                "(2, 10, 14, 'San Marcos')," +
                "(2, 10, 15, 'San Pedro de Chana')," +
                "(2, 10, 16, 'Uco')," +
                "(2, 11, 0, 'Huarmey')," +
                "(2, 11, 1, 'Huarmey')," +
                "(2, 11, 2, 'Cochapeti')," +
                "(2, 11, 3, 'Culebras')," +
                "(2, 11, 4, 'Huayan')," +
                "(2, 11, 5, 'Malvas')," +
                "(2, 12, 0, 'Huaylas')," +
                "(2, 12, 1, 'Caraz')," +
                "(2, 12, 2, 'Huallanca')," +
                "(2, 12, 3, 'Huata')," +
                "(2, 12, 4, 'Huaylas')," +
                "(2, 12, 5, 'Mato')," +
                "(2, 12, 6, 'Pamparomas')," +
                "(2, 12, 7, 'Pueblo Libre')," +
                "(2, 12, 8, 'Santa Cruz')," +
                "(2, 12, 9, 'Santo Toribio')," +
                "(2, 12, 10, 'Yuracmarca')," +
                "(2, 13, 0, 'Mariscal Luzuriaga')," +
                "(2, 13, 1, 'Piscobamba')," +
                "(2, 13, 2, 'Casca')," +
                "(2, 13, 3, 'Eleazar Guzman Barron')," +
                "(2, 13, 4, 'Fidel Olivas Escudero')," +
                "(2, 13, 5, 'Llama')," +
                "(2, 13, 6, 'Llumpa')," +
                "(2, 13, 7, 'Lucma')," +
                "(2, 13, 8, 'Musga')," +
                "(2, 14, 0, 'Ocros')," +
                "(2, 14, 1, 'Ocros')," +
                "(2, 14, 2, 'Acas')," +
                "(2, 14, 3, 'Cajamarquilla')," +
                "(2, 14, 4, 'Carhuapampa')," +
                "(2, 14, 5, 'Cochas')," +
                "(2, 14, 6, 'Congas')," +
                "(2, 14, 7, 'Llipa')," +
                "(2, 14, 8, 'San Cristobal de Rajan')," +
                "(2, 14, 9, 'San Pedro')," +
                "(2, 14, 10, 'Santiago de Chilcas')," +
                "(2, 15, 0, 'Pallasca')," +
                "(2, 15, 1, 'Cabana')," +
                "(2, 15, 2, 'Bolognesi')," +
                "(2, 15, 3, 'Conchucos')," +
                "(2, 15, 4, 'Huacaschuque')," +
                "(2, 15, 5, 'Huandoval')," +
                "(2, 15, 6, 'Lacabamba')," +
                "(2, 15, 7, 'Llapo')," +
                "(2, 15, 8, 'Pallasca')," +
                "(2, 15, 9, 'Pampas')," +
                "(2, 15, 10, 'Santa Rosa')," +
                "(2, 15, 11, 'Tauca')," +
                "(2, 16, 0, 'Pomabamba')," +
                "(2, 16, 1, 'Pomabamba')," +
                "(2, 16, 2, 'Huayllan')," +
                "(2, 16, 3, 'Parobamba')," +
                "(2, 16, 4, 'Quinuabamba')," +
                "(2, 17, 0, 'Recuay')," +
                "(2, 17, 1, 'Recuay')," +
                "(2, 17, 2, 'Catac')," +
                "(2, 17, 3, 'Cotaparaco')," +
                "(2, 17, 4, 'Huayllapampa')," +
                "(2, 17, 5, 'Llacllin')," +
                "(2, 17, 6, 'Marca')," +
                "(2, 17, 7, 'Pampas Chico')," +
                "(2, 17, 8, 'Pararin')," +
                "(2, 17, 9, 'Tapacocha')," +
                "(2, 17, 10, 'Ticapampa')," +
                "(2, 18, 0, 'Santa')," +
                "(2, 18, 1, 'Chimbote')," +
                "(2, 18, 2, 'Caceres del Peru')," +
                "(2, 18, 3, 'Coishco')," +
                "(2, 18, 4, 'Macate')," +
                "(2, 18, 5, 'Moro')," +
                "(2, 18, 6, 'Nepeña')," +
                "(2, 18, 7, 'Samanco')," +
                "(2, 18, 8, 'Santa')," +
                "(2, 18, 9, 'Nuevo Chimbote')," +
                "(2, 19, 0, 'Sihuas')," +
                "(2, 19, 1, 'Sihuas')," +
                "(2, 19, 2, 'Acobamba')," +
                "(2, 19, 3, 'Alfonso Ugarte')," +
                "(2, 19, 4, 'Cashapampa')," +
                "(2, 19, 5, 'Chingalpo')," +
                "(2, 19, 6, 'Huayllabamba')," +
                "(2, 19, 7, 'Quiches')," +
                "(2, 19, 8, 'Ragash')," +
                "(2, 19, 9, 'San Juan')," +
                "(2, 19, 10, 'Sicsibamba')," +
                "(2, 20, 0, 'Yungay')," +
                "(2, 20, 1, 'Yungay')," +
                "(2, 20, 2, 'Cascapara')," +
                "(2, 20, 3, 'Mancos')," +
                "(2, 20, 4, 'Matacoto')," +
                "(2, 20, 5, 'Quillo')," +
                "(2, 20, 6, 'Ranrahirca')," +
                "(2, 20, 7, 'Shupluy')," +
                "(2, 20, 8, 'Yanama')," +
                "(3, 0, 0, 'Apurimac')," +
                "(3, 1, 0, 'Abancay')," +
                "(3, 1, 1, 'Abancay')," +
                "(3, 1, 2, 'Chacoche')," +
                "(3, 1, 3, 'Circa')," +
                "(3, 1, 4, 'Curahuasi')," +
                "(3, 1, 5, 'Huanipaca')," +
                "(3, 1, 6, 'Lambrama')," +
                "(3, 1, 7, 'Pichirhua')," +
                "(3, 1, 8, 'San Pedro de Cachora')," +
                "(3, 1, 9, 'Tamburco')," +
                "(3, 2, 0, 'Andahuaylas')," +
                "(3, 2, 1, 'Andahuaylas')," +
                "(3, 2, 2, 'Andarapa')," +
                "(3, 2, 3, 'Chiara')," +
                "(3, 2, 4, 'Huancarama')," +
                "(3, 2, 5, 'Huancaray')," +
                "(3, 2, 6, 'Huayana')," +
                "(3, 2, 7, 'Kishuara')," +
                "(3, 2, 8, 'Pacobamba')," +
                "(3, 2, 9, 'Pacucha')," +
                "(3, 2, 10, 'Pampachiri')," +
                "(3, 2, 11, 'Pomacocha')," +
                "(3, 2, 12, 'San Antonio de Cachi')," +
                "(3, 2, 13, 'San Jeronimo')," +
                "(3, 2, 14, 'San Miguel de Chaccrampa')," +
                "(3, 2, 15, 'Santa Maria de Chicmo')," +
                "(3, 2, 16, 'Talavera')," +
                "(3, 2, 17, 'Tumay Huaraca')," +
                "(3, 2, 18, 'Turpo')," +
                "(3, 2, 19, 'Kaquiabamba')," +
                "(3, 3, 0, 'Antabamba')," +
                "(3, 3, 1, 'Antabamba')," +
                "(3, 3, 2, 'El Oro')," +
                "(3, 3, 3, 'Huaquirca')," +
                "(3, 3, 4, 'Juan Espinoza Medrano')," +
                "(3, 3, 5, 'Oropesa')," +
                "(3, 3, 6, 'Pachaconas')," +
                "(3, 3, 7, 'Sabaino')," +
                "(3, 4, 0, 'Aymaraes')," +
                "(3, 4, 1, 'Chalhuanca')," +
                "(3, 4, 2, 'Capaya')," +
                "(3, 4, 3, 'Caraybamba')," +
                "(3, 4, 4, 'Chapimarca')," +
                "(3, 4, 5, 'Colcabamba')," +
                "(3, 4, 6, 'Cotaruse')," +
                "(3, 4, 7, 'Huayllo')," +
                "(3, 4, 8, 'Justo Apu Sahuaraura')," +
                "(3, 4, 9, 'Lucre')," +
                "(3, 4, 10, 'Pocohuanca')," +
                "(3, 4, 11, 'San Juan de Chacña')," +
                "(3, 4, 12, 'Sañayca')," +
                "(3, 4, 13, 'Soraya')," +
                "(3, 4, 14, 'Tapairihua')," +
                "(3, 4, 15, 'Tintay')," +
                "(3, 4, 16, 'Toraya')," +
                "(3, 4, 17, 'Yanaca')," +
                "(3, 5, 0, 'Cotabambas')," +
                "(3, 5, 1, 'Tambobamba')," +
                "(3, 5, 2, 'Cotabambas')," +
                "(3, 5, 3, 'Coyllurqui')," +
                "(3, 5, 4, 'Haquira')," +
                "(3, 5, 5, 'Mara')," +
                "(3, 5, 6, 'Challhuahuacho')," +
                "(3, 6, 0, 'Chincheros')," +
                "(3, 6, 1, 'Chincheros')," +
                "(3, 6, 2, 'Anco-Huallo')," +
                "(3, 6, 3, 'Cocharcas')," +
                "(3, 6, 4, 'Huaccana')," +
                "(3, 6, 5, 'Ocobamba')," +
                "(3, 6, 6, 'Ongoy')," +
                "(3, 6, 7, 'Uranmarca')," +
                "(3, 6, 8, 'Ranracancha')," +
                "(3, 7, 0, 'Grau')," +
                "(3, 7, 1, 'Chuquibambilla')," +
                "(3, 7, 2, 'Curpahuasi')," +
                "(3, 7, 3, 'Gamarra')," +
                "(3, 7, 4, 'Huayllati')," +
                "(3, 7, 5, 'Mamara')," +
                "(3, 7, 6, 'Micaela Bastidas')," +
                "(3, 7, 7, 'Pataypampa')," +
                "(3, 7, 8, 'Progreso')," +
                "(3, 7, 9, 'San Antonio')," +
                "(3, 7, 10, 'Santa Rosa')," +
                "(3, 7, 11, 'Turpay')," +
                "(3, 7, 12, 'Vilcabamba')," +
                "(3, 7, 13, 'Virundo')," +
                "(3, 7, 14, 'Curasco')," +
                "(4, 0, 0, 'Arequipa')," +
                "(4, 1, 0, 'Arequipa')," +
                "(4, 1, 1, 'Arequipa')," +
                "(4, 1, 2, 'Alto Selva Alegre')," +
                "(4, 1, 3, 'Cayma')," +
                "(4, 1, 4, 'Cerro Colorado')," +
                "(4, 1, 5, 'Characato')," +
                "(4, 1, 6, 'Chiguata')," +
                "(4, 1, 7, 'Jacobo Hunter')," +
                "(4, 1, 8, 'La Joya')," +
                "(4, 1, 9, 'Mariano Melgar')," +
                "(4, 1, 10, 'Miraflores')," +
                "(4, 1, 11, 'Mollebaya')," +
                "(4, 1, 12, 'Paucarpata')," +
                "(4, 1, 13, 'Pocsi')," +
                "(4, 1, 14, 'Polobaya')," +
                "(4, 1, 15, 'Quequeña')," +
                "(4, 1, 16, 'Sabandia')," +
                "(4, 1, 17, 'Sachaca')," +
                "(4, 1, 18, 'San Juan de Siguas')," +
                "(4, 1, 19, 'San Juan de Tarucani')," +
                "(4, 1, 20, 'Santa Isabel de Siguas')," +
                "(4, 1, 21, 'Santa Rita de Siguas')," +
                "(4, 1, 22, 'Socabaya')," +
                "(4, 1, 23, 'Tiabaya')," +
                "(4, 1, 24, 'Uchumayo')," +
                "(4, 1, 25, 'Vitor')," +
                "(4, 1, 26, 'Yanahuara')," +
                "(4, 1, 27, 'Yarabamba')," +
                "(4, 1, 28, 'Yura')," +
                "(4, 1, 29, 'Jose Luis Bustamante y Rivero')," +
                "(4, 2, 0, 'Camana')," +
                "(4, 2, 1, 'Camana')," +
                "(4, 2, 2, 'Jose Maria Quimper')," +
                "(4, 2, 3, 'Mariano Nicolas Valcarcel')," +
                "(4, 2, 4, 'Mariscal Caceres')," +
                "(4, 2, 5, 'Nicolas de Pierola')," +
                "(4, 2, 6, 'Ocoña')," +
                "(4, 2, 7, 'Quilca')" ;
    }

    private static String instructionUbigeosTwo() {

        return "INSERT INTO ".concat(TABLE_UBIGEO) + " (CODE_DEPARTAMENTO, CODE_PROVINCIA, CODE_DISTRITO, NOMBRE) VALUES" +
                "(4, 2, 8, 'Samuel Pastor')," +
                "(4, 3, 0, 'Caraveli')," +
                "(4, 3, 1, 'Caraveli')," +
                "(4, 3, 2, 'Acari')," +
                "(4, 3, 3, 'Atico')," +
                "(4, 3, 4, 'Atiquipa')," +
                "(4, 3, 5, 'Bella Union')," +
                "(4, 3, 6, 'Cahuacho')," +
                "(4, 3, 7, 'Chala')," +
                "(4, 3, 8, 'Chaparra')," +
                "(4, 3, 9, 'Huanuhuanu')," +
                "(4, 3, 10, 'Jaqui')," +
                "(4, 3, 11, 'Lomas')," +
                "(4, 3, 12, 'Quicacha')," +
                "(4, 3, 13, 'Yauca')," +
                "(4, 4, 0, 'Castilla')," +
                "(4, 4, 1, 'Aplao')," +
                "(4, 4, 2, 'Andagua')," +
                "(4, 4, 3, 'Ayo')," +
                "(4, 4, 4, 'Chachas')," +
                "(4, 4, 5, 'Chilcaymarca')," +
                "(4, 4, 6, 'Choco')," +
                "(4, 4, 7, 'Huancarqui')," +
                "(4, 4, 8, 'Machaguay')," +
                "(4, 4, 9, 'Orcopampa')," +
                "(4, 4, 10, 'Pampacolca')," +
                "(4, 4, 11, 'Tipan')," +
                "(4, 4, 12, 'Uñon')," +
                "(4, 4, 13, 'Uraca')," +
                "(4, 4, 14, 'Viraco')," +
                "(4, 5, 0, 'Caylloma')," +
                "(4, 5, 1, 'Chivay')," +
                "(4, 5, 2, 'Achoma')," +
                "(4, 5, 3, 'Cabanaconde')," +
                "(4, 5, 4, 'Callalli')," +
                "(4, 5, 5, 'Caylloma')," +
                "(4, 5, 6, 'Coporaque')," +
                "(4, 5, 7, 'Huambo')," +
                "(4, 5, 8, 'Huanca')," +
                "(4, 5, 9, 'Ichupampa')," +
                "(4, 5, 10, 'Lari')," +
                "(4, 5, 11, 'Lluta')," +
                "(4, 5, 12, 'Maca')," +
                "(4, 5, 13, 'Madrigal')," +
                "(4, 5, 14, 'San Antonio de Chuca')," +
                "(4, 5, 15, 'Sibayo')," +
                "(4, 5, 16, 'Tapay')," +
                "(4, 5, 17, 'Tisco')," +
                "(4, 5, 18, 'Tuti')," +
                "(4, 5, 19, 'Yanque')," +
                "(4, 5, 20, 'Majes')," +
                "(4, 6, 0, 'Condesuyos')," +
                "(4, 6, 1, 'Chuquibamba')," +
                "(4, 6, 2, 'Andaray')," +
                "(4, 6, 3, 'Cayarani')," +
                "(4, 6, 4, 'Chichas')," +
                "(4, 6, 5, 'Iray')," +
                "(4, 6, 6, 'Rio Grande')," +
                "(4, 6, 7, 'Salamanca')," +
                "(4, 6, 8, 'Yanaquihua')," +
                "(4, 7, 0, 'Islay')," +
                "(4, 7, 1, 'Mollendo')," +
                "(4, 7, 2, 'Cocachacra')," +
                "(4, 7, 3, 'Dean Valdivia')," +
                "(4, 7, 4, 'Islay')," +
                "(4, 7, 5, 'Mejia')," +
                "(4, 7, 6, 'Punta de Bombon')," +
                "(4, 8, 0, 'La Union')," +
                "(4, 8, 1, 'Cotahuasi')," +
                "(4, 8, 2, 'Alca')," +
                "(4, 8, 3, 'Charcana')," +
                "(4, 8, 4, 'Huaynacotas')," +
                "(4, 8, 5, 'Pampamarca')," +
                "(4, 8, 6, 'Puyca')," +
                "(4, 8, 7, 'Quechualla')," +
                "(4, 8, 8, 'Sayla')," +
                "(4, 8, 9, 'Tauria')," +
                "(4, 8, 10, 'Tomepampa')," +
                "(4, 8, 11, 'Toro')," +
                "(5, 0, 0, 'Ayacucho')," +
                "(5, 1, 0, 'Huamanga')," +
                "(5, 1, 1, 'Ayacucho')," +
                "(5, 1, 2, 'Acocro')," +
                "(5, 1, 3, 'Acos Vinchos')," +
                "(5, 1, 4, 'Carmen Alto')," +
                "(5, 1, 5, 'Chiara')," +
                "(5, 1, 6, 'Ocros')," +
                "(5, 1, 7, 'Pacaycasa')," +
                "(5, 1, 8, 'Quinua')," +
                "(5, 1, 9, 'San Jose de Ticllas')," +
                "(5, 1, 10, 'San Juan Bautista')," +
                "(5, 1, 11, 'Santiago de Pischa')," +
                "(5, 1, 12, 'Socos')," +
                "(5, 1, 13, 'Tambillo')," +
                "(5, 1, 14, 'Vinchos')," +
                "(5, 1, 15, 'Jesús Nazareno')," +
                "(5, 1, 16, 'Andrés Avelino Cáceres Dorregay')," +
                "(5, 2, 0, 'Cangallo')," +
                "(5, 2, 1, 'Cangallo')," +
                "(5, 2, 2, 'Chuschi')," +
                "(5, 2, 3, 'Los Morochucos')," +
                "(5, 2, 4, 'Maria Parado de Bellido')," +
                "(5, 2, 5, 'Paras')," +
                "(5, 2, 6, 'Totos')," +
                "(5, 3, 0, 'Huanca Sancos')," +
                "(5, 3, 1, 'Sancos')," +
                "(5, 3, 2, 'Carapo')," +
                "(5, 3, 3, 'Sacsamarca')," +
                "(5, 3, 4, 'Santiago de Lucanamarca')," +
                "(5, 4, 0, 'Huanta')," +
                "(5, 4, 1, 'Huanta')," +
                "(5, 4, 2, 'Ayahuanco')," +
                "(5, 4, 3, 'Huamanguilla')," +
                "(5, 4, 4, 'Iguain')," +
                "(5, 4, 5, 'Luricocha')," +
                "(5, 4, 6, 'Santillana')," +
                "(5, 4, 7, 'Sivia')," +
                "(5, 4, 8, 'Llochegua')," +
                "(5, 4, 9, 'Canayre')," +
                "(5, 4, 10, 'Uchuraccay')," +
                "(5, 4, 11, 'Pucacolpa')," +
                "(5, 5, 0, 'La Mar')," +
                "(5, 5, 1, 'San Miguel')," +
                "(5, 5, 2, 'Anco')," +
                "(5, 5, 3, 'Ayna')," +
                "(5, 5, 4, 'Chilcas')," +
                "(5, 5, 5, 'Chungui')," +
                "(5, 5, 6, 'Luis Carranza')," +
                "(5, 5, 7, 'Santa Rosa')," +
                "(5, 5, 8, 'Tambo')," +
                "(5, 5, 9, 'Samugari')," +
                "(5, 5, 10, 'Anchihuay')," +
                "(5, 6, 0, 'Lucanas')," +
                "(5, 6, 1, 'Puquio')," +
                "(5, 6, 2, 'Aucara')," +
                "(5, 6, 3, 'Cabana')," +
                "(5, 6, 4, 'Carmen Salcedo')," +
                "(5, 6, 5, 'Chaviña')," +
                "(5, 6, 6, 'Chipao')," +
                "(5, 6, 7, 'Huac-Huas')," +
                "(5, 6, 8, 'Laramate')," +
                "(5, 6, 9, 'Leoncio Prado')," +
                "(5, 6, 10, 'Llauta')," +
                "(5, 6, 11, 'Lucanas')," +
                "(5, 6, 12, 'Ocaña')," +
                "(5, 6, 13, 'Otoca')," +
                "(5, 6, 14, 'Saisa')," +
                "(5, 6, 15, 'San Cristobal')," +
                "(5, 6, 16, 'San Juan')," +
                "(5, 6, 17, 'San Pedro')," +
                "(5, 6, 18, 'San Pedro de Palco')," +
                "(5, 6, 19, 'Sancos')," +
                "(5, 6, 20, 'Santa Ana de Huaycahuacho')," +
                "(5, 6, 21, 'Santa Lucia')," +
                "(5, 7, 0, 'Parinacochas')," +
                "(5, 7, 1, 'Coracora')," +
                "(5, 7, 2, 'Chumpi')," +
                "(5, 7, 3, 'Coronel Castañeda')," +
                "(5, 7, 4, 'Pacapausa')," +
                "(5, 7, 5, 'Pullo')," +
                "(5, 7, 6, 'Puyusca')," +
                "(5, 7, 7, 'San Francisco de Ravacayco')," +
                "(5, 7, 8, 'Upahuacho')," +
                "(5, 8, 0, 'Paucar del Sara Sara')," +
                "(5, 8, 1, 'Pausa')," +
                "(5, 8, 2, 'Colta')," +
                "(5, 8, 3, 'Corculla')," +
                "(5, 8, 4, 'Lampa')," +
                "(5, 8, 5, 'Marcabamba')," +
                "(5, 8, 6, 'Oyolo')," +
                "(5, 8, 7, 'Pararca')," +
                "(5, 8, 8, 'San Javier de Alpabamba')," +
                "(5, 8, 9, 'San Jose de Ushua')," +
                "(5, 8, 10, 'Sara Sara')," +
                "(5, 9, 0, 'Sucre')," +
                "(5, 9, 1, 'Querobamba')," +
                "(5, 9, 2, 'Belen')," +
                "(5, 9, 3, 'Chalcos')," +
                "(5, 9, 4, 'Chilcayoc')," +
                "(5, 9, 5, 'Huacaña')," +
                "(5, 9, 6, 'Morcolla')," +
                "(5, 9, 7, 'Paico')," +
                "(5, 9, 8, 'San Pedro de Larcay')," +
                "(5, 9, 9, 'San Salvador de Quije')," +
                "(5, 9, 10, 'Santiago de Paucaray')," +
                "(5, 9, 11, 'Soras')," +
                "(5, 10, 0, 'Victor Fajardo')," +
                "(5, 10, 1, 'Huancapi')," +
                "(5, 10, 2, 'Alcamenca')," +
                "(5, 10, 3, 'Apongo')," +
                "(5, 10, 4, 'Asquipata')," +
                "(5, 10, 5, 'Canaria')," +
                "(5, 10, 6, 'Cayara')," +
                "(5, 10, 7, 'Colca')," +
                "(5, 10, 8, 'Huamanquiquia')," +
                "(5, 10, 9, 'Huancaraylla')," +
                "(5, 10, 10, 'Huaya')," +
                "(5, 10, 11, 'Sarhua')," +
                "(5, 10, 12, 'Vilcanchos')," +
                "(5, 11, 0, 'Vilcas Huaman')," +
                "(5, 11, 1, 'Vilcas Huaman')," +
                "(5, 11, 2, 'Accomarca')," +
                "(5, 11, 3, 'Carhuanca')," +
                "(5, 11, 4, 'Concepcion')," +
                "(5, 11, 5, 'Huambalpa')," +
                "(5, 11, 6, 'Independencia')," +
                "(5, 11, 7, 'Saurama')," +
                "(5, 11, 8, 'Vischongo')," +
                "(6, 0, 0, 'Cajamarca')," +
                "(6, 1, 0, 'Cajamarca')," +
                "(6, 1, 1, 'Cajamarca')," +
                "(6, 1, 2, 'Asuncion')," +
                "(6, 1, 3, 'Chetilla')," +
                "(6, 1, 4, 'Cospan')," +
                "(6, 1, 5, 'Encañada')," +
                "(6, 1, 6, 'Jesus')," +
                "(6, 1, 7, 'Llacanora')," +
                "(6, 1, 8, 'Los Baños del Inca')," +
                "(6, 1, 9, 'Magdalena')," +
                "(6, 1, 10, 'Matara')," +
                "(6, 1, 11, 'Namora')," +
                "(6, 1, 12, 'San Juan')," +
                "(6, 2, 0, 'Cajabamba')," +
                "(6, 2, 1, 'Cajabamba')," +
                "(6, 2, 2, 'Cachachi')," +
                "(6, 2, 3, 'Condebamba')," +
                "(6, 2, 4, 'Sitacocha')," +
                "(6, 3, 0, 'Celendin')," +
                "(6, 3, 1, 'Celendin')," +
                "(6, 3, 2, 'Chumuch')," +
                "(6, 3, 3, 'Cortegana')," +
                "(6, 3, 4, 'Huasmin')," +
                "(6, 3, 5, 'Jorge Chavez')," +
                "(6, 3, 6, 'Jose Galvez')," +
                "(6, 3, 7, 'Miguel Iglesias')," +
                "(6, 3, 8, 'Oxamarca')," +
                "(6, 3, 9, 'Sorochuco')," +
                "(6, 3, 10, 'Sucre')," +
                "(6, 3, 11, 'Utco')," +
                "(6, 3, 12, 'La Libertad de Pallan')," +
                "(6, 4, 0, 'Chota')," +
                "(6, 4, 1, 'Chota')," +
                "(6, 4, 2, 'Anguia')," +
                "(6, 4, 3, 'Chadin')," +
                "(6, 4, 4, 'Chiguirip')," +
                "(6, 4, 5, 'Chimban')," +
                "(6, 4, 6, 'Choropampa')," +
                "(6, 4, 7, 'Cochabamba')," +
                "(6, 4, 8, 'Conchan')," +
                "(6, 4, 9, 'Huambos')," +
                "(6, 4, 10, 'Lajas')," +
                "(6, 4, 11, 'Llama')," +
                "(6, 4, 12, 'Miracosta')," +
                "(6, 4, 13, 'Paccha')," +
                "(6, 4, 14, 'Pion')," +
                "(6, 4, 15, 'Querocoto')," +
                "(6, 4, 16, 'San Juan de Licupis')," +
                "(6, 4, 17, 'Tacabamba')," +
                "(6, 4, 18, 'Tocmoche')," +
                "(6, 4, 19, 'Chalamarca')," +
                "(6, 5, 0, 'Contumaza')," +
                "(6, 5, 1, 'Contumaza')," +
                "(6, 5, 2, 'Chilete')," +
                "(6, 5, 3, 'Cupisnique')," +
                "(6, 5, 4, 'Guzmango')," +
                "(6, 5, 5, 'San Benito')," +
                "(6, 5, 6, 'Santa Cruz de Toled')," +
                "(6, 5, 7, 'Tantarica')," +
                "(6, 5, 8, 'Yonan')," +
                "(6, 6, 0, 'Cutervo')," +
                "(6, 6, 1, 'Cutervo')," +
                "(6, 6, 2, 'Callayuc')," +
                "(6, 6, 3, 'Choros')," +
                "(6, 6, 4, 'Cujillo')," +
                "(6, 6, 5, 'La Ramada')," +
                "(6, 6, 6, 'Pimpingos')," +
                "(6, 6, 7, 'Querocotillo')," +
                "(6, 6, 8, 'San Andres de Cutervo')," +
                "(6, 6, 9, 'San Juan de Cutervo')," +
                "(6, 6, 10, 'San Luis de Lucma')," +
                "(6, 6, 11, 'Santa Cruz')," +
                "(6, 6, 12, 'Santo Domingo de la Capilla')," +
                "(6, 6, 13, 'Santo Tomas')," +
                "(6, 6, 14, 'Socota')," +
                "(6, 6, 15, 'Toribio Casanova')," +
                "(6, 7, 0, 'Hualgayoc')," +
                "(6, 7, 1, 'Bambamarca')," +
                "(6, 7, 2, 'Chugur')," +
                "(6, 7, 3, 'Hualgayoc')," +
                "(6, 8, 0, 'Jaen')";



    }


    private static String instructionUbigeosEight() {

        return "INSERT INTO ".concat(TABLE_UBIGEO) + " (CODE_DEPARTAMENTO, CODE_PROVINCIA, CODE_DISTRITO, NOMBRE) VALUES" +

                "(6, 8, 1, 'Jaen')," +
                "(6, 8, 2, 'Bellavista')," +
                "(6, 8, 3, 'Chontali')," +
                "(6, 8, 4, 'Colasay')," +
                "(6, 8, 5, 'Huabal')," +
                "(6, 8, 6, 'Las Pirias')," +
                "(6, 8, 7, 'Pomahuaca')," +
                "(6, 8, 8, 'Pucara')," +
                "(6, 8, 9, 'Sallique')," +
                "(6, 8, 10, 'San Felipe')," +
                "(6, 8, 11, 'San Jose del Alto')," +
                "(6, 8, 12, 'Santa Rosa')," +
                "(6, 9, 0, 'San Ignacio')," +
                "(6, 9, 1, 'San Ignacio')," +
                "(6, 9, 2, 'Chirinos')," +
                "(6, 9, 3, 'Huarango')," +
                "(6, 9, 4, 'La Coipa')," +
                "(6, 9, 5, 'Namballe')," +
                "(6, 9, 6, 'San Jose de Lourdes')," +
                "(6, 9, 7, 'Tabaconas')," +
                "(6, 10, 0, 'San Marcos')," +
                "(6, 10, 1, 'Pedro Galvez')," +
                "(6, 10, 2, 'Chancay')," +
                "(6, 10, 3, 'Eduardo Villanueva')," +
                "(6, 10, 4, 'Gregorio Pita')," +
                "(6, 10, 5, 'Ichocan')," +
                "(6, 10, 6, 'Jose Manuel Quiroz')," +
                "(6, 10, 7, 'Jose Sabogal')," +
                "(6, 11, 0, 'San Miguel')," +
                "(6, 11, 1, 'San Miguel')," +
                "(6, 11, 2, 'Bolivar')," +
                "(6, 11, 3, 'Calquis')," +
                "(6, 11, 4, 'Catilluc')," +
                "(6, 11, 5, 'El Prado')," +
                "(6, 11, 6, 'La Florida')," +
                "(6, 11, 7, 'Llapa')," +
                "(6, 11, 8, 'Nanchoc')," +
                "(6, 11, 9, 'Niepos')," +
                "(6, 11, 10, 'San Gregorio')," +
                "(6, 11, 11, 'San Silvestre de Cochan')," +
                "(6, 11, 12, 'Tongod')," +
                "(6, 11, 13, 'Union Agua Blanca')," +
                "(6, 12, 0, 'San Pablo')," +
                "(6, 12, 1, 'San Pablo')," +
                "(6, 12, 2, 'San Bernardino')," +
                "(6, 12, 3, 'San Luis')," +
                "(6, 12, 4, 'Tumbaden')," +
                "(6, 13, 0, 'Santa Cruz')," +
                "(6, 13, 1, 'Santa Cruz')," +
                "(6, 13, 2, 'Andabamba')," +
                "(6, 13, 3, 'Catache')," +
                "(6, 13, 4, 'Chancaybaños')," +
                "(6, 13, 5, 'La Esperanza')," +
                "(6, 13, 6, 'Ninabamba')," +
                "(6, 13, 7, 'Pulan')," +
                "(6, 13, 8, 'Saucepampa')," +
                "(6, 13, 9, 'Sexi')," +
                "(6, 13, 10, 'Uticyacu')," +
                "(6, 13, 11, 'Yauyucan')," +
                "(7, 0, 0, 'Callao')," +
                "(7, 1, 0, 'Prov. Const. del Callao')," +
                "(7, 1, 1, 'Callao')," +
                "(7, 1, 2, 'Bellavista')," +
                "(7, 1, 3, 'Carmen de la Legua Reynoso')," +
                "(7, 1, 4, 'La Perla')," +
                "(7, 1, 5, 'La Punta')," +
                "(7, 1, 6, 'Ventanilla')," +
                "(7, 1, 7, 'Mi Perú')," +
                "(8, 0, 0, 'Cusco')," +
                "(8, 1, 0, 'Cusco')," +
                "(8, 1, 1, 'Cusco')," +
                "(8, 1, 2, 'Ccorca')," +
                "(8, 1, 3, 'Poroy')," +
                "(8, 1, 4, 'San Jeronimo')," +
                "(8, 1, 5, 'San Sebastian')," +
                "(8, 1, 6, 'Santiago')," +
                "(8, 1, 7, 'Saylla')," +
                "(8, 1, 8, 'Wanchaq')," +
                "(8, 2, 0, 'Acomayo')," +
                "(8, 2, 1, 'Acomayo')," +
                "(8, 2, 2, 'Acopia')," +
                "(8, 2, 3, 'Acos')," +
                "(8, 2, 4, 'Mosoc Llacta')," +
                "(8, 2, 5, 'Pomacanchi')," +
                "(8, 2, 6, 'Rondocan')," +
                "(8, 2, 7, 'Sangarara')," +
                "(8, 3, 0, 'Anta')," +
                "(8, 3, 1, 'Anta')," +
                "(8, 3, 2, 'Ancahuasi')," +
                "(8, 3, 3, 'Cachimayo')," +
                "(8, 3, 4, 'Chinchaypujio')," +
                "(8, 3, 5, 'Huarocondo')," +
                "(8, 3, 6, 'Limatambo')," +
                "(8, 3, 7, 'Mollepata')," +
                "(8, 3, 8, 'Pucyura')," +
                "(8, 3, 9, 'Zurite')," +
                "(8, 4, 0, 'Calca')," +
                "(8, 4, 1, 'Calca')," +
                "(8, 4, 2, 'Coya')," +
                "(8, 4, 3, 'Lamay')," +
                "(8, 4, 4, 'Lares')," +
                "(8, 4, 5, 'Pisac')," +
                "(8, 4, 6, 'San Salvador')," +
                "(8, 4, 7, 'Taray')," +
                "(8, 4, 8, 'Yanatile')," +
                "(8, 5, 0, 'Canas')," +
                "(8, 5, 1, 'Yanaoca')," +
                "(8, 5, 2, 'Checca')," +
                "(8, 5, 3, 'Kunturkanki')," +
                "(8, 5, 4, 'Langui')," +
                "(8, 5, 5, 'Layo')," +
                "(8, 5, 6, 'Pampamarca')," +
                "(8, 5, 7, 'Quehue')," +
                "(8, 5, 8, 'Tupac Amaru')," +
                "(8, 6, 0, 'Canchis')," +
                "(8, 6, 1, 'Sicuani')," +
                "(8, 6, 2, 'Checacupe')," +
                "(8, 6, 3, 'Combapata')," +
                "(8, 6, 4, 'Marangani')," +
                "(8, 6, 5, 'Pitumarca')," +
                "(8, 6, 6, 'San Pablo')," +
                "(8, 6, 7, 'San Pedro')," +
                "(8, 6, 8, 'Tinta')," +
                "(8, 7, 0, 'Chumbivilcas')," +
                "(8, 7, 1, 'Santo Tomas')," +
                "(8, 7, 2, 'Capacmarca')," +
                "(8, 7, 3, 'Chamaca')," +
                "(8, 7, 4, 'Colquemarca')," +
                "(8, 7, 5, 'Livitaca')," +
                "(8, 7, 6, 'Llusco')," +
                "(8, 7, 7, 'Quiñota')," +
                "(8, 7, 8, 'Velille')," +
                "(8, 8, 0, 'Espinar')," +
                "(8, 8, 1, 'Espinar')," +
                "(8, 8, 2, 'Condoroma')," +
                "(8, 8, 3, 'Coporaque')," +
                "(8, 8, 4, 'Ocoruro')," +
                "(8, 8, 5, 'Pallpata')," +
                "(8, 8, 6, 'Pichigua')," +
                "(8, 8, 7, 'Suyckutambo')," +
                "(8, 8, 8, 'Alto Pichigua')," +
                "(8, 9, 0, 'La Convencion')," +
                "(8, 9, 1, 'Santa Ana')," +
                "(8, 9, 2, 'Echarate')," +
                "(8, 9, 3, 'Huayopata')," +
                "(8, 9, 4, 'Maranura')," +
                "(8, 9, 5, 'Ocobamba')," +
                "(8, 9, 6, 'Quellouno')," +
                "(8, 9, 7, 'Kimbiri')," +
                "(8, 9, 8, 'Santa Teresa')," +
                "(8, 9, 9, 'Vilcabamba')," +
                "(8, 9, 10, 'Pichari')," +
                "(8, 9, 11, 'Inkawasi')," +
                "(8, 9, 12, 'Villa Virgen')," +
                "(8, 10, 0, 'Paruro')," +
                "(8, 10, 1, 'Paruro')," +
                "(8, 10, 2, 'Accha')," +
                "(8, 10, 3, 'Ccapi')," +
                "(8, 10, 4, 'Colcha')," +
                "(8, 10, 5, 'Huanoquite')," +
                "(8, 10, 6, 'Omacha')," +
                "(8, 10, 7, 'Paccaritambo')," +
                "(8, 10, 8, 'Pillpinto')," +
                "(8, 10, 9, 'Yaurisque')," +
                "(8, 11, 0, 'Paucartambo')," +
                "(8, 11, 1, 'Paucartambo')," +
                "(8, 11, 2, 'Caicay')," +
                "(8, 11, 3, 'Challabamba')," +
                "(8, 11, 4, 'Colquepata')," +
                "(8, 11, 5, 'Huancarani')," +
                "(8, 11, 6, 'Kosñipata')," +
                "(8, 12, 0, 'Quispicanchi')," +
                "(8, 12, 1, 'Urcos')," +
                "(8, 12, 2, 'Andahuaylillas')," +
                "(8, 12, 3, 'Camanti')," +
                "(8, 12, 4, 'Ccarhuayo')," +
                "(8, 12, 5, 'Ccatca')," +
                "(8, 12, 6, 'Cusipata')," +
                "(8, 12, 7, 'Huaro')," +
                "(8, 12, 8, 'Lucre')," +
                "(8, 12, 9, 'Marcapata')," +
                "(8, 12, 10, 'Ocongate')," +
                "(8, 12, 11, 'Oropesa')," +
                "(8, 12, 12, 'Quiquijana')," +
                "(8, 13, 0, 'Urubamba')," +
                "(8, 13, 1, 'Urubamba')," +
                "(8, 13, 2, 'Chinchero')," +
                "(8, 13, 3, 'Huayllabamba')," +
                "(8, 13, 4, 'Machupicchu')," +
                "(8, 13, 5, 'Maras')," +
                "(8, 13, 6, 'Ollantaytambo')," +
                "(8, 13, 7, 'Yucay')," +
                "(9, 0, 0, 'Huancavelica')," +
                "(9, 1, 0, 'Huancavelica')," +
                "(9, 1, 1, 'Huancavelica')," +
                "(9, 1, 2, 'Acobambilla')," +
                "(9, 1, 3, 'Acoria')," +
                "(9, 1, 4, 'Conayca')," +
                "(9, 1, 5, 'Cuenca')," +
                "(9, 1, 6, 'Huachocolpa')," +
                "(9, 1, 7, 'Huayllahuara')," +
                "(9, 1, 8, 'Izcuchaca')," +
                "(9, 1, 9, 'Laria')," +
                "(9, 1, 10, 'Manta')," +
                "(9, 1, 11, 'Mariscal Caceres')," +
                "(9, 1, 12, 'Moya')," +
                "(9, 1, 13, 'Nuevo Occoro')," +
                "(9, 1, 14, 'Palca')," +
                "(9, 1, 15, 'Pilchaca')";
    }


    private static String instructionUbigeosTSeven() {

        return "INSERT INTO ".concat(TABLE_UBIGEO) + " (CODE_DEPARTAMENTO, CODE_PROVINCIA, CODE_DISTRITO, NOMBRE) VALUES" +

                "(9, 1, 16, 'Vilca')," +
                "(9, 1, 17, 'Yauli')," +
                "(9, 1, 18, 'Ascensión')," +
                "(9, 1, 19, 'Huando')," +
                "(9, 2, 0, 'Acobamba')," +
                "(9, 2, 1, 'Acobamba')," +
                "(9, 2, 2, 'Andabamba')," +
                "(9, 2, 3, 'Anta')," +
                "(9, 2, 4, 'Caja')," +
                "(9, 2, 5, 'Marcas')," +
                "(9, 2, 6, 'Paucara')," +
                "(9, 2, 7, 'Pomacocha')," +
                "(9, 2, 8, 'Rosario')," +
                "(9, 3, 0, 'Angaraes')," +
                "(9, 3, 1, 'Lircay')," +
                "(9, 3, 2, 'Anchonga')," +
                "(9, 3, 3, 'Callanmarca')," +
                "(9, 3, 4, 'Ccochaccasa')," +
                "(9, 3, 5, 'Chincho')," +
                "(9, 3, 6, 'Congalla')," +
                "(9, 3, 7, 'Huanca-Huanca')," +
                "(9, 3, 8, 'Huayllay Grande')," +
                "(9, 3, 9, 'Julcamarca')," +
                "(9, 3, 10, 'San Antonio de Antaparco')," +
                "(9, 3, 11, 'Santo Tomas de Pata')," +
                "(9, 3, 12, 'Secclla')," +
                "(9, 4, 0, 'Castrovirreyna')," +
                "(9, 4, 1, 'Castrovirreyna')," +
                "(9, 4, 2, 'Arma')," +
                "(9, 4, 3, 'Aurahua')," +
                "(9, 4, 4, 'Capillas')," +
                "(9, 4, 5, 'Chupamarca')," +
                "(9, 4, 6, 'Cocas')," +
                "(9, 4, 7, 'Huachos')," +
                "(9, 4, 8, 'Huamatambo')," +
                "(9, 4, 9, 'Mollepampa')," +
                "(9, 4, 10, 'San Juan')," +
                "(9, 4, 11, 'Santa Ana')," +
                "(9, 4, 12, 'Tantara')," +
                "(9, 4, 13, 'Ticrapo')," +
                "(9, 5, 0, 'Churcampa')," +
                "(9, 5, 1, 'Churcampa')," +
                "(9, 5, 2, 'Anco')," +
                "(9, 5, 3, 'Chinchihuasi')," +
                "(9, 5, 4, 'El Carmen')," +
                "(9, 5, 5, 'La Merced')," +
                "(9, 5, 6, 'Locroja')," +
                "(9, 5, 7, 'Paucarbamba')," +
                "(9, 5, 8, 'San Miguel de Mayocc')," +
                "(9, 5, 9, 'San Pedro de Coris')," +
                "(9, 5, 10, 'Pachamarca')," +
                "(9, 5, 11, 'Cosme')," +
                "(9, 6, 0, 'Huaytara')," +
                "(9, 6, 1, 'Huaytara')," +
                "(9, 6, 2, 'Ayavi')," +
                "(9, 6, 3, 'Cordova')," +
                "(9, 6, 4, 'Huayacundo Arma')," +
                "(9, 6, 5, 'Laramarca')," +
                "(9, 6, 6, 'Ocoyo')," +
                "(9, 6, 7, 'Pilpichaca')," +
                "(9, 6, 8, 'Querco')," +
                "(9, 6, 9, 'Quito-Arma')," +
                "(9, 6, 10, 'San Antonio de Cusicancha')," +
                "(9, 6, 11, 'San Francisco de Sangayaico')," +
                "(9, 6, 12, 'San Isidro')," +
                "(9, 6, 13, 'Santiago de Chocorvos')," +
                "(9, 6, 14, 'Santiago de Quirahuara')," +
                "(9, 6, 15, 'Santo Domingo de Capillas')," +
                "(9, 6, 16, 'Tambo')," +
                "(9, 7, 0, 'Tayacaja')," +
                "(9, 7, 1, 'Pampas')," +
                "(9, 7, 2, 'Acostambo')," +
                "(9, 7, 3, 'Acraquia')," +
                "(9, 7, 4, 'Ahuaycha')," +
                "(9, 7, 5, 'Colcabamba')," +
                "(9, 7, 6, 'Daniel Hernandez')," +
                "(9, 7, 7, 'Huachocolpa')," +
                "(9, 7, 9, 'Huaribamba')," +
                "(9, 7, 10, 'ñahuimpuquio')," +
                "(9, 7, 11, 'Pazos')," +
                "(9, 7, 13, 'Quishuar')," +
                "(9, 7, 14, 'Salcabamba')," +
                "(9, 7, 15, 'Salcahuasi')," +
                "(9, 7, 16, 'San Marcos de Rocchac')," +
                "(9, 7, 17, 'Surcubamba')," +
                "(9, 7, 18, 'Tintay Puncu')," +
                "(10, 0, 0, 'Huanuco')," +
                "(10, 1, 0, 'Huanuco')," +
                "(10, 1, 1, 'Huanuco')," +
                "(10, 1, 2, 'Amarilis')," +
                "(10, 1, 3, 'Chinchao')," +
                "(10, 1, 4, 'Churubamba')," +
                "(10, 1, 5, 'Margos')," +
                "(10, 1, 6, 'Quisqui')," +
                "(10, 1, 7, 'San Francisco de Cayran')," +
                "(10, 1, 8, 'San Pedro de Chaulan')," +
                "(10, 1, 9, 'Santa Maria del Valle')," +
                "(10, 1, 10, 'Yarumayo')," +
                "(10, 1, 11, 'Pillco Marca')," +
                "(10, 1, 12, 'Yacus')," +
                "(10, 2, 0, 'Ambo')," +
                "(10, 2, 1, 'Ambo')," +
                "(10, 2, 2, 'Cayna')," +
                "(10, 2, 3, 'Colpas')," +
                "(10, 2, 4, 'Conchamarca')," +
                "(10, 2, 5, 'Huacar')," +
                "(10, 2, 6, 'San Francisco')," +
                "(10, 2, 7, 'San Rafael')," +
                "(10, 2, 8, 'Tomay Kichwa')," +
                "(10, 3, 0, 'Dos de Mayo')," +
                "(10, 3, 1, 'La Union')," +
                "(10, 3, 7, 'Chuquis')," +
                "(10, 3, 11, 'Marias')," +
                "(10, 3, 13, 'Pachas')," +
                "(10, 3, 16, 'Quivilla')," +
                "(10, 3, 17, 'Ripan')," +
                "(10, 3, 21, 'Shunqui')," +
                "(10, 3, 22, 'Sillapata')," +
                "(10, 3, 23, 'Yanas')," +
                "(10, 4, 0, 'Huacaybamba')," +
                "(10, 4, 1, 'Huacaybamba')," +
                "(10, 4, 2, 'Canchabamba')," +
                "(10, 4, 3, 'Cochabamba')," +
                "(10, 4, 4, 'Pinra')," +
                "(10, 5, 0, 'Huamalies')," +
                "(10, 5, 1, 'Llata')," +
                "(10, 5, 2, 'Arancay')," +
                "(10, 5, 3, 'Chavin de Pariarca')," +
                "(10, 5, 4, 'Jacas Grande')," +
                "(10, 5, 5, 'Jircan')," +
                "(10, 5, 6, 'Miraflores')," +
                "(10, 5, 7, 'Monzon')," +
                "(10, 5, 8, 'Punchao')," +
                "(10, 5, 9, 'Puños')," +
                "(10, 5, 10, 'Singa')," +
                "(10, 5, 11, 'Tantamayo')," +
                "(10, 6, 0, 'Leoncio Prado')," +
                "(10, 6, 1, 'Rupa-Rupa')," +
                "(10, 6, 2, 'Daniel Alomias Robles')," +
                "(10, 6, 3, 'Hermilio Valdizan')," +
                "(10, 6, 4, 'Jose Crespo y Castillo')," +
                "(10, 6, 5, 'Luyando')," +
                "(10, 6, 6, 'Mariano Damaso Beraun')," +
                "(10, 7, 0, 'Marañon')," +
                "(10, 7, 1, 'Huacrachuco')," +
                "(10, 7, 2, 'Cholon')," +
                "(10, 7, 3, 'San Buenaventura')," +
                "(10, 8, 0, 'Pachitea')," +
                "(10, 8, 1, 'Panao')," +
                "(10, 8, 2, 'Chaglla')," +
                "(10, 8, 3, 'Molino')," +
                "(10, 8, 4, 'Umari')," +
                "(10, 9, 0, 'Puerto Inca')," +
                "(10, 9, 1, 'Puerto Inca')," +
                "(10, 9, 2, 'Codo del Pozuzo')," +
                "(10, 9, 3, 'Honoria')," +
                "(10, 9, 4, 'Tournavista')," +
                "(10, 9, 5, 'Yuyapichis')," +
                "(10, 10, 0, 'Lauricocha')," +
                "(10, 10, 1, 'Jesus')," +
                "(10, 10, 2, 'Baños')," +
                "(10, 10, 3, 'Jivia')," +
                "(10, 10, 4, 'Queropalca')," +
                "(10, 10, 5, 'Rondos')," +
                "(10, 10, 6, 'San Francisco de Asis')," +
                "(10, 10, 7, 'San Miguel de Cauri')," +
                "(10, 11, 0, 'Yarowilca')," +
                "(10, 11, 1, 'Chavinillo')," +
                "(10, 11, 2, 'Cahuac')," +
                "(10, 11, 3, 'Chacabamba')," +
                "(10, 11, 4, 'Chupan')," +
                "(10, 11, 5, 'Jacas Chico')," +
                "(10, 11, 6, 'Obas')," +
                "(10, 11, 7, 'Pampamarca')," +
                "(10, 11, 8, 'Choras')," +
                "(11, 0, 0, 'Ica')," +
                "(11, 1, 0, 'Ica')," +
                "(11, 1, 1, 'Ica')," +
                "(11, 1, 2, 'La Tinguiña')," +
                "(11, 1, 3, 'Los Aquijes')," +
                "(11, 1, 4, 'Ocucaje')," +
                "(11, 1, 5, 'Pachacutec')," +
                "(11, 1, 6, 'Parcona')," +
                "(11, 1, 7, 'Pueblo Nuevo')," +
                "(11, 1, 8, 'Salas')," +
                "(11, 1, 9, 'San Jose de los Molinos')," +
                "(11, 1, 10, 'San Juan Bautista')," +
                "(11, 1, 11, 'Santiago')," +
                "(11, 1, 12, 'Subtanjalla')," +
                "(11, 1, 13, 'Tate')," +
                "(11, 1, 14, 'Yauca del Rosario')," +
                "(11, 2, 0, 'Chincha')," +
                "(11, 2, 1, 'Chincha Alta')," +
                "(11, 2, 2, 'Alto Laran')," +
                "(11, 2, 3, 'Chavin')," +
                "(11, 2, 4, 'Chincha Baja')," +
                "(11, 2, 5, 'El Carmen')," +
                "(11, 2, 6, 'Grocio Prado')," +
                "(11, 2, 7, 'Pueblo Nuevo')," +
                "(11, 2, 8, 'San Juan de Yanac')," +
                "(11, 2, 9, 'San Pedro de Huacarpana')," +
                "(11, 2, 10, 'Sunampe')," +
                "(11, 2, 11, 'Tambo de Mora')," +
                "(11, 3, 0, 'Nazca')," +
                "(11, 3, 1, 'Nazca')," +
                "(11, 3, 2, 'Changuillo')," +
                "(11, 3, 3, 'El Ingenio')," +
                "(11, 3, 4, 'Marcona')," +
                "(11, 3, 5, 'Vista Alegre')," +
                "(11, 4, 0, 'Palpa')," +
                "(11, 4, 1, 'Palpa')," +
                "(11, 4, 2, 'Llipata')," +
                "(11, 4, 3, 'Rio Grande')," +
                "(11, 4, 4, 'Santa Cruz')," +
                "(11, 4, 5, 'Tibillo')," +
                "(11, 5, 0, 'Pisco')," +
                "(11, 5, 1, 'Pisco')," +
                "(11, 5, 2, 'Huancano')," +
                "(11, 5, 3, 'Humay')," +
                "(11, 5, 4, 'Independencia')," +
                "(11, 5, 5, 'Paracas')," +
                "(11, 5, 6, 'San Andres')," +
                "(11, 5, 7, 'San Clemente')," +
                "(11, 5, 8, 'Tupac Amaru Inca')," +
                "(12, 0, 0, 'Junin')," +
                "(12, 1, 0, 'Huancayo')," +
                "(12, 1, 1, 'Huancayo')," +
                "(12, 1, 4, 'Carhuacallanga')," +
                "(12, 1, 5, 'Chacapampa')," +
                "(12, 1, 6, 'Chicche')," +
                "(12, 1, 7, 'Chilca')," +
                "(12, 1, 8, 'Chongos Alto')," +
                "(12, 1, 11, 'Chupuro')," +
                "(12, 1, 12, 'Colca')," +
                "(12, 1, 13, 'Cullhuas')," +
                "(12, 1, 14, 'El Tambo')," +
                "(12, 1, 16, 'Huacrapuquio')," +
                "(12, 1, 17, 'Hualhuas')," +
                "(12, 1, 19, 'Huancan')," +
                "(12, 1, 20, 'Huasicancha')," +
                "(12, 1, 21, 'Huayucachi')," +
                "(12, 1, 22, 'Ingenio')," +
                "(12, 1, 24, 'Pariahuanca')," +
                "(12, 1, 25, 'Pilcomayo')," +
                "(12, 1, 26, 'Pucara')," +
                "(12, 1, 27, 'Quichuay')," +
                "(12, 1, 28, 'Quilcas')," +
                "(12, 1, 29, 'San Agustin')," +
                "(12, 1, 30, 'San Jeronimo de Tunan')," +
                "(12, 1, 32, 'Saño')," +
                "(12, 1, 33, 'Sapallanga')," +
                "(12, 1, 34, 'Sicaya')," +
                "(12, 1, 35, 'Santo Domingo de Acobamba')," +
                "(12, 1, 36, 'Viques')," +
                "(12, 2, 0, 'Concepcion')," +
                "(12, 2, 1, 'Concepcion')," +
                "(12, 2, 2, 'Aco')," +
                "(12, 2, 3, 'Andamarca')," +
                "(12, 2, 4, 'Chambara')," +
                "(12, 2, 5, 'Cochas')," +
                "(12, 2, 6, 'Comas')," +
                "(12, 2, 7, 'Heroinas Toledo')," +
                "(12, 2, 8, 'Manzanares')," +
                "(12, 2, 9, 'Mariscal Castilla')," +
                "(12, 2, 10, 'Matahuasi')," +
                "(12, 2, 11, 'Mito')," +
                "(12, 2, 12, 'Nueve de Julio')," +
                "(12, 2, 13, 'Orcotuna')," +
                "(12, 2, 14, 'San Jose de Quero')," +
                "(12, 2, 15, 'Santa Rosa de Ocopa')," +
                "(12, 3, 0, 'Chanchamayo')," +
                "(12, 3, 1, 'Chanchamayo')," +
                "(12, 3, 2, 'Perene')," +
                "(12, 3, 3, 'Pichanaqui')," +
                "(12, 3, 4, 'San Luis de Shuaro')," +
                "(12, 3, 5, 'San Ramon')," +
                "(12, 3, 6, 'Vitoc')," +
                "(12, 4, 0, 'Jauja')," +
                "(12, 4, 1, 'Jauja')," +
                "(12, 4, 2, 'Acolla')," +
                "(12, 4, 3, 'Apata')," +
                "(12, 4, 4, 'Ataura')," +
                "(12, 4, 5, 'Canchayllo')," +
                "(12, 4, 6, 'Curicaca')," +
                "(12, 4, 7, 'El Mantaro')," +
                "(12, 4, 8, 'Huamali')," +
                "(12, 4, 9, 'Huaripampa')," +
                "(12, 4, 10, 'Huertas')," +
                "(12, 4, 11, 'Janjaillo')," +
                "(12, 4, 12, 'Julcan')," +
                "(12, 4, 13, 'Leonor Ordoñez')," +
                "(12, 4, 14, 'Llocllapampa')," +
                "(12, 4, 15, 'Marco')," +
                "(12, 4, 16, 'Masma')," +
                "(12, 4, 17, 'Masma Chicche')," +
                "(12, 4, 18, 'Molinos')," +
                "(12, 4, 19, 'Monobamba')," +
                "(12, 4, 20, 'Muqui')," +
                "(12, 4, 21, 'Muquiyauyo')," +
                "(12, 4, 22, 'Paca')," +
                "(12, 4, 23, 'Paccha')," +
                "(12, 4, 24, 'Pancan')," +
                "(12, 4, 25, 'Parco')," +
                "(12, 4, 26, 'Pomacancha')," +
                "(12, 4, 27, 'Ricran')," +
                "(12, 4, 28, 'San Lorenzo')," +
                "(12, 4, 29, 'San Pedro de Chunan')," +
                "(12, 4, 30, 'Sausa')," +
                "(12, 4, 31, 'Sincos')," +
                "(12, 4, 32, 'Tunan Marca')," +
                "(12, 4, 33, 'Yauli')," +
                "(12, 4, 34, 'Yauyos')," +
                "(12, 5, 0, 'Junin')," +
                "(12, 5, 1, 'Junin')," +
                "(12, 5, 2, 'Carhuamayo')," +
                "(12, 5, 3, 'Ondores')," +
                "(12, 5, 4, 'Ulcumayo')," +
                "(12, 6, 0, 'Satipo')," +
                "(12, 6, 1, 'Satipo')," +
                "(12, 6, 2, 'Coviriali')," +
                "(12, 6, 3, 'Llaylla')," +
                "(12, 6, 4, 'Mazamari')," +
                "(12, 6, 5, 'Pampa Hermosa')," +
                "(12, 6, 6, 'Pangoa')," +
                "(12, 6, 7, 'Rio Negro')," +
                "(12, 6, 8, 'Rio Tambo')," +
                "(12, 6, 99, 'Mazamari-Pangoa')," +
                "(12, 7, 0, 'Tarma')," +
                "(12, 7, 1, 'Tarma')," +
                "(12, 7, 2, 'Acobamba')," +
                "(12, 7, 3, 'Huaricolca')," +
                "(12, 7, 4, 'Huasahuasi')," +
                "(12, 7, 5, 'La Union')," +
                "(12, 7, 6, 'Palca')," +
                "(12, 7, 7, 'Palcamayo')," +
                "(12, 7, 8, 'San Pedro de Cajas')," +
                "(12, 7, 9, 'Tapo')," +
                "(12, 8, 0, 'Yauli')," +
                "(12, 8, 1, 'La Oroya')," +
                "(12, 8, 2, 'Chacapalpa')," +
                "(12, 8, 3, 'Huay-Huay')," +
                "(12, 8, 4, 'Marcapomacocha')," +
                "(12, 8, 5, 'Morococha')," +
                "(12, 8, 6, 'Paccha')," +
                "(12, 8, 7, 'Santa Barbara de Carhuacayan')," +
                "(12, 8, 8, 'Santa Rosa de Sacco')," +
                "(12, 8, 9, 'Suitucancha')," +
                "(12, 8, 10, 'Yauli')," +
                "(12, 9, 0, 'Chupaca')," +
                "(12, 9, 1, 'Chupaca')," +
                "(12, 9, 2, 'Ahuac')," +
                "(12, 9, 3, 'Chongos Bajo')," +
                "(12, 9, 4, 'Huachac')," +
                "(12, 9, 5, 'Huamancaca Chico')," +
                "(12, 9, 6, 'San Juan de Iscos')," +
                "(12, 9, 7, 'San Juan de Jarpa')," +
                "(12, 9, 8, '3 de Diciembre')," +
                "(12, 9, 9, 'Yanacancha')," +
                "(13, 0, 0, 'La Libertad')," +
                "(13, 1, 0, 'Trujillo')," +
                "(13, 1, 1, 'Trujillo')";
    }



    private static String instructionUbigeosThree() {

        return "INSERT INTO ".concat(TABLE_UBIGEO) + " (CODE_DEPARTAMENTO, CODE_PROVINCIA, CODE_DISTRITO, NOMBRE) VALUES" +
                "(13, 1, 2, 'El Porvenir')," +
                "(13, 1, 3, 'Florencia de Mora')," +
                "(13, 1, 4, 'Huanchaco')," +
                "(13, 1, 5, 'La Esperanza')," +
                "(13, 1, 6, 'Laredo')," +
                "(13, 1, 7, 'Moche')," +
                "(13, 1, 8, 'Poroto')," +
                "(13, 1, 9, 'Salaverry')," +
                "(13, 1, 10, 'Simbal')," +
                "(13, 1, 11, 'Victor Larco Herrera')," +
                "(13, 2, 0, 'Ascope')," +
                "(13, 2, 1, 'Ascope')," +
                "(13, 2, 2, 'Chicama')," +
                "(13, 2, 3, 'Chocope')," +
                "(13, 2, 4, 'Magdalena de Cao')," +
                "(13, 2, 5, 'Paijan')," +
                "(13, 2, 6, 'Razuri')," +
                "(13, 2, 7, 'Santiago de Cao')," +
                "(13, 2, 8, 'Casa Grande')," +
                "(13, 3, 0, 'Bolivar')," +
                "(13, 3, 1, 'Bolivar')," +
                "(13, 3, 2, 'Bambamarca')," +
                "(13, 3, 3, 'Condormarca')," +
                "(13, 3, 4, 'Longotea')," +
                "(13, 3, 5, 'Uchumarca')," +
                "(13, 3, 6, 'Ucuncha')," +
                "(13, 4, 0, 'Chepen')," +
                "(13, 4, 1, 'Chepen')," +
                "(13, 4, 2, 'Pacanga')," +
                "(13, 4, 3, 'Pueblo Nuevo')," +
                "(13, 5, 0, 'Julcan')," +
                "(13, 5, 1, 'Julcan')," +
                "(13, 5, 2, 'Calamarca')," +
                "(13, 5, 3, 'Carabamba')," +
                "(13, 5, 4, 'Huaso')," +
                "(13, 6, 0, 'Otuzco')," +
                "(13, 6, 1, 'Otuzco')," +
                "(13, 6, 2, 'Agallpampa')," +
                "(13, 6, 4, 'Charat')," +
                "(13, 6, 5, 'Huaranchal')," +
                "(13, 6, 6, 'La Cuesta')," +
                "(13, 6, 8, 'Mache')," +
                "(13, 6, 10, 'Paranday')," +
                "(13, 6, 11, 'Salpo')," +
                "(13, 6, 13, 'Sinsicap')," +
                "(13, 6, 14, 'Usquil')," +
                "(13, 7, 0, 'Pacasmayo')," +
                "(13, 7, 1, 'San Pedro de Lloc')," +
                "(13, 7, 2, 'Guadalupe')," +
                "(13, 7, 3, 'Jequetepeque')," +
                "(13, 7, 4, 'Pacasmayo')," +
                "(13, 7, 5, 'San Jose')," +
                "(13, 8, 0, 'Pataz')," +
                "(13, 8, 1, 'Tayabamba')," +
                "(13, 8, 2, 'Buldibuyo')," +
                "(13, 8, 3, 'Chillia')," +
                "(13, 8, 4, 'Huancaspata')," +
                "(13, 8, 5, 'Huaylillas')," +
                "(13, 8, 6, 'Huayo')," +
                "(13, 8, 7, 'Ongon')," +
                "(13, 8, 8, 'Parcoy')," +
                "(13, 8, 9, 'Pataz')," +
                "(13, 8, 10, 'Pias')," +
                "(13, 8, 11, 'Santiago de Challas')," +
                "(13, 8, 12, 'Taurija')," +
                "(13, 8, 13, 'Urpay')," +
                "(13, 9, 0, 'Sanchez Carrion')," +
                "(13, 9, 1, 'Huamachuco')," +
                "(13, 9, 2, 'Chugay')," +
                "(13, 9, 3, 'Cochorco')," +
                "(13, 9, 4, 'Curgos')," +
                "(13, 9, 5, 'Marcabal')," +
                "(13, 9, 6, 'Sanagoran')," +
                "(13, 9, 7, 'Sarin')," +
                "(13, 9, 8, 'Sartimbamba')," +
                "(13, 10, 0, 'Santiago de Chuco')," +
                "(13, 10, 1, 'Santiago de Chuco')," +
                "(13, 10, 2, 'Angasmarca')," +
                "(13, 10, 3, 'Cachicadan')," +
                "(13, 10, 4, 'Mollebamba')," +
                "(13, 10, 5, 'Mollepata')," +
                "(13, 10, 6, 'Quiruvilca')," +
                "(13, 10, 7, 'Santa Cruz de Chuca')," +
                "(13, 10, 8, 'Sitabamba')," +
                "(13, 11, 0, 'Gran Chimu')," +
                "(13, 11, 1, 'Cascas')," +
                "(13, 11, 2, 'Lucma')," +
                "(13, 11, 3, 'Marmot')," +
                "(13, 11, 4, 'Sayapullo')," +
                "(13, 12, 0, 'Viru')," +
                "(13, 12, 1, 'Viru')," +
                "(13, 12, 2, 'Chao')," +
                "(13, 12, 3, 'Guadalupito')," +
                "(14, 0, 0, 'Lambayeque')," +
                "(14, 1, 0, 'Chiclayo')," +
                "(14, 1, 1, 'Chiclayo')," +
                "(14, 1, 2, 'Chongoyape')," +
                "(14, 1, 3, 'Eten')," +
                "(14, 1, 4, 'Eten Puerto')," +
                "(14, 1, 5, 'Jose Leonardo Ortiz')," +
                "(14, 1, 6, 'La Victoria')," +
                "(14, 1, 7, 'Lagunas')," +
                "(14, 1, 8, 'Monsefu')," +
                "(14, 1, 9, 'Nueva Arica')," +
                "(14, 1, 10, 'Oyotun')," +
                "(14, 1, 11, 'Picsi')," +
                "(14, 1, 12, 'Pimentel')," +
                "(14, 1, 13, 'Reque')," +
                "(14, 1, 14, 'Santa Rosa')," +
                "(14, 1, 15, 'Saña')," +
                "(14, 1, 16, 'Cayaltí')," +
                "(14, 1, 17, 'Patapo')," +
                "(14, 1, 18, 'Pomalca')," +
                "(14, 1, 19, 'Pucalá')," +
                "(14, 1, 20, 'Tumán')," +
                "(14, 2, 0, 'Ferreñafe')," +
                "(14, 2, 1, 'Ferreñafe')," +
                "(14, 2, 2, 'Cañaris')," +
                "(14, 2, 3, 'Incahuasi')," +
                "(14, 2, 4, 'Manuel Antonio Mesones Muro')," +
                "(14, 2, 5, 'Pitipo')," +
                "(14, 2, 6, 'Pueblo Nuevo')," +
                "(14, 3, 0, 'Lambayeque')," +
                "(14, 3, 1, 'Lambayeque')," +
                "(14, 3, 2, 'Chochope')," +
                "(14, 3, 3, 'Illimo')," +
                "(14, 3, 4, 'Jayanca')," +
                "(14, 3, 5, 'Mochumi')," +
                "(14, 3, 6, 'Morrope')," +
                "(14, 3, 7, 'Motupe')," +
                "(14, 3, 8, 'Olmos')," +
                "(14, 3, 9, 'Pacora')," +
                "(14, 3, 10, 'Salas')," +
                "(14, 3, 11, 'San Jose')," +
                "(14, 3, 12, 'Tucume')," +
                "(15, 1, 0, 'Lima')," +
                "(15, 1, 1, 'Lima')," +
                "(15, 1, 2, 'Ancon')," +
                "(15, 1, 3, 'Ate')," +
                "(15, 1, 4, 'Barranco')," +
                "(15, 1, 5, 'Breña')," +
                "(15, 1, 6, 'Carabayllo')," +
                "(15, 1, 7, 'Chaclacayo')," +
                "(15, 1, 8, 'Chorrillos')," +
                "(15, 1, 9, 'Cieneguilla')," +
                "(15, 1, 10, 'Comas')," +
                "(15, 1, 11, 'El Agustino')," +
                "(15, 1, 12, 'Independencia')," +
                "(15, 1, 13, 'Jesus Maria')," +
                "(15, 1, 14, 'La Molina')," +
                "(15, 1, 15, 'La Victoria')," +
                "(15, 1, 16, 'Lince')," +
                "(15, 1, 17, 'Los Olivos')," +
                "(15, 1, 18, 'Lurigancho')," +
                "(15, 1, 19, 'Lurin')," +
                "(15, 1, 20, 'Magdalena del Mar')," +
                "(15, 1, 21, 'Pueblo Libre (Magdalena Vieja)')," +
                "(15, 1, 22, 'Miraflores')," +
                "(15, 1, 23, 'Pachacamac')," +
                "(15, 1, 24, 'Pucusana')," +
                "(15, 1, 25, 'Puente Piedra')," +
                "(15, 1, 26, 'Punta Hermosa')," +
                "(15, 1, 27, 'Punta Negra')," +
                "(15, 1, 28, 'Rimac')," +
                "(15, 1, 29, 'San Bartolo')," +
                "(15, 1, 30, 'San Borja')," +
                "(15, 1, 31, 'San Isidro')," +
                "(15, 1, 32, 'San Juan de Lurigancho')," +
                "(15, 1, 33, 'San Juan de Miraflores')," +
                "(15, 1, 34, 'San Luis')," +
                "(15, 1, 35, 'San Martin de Porres')," +
                "(15, 1, 36, 'San Miguel')," +
                "(15, 1, 37, 'Santa Anita')," +
                "(15, 1, 38, 'Santa Maria del Mar')," +
                "(15, 1, 39, 'Santa Rosa')," +
                "(15, 1, 40, 'Santiago de Surco')," +
                "(15, 1, 41, 'Surquillo')," +
                "(15, 1, 42, 'Villa El Salvador')," +
                "(15, 1, 43, 'Villa Maria del Triunfo')," +
                "(15, 2, 0, 'Barranca')," +
                "(15, 2, 1, 'Barranca')," +
                "(15, 2, 2, 'Paramonga')," +
                "(15, 2, 3, 'Pativilca')," +
                "(15, 2, 4, 'Supe')," +
                "(15, 2, 5, 'Supe Puerto')," +
                "(15, 3, 0, 'Cajatambo')," +
                "(15, 3, 1, 'Cajatambo')," +
                "(15, 3, 2, 'Copa')," +
                "(15, 3, 3, 'Gorgor')," +
                "(15, 3, 4, 'Huancapon')," +
                "(15, 3, 5, 'Manas')," +
                "(15, 4, 0, 'Canta')," +
                "(15, 4, 1, 'Canta')," +
                "(15, 4, 2, 'Arahuay')," +
                "(15, 4, 3, 'Huamantanga')," +
                "(15, 4, 4, 'Huaros')," +
                "(15, 4, 5, 'Lachaqui')," +
                "(15, 4, 6, 'San Buenaventura')," +
                "(15, 4, 7, 'Santa Rosa de Quives')," +
                "(15, 5, 0, 'Cañete')," +
                "(15, 5, 1, 'San Vicente de Cañete')," +
                "(15, 5, 2, 'Asia')," +
                "(15, 5, 3, 'Calango')," +
                "(15, 5, 4, 'Cerro Azul')," +
                "(15, 5, 5, 'Chilca')," +
                "(15, 5, 6, 'Coayllo')," +
                "(15, 5, 7, 'Imperial')," +
                "(15, 5, 8, 'Lunahuana')," +
                "(15, 5, 9, 'Mala')," +
                "(15, 5, 10, 'Nuevo Imperial')," +
                "(15, 5, 11, 'Pacaran')," +
                "(15, 5, 12, 'Quilmana')," +
                "(15, 5, 13, 'San Antonio')," +
                "(15, 5, 14, 'San Luis')," +
                "(15, 5, 15, 'Santa Cruz de Flores')," +
                "(15, 5, 16, 'Zuñiga')," +
                "(15, 6, 0, 'Huaral')," +
                "(15, 6, 1, 'Huaral')," +
                "(15, 6, 2, 'Atavillos Alto')," +
                "(15, 6, 3, 'Atavillos Bajo')," +
                "(15, 6, 4, 'Aucallama')," +
                "(15, 6, 5, 'Chancay')," +
                "(15, 6, 6, 'Ihuari')," +
                "(15, 6, 7, 'Lampian')," +
                "(15, 6, 8, 'Pacaraos')," +
                "(15, 6, 9, 'San Miguel de Acos')," +
                "(15, 6, 10, 'Santa Cruz de Andamarca')," +
                "(15, 6, 11, 'Sumbilca')," +
                "(15, 6, 12, 'Veintisiete de Noviembre')," +
                "(15, 7, 0, 'Huarochiri')," +
                "(15, 7, 1, 'Matucana')," +
                "(15, 7, 2, 'Antioquia')," +
                "(15, 7, 3, 'Callahuanca')," +
                "(15, 7, 4, 'Carampoma')," +
                "(15, 7, 5, 'Chicla')," +
                "(15, 7, 6, 'Cuenca')," +
                "(15, 7, 7, 'Huachupampa')," +
                "(15, 7, 8, 'Huanza')," +
                "(15, 7, 9, 'Huarochiri')," +
                "(15, 7, 10, 'Lahuaytambo')," +
                "(15, 7, 11, 'Langa')," +
                "(15, 7, 12, 'Laraos')," +
                "(15, 7, 13, 'Mariatana')," +
                "(15, 7, 14, 'Ricardo Palma')," +
                "(15, 7, 15, 'San Andres de Tupicocha')," +
                "(15, 7, 16, 'San Antonio')," +
                "(15, 7, 17, 'San Bartolome')," +
                "(15, 7, 18, 'San Damian')," +
                "(15, 7, 19, 'San Juan de Iris')," +
                "(15, 7, 20, 'San Juan de Tantaranche')," +
                "(15, 7, 21, 'San Lorenzo de Quinti')," +
                "(15, 7, 22, 'San Mateo')," +
                "(15, 7, 23, 'San Mateo de Otao')," +
                "(15, 7, 24, 'San Pedro de Casta')," +
                "(15, 7, 25, 'San Pedro de Huancayre')," +
                "(15, 7, 26, 'Sangallaya')," +
                "(15, 7, 27, 'Santa Cruz de Cocachacra')," +
                "(15, 7, 28, 'Santa Eulalia')," +
                "(15, 7, 29, 'Santiago de Anchucaya')," +
                "(15, 7, 30, 'Santiago de Tuna')," +
                "(15, 7, 31, 'Santo Domingo de los Olleros')," +
                "(15, 7, 32, 'Surco')," +
                "(15, 8, 0, 'Huaura')," +
                "(15, 8, 1, 'Huacho')," +
                "(15, 8, 2, 'Ambar')," +
                "(15, 8, 3, 'Caleta de Carquin')," +
                "(15, 8, 4, 'Checras')," +
                "(15, 8, 5, 'Hualmay')," +
                "(15, 8, 6, 'Huaura')," +
                "(15, 8, 7, 'Leoncio Prado')," +
                "(15, 8, 8, 'Paccho')," +
                "(15, 8, 9, 'Santa Leonor')," +
                "(15, 8, 10, 'Santa Maria')," +
                "(15, 8, 11, 'Sayan')," +
                "(15, 8, 12, 'Vegueta')," +
                "(15, 9, 0, 'Oyon')," +
                "(15, 9, 1, 'Oyon')," +
                "(15, 9, 2, 'Andajes')," +
                "(15, 9, 3, 'Caujul')," +
                "(15, 9, 4, 'Cochamarca')," +
                "(15, 9, 5, 'Navan')," +
                "(15, 9, 6, 'Pachangara')," +
                "(15, 10, 0, 'Yauyos')," +
                "(15, 10, 1, 'Yauyos')," +
                "(15, 10, 2, 'Alis')," +
                "(15, 10, 3, 'Ayauca')," +
                "(15, 10, 4, 'Ayaviri')," +
                "(15, 10, 5, 'Azangaro')," +
                "(15, 10, 6, 'Cacra')," +
                "(15, 10, 7, 'Carania')," +
                "(15, 10, 8, 'Catahuasi')," +
                "(15, 10, 9, 'Chocos')," +
                "(15, 10, 10, 'Cochas')," +
                "(15, 10, 11, 'Colonia')," +
                "(15, 10, 12, 'Hongos')," +
                "(15, 10, 13, 'Huampara')," +
                "(15, 10, 14, 'Huancaya')," +
                "(15, 10, 15, 'Huangascar')," +
                "(15, 10, 16, 'Huantan')," +
                "(15, 10, 17, 'Huañec')," +
                "(15, 10, 18, 'Laraos')," +
                "(15, 10, 19, 'Lincha')," +
                "(15, 10, 20, 'Madean')," +
                "(15, 10, 21, 'Miraflores')," +
                "(15, 10, 22, 'Omas')," +
                "(15, 10, 23, 'Putinza')," +
                "(15, 10, 24, 'Quinches')," +
                "(15, 10, 25, 'Quinocay')," +
                "(15, 10, 26, 'San Joaquin')," +
                "(15, 10, 27, 'San Pedro de Pilas')," +
                "(15, 10, 28, 'Tanta')," +
                "(15, 10, 29, 'Tauripampa')," +
                "(15, 10, 30, 'Tomas')," +
                "(15, 10, 31, 'Tupe')," +
                "(15, 10, 32, 'Viñac')," +
                "(15, 10, 33, 'Vitis')," +
                "(16, 0, 0, 'Loreto')," +
                "(16, 1, 0, 'Maynas')," +
                "(16, 1, 1, 'Iquitos')," +
                "(16, 1, 2, 'Alto Nanay')," +
                "(16, 1, 3, 'Fernando Lores')," +
                "(16, 1, 4, 'Indiana')," +
                "(16, 1, 5, 'Las Amazonas')," +
                "(16, 1, 6, 'Mazan')," +
                "(16, 1, 7, 'Napo')," +
                "(16, 1, 8, 'Punchana')," +
                "(16, 1, 9, 'Putumayo')," +
                "(16, 1, 10, 'Torres Causana')," +
                "(16, 1, 12, 'Belén')," +
                "(16, 1, 13, 'San Juan Bautista')," +
                "(16, 1, 14, 'Teniente Manuel Clavero')," +
                "(16, 2, 0, 'Alto Amazonas')," +
                "(16, 2, 1, 'Yurimaguas')," +
                "(16, 2, 2, 'Balsapuerto')," +
                "(16, 2, 5, 'Jeberos')," +
                "(16, 2, 6, 'Lagunas')," +
                "(16, 2, 10, 'Santa Cruz')," +
                "(16, 2, 11, 'Teniente Cesar Lopez Rojas')," +
                "(16, 3, 0, 'Loreto')," +
                "(16, 3, 1, 'Nauta')," +
                "(16, 3, 2, 'Parinari')," +
                "(16, 3, 3, 'Tigre')," +
                "(16, 3, 4, 'Trompeteros')," +
                "(16, 3, 5, 'Urarinas')," +
                "(16, 4, 0, 'Mariscal Ramon Castilla')," +
                "(16, 4, 1, 'Ramon Castilla')," +
                "(16, 4, 2, 'Pebas')," +
                "(16, 4, 3, 'Yavari')," +
                "(16, 4, 4, 'San Pablo')," +
                "(16, 5, 0, 'Requena')," +
                "(16, 5, 1, 'Requena')," +
                "(16, 5, 2, 'Alto Tapiche')," +
                "(16, 5, 3, 'Capelo')," +
                "(16, 5, 4, 'Emilio San Martin')," +
                "(16, 5, 5, 'Maquia')," +
                "(16, 5, 6, 'Puinahua')," +
                "(16, 5, 7, 'Saquena')," +
                "(16, 5, 8, 'Soplin')," +
                "(16, 5, 9, 'Tapiche')," +
                "(16, 5, 10, 'Jenaro Herrera')," +
                "(16, 5, 11, 'Yaquerana')," +
                "(16, 6, 0, 'Ucayali')," +
                "(16, 6, 1, 'Contamana')," +
                "(16, 6, 2, 'Inahuaya')," +
                "(16, 6, 3, 'Padre Marquez')," +
                "(16, 6, 4, 'Pampa Hermosa')," +
                "(16, 6, 5, 'Sarayacu')," +
                "(16, 6, 6, 'Vargas Guerra')," +
                "(16, 7, 0, 'Datem del Marañón')," +
                "(16, 7, 1, 'Barranca')," +
                "(16, 7, 2, 'Cahuapanas')," +
                "(16, 7, 3, 'Manseriche')" ;


    }


    private static String instructionUbigeosFor() {

        return "INSERT INTO ".concat(TABLE_UBIGEO) + " (CODE_DEPARTAMENTO, CODE_PROVINCIA, CODE_DISTRITO, NOMBRE) VALUES" +
                "(16, 7, 4, 'Morona')," +
                "(16, 7, 5, 'Pastaza')," +
                "(16, 7, 6, 'Andoas')," +
                "(16, 8, 0, 'Putumayo')," +
                "(16, 8, 1, 'Putumayo')," +
                "(16, 8, 2, 'Rosa Panduro')," +
                "(16, 8, 3, 'Teniente Manuel Clavero')," +
                "(16, 8, 4, 'Yaguas')," +
                "(17, 0, 0, 'Madre de Dios')," +
                "(17, 1, 0, 'Tambopata')," +
                "(17, 1, 1, 'Tambopata')," +
                "(17, 1, 2, 'Inambari')," +
                "(17, 1, 3, 'Las Piedras')," +
                "(17, 1, 4, 'Laberinto')," +
                "(17, 2, 0, 'Manu')," +
                "(17, 2, 1, 'Manu')," +
                "(17, 2, 2, 'Fitzcarrald')," +
                "(17, 2, 3, 'Madre de Dios')," +
                "(17, 2, 4, 'Huepetuhe')," +
                "(17, 3, 0, 'Tahuamanu')," +
                "(17, 3, 1, 'Iñapari')," +
                "(17, 3, 2, 'Iberia')," +
                "(17, 3, 3, 'Tahuamanu')," +
                "(18, 0, 0, 'Moquegua')," +
                "(18, 1, 0, 'Mariscal Nieto')," +
                "(18, 1, 1, 'Moquegua')," +
                "(18, 1, 2, 'Carumas')," +
                "(18, 1, 3, 'Cuchumbaya')," +
                "(18, 1, 4, 'Samegua')," +
                "(18, 1, 5, 'San Cristobal')," +
                "(18, 1, 6, 'Torata')," +
                "(18, 2, 0, 'General Sanchez Cerro')," +
                "(18, 2, 1, 'Omate')," +
                "(18, 2, 2, 'Chojata')," +
                "(18, 2, 3, 'Coalaque')," +
                "(18, 2, 4, 'Ichuña')," +
                "(18, 2, 5, 'La Capilla')," +
                "(18, 2, 6, 'Lloque')," +
                "(18, 2, 7, 'Matalaque')," +
                "(18, 2, 8, 'Puquina')," +
                "(18, 2, 9, 'Quinistaquillas')," +
                "(18, 2, 10, 'Ubinas')," +
                "(18, 2, 11, 'Yunga')," +
                "(18, 3, 0, 'Ilo')," +
                "(18, 3, 1, 'Ilo')," +
                "(18, 3, 2, 'El Algarrobal')," +
                "(18, 3, 3, 'Pacocha')," +
                "(19, 0, 0, 'Pasco')," +
                "(19, 1, 0, 'Pasco')," +
                "(19, 1, 1, 'Chaupimarca')," +
                "(19, 1, 2, 'Huachon')," +
                "(19, 1, 3, 'Huariaca')," +
                "(19, 1, 4, 'Huayllay')," +
                "(19, 1, 5, 'Ninacaca')," +
                "(19, 1, 6, 'Pallanchacra')," +
                "(19, 1, 7, 'Paucartambo')," +
                "(19, 1, 8, 'San Fco. de Asís de Yarusyacán')," +
                "(19, 1, 9, 'Simon Bolivar')," +
                "(19, 1, 10, 'Ticlacayan')," +
                "(19, 1, 11, 'Tinyahuarco')," +
                "(19, 1, 12, 'Vicco')," +
                "(19, 1, 13, 'Yanacancha')," +
                "(19, 2, 0, 'Daniel Alcides Carrion')," +
                "(19, 2, 1, 'Yanahuanca')," +
                "(19, 2, 2, 'Chacayan')," +
                "(19, 2, 3, 'Goyllarisquizga')," +
                "(19, 2, 4, 'Paucar')," +
                "(19, 2, 5, 'San Pedro de Pillao')," +
                "(19, 2, 6, 'Santa Ana de Tusi')," +
                "(19, 2, 7, 'Tapuc')," +
                "(19, 2, 8, 'Vilcabamba')," +
                "(19, 3, 0, 'Oxapampa')," +
                "(19, 3, 1, 'Oxapampa')," +
                "(19, 3, 2, 'Chontabamba')," +
                "(19, 3, 3, 'Huancabamba')," +
                "(19, 3, 4, 'Palcazu')," +
                "(19, 3, 5, 'Pozuzo')," +
                "(19, 3, 6, 'Puerto Bermudez')," +
                "(19, 3, 7, 'Villa Rica')," +
                "(19, 3, 8, 'Constitucion')," +
                "(20, 0, 0, 'Piura')," +
                "(20, 1, 0, 'Piura')," +
                "(20, 1, 1, 'Piura')," +
                "(20, 1, 4, 'Castilla')," +
                "(20, 1, 5, 'Catacaos')," +
                "(20, 1, 7, 'Cura Mori')," +
                "(20, 1, 8, 'El Tallan')," +
                "(20, 1, 9, 'La Arena')," +
                "(20, 1, 10, 'La Union')," +
                "(20, 1, 11, 'Las Lomas')," +
                "(20, 1, 14, 'Tambo Grande')," +
                "(20, 1, 15, 'Veintiséis de Octubre')," +
                "(20, 2, 0, 'Ayabaca')," +
                "(20, 2, 1, 'Ayabaca')," +
                "(20, 2, 2, 'Frias')," +
                "(20, 2, 3, 'Jilili')," +
                "(20, 2, 4, 'Lagunas')," +
                "(20, 2, 5, 'Montero')," +
                "(20, 2, 6, 'Pacaipampa')," +
                "(20, 2, 7, 'Paimas')," +
                "(20, 2, 8, 'Sapillica')," +
                "(20, 2, 9, 'Sicchez')," +
                "(20, 2, 10, 'Suyo')," +
                "(20, 3, 0, 'Huancabamba')," +
                "(20, 3, 1, 'Huancabamba')," +
                "(20, 3, 2, 'Canchaque')," +
                "(20, 3, 3, 'El Carmen de la Frontera')," +
                "(20, 3, 4, 'Huarmaca')," +
                "(20, 3, 5, 'Lalaquiz')," +
                "(20, 3, 6, 'San Miguel de El Faique')," +
                "(20, 3, 7, 'Sondor')," +
                "(20, 3, 8, 'Sondorillo')," +
                "(20, 4, 0, 'Morropon')," +
                "(20, 4, 1, 'Chulucanas')," +
                "(20, 4, 2, 'Buenos Aires')," +
                "(20, 4, 3, 'Chalaco')," +
                "(20, 4, 4, 'La Matanza')," +
                "(20, 4, 5, 'Morropon')," +
                "(20, 4, 6, 'Salitral')," +
                "(20, 4, 7, 'San Juan de Bigote')," +
                "(20, 4, 8, 'Santa Catalina de Mossa')," +
                "(20, 4, 9, 'Santo Domingo')," +
                "(20, 4, 10, 'Yamango')," +
                "(20, 5, 0, 'Paita')," +
                "(20, 5, 1, 'Paita')," +
                "(20, 5, 2, 'Amotape')," +
                "(20, 5, 3, 'Arenal')," +
                "(20, 5, 4, 'Colan')," +
                "(20, 5, 5, 'La Huaca')," +
                "(20, 5, 6, 'Tamarindo')," +
                "(20, 5, 7, 'Vichayal')," +
                "(20, 6, 0, 'Sullana')," +
                "(20, 6, 1, 'Sullana')," +
                "(20, 6, 2, 'Bellavista')," +
                "(20, 6, 3, 'Ignacio Escudero')," +
                "(20, 6, 4, 'Lancones')," +
                "(20, 6, 5, 'Marcavelica')," +
                "(20, 6, 6, 'Miguel Checa')," +
                "(20, 6, 7, 'Querecotillo')," +
                "(20, 6, 8, 'Salitral')," +
                "(20, 7, 0, 'Talara')," +
                "(20, 7, 1, 'Pariñas')," +
                "(20, 7, 2, 'El Alto')," +
                "(20, 7, 3, 'La Brea')," +
                "(20, 7, 4, 'Lobitos')," +
                "(20, 7, 5, 'Los Organos')," +
                "(20, 7, 6, 'Mancora')," +
                "(20, 8, 0, 'Sechura')," +
                "(20, 8, 1, 'Sechura')," +
                "(20, 8, 2, 'Bellavista de la Union')," +
                "(20, 8, 3, 'Bernal')," +
                "(20, 8, 4, 'Cristo Nos Valga')," +
                "(20, 8, 5, 'Vice')," +
                "(20, 8, 6, 'Rinconada Llicuar')," +
                "(21, 0, 0, 'Puno')," +
                "(21, 1, 0, 'Puno')," +
                "(21, 1, 1, 'Puno')," +
                "(21, 1, 2, 'Acora')," +
                "(21, 1, 3, 'Amantani')," +
                "(21, 1, 4, 'Atuncolla')," +
                "(21, 1, 5, 'Capachica')," +
                "(21, 1, 6, 'Chucuito')," +
                "(21, 1, 7, 'Coata')," +
                "(21, 1, 8, 'Huata')," +
                "(21, 1, 9, 'Mañazo')," +
                "(21, 1, 10, 'Paucarcolla')," +
                "(21, 1, 11, 'Pichacani')," +
                "(21, 1, 12, 'Plateria')," +
                "(21, 1, 13, 'San Antonio')," +
                "(21, 1, 14, 'Tiquillaca')," +
                "(21, 1, 15, 'Vilque')," +
                "(21, 2, 0, 'Azangaro')," +
                "(21, 2, 1, 'Azangaro')," +
                "(21, 2, 2, 'Achaya')," +
                "(21, 2, 3, 'Arapa')," +
                "(21, 2, 4, 'Asillo')," +
                "(21, 2, 5, 'Caminaca')," +
                "(21, 2, 6, 'Chupa')," +
                "(21, 2, 7, 'Jose Domingo Choquehuanca')," +
                "(21, 2, 8, 'Muñani')," +
                "(21, 2, 9, 'Potoni')," +
                "(21, 2, 10, 'Saman')," +
                "(21, 2, 11, 'San Anton')," +
                "(21, 2, 12, 'San Jose')," +
                "(21, 2, 13, 'San Juan de Salinas')," +
                "(21, 2, 14, 'Santiago de Pupuja')," +
                "(21, 2, 15, 'Tirapata')," +
                "(21, 3, 0, 'Carabaya')," +
                "(21, 3, 1, 'Macusani')," +
                "(21, 3, 2, 'Ajoyani')," +
                "(21, 3, 3, 'Ayapata')," +
                "(21, 3, 4, 'Coasa')," +
                "(21, 3, 5, 'Corani')," +
                "(21, 3, 6, 'Crucero')," +
                "(21, 3, 7, 'Ituata')," +
                "(21, 3, 8, 'Ollachea')," +
                "(21, 3, 9, 'San Gaban')," +
                "(21, 3, 10, 'Usicayos')," +
                "(21, 4, 0, 'Chucuito')," +
                "(21, 4, 1, 'Juli')," +
                "(21, 4, 2, 'Desaguadero')," +
                "(21, 4, 3, 'Huacullani')," +
                "(21, 4, 4, 'Kelluyo')," +
                "(21, 4, 5, 'Pisacoma')," +
                "(21, 4, 6, 'Pomata')," +
                "(21, 4, 7, 'Zepita')," +
                "(21, 5, 0, 'El Collao')," +
                "(21, 5, 1, 'Ilave')," +
                "(21, 5, 2, 'Capaso')," +
                "(21, 5, 3, 'Pilcuyo')," +
                "(21, 5, 4, 'Santa Rosa')," +
                "(21, 5, 5, 'Conduriri')," +
                "(21, 6, 0, 'Huancane')," +
                "(21, 6, 1, 'Huancane')," +
                "(21, 6, 2, 'Cojata')," +
                "(21, 6, 3, 'Huatasani')," +
                "(21, 6, 4, 'Inchupalla')," +
                "(21, 6, 5, 'Pusi')," +
                "(21, 6, 6, 'Rosaspata')," +
                "(21, 6, 7, 'Taraco')," +
                "(21, 6, 8, 'Vilque Chico')," +
                "(21, 7, 0, 'Lampa')," +
                "(21, 7, 1, 'Lampa')," +
                "(21, 7, 2, 'Cabanilla')," +
                "(21, 7, 3, 'Calapuja')," +
                "(21, 7, 4, 'Nicasio')," +
                "(21, 7, 5, 'Ocuviri')," +
                "(21, 7, 6, 'Palca')," +
                "(21, 7, 7, 'Paratia')," +
                "(21, 7, 8, 'Pucara')," +
                "(21, 7, 9, 'Santa Lucia')," +
                "(21, 7, 10, 'Vilavila')," +
                "(21, 8, 0, 'Melgar')," +
                "(21, 8, 1, 'Ayaviri')," +
                "(21, 8, 2, 'Antauta')," +
                "(21, 8, 3, 'Cupi')," +
                "(21, 8, 4, 'Llalli')," +
                "(21, 8, 5, 'Macari')," +
                "(21, 8, 6, 'Nuñoa')," +
                "(21, 8, 7, 'Orurillo')," +
                "(21, 8, 8, 'Santa Rosa')," +
                "(21, 8, 9, 'Umachiri')," +
                "(21, 9, 0, 'Moho')," +
                "(21, 9, 1, 'Moho')," +
                "(21, 9, 2, 'Conima')," +
                "(21, 9, 3, 'Huayrapata')," +
                "(21, 9, 4, 'Tilali')," +
                "(21, 10, 0, 'San Antonio de Putina')," +
                "(21, 10, 1, 'Putina')," +
                "(21, 10, 2, 'Ananea')," +
                "(21, 10, 3, 'Pedro Vilca Apaza')," +
                "(21, 10, 4, 'Quilcapuncu')," +
                "(21, 10, 5, 'Sina')," +
                "(21, 11, 0, 'San Roman')," +
                "(21, 11, 1, 'Juliaca')," +
                "(21, 11, 2, 'Cabana')," +
                "(21, 11, 3, 'Cabanillas')," +
                "(21, 11, 4, 'Caracoto')," +
                "(21, 12, 0, 'Sandia')," +
                "(21, 12, 1, 'Sandia')," +
                "(21, 12, 2, 'Cuyocuyo')," +
                "(21, 12, 3, 'Limbani')," +
                "(21, 12, 4, 'Patambuco')," +
                "(21, 12, 5, 'Phara')," +
                "(21, 12, 6, 'Quiaca')," +
                "(21, 12, 7, 'San Juan del Oro')," +
                "(21, 12, 8, 'Yanahuaya')," +
                "(21, 12, 9, 'Alto Inambari')," +
                "(21, 12, 10, 'San Pedro de Putina Punco')," +
                "(21, 13, 0, 'Yunguyo')," +
                "(21, 13, 1, 'Yunguyo')," +
                "(21, 13, 2, 'Anapia')," +
                "(21, 13, 3, 'Copani')," +
                "(21, 13, 4, 'Cuturapi')," +
                "(21, 13, 5, 'Ollaraya')," +
                "(21, 13, 6, 'Tinicachi')," +
                "(21, 13, 7, 'Unicachi')," +
                "(22, 0, 0, 'San Martin')," +
                "(22, 1, 0, 'Moyobamba')," +
                "(22, 1, 1, 'Moyobamba')," +
                "(22, 1, 2, 'Calzada')," +
                "(22, 1, 3, 'Habana')," +
                "(22, 1, 4, 'Jepelacio')," +
                "(22, 1, 5, 'Soritor')," +
                "(22, 1, 6, 'Yantalo')," +
                "(22, 2, 0, 'Bellavista')," +
                "(22, 2, 1, 'Bellavista')," +
                "(22, 2, 2, 'Alto Biavo')," +
                "(22, 2, 3, 'Bajo Biavo')," +
                "(22, 2, 4, 'Huallaga')," +
                "(22, 2, 5, 'San Pablo')," +
                "(22, 2, 6, 'San Rafael')," +
                "(22, 3, 0, 'El Dorado')," +
                "(22, 3, 1, 'San Jose de Sisa')," +
                "(22, 3, 2, 'Agua Blanca')," +
                "(22, 3, 3, 'San Martin')," +
                "(22, 3, 4, 'Santa Rosa')," +
                "(22, 3, 5, 'Shatoja')," +
                "(22, 4, 0, 'Huallaga')," +
                "(22, 4, 1, 'Saposoa')," +
                "(22, 4, 2, 'Alto Saposoa')," +
                "(22, 4, 3, 'El Eslabon')," +
                "(22, 4, 4, 'Piscoyacu')," +
                "(22, 4, 5, 'Sacanche')," +
                "(22, 4, 6, 'Tingo de Saposoa')," +
                "(22, 5, 0, 'Lamas')," +
                "(22, 5, 1, 'Lamas')," +
                "(22, 5, 2, 'Alonso de Alvarado')," +
                "(22, 5, 3, 'Barranquita')," +
                "(22, 5, 4, 'Caynarachi')," +
                "(22, 5, 5, 'Cuñumbuqui')," +
                "(22, 5, 6, 'Pinto Recodo')," +
                "(22, 5, 7, 'Rumisapa')," +
                "(22, 5, 8, 'San Roque de Cumbaza')," +
                "(22, 5, 9, 'Shanao')," +
                "(22, 5, 10, 'Tabalosos')," +
                "(22, 5, 11, 'Zapatero')," +
                "(22, 6, 0, 'Mariscal Caceres')," +
                "(22, 6, 1, 'Juanjui')," +
                "(22, 6, 2, 'Campanilla')," +
                "(22, 6, 3, 'Huicungo')," +
                "(22, 6, 4, 'Pachiza')," +
                "(22, 6, 5, 'Pajarillo')," +
                "(22, 7, 0, 'Picota')," +
                "(22, 7, 1, 'Picota')," +
                "(22, 7, 2, 'Buenos Aires')," +
                "(22, 7, 3, 'Caspisapa')," +
                "(22, 7, 4, 'Pilluana')," +
                "(22, 7, 5, 'Pucacaca')," +
                "(22, 7, 6, 'San Cristobal')," +
                "(22, 7, 7, 'San Hilarion')," +
                "(22, 7, 8, 'Shamboyacu')," +
                "(22, 7, 9, 'Tingo de Ponasa')," +
                "(22, 7, 10, 'Tres Unidos')," +
                "(22, 8, 0, 'Rioja')," +
                "(22, 8, 1, 'Rioja')," +
                "(22, 8, 2, 'Awajun')," +
                "(22, 8, 3, 'Elias Soplin Vargas')," +
                "(22, 8, 4, 'Nueva Cajamarca')," +
                "(22, 8, 5, 'Pardo Miguel')," +
                "(22, 8, 6, 'Posic')," +
                "(22, 8, 7, 'San Fernando')," +
                "(22, 8, 8, 'Yorongos')," +
                "(22, 8, 9, 'Yuracyacu')," +
                "(22, 9, 0, 'San Martin')," +
                "(22, 9, 1, 'Tarapoto')," +
                "(22, 9, 2, 'Alberto Leveau')," +
                "(22, 9, 3, 'Cacatachi')," +
                "(22, 9, 4, 'Chazuta')," +
                "(22, 9, 5, 'Chipurana')," +
                "(22, 9, 6, 'El Porvenir')," +
                "(22, 9, 7, 'Huimbayoc')," +
                "(22, 9, 8, 'Juan Guerra')," +
                "(22, 9, 9, 'La Banda de Shilcayo')," +
                "(22, 9, 10, 'Morales')," +
                "(22, 9, 11, 'Papaplaya')," +
                "(22, 9, 12, 'San Antonio')," +
                "(22, 9, 13, 'Sauce')," +
                "(22, 9, 14, 'Shapaja')," +
                "(22, 10, 0, 'Tocache')," +
                "(22, 10, 1, 'Tocache')," +
                "(22, 10, 2, 'Nuevo Progreso')," +
                "(22, 10, 3, 'Polvora')," +
                "(22, 10, 4, 'Shunte')," +
                "(22, 10, 5, 'Uchiza')," +
                "(23, 0, 0, 'Tacna')," +
                "(23, 1, 0, 'Tacna')," +
                "(23, 1, 1, 'Tacna')," +
                "(23, 1, 2, 'Alto de la Alianza')," +
                "(23, 1, 3, 'Calana')," +
                "(23, 1, 4, 'Ciudad Nueva')," +
                "(23, 1, 5, 'Inclan')," +
                "(23, 1, 6, 'Pachia')," +
                "(23, 1, 7, 'Palca')," +
                "(23, 1, 8, 'Pocollay')," +
                "(23, 1, 9, 'Sama')," +
                "(23, 1, 10, 'Coronel Gregorio Albarracín L')," +
                "(23, 2, 0, 'Candarave')," +
                "(23, 2, 1, 'Candarave')," +
                "(23, 2, 2, 'Cairani')," +
                "(23, 2, 3, 'Camilaca')," +
                "(23, 2, 4, 'Curibaya')," +
                "(23, 2, 5, 'Huanuara')," +
                "(23, 2, 6, 'Quilahuani')," +
                "(23, 3, 0, 'Jorge Basadre')," +
                "(23, 3, 1, 'Locumba')," +
                "(23, 3, 2, 'Ilabaya')," +
                "(23, 3, 3, 'Ite')," +
                "(23, 4, 0, 'Tarata')," +
                "(23, 4, 1, 'Tarata')," +
                "(23, 4, 2, 'Chucatamani')," +
                "(23, 4, 3, 'Estique')," +
                "(23, 4, 4, 'Estique-Pampa')," +
                "(23, 4, 5, 'Sitajara')," +
                "(23, 4, 6, 'Susapaya')," +
                "(23, 4, 7, 'Tarucachi')," +
                "(23, 4, 8, 'Ticaco')," +
                "(24, 0, 0, 'Tumbes')," +
                "(24, 1, 0, 'Tumbes')," +
                "(24, 1, 1, 'Tumbes')," +
                "(24, 1, 2, 'Corrales')," +
                "(24, 1, 3, 'La Cruz')," +
                "(24, 1, 4, 'Pampas de Hospital')," +
                "(24, 1, 5, 'San Jacinto')," +
                "(24, 1, 6, 'San Juan de la Virgen')," +
                "(24, 2, 0, 'Contralmirante Villar')," +
                "(24, 2, 1, 'Zorritos')," +
                "(24, 2, 2, 'Casitas')," +
                "(24, 2, 3, 'Canoas de Punta Sal')," +
                "(24, 3, 0, 'Zarumilla')," +
                "(24, 3, 1, 'Zarumilla')," +
                "(24, 3, 2, 'Aguas Verdes')," +
                "(24, 3, 3, 'Matapalo')," +
                "(24, 3, 4, 'Papayal')," +
                "(25, 0, 0, 'Ucayali')," +
                "(25, 1, 0, 'Coronel Portillo')," +
                "(25, 1, 1, 'Callaria')," +
                "(25, 1, 2, 'Campoverde')," +
                "(25, 1, 3, 'Iparia')," +
                "(25, 1, 4, 'Masisea')," +
                "(25, 1, 5, 'Yarinacocha')," +
                "(25, 1, 6, 'Nueva Requena')," +
                "(25, 1, 7, 'Manantay')," +
                "(25, 2, 0, 'Atalaya')," +
                "(25, 2, 1, 'Raymondi')," +
                "(25, 2, 2, 'Sepahua')," +
                "(25, 2, 3, 'Tahuania')," +
                "(25, 2, 4, 'Yurua')," +
                "(25, 3, 0, 'Padre Abad')," +
                "(25, 3, 1, 'Padre Abad')," +
                "(25, 3, 2, 'Irazola')," +
                "(25, 3, 3, 'Curimana')," +
                "(25, 4, 0, 'Purus')," +
                "(25, 4, 1, 'Purus')," +
                "(1, 0, 0, 'Amazonas');";
    }


}