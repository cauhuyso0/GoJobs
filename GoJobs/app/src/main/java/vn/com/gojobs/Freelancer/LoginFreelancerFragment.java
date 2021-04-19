package vn.com.gojobs.Freelancer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.Fragment.ForgotPasswordFragment;
import vn.com.gojobs.MainFreelancer;
import vn.com.gojobs.Model.Freelancer;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;

import static android.content.Context.MODE_PRIVATE;
import static vn.com.gojobs.AuthActivity.tokenDevice;

public class LoginFreelancerFragment extends Fragment implements View.OnClickListener {

    TextView tvFreelancerRegister, tvFreelancerResetPassword;
    Button btnFreelancerSignin, btnBackFreelancerSignin;
    LoginButton btnFreelancerLoginFacebook;
    SignInButtonImpl btnFreelancerLoginGoogle;
    EditText edtEmail, edtMatKhau;
    FragmentManager fragmentManager;
    public static boolean isFreelancer = false;
    CallbackManager callbackManager;
    public GoogleSignInClient googleSignInClient;
    RetrofitClient retrofitClient = new RetrofitClient();
    final int RC_SIGN_IN = 0;
    public static boolean isLoggedInFlc;
    public static String _id, accessTokenDb, flcName, flcEmail;
    SharedPreferences sharedPreferences;
    private final String SHARE_PREF_NAME = "mypref";
    private final String KEY_EMAIL_FLC = "flcEmail";
    private final String KEY_NAME_FLC = "flcName";
    private final String KEY_ID_FLC = "flc_id";
    private final String KEY_ACCESSTOKENDB_FLC = "flc_accessTokenDb";
    public String matkhau;
    private CustomProgressBar customProgressBar;


