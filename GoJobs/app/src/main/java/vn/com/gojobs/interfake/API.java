package vn.com.gojobs.interfake;

import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.com.gojobs.Model.APIContract;
import vn.com.gojobs.Model.Contract;
import vn.com.gojobs.Model.Employer;
import vn.com.gojobs.Model.Feedback;
import vn.com.gojobs.Model.Follow;
import vn.com.gojobs.Model.Freelancer;
import vn.com.gojobs.Model.Job;
import vn.com.gojobs.Model.Location;
import vn.com.gojobs.Model.Message;
import vn.com.gojobs.Model.Notification;
import vn.com.gojobs.Model.Receipt;
import vn.com.gojobs.Model.RoomMessage;
import vn.com.gojobs.Model.Wallet;

public interface API {

    //API register account employer
    @FormUrlEncoded
    @POST("/employer/register")
    Call<Void> empRegister(@Field("empEmail") String empEmail, @Field("empPassword") String empPassword, @Field("empTerm") boolean empTerm);

    //API login employer
    @FormUrlEncoded
    @POST("/employer/login")
    Call<Employer> empLogin(@Field("empEmail") String empEmail, @Field("empPassword") String empPassword, @Field("empTokenDevice") String empTokenDevice);

    //API register Freelancer
    @FormUrlEncoded
    @POST("/freelancer/flcRegister")
    Call<Void> flcRegister(@Field("flcEmail") String flcEmail, @Field("flcPassword") String flcPassword, @Field("flcTerm") boolean flcTerm);

    //API login Freelancer
    @FormUrlEncoded
    @POST("/freelancer/flcLogin")
    Call<Freelancer> flcLogin(@Field("flcEmail") String flcEmail, @Field("flcPassword") String flcPassword, @Field("flcTokenDevice") String flcTokenDevice);

    //API forgot password freelancer
    @POST("sendMail/sendEmailRePasswordFlc")
    Call<Void> flcSendEmailRePassword(@Query("flcEmail") String flcEmail);

    //API forgot password emoloyer
    @POST("sendMail/sendEmailRePasswordEmp")
    Call<Void> empSendEmailRePassword(@Query("empEmail") String empEmail);

    //API change password freelancer
    @FormUrlEncoded
    @PUT("freelancer/updatePassword")
    Call<Void> updatePasswordFlc(@Field("flcEmail") String flcEmail,
                                 @Field("flcPassword") String flcPasswor,
                                 @Field("flcNewPassword") String flcNewPassword,
                                 @Field("accessTokenDb") String accessTokenDb);

    //API get profile employer
    @GET("employer/findEmployerById")
    Call<Employer> getProfileEmployer(@Query("_id") String _id,
                                      @Query("accessTokenDb") String accessTokenDb);

    //API update token device freelancer when logout
    @PUT("freelancer/flcUpdateToken")
    Call<Void> flcUpdateTokenDevice(@Query("_id") String _id,
                                    @Query("flcTokenDevice") String flcTokenDevice,
                                    @Query("accessTokenDb") String accessTokenDb);

    //API change password employer
    @FormUrlEncoded
    @PUT("employer/updatePassword")
    Call<Void> updatePasswordEmp(@Field("empEmail") String empEmail,
                                 @Field("empPassword") String empPassword,
                                 @Field("empNewPassword") String empNewPassword,
                                 @Field("accessTokenDb") String accessTokenDb);

    //API update token device freelancer when logout
    @PUT("employer/empUpdateToken")
    Call<Void> empUpdateTokenDevice(@Query("_id") String _id,
                                    @Query("empTokenDevice") String empTokenDevice,
                                    @Query("accessTokenDb") String accessTokenDb);

    //API send OTP
    @POST("otp/getOTP")
    Call<Void> getOTP(@Query("phone") String phone,
                      @Query("accessTokenDb") String accessTokenDb);

    //API verify OTP
    @POST("otp/verifyOTP")
    Call<Void> verifyOTP(@Query("phone") String phone,
                         @Query("code") String code,
                         @Query("accessTokenDb") String accessTokenDb);

    //API create new Job
    @POST("job/createNewJob")
    Call<Void> createNewJob(@Query("empId") String empId,
                            @Query("jobTitle") String jobTitle,
                            @Query("jobDescription") String jobDescription,
                            @Query("jobSalary") int jobSalary,
                            @Query("jobPaymentType") String jobPaymentType,
                            @Query("experienceRequired") boolean experienceRequired,
                            @Query("jobField") String jobField,
                            @Query("jobStart") String jobStart,
                            @Query("jobEnd") String jobEnd,
                            @Query("jobDuration") int jobDuration,
                            @Query("jobStatus") String jobStatus,
                            @Query("jobTotalSalaryPerHeadCount") int jobTotalSalaryPerHeadCount,
                            @Query("jobHeadCountTarget") int jobHeadCountTarget,
                            @Query("jobAddress") String jobAddress,
                            @Query("accessTokenDb") String accessTokenDb);

