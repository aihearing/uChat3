package com.reapex.sv.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AUser user);

    @Query("UPDATE AUser SET userSignature = :userSignature WHERE userPhone = :userPhone")
    void updateSignature(String userPhone, String userSignature);

    @Query("UPDATE AUser SET userGender = :userGender WHERE userPhone = :userPhone")
    void updateGender(String userPhone, String userGender);

    @Query("UPDATE AUser SET userRegion = :userRegion WHERE userPhone = :userPhone")
    void updateRegion(String userPhone, String userRegion);

    @Query("UPDATE AUser SET userNickName = :userNickName WHERE userPhone = :userPhone")
    void updateNickName(String userPhone, String userNickName);

    @Query("UPDATE AUser SET userPassword = :userPassword WHERE userPhone = :userPhone")
    void updatePassword(String userPhone, String userPassword);

    @Query("UPDATE AUser SET userAvatarUri = :userAvatarUri WHERE  userPhone = :userPhone")
    void updateUserAvatarUri(String userPhone, String userAvatarUri);

    @Update
    void updateUser(AUser user);

    @Delete
    void delete(AUser user);

    @Query("SELECT * FROM AUSER ORDER BY userPhone DESC")
    List<AUser> getAllUsers();

    @Query("SELECT * FROM Auser WHERE UserPhone=:userPhone")
    List<AUser> getUserByPhone(String userPhone);

    @Query("SELECT * FROM Auser WHERE UserId=:userId")
    List<AUser> getUserById(String userId);

    @Query("SELECT * FROM Auser WHERE UserId=:userId AND UserPassword =:userPassword")
    List<AUser> getUserAndPassword(String userId, String userPassword);
}