    public LoginFreelancerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_freelancer_login, null);

        // ánh xạ
        initLayout(view);
        setOnClickForItem();
        customProgressBar = new CustomProgressBar(getContext());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        callbackManager = CallbackManager.Factory.create();
        btnFreelancerLoginFacebook.setReadPermissions("email");
        btnFreelancerLoginFacebook.setFragment(this);
        btnFreelancerLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                isLoggedInFlc = true;
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("fb ERR " + error);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            isLoggedInFlc = true;
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        } else if (isLoggedInFlc == true) {

            GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {

                    try {
                        flcEmail = object.getString("email");
                        final String flcPassword = object.getString("id");
                        sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_EMAIL_FLC, flcEmail);
                        editor.apply();

                        customProgressBar.show();
                        API api = retrofitClient.getClien().create(API.class);
                        api.flcRegister(flcEmail, flcPassword, true).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() == 409) {

                                    API api = retrofitClient.getClien().create(API.class);
                                    api.flcLogin(flcEmail,
                                            flcPassword,
                                            tokenDevice).enqueue(new Callback<Freelancer>() {
                                        @Override
                                        public void onResponse(Call<Freelancer> call, Response<Freelancer> response) {
                                            if (response.code() == 200) {

                                                Freelancer freelancer = response.body();
                                                _id = freelancer.get_id();
                                                accessTokenDb = freelancer.getAccessTokenDb();
                                                flcName = freelancer.getFlcName();
                                                sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(KEY_EMAIL_FLC, flcEmail);
                                                editor.putString(KEY_ID_FLC, _id);
                                                editor.putString(KEY_ACCESSTOKENDB_FLC, accessTokenDb);
                                                editor.putString(KEY_NAME_FLC, flcName);
                                                editor.apply();

                                                customProgressBar.dismiss();
                                                fragmentManager = getFragmentManager();
                                                MainFreelancer mainFreelancer = new MainFreelancer();
                                                fragmentManager.beginTransaction().replace(R.id.fragment_auth, mainFreelancer).commit();
                                                Toast.makeText(getActivity(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                customProgressBar.dismiss();
                                                AccessToken.setCurrentAccessToken(null);
                                                Toast.makeText(getActivity(), "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Freelancer> call, Throwable t) {
                                            customProgressBar.dismiss();
                                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else if (response.code() == 200) {
                                    API api = retrofitClient.getClien().create(API.class);
                                    api.flcLogin(flcEmail,
                                            flcPassword,
                                            tokenDevice).enqueue(new Callback<Freelancer>() {
                                        @Override
                                        public void onResponse(Call<Freelancer> call, Response<Freelancer> response) {
                                            if (response.code() == 200) {

                                                Freelancer freelancer = response.body();
                                                _id = freelancer.get_id();
                                                accessTokenDb = freelancer.getAccessTokenDb();
                                                flcName = freelancer.getFlcName();
                                                sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(KEY_EMAIL_FLC, flcEmail);
                                                editor.putString(KEY_ID_FLC, _id);
                                                editor.putString(KEY_ACCESSTOKENDB_FLC, accessTokenDb);
                                                editor.putString(KEY_NAME_FLC, flcName);
                                                editor.apply();

                                                customProgressBar.dismiss();
                                                fragmentManager = getFragmentManager();
                                                MainFreelancer mainFreelancer = new MainFreelancer();
                                                fragmentManager.beginTransaction().replace(R.id.fragment_auth, mainFreelancer).commit();
                                                Toast.makeText(getActivity(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                customProgressBar.dismiss();
                                                AccessToken.setCurrentAccessToken(null);
                                                Toast.makeText(getActivity(), "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Freelancer> call, Throwable t) {
                                            customProgressBar.dismiss();
                                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    customProgressBar.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                customProgressBar.dismiss();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle bundle = new Bundle();
            bundle.putString("fields", "gender, name, id, email, first_name, last_name");
            graphRequest.setParameters(bundle);
            graphRequest.executeAsync();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            flcEmail = account.getEmail();
            final String flcPassword = account.getId();

            customProgressBar.show();
            API api = retrofitClient.getClien().create(API.class);
            api.flcRegister(flcEmail, flcPassword, true).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 409) {
                        System.out.println(response.code());
                        API api = retrofitClient.getClien().create(API.class);
                        api.flcLogin(flcEmail,
                                flcPassword,
                                tokenDevice).enqueue(new Callback<Freelancer>() {
                            @Override
                            public void onResponse(Call<Freelancer> call, Response<Freelancer> response) {
                                System.out.println("API Login: " + response.code());
                                if (response.code() == 200) {
                                    Freelancer freelancer = response.body();
                                    _id = freelancer.get_id();
                                    Log.d("id freelancer", _id);
                                    accessTokenDb = freelancer.getAccessTokenDb();
                                    flcName = freelancer.getFlcName();
                                    sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_EMAIL_FLC, flcEmail);
                                    editor.putString(KEY_ID_FLC, _id);
                                    editor.putString(KEY_ACCESSTOKENDB_FLC, accessTokenDb);
                                    editor.putString(KEY_NAME_FLC, flcName);
                                    editor.apply();

                                    customProgressBar.dismiss();
                                    fragmentManager = getFragmentManager();
                                    final MainFreelancer mainFreelancer = new MainFreelancer();
                                    fragmentManager.beginTransaction().replace(R.id.fragment_auth, mainFreelancer).commit();
                                    Toast.makeText(getActivity(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 400) {

                                    customProgressBar.dismiss();
                                    Toast.makeText(getActivity(), "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                                    googleSignInClient.signOut();
                                }
                            }

                            @Override
                            public void onFailure(Call<Freelancer> call, Throwable t) {
                                customProgressBar.dismiss();
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (response.code() == 200) {
                        API api = retrofitClient.getClien().create(API.class);
                        api.flcLogin(flcEmail,
                                flcPassword,
                                tokenDevice).enqueue(new Callback<Freelancer>() {
                            @Override
                            public void onResponse(Call<Freelancer> call, Response<Freelancer> response) {
                                System.out.println("API Login: " + response.code());
                                if (response.code() == 200) {
                                    Freelancer freelancer = response.body();
                                    _id = freelancer.get_id();
                                    accessTokenDb = freelancer.getAccessTokenDb();
                                    flcName = freelancer.getFlcName();
                                    sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_EMAIL_FLC, flcEmail);
                                    editor.putString(KEY_ID_FLC, _id);
                                    editor.putString(KEY_ACCESSTOKENDB_FLC, accessTokenDb);
                                    editor.putString(KEY_NAME_FLC, flcName);
                                    editor.apply();

                                    customProgressBar.dismiss();
                                    fragmentManager = getFragmentManager();
                                    final MainFreelancer mainFreelancer = new MainFreelancer();
                                    fragmentManager.beginTransaction().replace(R.id.fragment_auth, mainFreelancer).commit();
                                    Toast.makeText(getActivity(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 400) {

                                    customProgressBar.dismiss();
                                    Toast.makeText(getActivity(), "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                                    googleSignInClient.signOut();
                                }
                            }

                            @Override
                            public void onFailure(Call<Freelancer> call, Throwable t) {

                                customProgressBar.dismiss();
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    /**
     * map all component in this screen
     *
     * @param view
     */
    private void initLayout(View view) {
        tvFreelancerRegister = view.findViewById(R.id.tvRegister_Freelancer);
        tvFreelancerResetPassword = view.findViewById(R.id.tvResetPassword_Freelancer);
        btnFreelancerSignin = view.findViewById(R.id.btnSignin_Freelancer);
        btnBackFreelancerSignin = view.findViewById(R.id.btnBackSignin_Freelancer);
        btnFreelancerLoginFacebook = view.findViewById(R.id.btnLoginFacebook_Freelancer);
        btnFreelancerLoginGoogle = view.findViewById(R.id.btnLoginGoogle_Freelancer);
        edtEmail = view.findViewById(R.id.edtEmailSignin_Freelancer);
        edtMatKhau = view.findViewById(R.id.edtPasswordSignin_Freelancer);
    }

    /**
     * set event click for item on screen
     */
    private void setOnClickForItem() {
        tvFreelancerRegister.setOnClickListener(this);
        tvFreelancerResetPassword.setOnClickListener(this);
        btnFreelancerSignin.setOnClickListener(this);
        btnBackFreelancerSignin.setOnClickListener(this);
        btnFreelancerLoginFacebook.setOnClickListener(this);
        btnFreelancerLoginGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBackSignin_Freelancer: //button back on left navigation
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btnLoginFacebook_Freelancer: //btn login with facebook

                break;
            case R.id.btnLoginGoogle_Freelancer: //btn login with google
                signIn();
                break;
            case R.id.btnSignin_Freelancer: //btn dang nhap

                customProgressBar.show();
                flcEmail = edtEmail.getText().toString();
                matkhau = edtMatKhau.getText().toString();
                fragmentManager = getFragmentManager();
                isFreelancer = true;
                API api = retrofitClient.getClien().create(API.class);

                api.flcLogin(flcEmail, matkhau, tokenDevice).enqueue(new Callback<Freelancer>() {
                    @Override
                    public void onResponse(Call<Freelancer> call, Response<Freelancer> response) {
                        if (response.code() == 200) {

                            Freelancer freelancer = response.body();
                            _id = freelancer.get_id();
                            accessTokenDb = freelancer.getAccessTokenDb();
                            sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_EMAIL_FLC, flcEmail);
                            editor.putString(KEY_ID_FLC, _id);
                            editor.putString(KEY_ACCESSTOKENDB_FLC, accessTokenDb);
                            editor.apply();

                            //show dialog đăng nhập thành công
                            customProgressBar.dismiss();
                            Toast.makeText(getActivity(), "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                            //chuyển màn hình main freelancer
                            MainFreelancer mainFreelancer = new MainFreelancer();
                            fragmentManager.beginTransaction().replace(R.id.fragment_auth, mainFreelancer).commit();

                        } else if (response.code() == 404) {

                            customProgressBar.dismiss();
                            Toast.makeText(getActivity(), "Tài khoản không tồn tại.", Toast.LENGTH_SHORT).show();

                        } else if (response.code() == 409) {

                            customProgressBar.dismiss();
                            Toast.makeText(getActivity(), "Sai mật khẩu.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Freelancer> call, Throwable t) {
                        //show dialog Sai mật khẩu
                        customProgressBar.dismiss();
                        Toast.makeText(getActivity(), "Gặp vấn đề về mạng.", Toast.LENGTH_SHORT).show();
                    }
                });

                break;

            case R.id.tvRegister_Freelancer: //tv dang ky
                fragmentManager = getFragmentManager();
                RegisterFreelancerFragment registerFreelancerFragment = new RegisterFreelancerFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_auth, registerFreelancerFragment).addToBackStack(RegisterFreelancerFragment.TAG).commit();
                break;

            case R.id.tvResetPassword_Freelancer: //tv quen mat khau
                fragmentManager = getFragmentManager();
                ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_auth, forgotPasswordFragment).addToBackStack(ForgotPasswordFragment.TAG).commit();
                break;

            default:
                break;
        }
    }



    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
