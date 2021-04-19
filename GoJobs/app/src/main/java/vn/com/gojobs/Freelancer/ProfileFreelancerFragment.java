package vn.com.gojobs.Freelancer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Model.Freelancer;
import vn.com.gojobs.Model.Location;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CaptureDialog;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.OnImageCallback;

import static android.app.Activity.RESULT_OK;
import static com.facebook.internal.instrument.InstrumentUtility.deleteFile;

public class ProfileFreelancerFragment extends Fragment implements View.OnClickListener, OnImageCallback {

    public static final String TAG = "ProfileFreelancerFragment";
    final Calendar newCalendar = Calendar.getInstance();
    private final String DATE_FORMAT = "dd-MM-yyyy";
    private Spinner spGioiTinh, spProvince, spDistrict;
    private String[] sexs = {"Nam", "Nữ"};
    private ArrayList<String> districts = new ArrayList<>();
    private ArrayList<String> province = new ArrayList<>();
    private ImageView circleImageView;
    private FragmentTransaction fragmentTransaction;
    private OnImageCallback onImageCallback;
    RetrofitClient retrofitClient = new RetrofitClient();
    String endFlcId, mailFlc;
    Button btnFinish;
    private TextView tvBirthday;
    private EditText edtTen, edtEmail, edtDiaChi, edtSDT, edtTrinhDoNgoaiNgu, edtNgheNghiep, edtHocVan;
    private String ten = "", mail = "", sdt = "", ngheNghiep = "", ngaySinh = "", trinhDoNgoaiNgu = "", hocVan = "", gioiTinh = "", diaChi = "";
    public final int REQUEST_IMAGE_CAPTURE = 1;
    public final int REQUEST_IMAGE_GALLERY = 2;
    private ArrayList<Location> locations;
    CustomProgressBar customProgressBar;
    private static File mFileName;
    private static Bitmap bitmap;
    private View view;
    private static boolean isCapture = false;
    private static boolean isGallery = false;
    Uri selectedImage;
    private static Bitmap avatar;
    private String avatarUser;
    private boolean isDone = false;
    String accessTokenDb;
    private boolean isHasAvatar = false;
    private boolean loadDataDone = false;

    public String getFileName() {
        return mFileName.getPath();
    }

    public OnImageCallback setImageCallback() {
        return onImageCallback;
    }

    public ProfileFreelancerFragment() {
        this.onImageCallback = this;
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onImageCallback = this;

        customProgressBar = new CustomProgressBar(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_freelancer_profile, null);

        // map item and set action click
        mapItem(view);

        if (LoginFreelancerFragment._id != null) {
            endFlcId = LoginFreelancerFragment._id;
            mailFlc = LoginFreelancerFragment.flcEmail;
            accessTokenDb = LoginFreelancerFragment.accessTokenDb;
        } else {
            endFlcId = AuthActivity.flcId;
            mailFlc = AuthActivity.flcEmail;
            accessTokenDb = AuthActivity.accessTokenDbFlc;
        }

        edtEmail.setText(mailFlc);

        //dummy data
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, sexs);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spGioiTinh.setAdapter(aa);

