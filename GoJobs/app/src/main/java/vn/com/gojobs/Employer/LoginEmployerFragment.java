package vn.com.gojobs.Employer;

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
import androidx.fragment.app.FragmentTransaction;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
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
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Fragment.ForgotPasswordFragment;
import vn.com.gojobs.MainEmployer;
import vn.com.gojobs.MainFreelancer;
import vn.com.gojobs.Model.Employer;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;

import static android.content.Context.MODE_PRIVATE;

public class LoginEmployerFragment extends Fragment implements View.OnClickListener {
    private static final String KEY_EMAIL_EPL = "";
    TextView tvRegisterEmployer, tvResetPasswordEmployer;
    Button btnSigninEmployer, btnBackSignInEmployer;
    EditText edtEmailSigninEmployer, edtPasswordSigninEmployer;
    FragmentManager fragmentManager;
    public static boolean isEmployer ;
    RetrofitClient retrofitClient = new RetrofitClient();
    LoginButton btnLoginFacebookEmployer;
    SignInButtonImpl btnLoginGoogleEmployer;
    CallbackManager callbackManager;
    public static GoogleSignInClient googleSignInClient;
    int RC_SIGN_IN = 0;
    public static boolean isLoggedInEmp;
    public static AccessToken accessTokenEmp;
    public static String empEmail;
    public static String _id, accessTokenDb, empName;
    SharedPreferences sharedPreferences;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String KEY_EMAIL_EMP = "empEmail";
    private static final String KEY_ID_EMP = "_id";
    private static final String KEY_NAME_EMP = "empName";
    private static final String KEY_ACCESSTOKENDB_EMP = "accessTokenDb";
    private FragmentTransaction fragmentTransaction;
    private CustomProgressBar customProgressBar;