    //API get job with time create
    @GET("job/jobPaginationWithTime")
    Call<List<Job>> getJobWithTime(@Query("sort") String sort,
                                   @Query("pageNumber") int pageNumber,
                                   @Query("pageSize") int pageSize,
                                   @Query("accessTokenDb") String accessTokenDb);

    //API get job with filter
    @GET("job/jobPagination")
    Call<List<Job>> getJobWithFilter(
            @Query("sort") String sort,
            @Query("filter") List<String> filter,
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize,
            @Query("accessTokenDb") String accessTokenDb);

    //API get job with search
    @GET("job/jobPagination")
    Call<List<Job>> getJobWithSearch(@Query("search") String search,
                                     @Query("sort") String sort,
                                     @Query("pageNumber") int pageNumber,
                                     @Query("pageSize") int pageSize,
                                     @Query("accessTokenDb") String accessTokenDb);

    //API get job with address
    @GET("job/jobPaginationWithAddress")
    Call<List<Job>> getJobWithAddress(@Query("sort") String sort,
                                      @Query("search") String search,
                                      @Query("pageNumber") int pageNumber,
                                      @Query("pageSize") int pageSize,
                                      @Query("accessTokenDb") String accessTokenDb);

    //API get job by empId and contractStatus
    @GET("contract/getJobByContractStatus")
    Call<List<Job>> getJobByContractStatus(@Query("userId") String userId,
                                           @Query("contractStatus") String contractStatus,
                                           @Query("pageNumber") int pageNumber,
                                           @Query("pageSize") int pageSize,
                                           @Query("accessTokenDb") String accessTokenDb);

    //API get contract by jobId
    @GET("contract/getContractByJobId")
    Call<List<Contract>> getContractByJobId(@Query("jobId") String jobId,
                                            @Query("contractStatus") String contractStatus,
                                            @Query("accessTokenDb") String accessTokenDb);

    @GET("job/allJobsByEmpId")
    Call<List<Job>> getJobByEmpId(@Query("empId") String empId,
                                  @Query("accessTokenDb") String accessTokenDb);

    //API get freelancer with rating
    @GET("freelancer/flcPaginationAll")
    Call<List<Freelancer>> getFlcPaginationAll(@Query("sort") String sort,
                                               @Query("pageNumber") int pageNumber,
                                               @Query("pageSize") int pageSize,
                                               @Query("accessTokenDb") String accessTokenDb);

    @GET("freelancer/flcPaginationWithAddress")
    Call<List<Freelancer>> getFlcPaginationWithAddress(@Query("search") String search,
                                                       @Query("sort") String sort,
                                                       @Query("pageNumber") int pageNumber,
                                                       @Query("pageSize") int pageSize,
                                                       @Query("accessTokenDb") String accessTokenDb);

    //API get freelancer with filter address
    @GET("freelancer/flcPaginations")
    Call<List<Freelancer>> getFlcPaginations(@Query("search") String search,
                                             @Query("sort") String sort,
                                             @Query("pageNumber") int pageNumber,
                                             @Query("pageSize") int pageSize,
                                             @Query("accessTokenDb") String accessTokenDb);

    //API get profile freelancer
    @GET("freelancer/flcProfile")
    Call<Freelancer> getFlcProfile(@Query("_id") String _id,
                                   @Query("accessTokenDb") String accessTokenDb);

    //API get location city
    @GET("location/city")
    Call<List<Location>> getDistrict();

    //API get location district in city
    @GET("location/city/{id}/district")
    Call<List<String>> getProvince(@Path("id") int ID);

    //API update info freelancer
    @FormUrlEncoded
    @POST("freelancer/flcUpdatedInfo")
    Call<Freelancer> flcUpdateInfo(@Field("flcName") String flcName,
                                   @Field("flcPhone") String flcPhone,
                                   @Field("_id") String _id,
                                   @Field("flcBirthday") String flcBirthday,
                                   @Field("flcAvatar") String flcAvatar,
                                   @Field("flcSex") String flcSex,
                                   @Field("flcAddress") String flcAddress,
                                   @Field("flcEdu") String flcEdu,
                                   @Field("flcMajor") String flcMajor,
                                   @Field("flcLanguages") String flcLanguages,
                                   @Field("accessTokenDb") String accessTokenDb);