        getDistrict();

        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tinh = spDistrict.getSelectedItem().toString();
                for (Location location : locations) {
                    if (tinh.equals(location.getTitle())) {
                        getProvince(location.getID());
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void getProfileFlc() {
        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.getFlcProfile(endFlcId, accessTokenDb).enqueue(new Callback<Freelancer>() {
            @Override
            public void onResponse(Call<Freelancer> call, Response<Freelancer> response) {
                Freelancer freelancer = response.body();

                if (freelancer != null) {
                    String avatar = freelancer.getFlcAvatar();
                    avatarUser = avatar;
                    if (avatar != null && !avatar.equals("")) {
                        isHasAvatar = true;
                        // parse base64 to bitmap (param : avataruser:  Result : String base 64 to bitmap)
                        byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        circleImageView.setImageBitmap(decodedByte);
                    } else {
                        circleImageView.setImageDrawable(getActivity().getDrawable(R.drawable.avatar_logo));
                    }

                    if (!freelancer.getFlcName().equals("")) {
                        edtTen.setText(freelancer.getFlcName());
                    } else {
                        edtTen.setText("");
                    }
                    edtEmail.setText(freelancer.getFlcEmail());
                    if (freelancer.getFlcPhone() != null) {
                        edtSDT.setText(freelancer.getFlcPhone() + "");
                    } else {
                        edtSDT.setHint("123456789");
                    }
                    edtHocVan.setText(freelancer.getFlcEdu());
                    edtNgheNghiep.setText(freelancer.getFlcMajor());
                    edtTrinhDoNgoaiNgu.setText(freelancer.getFlcLanguages());
                    String gioiTInh = freelancer.getFlcSex();
                    if (gioiTInh != null) {
                        if (gioiTInh.equals("Nam")) {
                            spGioiTinh.setSelection(0);
                        } else {
                            spGioiTinh.setSelection(1);
                        }
                    } else {
                        spGioiTinh.setSelection(0);
                    }
                    if (freelancer.getFlcBirthday() != null && !freelancer.getFlcBirthday().equals("")) {
                        tvBirthday.setText(freelancer.getFlcBirthday());
                    } else {
                        tvBirthday.setHint("01-01-2000");
                    }

                    Log.d("address : ", "ad : ---- " + freelancer.getFlcAddress() + "index char ',' : " + freelancer.getFlcAddress().indexOf(",") + " char num2 ',' : " + freelancer.getFlcAddress().indexOf(",", freelancer.getFlcAddress().indexOf(",") + 1));
                    if (freelancer.getFlcAddress().contains(",")) {
                        edtDiaChi.setText(freelancer.getFlcAddress().substring(0, freelancer.getFlcAddress().indexOf(",")));
                        String quan = freelancer.getFlcAddress().substring(freelancer.getFlcAddress().indexOf(",") + 2, freelancer.getFlcAddress().indexOf(",", freelancer.getFlcAddress().indexOf(",") + 1)).trim();
                        String tinh = freelancer.getFlcAddress().substring(freelancer.getFlcAddress().indexOf(",", freelancer.getFlcAddress().indexOf(",") + 1) + 2).trim();

                        for (int i = 0; i < districts.size(); i++) {
                            Log.e("duc", "onResponse: " + districts.get(i));
                            if (districts.get(i).equals(tinh)) {
                                spDistrict.setSelection(i);
                                break;
                            }
                        }

                        for (int i = 0; i < province.size(); i++) {
                            Log.e("duc", "onResponse: " + province.get(i));
                            if (province.get(i).equals(quan)) {
                                spProvince.setSelection(i);
                                break;
                            }
                        }
                    } else {
                        edtDiaChi.setText(freelancer.getFlcAddress());
                    }
                }
                customProgressBar.dismiss();
                loadDataDone = true;
            }
            @Override
            public void onFailure(Call<Freelancer> call, Throwable t) {

                customProgressBar.dismiss();
            }
        });
    }

    private void mapItem(View view) {
        circleImageView = view.findViewById(R.id.img_avatar_freelancer_profile);
        circleImageView.setOnClickListener(this);
        edtTen = view.findViewById(R.id.edt_name_freelancer);
        edtEmail = view.findViewById(R.id.edt_email_freelancer);
        edtSDT = view.findViewById(R.id.edt_sdt_freelancer);
        edtHocVan = view.findViewById(R.id.edt_hoc_van_freelancer);
        edtNgheNghiep = view.findViewById(R.id.edt_nghe_nghiep_freelancer);
        edtTrinhDoNgoaiNgu = view.findViewById(R.id.edt_trinh_do_ngoai_ngu_freelancer);
        spGioiTinh = view.findViewById(R.id.sp_gioi_tinh_freelancer);
        tvBirthday = view.findViewById(R.id.tv_ngay_sinh_freelancer);
        tvBirthday.setOnClickListener(this);
        spProvince = view.findViewById(R.id.sp_huyen);
        spDistrict = view.findViewById(R.id.sp_tinh);
        edtDiaChi = view.findViewById(R.id.edt_dia_chi_freelancer);
        btnFinish = view.findViewById(R.id.btn_finish_freelancer);
        btnFinish.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ngay_sinh_freelancer:
                showDatePickerDialog();
                break;
            case R.id.img_avatar_freelancer_profile:

                AuthActivity.takeFrom = "freelancer";
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                CaptureDialog captureDialog = new CaptureDialog(this);
                captureDialog.show(fragmentTransaction, CaptureDialog.TAG);

                break;
            case R.id.btn_finish_freelancer:

                if (isDone || isHasAvatar) {
                    flcUpdateInfo();
                    btnFinish.setEnabled(false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnFinish.setEnabled(true);
                        }
                    }, 3000);
                    break;
                } else {
                    Toast.makeText(getActivity(), "Vui lòng đợi trong giây lát.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    // show dialog date picker
    private void showDatePickerDialog() {
        final DatePickerDialog StartTime = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                dateFormat.setTimeZone(TimeZone.getTimeZone("EN"));
                String date = dateFormat.format(newDate.getTime());
                tvBirthday.setText(date);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.show();
    }

    @Override
    public void takePicture() {
        dispatchTakePictureIntent();
    }

    @Override
    public void takeGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent, REQUEST_IMAGE_GALLERY);

    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public void onImageCallback(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("test", "onImageCallback: into image call back");

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {

                isCapture = true;
                bitmap = null;

                String s = getFileName();
                Bitmap bm = BitmapFactory.decodeFile(s);
                Matrix matrix = new Matrix();
                matrix.postRotate(0);

                ExifInterface ei = null;
                int orientation = 0;
                try {
                    ei = new ExifInterface(s);
                    orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                switch (orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        bm = rotateImage(bm, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        bm = rotateImage(bm, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        bm = rotateImage(bm, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        bm = bm;
                        break;
                }
                Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                bitmap = rotatedBitmap;

            } else {
                isCapture = false;
            }

        } else if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (resultCode == RESULT_OK) {
                isGallery = true;
                bitmap = null;

                try {
                    selectedImage = data.getData();

                    Context context = AuthActivity.getContextApplication();
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);

                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else {
                isGallery = false;
            }
        }
    }

    @Override
    public void onRemove() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circleImageView.setImageDrawable(getActivity().getDrawable(R.drawable.avatar_logo));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isGallery) {
            circleImageView.setImageBitmap(bitmap);
            avatar = bitmap;
            isGallery = false;
            bitmap = null;
        } else if (isCapture) {
            circleImageView.setImageBitmap(bitmap);
            avatar = bitmap;
            isCapture = false;
            bitmap = null;
        }
        new MyThread().start();
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            //TODO
            avatarUser = "";

            if (avatar != null) {

                Bitmap resized = Bitmap.createScaledBitmap(avatar, (int) (avatar.getWidth() * 0.5), (int) (avatar.getHeight() * 0.5), true);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                avatarUser = Base64.encodeToString(byteArray, Base64.DEFAULT);

                isDone = true;
            }
        }
    }

    void flcUpdateInfo() {

        if (edtTen.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Bạn cần nhập tên.", Toast.LENGTH_SHORT).show();
        } else {
            ten = edtTen.getText().toString();
        }

        mail = edtEmail.getText().toString();

        if (edtSDT.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Bạn cần nhập sdt.", Toast.LENGTH_SHORT).show();
        } else {
            sdt = edtSDT.getText().toString();
        }

        if (edtHocVan.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Bạn cần nhập học vấn.", Toast.LENGTH_SHORT).show();
        } else {
            hocVan = edtHocVan.getText().toString();
        }

        if (edtNgheNghiep.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Bạn cần nhập nghề nghiệp.", Toast.LENGTH_SHORT).show();
        } else {
            ngheNghiep = edtNgheNghiep.getText().toString();
        }

        if (edtTrinhDoNgoaiNgu.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Bạn cần nhập trình độ ngoại ngữ.", Toast.LENGTH_SHORT).show();
        } else {
            trinhDoNgoaiNgu = edtTrinhDoNgoaiNgu.getText().toString();
        }

        gioiTinh = spGioiTinh.getSelectedItem().toString();
        ngaySinh = tvBirthday.getText().toString();

        if (edtDiaChi.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Bạn cần nhập địa chỉ.", Toast.LENGTH_SHORT).show();
        } else {
            diaChi = edtDiaChi.getText().toString();
            diaChi = diaChi + ", " + spProvince.getSelectedItem().toString() + ", " + spDistrict.getSelectedItem().toString();
        }

        if (!ten.equals("") && !mail.equals("") && !sdt.equals("") && !hocVan.equals("") && !ngheNghiep.equals("") && !trinhDoNgoaiNgu.equals("") && !diaChi.equals("")) {

            customProgressBar.show();

            API api = retrofitClient.getClien().create(API.class);
            api.flcUpdateInfo(ten + "",
                    sdt + "",
                    endFlcId + "",
                    ngaySinh + "",
                    avatarUser + "",
                    gioiTinh + "",
                    diaChi + "",
                    hocVan + "",
                    ngheNghiep + "",
                    trinhDoNgoaiNgu + "",
                    accessTokenDb).enqueue(new Callback<Freelancer>() {
                @Override
                public void onResponse(Call<Freelancer> call, Response<Freelancer> response) {

                    if (response.code() == 200) {
                        customProgressBar.dismiss();
                        Toast.makeText(getActivity(), "Cập nhật thông tin thành công.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Freelancer> call, Throwable t) {
                    customProgressBar.dismiss();
                    Toast.makeText(getActivity(), "Sửa thông tin thất bại.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    void createEmpFollowFlc() {
        API api = retrofitClient.getClien().create(API.class);
        api.createEmpFollowFlc("flcId", "empId", "empId", accessTokenDb).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("code create emp follow flc " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("err");
            }
        });
    }

    private File createImageFile(Context context) {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp;
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir     /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        mFileName = image;
        Log.d("TAG", "createImageFile: " + mFileName);
        return image;
    }

    @SuppressLint("RestrictedApi")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile(getContext());

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", photoFile);
                deleteFile(photoFile.getName());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                getActivity().startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                photoFile.deleteOnExit();
            }
        }
    }

    private void getDistrict() {
        customProgressBar.show();

        districts.clear();
        API api = retrofitClient.getClien().create(API.class);
        api.getDistrict().enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                locations = (ArrayList<Location>) response.body();

                if (locations != null) {
                    for (Location cc : locations) {
                        districts.add(cc.getTitle());
                    }

                    ArrayAdapter cc = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, districts);
                    cc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Setting the ArrayAdapter data on the Spinner
                    spDistrict.setAdapter(cc);
                }
                customProgressBar.dismiss();

            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                System.out.println("err");
                customProgressBar.dismiss();
            }
        });
    }

    private void getProvince(int idProvince) {
        customProgressBar.show();
        province.clear();
        API api = retrofitClient.getClien().create(API.class);
        api.getProvince(idProvince).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> strings = response.body();
                if (strings != null) {
                    province.addAll(strings);
                    ArrayAdapter bb = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, province);
                    bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spProvince.setAdapter(bb);
                    if (!loadDataDone){
                        getProfileFlc();
                    }

                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                customProgressBar.dismiss();
                System.out.println("err");
            }
        });
    }
}