    public LoginEmployerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_employer_login, null);

        customProgressBar = new CustomProgressBar(getContext());
        isEmployer = true;
        // ánh xạ
        initItem(view);
        Log.d("device", "tokenDevice " + AuthActivity.tokenDevice );
        // set onclick item
        setOnClick();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.

        callbackManager = CallbackManager.Factory.create();
        btnLoginFacebookEmployer.setReadPermissions("email");
        btnLoginFacebookEmployer.setFragment(this);
        btnLoginFacebookEmployer.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Demo", "successs!");
            }

            @Override
            public void onCancel() {
                Log.d("Demo", "canceled");
                isLoggedInEmp = false;
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Demo", "error");
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fragmentManager = getFragmentManager();
                MainFreelancer mainFreelancer = new MainFreelancer();
                fragmentManager.beginTransaction().replace(R.id.fragment_auth, mainFreelancer).commit();
            }

            @Override
            public void onCancel() {
                isLoggedInEmp = false;
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        return view;
    }

    private void initItem(View view) {
        tvRegisterEmployer = view.findViewById(R.id.tv_register_employer);
        tvResetPasswordEmployer = view.findViewById(R.id.tv_reset_password_employer);
        btnSigninEmployer = view.findViewById(R.id.btn_signIn_employer);
        btnBackSignInEmployer = view.findViewById(R.id.btn_back_signIn_employer);
        edtEmailSigninEmployer = view.findViewById(R.id.edt_email_signIn_employer);
        edtPasswordSigninEmployer = view.findViewById(R.id.edt_password_signIn_employer);
        btnLoginFacebookEmployer = view.findViewById(R.id.btn_login_facebook_employer);
        btnLoginGoogleEmployer = view.findViewById(R.id.btn_login_google_employer);
    }

    private void setOnClick() {
        btnBackSignInEmployer.setOnClickListener(this);
        btnLoginFacebookEmployer.setOnClickListener(this);
        btnLoginGoogleEmployer.setOnClickListener(this);
        btnSigninEmployer.setOnClickListener(this);
        tvRegisterEmployer.setOnClickListener(this);
        tvResetPasswordEmployer.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Login emp success!");

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        } else if (isLoggedInEmp == true){

            GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
//                    Log.d("demo", object.toString());
                    try {
                        empEmail = object.getString("email");
                        final String empPassword = object.getString("id");

                        API api = retrofitClient.getClien().create(API.class);
                        api.empRegister(empEmail, empPassword, true).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() == 409) {

                                    API api = retrofitClient.getClien().create(API.class);
                                    api.empLogin(empEmail,
                                            empPassword,
                                            AuthActivity.tokenDevice).enqueue(new Callback<Employer>() {
                                        @Override
                                        public void onResponse(Call<Employer> call, Response<Employer> response) {
                                            if (response.code() == 200) {
                                                Employer employer = response.body();
                                                _id = employer.get_id();
                                                accessTokenDb = employer.getAccessTokenDb();
                                                empName = employer.getEmpName();
                                                sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(KEY_EMAIL_EMP, empEmail);
                                                editor.putString(KEY_ID_EMP, _id);
                                                editor.putString(KEY_ACCESSTOKENDB_EMP, accessTokenDb);
                                                editor.putString(KEY_NAME_EMP, empName);
                                                editor.apply();
                                                fragmentManager = getFragmentManager();
                                                final MainEmployer mainEmployer = new MainEmployer();
                                                fragmentManager.beginTransaction().replace(R.id.fragment_auth, mainEmployer).commit();
                                                Toast.makeText(getActivity(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                AccessToken.setCurrentAccessToken(null);
                                                Toast.makeText(getActivity(), "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Employer> call, Throwable t) {
                                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else if (response.code() == 200) {
                                    API api = retrofitClient.getClien().create(API.class);
                                    api.empLogin(empEmail,
                                            empPassword,
                                            AuthActivity.tokenDevice).enqueue(new Callback<Employer>() {
                                        @Override
                                        public void onResponse(Call<Employer> call, Response<Employer> response) {
                                            System.out.println("API Login: " + response.code());
                                            if (response.code() == 200) {
                                                Employer employer = response.body();
                                                _id = employer.get_id();
                                                accessTokenDb = employer.getAccessTokenDb();
                                                empName = employer.getEmpName();
                                                sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(KEY_EMAIL_EMP, empEmail);
                                                editor.putString(KEY_ID_EMP, _id);
                                                editor.putString(KEY_ACCESSTOKENDB_EMP, accessTokenDb);
                                                editor.putString(KEY_NAME_EMP, empName);
                                                editor.apply();
                                                fragmentManager = getFragmentManager();
                                                final MainEmployer mainEmployer = new MainEmployer();
                                                fragmentManager.beginTransaction().replace(R.id.fragment_auth, mainEmployer).commit();
                                                Toast.makeText(getActivity(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                            } else if (response.code() == 400) {
                                                Toast.makeText(getActivity(), "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                                                googleSignInClient.signOut();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Employer> call, Throwable t) {
                                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

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
            isLoggedInEmp = true;
            empEmail = account.getEmail();
            final String empPassword = account.getId();
            API api = retrofitClient.getClien().create(API.class);
            api.empRegister(empEmail, empPassword, true).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 409) {
                        System.out.println(response.code());
                        API api = retrofitClient.getClien().create(API.class);
                        api.empLogin(empEmail,
                                empPassword,
                                AuthActivity.tokenDevice).enqueue(new Callback<Employer>() {
                            @Override
                            public void onResponse(Call<Employer> call, Response<Employer> response) {
                                System.out.println("API Login: " + response.code());
                                if (response.code() == 200) {
                                    Employer employer = response.body();
                                    Log.d("employer", "employer " + employer);
                                    _id = employer.get_id();
                                    accessTokenDb = employer.getAccessTokenDb();
                                    empName = employer.getEmpName();
                                    Log.d("nem", "nem " + empName);
                                    sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_EMAIL_EMP, empEmail);
                                    editor.putString(KEY_ID_EMP, _id);
                                    editor.putString(KEY_ACCESSTOKENDB_EMP, accessTokenDb);
                                    editor.putString(KEY_NAME_EMP, empName);
                                    editor.apply();
                                    fragmentManager = getFragmentManager();
                                    final MainEmployer mainEmployer = new MainEmployer();
                                    fragmentManager.beginTransaction().replace(R.id.fragment_auth, mainEmployer).commit();
                                    Toast.makeText(getActivity(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 400) {
                                    Toast.makeText(getActivity(), "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                                    googleSignInClient.signOut();
                                }
                            }

                            @Override
                            public void onFailure(Call<Employer> call, Throwable t) {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (response.code() == 200) {
                        API api = retrofitClient.getClien().create(API.class);
                        api.empLogin(empEmail,
                                empPassword,
                                AuthActivity.tokenDevice).enqueue(new Callback<Employer>() {
                            @Override
                            public void onResponse(Call<Employer> call, Response<Employer> response) {
                                System.out.println("API Login: " + response.code());
                                if (response.code() == 200) {
                                    Employer employer = response.body();
                                    _id = employer.get_id();
                                    accessTokenDb = employer.getAccessTokenDb();
                                    empName = employer.getEmpName();
                                    Log.d("a", "empName: " + empName );
                                    sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_EMAIL_EMP, empEmail);
                                    editor.putString(KEY_ID_EMP, _id);
                                    editor.putString(KEY_ACCESSTOKENDB_EMP, accessTokenDb);
                                    editor.putString(KEY_NAME_EMP, empName);
                                    editor.apply();
                                    fragmentManager = getFragmentManager();
                                    final MainEmployer mainEmployer = new MainEmployer();
                                    fragmentManager.beginTransaction().replace(R.id.fragment_auth, mainEmployer).commit();
                                    Toast.makeText(getActivity(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 400) {
                                    Toast.makeText(getActivity(), "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                                    googleSignInClient.signOut();
                                }
                            }

                            @Override
                            public void onFailure(Call<Employer> call, Throwable t) {
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
            e.printStackTrace();
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back_signIn_employer: //button back on left navigation

                getActivity().getSupportFragmentManager().popBackStack();

                break;

            case R.id.btn_login_google_employer: //btn login with google

                signIn();

                break;
            case R.id.btn_signIn_employer: //btn dang nhap

                customProgressBar.show();
                isEmployer = true;

                API api = retrofitClient.getClien().create(API.class);
                api.empLogin(edtEmailSigninEmployer.getText().toString(),
                        edtPasswordSigninEmployer.getText().toString(),
                        AuthActivity.tokenDevice).enqueue(new Callback<Employer>() {
                    @Override
                    public void onResponse(Call<Employer> call, Response<Employer> response) {
                        if (response.code() == 200) {
                            Employer employer = response.body();
                            _id = employer.get_id();
                            accessTokenDb = employer.getAccessTokenDb();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            empName = employer.getEmpName();

                            sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_EMAIL_EMP, edtEmailSigninEmployer.getText().toString());
                            editor.putString(KEY_ID_EMP, _id);
                            editor.putString(KEY_ACCESSTOKENDB_EMP, accessTokenDb);
                            editor.putString(KEY_NAME_EMP, empName);
                            editor.apply();

                            MainEmployer mainEmployer = new MainEmployer();
                            fragmentManager.beginTransaction().replace(R.id.fragment_auth, mainEmployer).commit();

                            Toast.makeText(getActivity(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 404) {

                            Toast.makeText(getActivity(), "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                            edtEmailSigninEmployer.setText("");
                            edtPasswordSigninEmployer.setText("");

                        } else if (response.code() == 400) {

                            Toast.makeText(getActivity(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            edtEmailSigninEmployer.setText("");
                            edtPasswordSigninEmployer.setText("");

                        }
                        customProgressBar.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Employer> call, Throwable t) {
                        customProgressBar.dismiss();
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.tv_register_employer: //tv dang ky

                fragmentManager = getFragmentManager();
                RegisterEmployerFragment registerEmployerFragment = new RegisterEmployerFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_auth, registerEmployerFragment).addToBackStack(RegisterEmployerFragment.TAG).commit();

                break;
            case R.id.tv_reset_password_employer: //tv quen mat khau

                fragmentManager = getFragmentManager();
                ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_auth, forgotPasswordFragment).addToBackStack(ForgotPasswordFragment.TAG).commit();

                break;
            default:
                break;
        }
    }
}