    @FormUrlEncoded
    @POST("employer/updatedEmployerInfo")
    Call<Employer> updatedEmployerInfo(@Field("empLogo") String empLogo,
                                       @Field("empName") String empName,
                                       @Field("empPhone") String empPhone,
                                       @Field("empAddress") String empAddress,
                                       @Field("empDescription") String empDescription,
                                       @Field("empTaxCode") String empTaxCode,
                                       @Field("_id") String _id,
                                       @Field("accessTokenDb") String accessTokenDb);

    //API get notification for freelancer
    @GET("notification/getNotification")
    Call<List<Notification>> getNotificationFlc(@Query("flcId") String flcId,
                                                @Query("pageNumber") int pageNumber,
                                                @Query("pageSize") int pageSize,
                                                @Query("accessTokenDb") String accessTokenDb);

    //API get notification for employer
    @GET("notification/getNotificationForEmp")
    Call<List<Notification>> getNotificationForEmp(@Query("empId") String empId,
                                                   @Query("pageNumber") int pageNumber,
                                                   @Query("pageSize") int pageSize,
                                                   @Query("accessTokenDb") String accessTokenDb);

    //API get Job Detail with notification
    @GET("job/jobDetail")
    Call<List<Job>> getJobDetail(@Query("_id") String _id,
                                 @Query("accessTokenDb") String accessTokenDb);

    @POST("contract/createNewContract")
    Call<Void> createContractByFreelancer(@Query("jobId") String jobId,
                                          @Query("flcId") String flcId,
                                          @Query("jobTotalSalaryPerHeadCount") String jobTotalSalaryPerHeadCount,
                                          @Query("contractStatus") String contracStatus,
                                          @Query("empId") String empId,
                                          @Query("accessTokenDb") String accessTokenDb);

    //API delete contract by freelancer
    @DELETE("contract/deteledContract")
    Call<Void> deleteContractById(@Query("_id") String _id,
                                  @Query("accessTokenDb") String accessTokenDb);

    //API new room chat
    @FormUrlEncoded
    @POST("message/newMessage")
    Call<RoomMessage> newMessage(@Field("empId") String empId,
                                 @Field("flcId") String flcId,
                                 @Field("accessTokenDb") String accessTokenDb);

    //API new message
    @FormUrlEncoded
    @POST("message/newMessage")
    Call<Void> newMessage1(@Field("empId") String empId,
                           @Field("flcId") String flcId,
                           @Field("mess") JSONArray mess,
                           @Field("accessTokenDb") String accessTokenDb);

    //API get message notification by flc
    @GET("message/getNotificationMessageByFlc")
    Call<List<Message>> getMessageNotificationByFlc(@Query("flcId") String flcId,
                                                    @Query("pageNumber") int pageNumber,
                                                    @Query("pageSize") int pageSize,
                                                    @Query("accessTokenDb") String accessTokenDb);

    //API get message notification by emp
    @GET("message/getNotificationMessageByEmp")
    Call<List<Message>> getMessageNotificationByEmp(@Query("empId") String empId,
                                                    @Query("pageNumber") int pageNumber,
                                                    @Query("pageSize") int pageSize,
                                                    @Query("accessTokenDb") String accessTokenDb);

    //API get message detail
    @GET("message/getMessageDetail")
    Call<Message> getMessageDetail(@Query("_id") String _id,
                                   @Query("accessTokenDb") String accessTokenDb);

    //API employer create feedback
    @POST("feedback/createEmpFeedback")
    Call<Void> createEmpfeedback(@Query("empId") String empId,
                                 @Query("flcId") String flcId,
                                 @Query("jobId") String jobId,
                                 @Query("starRating") float starRating,
                                 @Query("comment") String comment,
                                 @Query("accessTokenDb") String accessTokenDb);

    //API freelancer create feedback
    @POST("feedback/createFlcFeedback")
    Call<Void> createFlcfeedback(@Query("empId") String empId,
                                 @Query("flcId") String flcId,
                                 @Query("jobId") String jobId,
                                 @Query("starRating") int starRating,
                                 @Query("commnent") String commnent,
                                 @Query("accessTokenDb") String accessTokenDb);

    //API get feedback by freelancer
    @GET("feedback/getFeedbackByFlc")
    Call<List<Feedback>> getFeedbackByFlc(@Query("flcId") String flcId,
                                          @Query("accessTokenDb") String accessTokenDb);

    //API get feedback by employer
    @GET("feedback/getFeedbackByEmp")
    Call<List<Feedback>> getFeedbackByEmp(@Query("empId") String empId,
                                          @Query("accessTokenDb") String accessTokenDb);

    //API freelancer follow employer
    @POST("follow/createFlcFollowEmp")
    Call<Void> createFlcFollowEmp(@Query("flcId") String flcId,
                                  @Query("empId") String empId,
                                  @Query("createdBy") String createdBy,
                                  @Query("accessTokenDb") String accessTokenDb);

