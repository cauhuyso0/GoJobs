package vn.com.gojobs.Employer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Fragment.ChatFragment;
import vn.com.gojobs.Model.Freelancer;
import vn.com.gojobs.Model.RoomMessage;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;

public class CVFreelancerFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "CVFreelancerFragment";
    String idFreelancer;
    ImageView img_avatar_freelancer;
    TextView tvLienHe, tvLuuCV;
    private FragmentManager fragmentManager;
    EditText edt_name_freelancer, edt_email_freelancer, edt_sdt_freelancer, edt_hoc_van_freelancer, edt_nghe_nghiep_freelancer,
            edt_trinh_do_ngoai_ngu_freelancer, edt_gioi_tinh_CV, edt_ngay_sinh_CV, edt_dia_chi_freelancer;
    RetrofitClient retrofitClient = new RetrofitClient();
    private String endEmpId;
    String accessTokenDb;
    CustomProgressBar customProgressBar;

    public CVFreelancerFragment(String idFreelancer) {
        // Required empty public constructor
        this.idFreelancer = idFreelancer;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_v_freelancer, container, false);
        init(view);
        customProgressBar = new CustomProgressBar(getContext());

        if (LoginEmployerFragment._id != null) {
            endEmpId = LoginEmployerFragment._id;
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        } else {
            endEmpId = AuthActivity.empId;
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }

        freelancerDetail(idFreelancer);

        return view;
    }

    void init(View view) {
        img_avatar_freelancer = view.findViewById(R.id.img_avatar_freelancer);
        tvLienHe = view.findViewById(R.id.tv_lien_he_cv);
        tvLienHe.setOnClickListener(this);
        tvLuuCV = view.findViewById(R.id.tv_luu_cv);
        tvLuuCV.setOnClickListener(this);
        edt_name_freelancer = view.findViewById(R.id.edt_name_freelancer);
        edt_email_freelancer = view.findViewById(R.id.edt_email_freelancer);
        edt_sdt_freelancer = view.findViewById(R.id.edt_sdt_freelancer);
        edt_hoc_van_freelancer = view.findViewById(R.id.edt_hoc_van_freelancer);
        edt_nghe_nghiep_freelancer = view.findViewById(R.id.edt_nghe_nghiep_freelancer);
        edt_trinh_do_ngoai_ngu_freelancer = view.findViewById(R.id.edt_trinh_do_ngoai_ngu_freelancer);
        edt_gioi_tinh_CV = view.findViewById(R.id.edt_gioi_tinh_CV);
        edt_ngay_sinh_CV = view.findViewById(R.id.edt_ngay_sinh_CV);
        edt_dia_chi_freelancer = view.findViewById(R.id.edt_dia_chi_freelancer);
    }

    private void freelancerDetail(String nameItem) {

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.getFlcProfile(nameItem + "", accessTokenDb).enqueue(new Callback<Freelancer>() {
            @Override
            public void onResponse(Call<Freelancer> call, Response<Freelancer> response) {
                Freelancer freelancer = response.body();

                if (freelancer != null) {
                    if (freelancer.getFlcAvatar() != null) {
                        String avatar = freelancer.getFlcAvatar();
                        // parse base64 to bitmap (param : avataruser:  Result : String base 64 to bitmap)
                        byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        img_avatar_freelancer.setImageBitmap(decodedByte);
                    }

                    edt_name_freelancer.setText(freelancer.getFlcName());
                    edt_email_freelancer.setText(freelancer.getFlcEmail());
                    edt_sdt_freelancer.setText(freelancer.getFlcPhone() + "");
                    edt_hoc_van_freelancer.setText(freelancer.getFlcEdu());
                    edt_nghe_nghiep_freelancer.setText(freelancer.getFlcMajor());
                    edt_trinh_do_ngoai_ngu_freelancer.setText(freelancer.getFlcLanguages());
                    edt_gioi_tinh_CV.setText(freelancer.getFlcSex());
                    edt_ngay_sinh_CV.setText(freelancer.getFlcBirthday());
                    edt_dia_chi_freelancer.setText(freelancer.getFlcAddress());
                }

                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<Freelancer> call, Throwable t) {
                customProgressBar.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_lien_he_cv: //button back on left navigation
                newChat();
                break;

            case R.id.tv_luu_cv:
                customProgressBar.show();
                API api = retrofitClient.getClien().create(API.class);
                api.createEmpFollowFlc(idFreelancer + "",endEmpId + "",endEmpId + "",accessTokenDb).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200){
                            Toast.makeText(getActivity(), "Lưu CV thành công.", Toast.LENGTH_SHORT).show();
                        }
                        customProgressBar.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "Lưu CV thất bại.", Toast.LENGTH_SHORT).show();
                        customProgressBar.dismiss();
                    }
                });
                break;
        }
    }

    private void newChat() {
        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.newMessage(endEmpId + "", idFreelancer + "", accessTokenDb).enqueue(new Callback<RoomMessage>() {
            @Override
            public void onResponse(Call<RoomMessage> call, Response<RoomMessage> response) {
                RoomMessage roomMessage = response.body();
                LoginEmployerFragment._id = roomMessage.get_id();
                if (LoginEmployerFragment._id != null) {
                    fragmentManager = getFragmentManager();
                    ChatFragment chatFragment = new ChatFragment(LoginEmployerFragment._id);
                    fragmentManager.beginTransaction().replace(R.id.fl2, chatFragment).addToBackStack(ChatFragment.TAG).commit();
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<RoomMessage> call, Throwable t) {
                customProgressBar.dismiss();
            }
        });
    }
}