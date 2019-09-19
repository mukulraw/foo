package com.ratna.foosip;

import com.ratna.foosip.allUsersPOJO.allUsersBean;
import com.ratna.foosip.allUsersPOJO.allUsersRequestBean;
import com.ratna.foosip.profilePOJO.picRequestBean;
import com.ratna.foosip.profilePOJO.profileBean;
import com.ratna.foosip.profilePOJO.profileRequestBean;
import com.ratna.foosip.profilePOJO.profileUpdateBean;
import com.ratna.foosip.profilePOJO.statusRequestBean;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllAPIs {

    @POST("usersapi/active_users")
    Call<allUsersBean> getAllUsers(
            @Body allUsersRequestBean body , @HeaderMap Map<String , String> map
            );


    @POST("usersapi/profile")
    Call<profileBean> getProfile(
            @Body profileRequestBean body , @HeaderMap Map<String , String> map
    );


    @POST("usersapi/getuser")
    Call<profileBean> getUser(
            @Body getUserModel body , @HeaderMap Map<String , String> map
    );

    @POST("usersapi/editprofile")
    Call<profileBean> updatePic(
            @Body picRequestBean body , @HeaderMap Map<String , String> map
    );

    @POST("usersapi/editprofile")
    Call<profileBean> updateStatus(
            @Body statusRequestBean body , @HeaderMap Map<String , String> map
    );

    @POST("usersapi/editprofile")
    Call<profileBean> updateProfile(
            @Body profileUpdateBean body , @HeaderMap Map<String , String> map
    );





    // Core APIs
    @Multipart
    @POST("api/like_post.php")
    Call<String> LikePost(
            @Part("user_id") String user_id,
            @Part("post_id") String post_id);

    @Multipart
    @POST("api/unlike_post.php")
    Call<String> UnLikePost(
            @Part("user_id") String user_id,
            @Part("post_id") String post_id);

    @Multipart
    @POST("api/add_comment.php")
    Call<String> add_comment(
            @Part("user_id") String user_id,
            @Part("post_id") String post_id,
            @Part("comment") String comment);

    @Multipart
    @POST("api/get_groups.php")
    Call<List<groupListBean>> getGroupList(
            @Part("user_id") String user_id,
            @Part("rid") String rid
    );

    @Multipart
    @POST("api/create_group.php")
    Call<createGroupBean> createGroup(
            @Part("user_id") String user_id,
            @Part("rid") String rid,
            @Part("group_name") String groupName,
            @Part("group_users") String groupUsers
    );


    @Multipart
    @POST("api/send_message.php")
    Call<createGroupBean> sendMessage(
            @Part("group_id") String groupId,
            @Part("sender_id") String senderId,
            @Part("message") String message
    );

    @Multipart
    @POST("api/updateFirebase.php")
    Call<Integer> updateFirebase(
            @Part("user_id") String userId,
            @Part("fid") String fid
    );


    @Multipart
    @POST("api/get_messsages.php")
    Call<List<chatBean>> getMessages(
            @Part("group_id") String groupId
    );

    @Multipart
    @POST("api/post_image.php")
    Call<String> postImage(
            @Part("user_id") String userid,
            @Part("rid") String rid,
            @Part("post_type") String postType,
            @Part("desc") String desc,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("api/post_text.php")
    Call<String> postText(
            @Part("user_id") String userid,
            @Part("rid") String rid,
            @Part("post_type") String postType,
            @Part("message") String message
    );

    @Multipart
    @POST("api/get_posts.php")
    Call<List<postListbean>> getPosts(
            @Part("rid") String rid
    );



}