    @GET("follow/findFollow")
    Call<Void> hasFollowJob(@Query("empId") String empId,
                            @Query("flcId") String flcId,
                            @Query("accessTokenDb") String accessTokenDb);

    @GET("follow/findFollow")
    Call<Void> hadSaved(@Query("jobId") String jobId,
                        @Query("flcId") String flcId,
                        @Query("accessTokenDb") String accessTokenDb);

    //API employer follow freelancer
    @POST("follow/createEmpFollowFlc")
    Call<Void> createEmpFollowFlc(@Query("flcId") String flcId,
                                  @Query("empId") String empId,
                                  @Query("createdBy") String createdBy,
                                  @Query("accessTokenDb") String accessTokenDb);

    //API freelancer follow job
    @POST("follow/createFlcFollowJob")
    Call<Void> createFlcFollowJob(@Query("flcId") String flcId,
                                  @Query("jobId") String jobId,
                                  @Query("createdBy") String createdBy,
                                  @Query("accessTokenDb") String accessTokenDb);

    //API get job by freelancer follow
    @GET("follow/getJobByFlcFollow")
    Call<List<Follow>> getJobByFlcFollow(@Query("flcId") String flcId,
                                         @Query("accessTokenDb") String accessTokenDb);

    //API get job by freelancer follow
    @GET("job/fieldForSearch")
    Call<List<String>> getFieldForSearch(@Query("accessTokenDb") String accessTokenDb);

    //API get job by freelancer follow
    @GET("job/getFieldForSearchFlc")
    Call<List<String>> getFieldForSearchFlc(@Query("accessTokenDb") String accessTokenDb);

    //API get freelancer by employer follow
    @GET("follow/getFlcByEmpFollow")
    Call<List<Follow>> getFlcByEmpFollow(@Query("empId") String empId,
                                         @Query("accessTokenDb") String accessTokenDb);

    //API get employer by freelancer follow
    @GET("follow/getEmpByFlcFollow")
    Call<List<Follow>> getEmpByFlcFollow(@Query("flcId") String flcId,
                                         @Query("accessTokenDb") String accessTokenDb);

    //API freelancer del follow employer
    @DELETE("follow/delFollow")
    Call<Void> delFollowEmpByFlc(@Query("empId") String empId,
                                 @Query("flcId") String flcId,
                                 @Query("createdBy") String freelancerName,
                                 @Query("accessTokenDb") String accessTokenDb);

    //API employer del follow freelancer
    @DELETE("follow/delFollow")
    Call<Void> delFollowFlcByEmp(@Query("empId") String empId,
                                 @Query("flcId") String flcId,
                                 @Query("createdBy") String employerName,
                                 @Query("accessTokenDb") String accessTokenDb);

    //API freelancer del follow job
    @DELETE("follow/delFollow")
    Call<Void> delFollowJobByFlc(@Query("jobId") String jobId,
                                 @Query("flcId") String flcId,
                                 @Query("accessTokenDb") String accessTokenDb);

    //API get wallet for employer and freelancer
    @GET("wallet/getWalletByEndUserId")
    Call<Wallet> getWalletByEndUserId(@Query("_id") String _id,
                                      @Query("accessTokenDb") String accessTokenDb);

    //API get receipt history by id
    @GET("receipt/getReceiptHistory")
    Call<List<Receipt>> getReceiptHistory(@Query("_id") String _id,
                                          @Query("pageNumber") int pageNumber,
                                          @Query("pageSize") int pageSize,
                                          @Query("accessTokenDb") String accessTokenDb);

    //API employer accepte freelancer
    @PUT("contract/updateContractStatusById")
    Call<Void> updateContractStatusACCEPTED(@Query("_id") String _id,
                                            @Query("contractStatus") String contractStatus,
                                            @Query("updatedBy") String updatedBy,
                                            @Query("accessTokenDb") String accessTokenDb);

    //API employer approve contract
    @PUT("contract/updateContractStatusById")
    Call<Void> updateContractStatusAPPROVED(@Query("_id") String _id,
                                            @Query("contractStatus") String contractStatus,
                                            @Query("updatedBy") String updatedBy,
                                            @Query("accessTokenDb") String accessTokenDb);

    //API employer completed contract
    @PUT("contract/markContractsCompleted")
    Call<List<APIContract>> updateContractStatusCOMPLETED(@Query("_idContractList") String _idContractList,
                                                          @Query("accessTokenDb") String accessTokenDb);

    //API employer cancel contract
    @PUT("contract/markOneContractCancelled")
    Call<Void> updateContractStatusCANCELLED(@Query("_idContractList") String _idContractList,
                                             @Query("accessTokenDb") String accessTokenDb);

}
