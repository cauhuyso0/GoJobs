package vn.com.gojobs.Employer;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Model.Employer;
import vn.com.gojobs.Model.Location;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.dialog.CaptureDialog;
import vn.com.gojobs.dialog.CustomProgressBar;
import vn.com.gojobs.interfake.API;
import vn.com.gojobs.interfake.OnImageCallback;

import static android.app.Activity.RESULT_OK;
import static com.facebook.internal.instrument.InstrumentUtility.deleteFile;
import static vn.com.gojobs.Employer.LoginEmployerFragment._id;

public class ProfileEmployerFragment extends Fragment implements View.OnClickListener, OnImageCallback {

    private Spinner spQuan, spDistrict;
    String endEmpId;
    Button employerProfileFragment_btnFinish;
    ImageView employerProfileFragment_civProfile;
    EditText employerProfileFragment_edtEmail, employerProfileFragment_edtEmployerName, employerProfileFragment_edtPhone,
            edt_dia_chi_employer, employerProfileFragment_edtTaxCode, employerProfileFragment_edtDescription;
    RetrofitClient retrofitClient = new RetrofitClient();
    Uri selectedImage;
    byte[] array;
    private OnImageCallback onImageCallback;
    CustomProgressBar customProgressBar;
    private static File mFileName;
    private static Bitmap bitmap;
    private static boolean isCapture = false;
    private static boolean isGallery = false;
    private FragmentTransaction fragmentTransaction;
    String accessTokenDb;
    private static Bitmap avatar;

    public final int REQUEST_IMAGE_CAPTURE = 1;
    public final int REQUEST_IMAGE_GALLERY = 2;
    private String avatarUser;
    private boolean isDone = false;
    private boolean isHasAvatar = false;
    final List<String> districts = new ArrayList<>();
    final ArrayList<String> province = new ArrayList<>();

    public ProfileEmployerFragment() {
        this.onImageCallback = this;
        // Required empty public constructor
    }

    public String getFileName() {
        return mFileName.getPath();
    }

    public OnImageCallback setImageCallback() {
        return onImageCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_employer_profile, null);
        customProgressBar = new CustomProgressBar(getActivity());
        mapItem(view);

        if (_id != null) {
            endEmpId = _id;
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        } else {
            endEmpId = AuthActivity.empId;
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }

        getDistrict();
        employerDetail();

        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getProvince(spDistrict.getSelectedItemPosition() + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void mapItem(View view) {
        spDistrict = view.findViewById(R.id.sp_TP);
        spQuan = view.findViewById(R.id.sp_Quan);
        employerProfileFragment_civProfile = view.findViewById(R.id.img_avatar_employer);
        employerProfileFragment_civProfile.setOnClickListener(this);
        employerProfileFragment_btnFinish = view.findViewById(R.id.employerProfileFragment_btnFinish);
        employerProfileFragment_btnFinish.setOnClickListener(this);
        employerProfileFragment_edtEmail = view.findViewById(R.id.employerProfileFragment_edtEmail);
        employerProfileFragment_edtEmployerName = view.findViewById(R.id.employerProfileFragment_edtEmployerName);
        employerProfileFragment_edtPhone = view.findViewById(R.id.employerProfileFragment_edtPhone);
        edt_dia_chi_employer = view.findViewById(R.id.edt_dia_chi_employer);
        employerProfileFragment_edtTaxCode = view.findViewById(R.id.employerProfileFragment_edtTaxCode);
        employerProfileFragment_edtDescription = view.findViewById(R.id.employerProfileFragment_edtDescription);
    }

    private void employerDetail() {
        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);
        api.getProfileEmployer(endEmpId, accessTokenDb).enqueue(new Callback<Employer>() {
            @Override
            public void onResponse(Call<Employer> call, Response<Employer> response) {
                Employer employer = response.body();

                if (employer != null) {

                    String avatar = employer.getEmpLogo();
                    avatarUser = avatar;
                    if (avatar != null && !avatar.equals("")) {
                        isHasAvatar = true;
                        // parse base64 to bitmap (param : avataruser:  Result : String base 64 to bitmap)
                        byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        employerProfileFragment_civProfile.setImageBitmap(decodedByte);
                    } else {
                        employerProfileFragment_civProfile.setImageDrawable(getActivity().getDrawable(R.drawable.avatar_logo));
                    }

                    employerProfileFragment_edtEmail.setText(employer.getEmpEmail());
                    employerProfileFragment_edtEmployerName.setText(employer.getEmpName());
                    employerProfileFragment_edtPhone.setText(employer.getEmpPhone());
                    edt_dia_chi_employer.setText(employer.getEmpAddress());

                    if (employer.getEmpAddress().contains(",")) {
                        edt_dia_chi_employer.setText(employer.getEmpAddress().substring(0, employer.getEmpAddress().indexOf(",")));
                    } else {
                        edt_dia_chi_employer.setText(employer.getEmpAddress());
                    }

                    String quan = employer.getEmpAddress().substring(employer.getEmpAddress().indexOf(",") + 2,employer.getEmpAddress().indexOf(",",employer.getEmpAddress().indexOf(",") + 1)).trim();
                    String tinh = employer.getEmpAddress().substring(employer.getEmpAddress().indexOf(",",employer.getEmpAddress().indexOf(",") + 1) + 2).trim();

                    for (int i = 0; i < districts.size(); i++){
                        if (districts.get(i).equals(tinh)){
                            spDistrict.setSelection(i);
                        }
                    }

                    for (int i = 0; i < province.size(); i++){
                        if (province.get(i).equals(quan)){
                            spQuan.setSelection(i);
                        }
                    }

                    employerProfileFragment_edtTaxCode.setText(employer.getEmpTaxCode());
                    employerProfileFragment_edtDescription.setText(employer.getEmpDescription());
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<Employer> call, Throwable t) {
                customProgressBar.dismiss();
            }
        });
    }

    void updateInfoEmp() {

        customProgressBar.show();
        API api = retrofitClient.getClien().create(API.class);

        String empName = employerProfileFragment_edtEmployerName.getText().toString();
        String empsdt = employerProfileFragment_edtPhone.getText().toString();
        String empMota = employerProfileFragment_edtDescription.getText().toString();
        String empTaxCode = employerProfileFragment_edtTaxCode.getText().toString();
        String empDes = employerProfileFragment_edtDescription.getText().toString();

        api.updatedEmployerInfo(avatarUser + "",
                employerProfileFragment_edtEmployerName.getText().toString(),
                employerProfileFragment_edtPhone.getText().toString(),
                edt_dia_chi_employer.getText().toString() + "",
                employerProfileFragment_edtDescription.getText().toString(),
                employerProfileFragment_edtTaxCode.getText().toString(),
                endEmpId,
                accessTokenDb).enqueue(new Callback<Employer>() {
            @Override
            public void onResponse(Call<Employer> call, Response<Employer> response) {

                if (response.code() == 200) {
                    customProgressBar.dismiss();
                    Toast.makeText(getActivity(), "Cập nhật thông tin thành công.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Employer> call, Throwable t) {

                Toast.makeText(getActivity(), "Cập nhật thất bại.", Toast.LENGTH_SHORT).show();
                customProgressBar.dismiss();
            }
        });

    }

    private void getProvince(int ID) {
        customProgressBar.show();
        province.clear();
        API api = retrofitClient.getClien().create(API.class);
        api.getProvince(ID).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> strings = response.body();
                if (strings != null) {
                    province.addAll(strings);
                    ArrayAdapter bb = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, province);
                    bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Setting the ArrayAdapter data on the Spinner
                    spQuan.setAdapter(bb);
                }
                customProgressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                System.out.println("err");
                customProgressBar.dismiss();
            }
        });

    }

    private void getDistrict() {
        customProgressBar.show();
        districts.clear();
        API api = retrofitClient.getClien().create(API.class);
        api.getDistrict().enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                List<Location> locations1 = response.body();
                if (locations1 != null) {
                    for (int i = 0; i < locations1.size(); i++) {
                        Location location = locations1.get(i);
                        districts.add(location.getTitle());
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
                customProgressBar.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isGallery) {
            employerProfileFragment_civProfile.setImageBitmap(bitmap);
            avatar = bitmap;
            isGallery = false;
            bitmap = null;
        } else if (isCapture) {
            employerProfileFragment_civProfile.setImageBitmap(bitmap);
            avatar = bitmap;
            isCapture = false;
            bitmap = null;
        }
        new MyThread().start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.employerProfileFragment_btnFinish:

                if (isDone || isHasAvatar) {
                    updateInfoEmp();
                    employerProfileFragment_btnFinish.setEnabled(false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            employerProfileFragment_btnFinish.setEnabled(true);
                        }
                    }, 3000);
                    break;
                } else {
                    Toast.makeText(getActivity(), "Vui lòng đợi trong giây lát.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.img_avatar_employer:

                AuthActivity.takeFrom = "employer";
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                CaptureDialog captureDialog = new CaptureDialog(this);
                captureDialog.show(fragmentTransaction, CaptureDialog.TAG);
                break;
        }
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public void takePicture() {
        dispatchTakePictureIntent();
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

    @Override
    public void takeGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
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

    @Override
    public void onImageCallback(int requestCode, int resultCode, @Nullable Intent data) {
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
            employerProfileFragment_civProfile.setImageDrawable(getActivity().getDrawable(R.drawable.avatar_logo));
        }
    }
}
