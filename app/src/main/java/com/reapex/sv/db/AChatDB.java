package com.reapex.sv.db;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.reapex.sv.MyApp;
import com.reapex.sv.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {AUser.class, AMessage.class}, version = 1, exportSchema = false)
public abstract class AChatDB extends RoomDatabase {

    public abstract AUserDao getUserDao();          //Define the DAOs that work with the database. Provide an abstract "getter" method for each @Dao.
    public abstract AMessageDao getMessageDao();          //Define the DAOs that work with the database. Provide an abstract "getter" method for each @Dao.
    private static volatile AChatDB INSTANCE;   //Create the WordRoomDatabase as a singleton

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService excutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized AChatDB getDatabase(Context context){
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AChatDB.class, "AChat.DB")
                    .allowMainThreadQueries()
                    .addCallback(sRoomDatabaseCallback)
                    .build();
        }
        return INSTANCE;
    }

    private static final Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            excutorService.execute(() -> {
                AUserDao userDao = INSTANCE.getUserDao();
                AUser user = new AUser("18018018018", "你说", "sv180180", "android.resource://com.reapex.sv/mipmap/default_user_avatar","888888");
                userDao.insert(user);
            });
        }
    };
}

/*            String avatar = (Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + MyApp.getMyContext().getResources().getResourcePackageName(R.mipmap.default_user_avatar) + "/"
                    + MyApp.getMyContext().getResources().getResourceTypeName(R.mipmap.default_user_avatar) + "/"
                    + MyApp.getMyContext().getResources().getResourceEntryName(R.mipmap.default_user_avatar))).toString();


                // public AMessage(String content, String senderId, String senderName, int msgAvatarResource, boolean isSend) {

                AMessageDao messageDao = INSTANCE.getMessageDao();
                AMessage msg1 = new AMessage("系统初始化成功。点击底部按钮，开始说吧，我听着呢。", "800", "你说", R.mipmap.default_user_avatar, false);
                msg1.setMessageId("800");
                messageDao.insert(msg1);

*/

/*            if(db!=null){
        if(db.isOpen()) {
            db.close();
        }
        db=null;
    }

            if(MyApp.db!=null){
                if(MyApp.db.isOpen()) {
                    MyApp.db.close();
                }
                MyApp.db=null;
            }


*/
