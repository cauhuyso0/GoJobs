package vn.com.gojobs.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.gojobs.AuthActivity;
import vn.com.gojobs.Employer.LoginEmployerFragment;
import vn.com.gojobs.Model.Location;
import vn.com.gojobs.R;
import vn.com.gojobs.RetrofitClient;
import vn.com.gojobs.interfake.API;

import static vn.com.gojobs.Employer.LoginEmployerFragment._id;

public class AddJobFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "AddJobFragment";
    private final String DATE_FORMAT = "dd-MM-yyyy";
    RetrofitClient retrofitClient = new RetrofitClient();
    FragmentManager fragmentManager;
    private Button btnAddJob;
    private String endEmpId;
    private EditText edtTitleJob, edtDescriberJob, edtLuong, edtThoiHan, edtSoLuongUngVien, edtDiaChi;
    private TextView tvNgayBatDau, tvNgayKetThuc, tvTongLuong;
    private CheckBox cbCo, cbKhong;
    final Calendar newCalendar = Calendar.getInstance();

    private Spinner spLoaiCV, spNganhNghe, spQuan, spTP;

    private String[] loaiCVs = {"Theo giờ", "Theo ngày"};
    private String[] nganhNghes = {"Kĩ sư phần mền", "Thiết kế đồ họa", "Lao động phổ thông", "Phụ bếp, nhà hàng - khách sạn",
            "Hướng dẫn viên du lịch ", "Tiếp thị", "Nhân viên BĐS",
            "Giúp việc", "Giữ trẻ", "Chăm sóc thú cưng", "Dạy kèm", "Phụ quán"};
    private ArrayList<String> districts = new ArrayList<>();
    private ArrayList<String> province = new ArrayList<>();
    private int tongLuong = 0;
    private String tenCV = "", motaCV = "", loaiCV = "", nganhNghe = "", diaChi = "";
    private int luong, thoiHan, soLuongUngVien;
    private boolean yeuCauKN;
    private ArrayList<Location> locations;
    String accessTokenDb;

    public AddJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_job, container, false);

        mapItem(view);

        setOnClick();

        if (_id != null) {
            endEmpId = _id;
            accessTokenDb = LoginEmployerFragment.accessTokenDb;
        } else {
            endEmpId = AuthActivity.empId;
            accessTokenDb = AuthActivity.accessTokenDbEmp;
        }

        //dummy data
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, loaiCVs);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spLoaiCV.setAdapter(aa);

        ArrayAdapter dd = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, nganhNghes);
        dd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spNganhNghe.setAdapter(dd);


        // call api set data for district
        getCity();

        spTP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tinh = spTP.getSelectedItem().toString();
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

    private void mapItem(View view) {
        edtTitleJob = view.findViewById(R.id.edt_job_title);
        edtDescriberJob = view.findViewById(R.id.edt_mo_ta_cong_viec);
        spLoaiCV = view.findViewById(R.id.spinner_loai_cong_viec);
        edtLuong = view.findViewById(R.id.edt_luong);
        cbCo = view.findViewById(R.id.cb_co);
        cbKhong = view.findViewById(R.id.cb_khong);
        spNganhNghe = view.findViewById(R.id.spn_nganh_nghe);
        edtThoiHan = view.findViewById(R.id.edt_thoi_han);
        tvNgayBatDau = view.findViewById(R.id.tv_ngay_bat_dau);
        tvNgayKetThuc = view.findViewById(R.id.tv_ngay_ket_thuc);
        tvTongLuong = view.findViewById(R.id.tv_tong_luong);
        edtSoLuongUngVien = view.findViewById(R.id.edt_so_luong_ung_vien);
        edtDiaChi = view.findViewById(R.id.edt_dia_chi_job);
        spQuan = view.findViewById(R.id.sp_Quan);
        spTP = view.findViewById(R.id.sp_TP);
        btnAddJob = view.findViewById(R.id.btn_submit_add_job);

        edtLuong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("") || edtThoiHan.getText().toString().equals("")) {
                    tvTongLuong.setText("0");
                } else {
                    String result = (Integer.parseInt(editable.toString()) * Integer.parseInt(edtThoiHan.getText().toString())) + "";
                    tvTongLuong.setText(result);
                }
            }
        });

        edtThoiHan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("") || edtLuong.getText().toString().equals("")) {
                    tvTongLuong.setText("0");
                } else {
                    String result = (Integer.parseInt(editable.toString()) * Integer.parseInt(edtLuong.getText().toString())) + "";
                    tvTongLuong.setText(result);
                }
            }
        });

        cbCo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbKhong.setChecked(false);
                } else {
                    cbKhong.setChecked(true);
                }
            }
        });

        cbKhong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbCo.setChecked(false);
                } else {
                    cbCo.setChecked(true);
                }
            }
        });

        edtSoLuongUngVien.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setOnClick() {
        tvNgayBatDau.setOnClickListener(this);
        tvNgayKetThuc.setOnClickListener(this);
        btnAddJob.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ngay_bat_dau:

                final DatePickerDialog StartTime = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                        dateFormat.setTimeZone(TimeZone.getTimeZone("EN"));
                        String date = dateFormat.format(newDate.getTime());
                        tvNgayBatDau.setText(date);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                StartTime.show();
                break;

            case R.id.tv_ngay_ket_thuc:
                final DatePickerDialog EndTime = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                        dateFormat.setTimeZone(TimeZone.getTimeZone("EN"));
                        String date = dateFormat.format(newDate.getTime());
                        tvNgayKetThuc.setText(date);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                EndTime.show();
                break;
            case R.id.btn_submit_add_job:
                addJob();
                break;
        }
    }

    void addJob() {
        if (edtTitleJob.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Bạn cần nhập tên CV", Toast.LENGTH_SHORT).show();
        } else {
            tenCV = edtTitleJob.getText().toString();
        }

        if (edtDescriberJob.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Bạn cần nhập mô tả CV", Toast.LENGTH_SHORT).show();
        } else {
            motaCV = edtDescriberJob.getText().toString();
        }

        loaiCV = spLoaiCV.getSelectedItem().toString();

        luong = 0;
        if (Integer.parseInt(edtLuong.getText().toString()) == 0 || edtLuong.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Bạn cần nhập lương CV", Toast.LENGTH_SHORT).show();
        } else {
            luong = Integer.parseInt(edtLuong.getText().toString());
        }

        yeuCauKN = false;
        if (cbCo.isChecked()) {
            yeuCauKN = true;
        } else {
            yeuCauKN = false;
        }

        nganhNghe = spNganhNghe.getSelectedItem().toString();

        thoiHan = 0;
        if (Integer.parseInt(edtThoiHan.getText().toString()) == 0 || edtThoiHan.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Bạn cần nhập thời hạn CV", Toast.LENGTH_SHORT).show();
        } else {
            thoiHan = Integer.parseInt(edtThoiHan.getText().toString());
        }

        String ngayBatDau = tvNgayBatDau.getText().toString();
        String ngayKetThuc = tvNgayKetThuc.getText().toString();

        if (luong != 0 && thoiHan != 0) {
            tongLuong = luong * thoiHan;
        }

        if (edtSoLuongUngVien.getText().toString().equals("")) {
            soLuongUngVien = 1;
        } else {
            soLuongUngVien = Integer.parseInt(edtSoLuongUngVien.getText().toString());
        }

        if (edtDiaChi.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Bạn cần nhập địa chỉ CV", Toast.LENGTH_SHORT).show();
        } else {
            diaChi = edtDiaChi.getText().toString();
            String quan = spQuan.getSelectedItem().toString();
            String tinh = spTP.getSelectedItem().toString();
            diaChi = diaChi + " " + quan + " " + tinh;
        }

        if (!tenCV.equals("") && !motaCV.equals("") && luong != 0 && thoiHan != 0 && !diaChi.equals("")) {
            API api = retrofitClient.getClien().create(API.class);
            api.createNewJob(endEmpId,
                    tenCV,
                    motaCV,
                    luong,
                    loaiCV,
                    yeuCauKN,
                    nganhNghe,
                    ngayBatDau,
                    ngayKetThuc,
                    thoiHan,
                    "Open",
                    tongLuong,
                    soLuongUngVien,
                    diaChi, accessTokenDb).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getActivity(), "Tạo công việc mới thành công", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), "Tạo công việc mới thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void getCity() {
        API api = retrofitClient.getClien().create(API.class);
        api.getDistrict().enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                locations = (ArrayList<Location>) response.body();

                for (Location cc : locations) {
                    province.add(cc.getTitle());
                }

                ArrayAdapter cc = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, province);
                cc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spTP.setAdapter(cc);

            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                System.out.println("err");
            }
        });
    }

    private void getProvince(int idProvince) {
        districts.clear();
        API api = retrofitClient.getClien().create(API.class);
        api.getProvince(idProvince).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> strings = response.body();
                districts.addAll(strings);
                ArrayAdapter bb = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, districts);
                bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spQuan.setAdapter(bb);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                System.out.println("err");
            }
        });
    }

}