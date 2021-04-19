package vn.com.gojobs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import vn.com.gojobs.Employer.LoginEmployerFragment;
import vn.com.gojobs.Employer.ProfileEmployerFragment;
import vn.com.gojobs.Freelancer.LoginFreelancerFragment;
import vn.com.gojobs.Freelancer.ProfileFreelancerFragment;

public class AuthActivity extends AppCompatActivity {

    public static final String TAG = "AuthActivity";
    ViewFlipper vFlipper;
    Button btnFreelancer_authFrag;
    Button btnEmployer_authFrag;
    FragmentManager fragmentManager;
    public static boolean isEmployer = false ;
    public static String tokenDevice;
    public static String empId;
    public static String flcId;
    public static String empName;
    public static String flcName;
    public static String empEmail;
    public static String flcEmail;
    public static Set<String> listFilter;
    public static SharedPreferences sharedPreferences;
    private final String SHARE_PREF_NAME = "mypref";
    private final String KEY_EMAIL_FLC = "flcEmail";
    private final String KEY_EMAIL_EMP = "empEmail";
    private final String KEY_ID_EMP = "_id";
    private final String KEY_NAME_FLC = "flcName";
    private final String KEY_NAME_EMP = "empName";
    private final String KEY_LIST_FILTER = "list_filter";
    private final String KEY_ID_FLC = "flc_id";
    private final String KEY_ACCESSTOKENDB_EMP = "accessTokenDb";
    private final String KEY_ACCESSTOKENDB_FLC = "flc_accessTokenDb";
    public static String accessTokenDbFlc, accessTokenDbEmp;
    public static Context contextApplication;
    public static String takeFrom = "";

    public static Context getContextApplication() {
        return contextApplication;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        contextApplication = getApplicationContext();
        setContentView(R.layout.activity_auth);
//        printHashKey(AuthActivity.this);
        //Ánh xạ

        btnEmployer_authFrag = findViewById(R.id.btnEmployer_authFrag);
        btnFreelancer_authFrag = findViewById(R.id.btnFreelancer_authFrag);
        vFlipper = findViewById(R.id.carouselView);

        sharedPreferences = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
        empEmail = sharedPreferences.getString(KEY_EMAIL_EMP, null);
        flcEmail = sharedPreferences.getString(KEY_EMAIL_FLC, null);
        empId = sharedPreferences.getString(KEY_ID_EMP, null);
        flcId = sharedPreferences.getString(KEY_ID_FLC, null);
        listFilter = sharedPreferences.getStringSet(KEY_LIST_FILTER, null);
        accessTokenDbFlc = sharedPreferences.getString(KEY_ACCESSTOKENDB_FLC, null);
        accessTokenDbEmp = sharedPreferences.getString(KEY_ACCESSTOKENDB_EMP, null);
        empName = sharedPreferences.getString(KEY_NAME_EMP, null);
        flcName = sharedPreferences.getString(KEY_NAME_FLC, null);
        Log.d("nam", "empName " + flcId + "  "+empId + flcEmail+ " "+empEmail);
        Log.d("acc", "token " + accessTokenDbFlc + "\n"+ accessTokenDbEmp);
        if (empEmail != null) {
            isEmployer = true;
            fragmentManager = getSupportFragmentManager();
            MainEmployer mainEmployer = new MainEmployer();
            fragmentManager.beginTransaction().replace(R.id.fragment_auth, mainEmployer).commit();
        } else if (flcEmail != null){
            fragmentManager = getSupportFragmentManager();
            MainFreelancer mainFreelancer = new MainFreelancer();
            fragmentManager.beginTransaction().replace(R.id.fragment_auth, mainFreelancer).commit();
        }

        int images[] = {R.drawable.carosel01, R.drawable.carosel02, R.drawable.carosel03, R.drawable.carosel04, R.drawable.carosel05};
        //foreach
        for (int image : images) {
            flipperImages(image);
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(AuthActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                tokenDevice = instanceIdResult.getToken();
                Log.i("FCM Token", tokenDevice);
                //saveToken(token);
            }
        });
        moveToLogin();
    }

    public void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        vFlipper.addView(imageView);
        vFlipper.setFlipInterval(4000); //4 secs
        vFlipper.setAutoStart(true);

        //animation
        vFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        vFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    public void moveToLogin() {

        fragmentManager = getSupportFragmentManager();

        // to fragment FreelancerLogin
        btnFreelancer_authFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFreelancerFragment loginFreelancerFragment = new LoginFreelancerFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_auth, loginFreelancerFragment).addToBackStack(TAG).commit();

            }
        });
        // to fragment EmployerLogin
        btnEmployer_authFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginEmployerFragment loginEmployerFragment = new LoginEmployerFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_auth, loginEmployerFragment).addToBackStack(TAG).commit();
                isEmployer = true;
            }
        });
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (takeFrom.equals("freelancer")) {
            ProfileFreelancerFragment profileFreelancerFragment = new ProfileFreelancerFragment();
            profileFreelancerFragment.setImageCallback().onImageCallback(requestCode, resultCode, data);
            takeFrom = "";
        } else if (takeFrom.equals("employer")) {
            ProfileEmployerFragment profileEmployerFragment = new ProfileEmployerFragment();
            profileEmployerFragment.setImageCallback().onImageCallback(requestCode, resultCode, data);
            takeFrom = "";
        }
    }
}
