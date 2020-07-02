package com.example.zn.emotion_awareness2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap map;
    private static int LOCATION_PERMISSION_REQUEST_CODE=1001;
    private static boolean rLocationGranted=false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_location,container,false);
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        if(checkService()==true){
            initiaMap();
            if(rLocationGranted==true){
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }
        }

        return view;
    }

    private void initiaMap() {
        Context c=getContext();
        String [] permissions={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
        if((ContextCompat.checkSelfPermission(c.getApplicationContext(),permissions[0]) == PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(c.getApplicationContext(),permissions[1]) == PackageManager.PERMISSION_GRANTED)) {

            rLocationGranted=true;
        }
        else
        {
            ActivityCompat.requestPermissions((Activity) c,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    private void getDeviceLocation(){
        Context c=getContext();
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(c);
        try{
            if(rLocationGranted==true){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location mlocation=(Location) task.getResult();
                            LatLng sydney = new LatLng(22.905800,120.272714);
//                            Log.i("mlocation", String.valueOf(mlocation.getLatitude()));
//                            Log.i("mlocation", String.valueOf(mlocation.getLongitude()));
                            map.addMarker(new MarkerOptions().position(sydney).title("目前位置"));
                            map.setMinZoomPreference(6.0f);
                            map.setMaxZoomPreference(14.0f);
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mlocation.getLatitude(),mlocation.getLongitude())));
                            markother();
                        }
                    }
                });
            }
        }
        catch (SecurityException ex){
            Log.i("LocationError",ex.getMessage());
        }
    }

    private void markother() {

        map.addMarker(new MarkerOptions().position(new LatLng(22.9730362,120.1886245)).title("康舟診所").snippet("電話:null").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.1280375,121.7488527)).title("基隆心身心精神科診所").snippet("電話:02-24283399").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.1304578,121.7449673)).title("詠欣精神科診所").snippet("電話:02-24272247").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0960995,121.711531)).title("佑仁聯合診所").snippet("電話:02-24562212").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0449301,121.543867)).title("林青穀家庭醫學專科診所").snippet("電話:02-27217055").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0250736,121.5502358)).title("敦南心診所").snippet("電話:02-27331995").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0274887,121.5410473)).title("光慧診所").snippet("電話:02-27555627").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0396008,121.5413573)).title("杏語心靈診所").snippet("電話:02-77121526").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0251862,121.5351226)).title("書田泌尿科眼科診所").snippet("電話:02-23690211").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.8455423,119.8125874)).title("欣美診所").snippet("電話:02-23253218").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0332499,121.5411171)).title("昱捷診所").snippet("電話:02-27002709").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0329079,121.54116)).title("宇寧身心診所").snippet("電話:02-27080706").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0421356,121.5522668)).title("晴天身心精神科診所").snippet("電話:02-87716545").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0183267,121.5298315)).title("主愛心靈診所").snippet("電話:02-23651224").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0334418,121.5285683)).title("馨思身心精神科診所").snippet("電話:02-33933030").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.2764983,120.2323837)).title("大心診所").snippet("電話:02-27718821").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0312265,121.5461852)).title("春暘診所").snippet("電話:02-27019586").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0305226,121.532333)).title("大安身心診所").snippet("電話:02-23658680").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0574462,121.520393)).title("澄心診所").snippet("電話:02-25230480").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0526402,121.5191832)).title("黃雅芬兒童心智診所").snippet("電話:02-25675690").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0538618,121.5212322)).title("微煦心靈診所").snippet("電話:02-25312626").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0497781,121.5208202)).title("黃偉俐診所").snippet("電話:02-25114123").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0518607,121.5315151)).title("鈺璽診所").snippet("電話:02-25095569").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0513638,121.5416203)).title("心禾診所").snippet("電話:02-27506122").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0587934,121.5416526)).title("振芝心身醫學診所").snippet("電話:02-55963919").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.1104236,121.5235069)).title("天晴診所").snippet("電話:02-28355329").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.1040983,121.5189962)).title("天母康健身心診所").snippet("電話:02-28378787").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0806775,121.5086095)).title("社子安心診所").snippet("電話:02-28136622").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.1287586,121.4990764)).title("奇岩身心診所").snippet("電話:02-28919008").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.1135008,121.5088553)).title("石牌鄭身心醫學診所").snippet("電話:02-28227660").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0468068,121.5512153)).title("邱楠超診所").snippet("電話:02-25702296").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.05652,121.5616147)).title("李政洋身心診所").snippet("電話:02-27620086").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0510743,121.566078)).title("双悅診所").snippet("電話:02-27609122").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9703403,120.2591529)).title("泓仁診所").snippet("電話:02-29382627").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0016408,121.5527303)).title("木柵身心診所").snippet("電話:02-29301550").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0351241,121.496679)).title("萬華身心精神科診所").snippet("電話:02-23088878").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0247156,121.5075544)).title("福全身心科診所").snippet("電話:02-23327712").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.040895,121.5764499)).title("佑泉診所").snippet("電話:02-27595512").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0377513,121.5727933)).title("松德精神科診所").snippet("電話:02-87894477").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.036283,121.5556047)).title("伯特利身心診所").snippet("電話:02-27200938").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0272392,121.5639403)).title("信義身心精神科診所").snippet("電話:02-27580988").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0342361,121.5588204)).title("醫者診所").snippet("電話:02-27576888").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0795988,121.5878403)).title("夏一新精神科診所").snippet("電話:02-87928213").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0692616,121.5905619)).title("曜暘診所").snippet("電話:02-87925925").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.068682,121.6104324)).title("林威廷身心精神科診所").snippet("電話:02-26308128").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0566488,121.5152403)).title("夏凱納生活診所").snippet("電話:02-25558313").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0188808,121.5276003)).title("平安身心精神科診所").snippet("電話:02-23680055").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9139955,120.2015139)).title("永康身心診所").snippet("電話:02-23581818").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0283447,121.5173294)).title("蘭心診所").snippet("電話:02-23515995").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0429958,121.5088373)).title("遠東聯合診所").snippet("電話:02-23111525").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0238667,121.4665102)).title("楊思亮診所").snippet("電話:02-82528779").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0185041,121.4624764)).title("楊孟達身心精神科診所").snippet("電話:02-22555222").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0288506,121.4702947)).title("家樺聯合診所").snippet("電話:02-82523000").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0020688,121.4595863)).title("放開心身心精神科診所").snippet("電話:02-29557768").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0056528,121.4619263)).title("幸福身心精神科診所").snippet("電話:02-29578000").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0190148,121.4607153)).title("樂為診所").snippet("電話:02-22518068").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.5450392,120.447184)).title("順心診所").snippet("電話:02-89520669").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9940958,120.2291415)).title("喜樂診所").snippet("電話:02-29511737").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0633138,121.6545483)).title("謝宏杰身心精神科診所").snippet("電話:02-86916588").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9139279,120.236535)).title("合康診所").snippet("電話:02-86425222").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9752871,121.5452976)).title("楊聰才診所").snippet("電話:02-29181299").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0171722,121.3360999)).title("心晴診所").snippet("電話:02-89250802").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0102975,121.5127128)).title("明心診所").snippet("電話:02-29286111").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0091528,121.5069503)).title("永平身心診所").snippet("電話:02-29205660").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0058589,121.5150407)).title("中永和身心精神科診所").snippet("電話:02-29291555").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9923658,121.5094173)).title("樂活精神科診所").snippet("電話:02-29463655").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0032893,121.5104356)).title("寧靜海診所").snippet("電話:02-29212321").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0033328,121.5096014)).title("福和身心診所").snippet("電話:02-29291868").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9869639,121.4521555)).title("悠活精神科診所").snippet("電話:02-22748877").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9139955,120.2015139)).title("永康身心診所").snippet("電話:02-22730199").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9864818,121.4626473)).title("合康身心診所").snippet("電話:02-22643905").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9561528,121.3539133)).title("養心診所").snippet("電話:02-26775767").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.953085,121.3488339)).title("欣慈診所").snippet("電話:02-26775601").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0675222,121.493954)).title("美麗心成人兒童精神科診所").snippet("電話:02-29886773").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0703234,121.4926546)).title("陳信任精神科診所").snippet("電話:02-29871868").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0690228,121.4890372)).title("林南身心科診所").snippet("電話:02-29809803").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0597542,121.4894631)).title("安興精神科診所").snippet("電話:02-29743029").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0379219,121.4449976)).title("育心身心精神科診所").snippet("電話:02-22779773").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0324197,121.4324093)).title("晴美身心診所").snippet("電話:02-29081775").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0438335,121.4516841)).title("好心情身心精神科診所").snippet("電話:02-29965868").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.5881369,120.3681398)).title("大順醫院").snippet("電話:02-22797229").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0847268,121.4673232)).title("壬康精神科診所").snippet("電話:02-82825885").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0813688,121.4730713)).title("長建身心診所").snippet("電話:02-28471313").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.5353076,120.5565078)).title("福田診所").snippet("電話:02-82863287").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0566488,121.5152403)).title("開心生活診所").snippet("電話:02-82823531").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9674051,120.2255693)).title("喜悅診所").snippet("電話:02-26820039").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.1794102,121.4487498)).title("陳灼彭身心醫學科診所").snippet("電話:02-26298996").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.1719341,121.4436023)).title("心悅身心診所").snippet("電話:02-26225003").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9326784,121.3716195)).title("王湘琦身心診所").snippet("電話:02-26710427").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0715418,121.3664923)).title("欣泉身心診所").snippet("電話:02-26006182").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0754747,121.3676338)).title("龍霖身心診所").snippet("電話:02-26069556").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9875249,121.2809591)).title("心寧診所").snippet("電話:03-3920522").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.6050486,120.5427872)).title("全新診所").snippet("電話:03-3586944").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9139955,120.2015139)).title("安心診所").snippet("電話:03-3162020").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9985983,121.3056479)).title("詠美身心診所").snippet("電話:03-3363833").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9870729,121.3155745)).title("吳俊毅身心精神科診所").snippet("電話:03-3629995").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0110781,121.2951746)).title("悅情身心診所").snippet("電話:03-3411220").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0202653,121.2930021)).title("一德身心診所").snippet("電話:03-3583115").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9801748,121.2675683)).title("周孫元診所").snippet("電話:0800-226260").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0171722,121.3360999)).title("心晴診所").snippet("電話:03-3467895").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0060931,121.3143598)).title("聖洸身心診所").snippet("電話:03-3380657").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9554624,121.2252923)).title("迎旭診所").snippet("電話:03-4277126").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9645488,121.2216043)).title("陳炯旭診所").snippet("電話:03-2805285").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.5450392,120.447184)).title("順心診所").snippet("電話:03-4519958").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9741276,121.2539819)).title("尚語身心診所").snippet("電話:03-4512181").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9542676,121.2352671)).title("柏樂診所").snippet("電話:03-4560208").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9532176,121.2265933)).title("博士身心醫學診所").snippet("電話:03-2842824").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.045565,121.4558767)).title("自立診所").snippet("電話:03-2853377").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9591358,121.2974753)).title("八德身心診所").snippet("電話:03-3775953").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9609781,121.2976305)).title("雷亞診所").snippet("電話:03-3636956").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9821349,120.1930153)).title("欣悅診所").snippet("電話:03-3520876").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0489868,121.2887892)).title("晨新聯合診所").snippet("電話:03-3222232").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.8618538,121.2153219)).title("楊延壽診所").snippet("電話:03-4899242").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.9455618,121.2035153)).title("黃正龍診所").snippet("電話:03-4936667").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.910379,121.1546572)).title("新楊梅診所").snippet("電話:03-4856530").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0187429,121.4077352)).title("蘇宗偉身心診所").snippet("電話:02-82003001").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.058447,121.3622556)).title("清心身心診所").snippet("電話:03-3187887").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.8165492,121.0273801)).title("六竹診所").snippet("電話:03-5505550").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.8249039,121.0221313)).title("安立身心診所").snippet("電話:03-6577622").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.8324814,121.0086765)).title("台齡身心診所").snippet("電話:03-5586857").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.7226512,121.0956188)).title("陽光精神科診所").snippet("電話:03-5942371").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.1495678,119.5008434)).title("瑞安診所").snippet("電話:03-5956630").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.8089883,120.9728017)).title("能清安欣診所").snippet("電話:03-5357600").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.8020076,120.9864647)).title("林正修診所").snippet("電話:03-5166746").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.7876899,121.0089658)).title("馬大元診所").snippet("電話:03-5794698").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.8131452,120.9666803)).title("平衡身心診所").snippet("電話:03-5320622").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.7317166,119.9577946)).title("益康診所").snippet("電話:037-378566").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.4398088,120.6539271)).title("天慈身心診所").snippet("電話:037-865025").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.6881341,120.9082935)).title("承美身心科診所").snippet("電話:037-683748").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1426747,120.6915746)).title("明功堂精神科診所").snippet("電話:04-22120083").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1493678,120.6597402)).title("游文治精神科診所").snippet("電話:04-23029995").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1338345,120.6596694)).title("抒情診所").snippet("電話:04-23723152").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1468009,120.6543393)).title("劉昭賢精神科診所").snippet("電話:04-23223123").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1400049,120.6532283)).title("展新診所").snippet("電話:04-23780900").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9821349,120.1930153)).title("欣悅診所").snippet("電話:04-22659507").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1608765,120.6920269)).title("劉騰光心身診所").snippet("電話:04-22371903").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1551516,120.6794344)).title("黃淑琦心身診所").snippet("電話:04-22083603").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1720859,120.6693463)).title("詹益忠身心醫學診所").snippet("電話:04-22925858").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1405133,120.6751835)).title("詹東霖心身診所").snippet("電話:04-22256362").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1667214,120.6418565)).title("開心房身心診所").snippet("電話:04-27003342").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1719948,120.6609352)).title("呂健弘精神科診所").snippet("電話:04-23115249").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1695982,120.6574681)).title("全新生活診所").snippet("電話:04-23156988").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1807669,120.6185693)).title("卓大夫診所").snippet("電話:04-24613097").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1862224,120.6076588)).title("光流聯合診所").snippet("電話:04-24608480").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1718388,120.6416882)).title("昕情診所").snippet("電話:04-27085855").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1908169,120.6117423)).title("忘憂森林身心診所").snippet("電話:04-24630102").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.149956,120.6341275)).title("明燈心身診所").snippet("電話:04-22516670").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1529063,120.6473983)).title("康誠心身醫學診所").snippet("電話:04-23103968").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1478346,120.6401208)).title("心身美身心醫學診所").snippet("電話:04-23100050").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1415451,120.6360851)).title("趙玉良身心醫學診所").snippet("電話:04-23823966").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1489906,120.6427378)).title("圓情居身心靈診所").snippet("電話:04-22591060").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1525383,120.6347087)).title("本堂診所").snippet("電話:04-22550221").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.6682052,119.9091436)).title("佳佑診所").snippet("電話:04-22474309").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1681948,120.6938203)).title("尚義診所").snippet("電話:04-22355155").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1827178,120.6906343)).title("元亨診所").snippet("電話:04-22413959").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1760059,120.7175191)).title("王家駿身心科診所").snippet("電話:04-22393158").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1104348,120.6822513)).title("王志中診所").snippet("電話:04-24823855").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1154119,120.6857111)).title("鄭曜忠身心診所").snippet("電話:04-24854547").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.2517965,120.7146865)).title("淇祥診所").snippet("電話:04-25289595").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.250491,120.7087066)).title("趙世淋診所").snippet("電話:04-25207017").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.2537206,120.7184832)).title("享開心身心診所").snippet("電話:04-25225361").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0421356,121.5522668)).title("好晴天身心診所").snippet("電話:04-25339907").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1497536,120.7138802)).title("湯元皓診所").snippet("電話:04-23939203").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.2462543,120.5601729)).title("新活力診所").snippet("電話:04-26622395").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.2425369,120.5588463)).title("林令世診所").snippet("電話:04-26652588").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9139279,120.236535)).title("望晴診所").snippet("電話:04-25585284").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1667214,120.6418565)).title("挺開朗身心診所").snippet("電話:04-23396090").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.0830038,120.5411783)).title("吳潮聰精神科診所").snippet("電話:04-7239121").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.0889537,120.5322081)).title("修慧診所").snippet("電話:04-7266158").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.0725775,120.5406775)).title("頤晴診所").snippet("電話:04-7288333").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.0567598,120.4331941)).title("溫建文診所").snippet("電話:04-8990706").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.0265439,120.5392023)).title("沈祿從診所").snippet("電話:04-7868238").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6308539,120.3219833)).title("高杏診所").snippet("電話:04-8347798").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.9656394,120.5747775)).title("存寬診所").snippet("電話:04-8366800").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.901921,120.5349378)).title("李光耀診所").snippet("電話:04-8221899").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.8739469,120.5195993)).title("陳建達診所").snippet("電話:04-8877228").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.1145603,120.4910023)).title("和美身心醫學診所").snippet("電話:04-7577566").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.9709454,120.974683)).title("惠承診所").snippet("電話:049-2420453").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.9095701,120.6875213)).title("惠良診所").snippet("電話:049-2208296").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.7561622,120.6892321)).title("惠元診所").snippet("電話:049-2651932").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.9809298,120.6837862)).title("協安診所").snippet("電話:049-2314945").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.7084936,120.5452289)).title("林文博聯合診所").snippet("電話:05-5330126").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.7040147,120.5316528)).title("何正岳身心診所").snippet("電話:05-5328680").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.7122136,120.4344322)).title("趙夢麒診所").snippet("電話:05-6326055").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.7141084,120.4386902)).title("廖寶全診所").snippet("電話:05-6322584").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.7010929,120.5269103)).title("雲萱診所").snippet("電話:05-5371689").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.7096419,120.5451983)).title("晴明診所").snippet("電話:05-5330567").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.4683119,120.4411743)).title("吳南逸診所").snippet("電話:05-2232388").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.4756028,120.4426745)).title("真善渼診所").snippet("電話:05-2230505").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.4806869,120.4518243)).title("周裕軒身心醫學診所").snippet("電話:05-2229823").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.480876,120.4587592)).title("蕭正誠診所").snippet("電話:05-2778187").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.4793335,120.4421665)).title("知心連冀身心醫學科診所").snippet("電話:05-2286808").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.5565605,120.4247962)).title("徐鴻傑身心診所").snippet("電話:05-2265969").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9895038,120.2087163)).title("翁桂芳精神科診所").snippet("電話:06-2226700").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.0017891,120.1851783)).title("晟欣診所").snippet("電話:06-2585766").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9847709,120.2225558)).title("蕭文勝診所").snippet("電話:06-2755088").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9139955,120.2015139)).title("活泉診所").snippet("電話:06-2681600").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9745701,120.2267803)).title("王盈彬精神科診所").snippet("電話:06-2600661").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9928609,120.2281033)).title("心樂活診所").snippet("電話:06-2383636").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9864672,120.2209479)).title("以恩診所").snippet("電話:06-2083653").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9818332,120.2231598)).title("蔡明輝診所").snippet("電話:06-3369595").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.989682,120.217671)).title("春暉精神科診所").snippet("電話:06-2373888").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9821349,120.1930153)).title("欣悅診所").snippet("電話:06-2283065").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9873928,120.1951929)).title("林春銘神經精神科內科診所").snippet("電話:06-2266731").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.0051988,120.2060739)).title("殷建智精神科診所").snippet("電話:06-2810008").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.0065739,120.2195513)).title("林日光診所").snippet("電話:06-2376506").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.0060129,120.2195953)).title("羅信宜精神科診所").snippet("電話:06-2002268").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.0132568,120.2044704)).title("許森彥精神科診所").snippet("電話:06-2513283").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.0019464,120.1941616)).title("心悠活診所").snippet("電話:06-2236766").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9139955,120.2015139)).title("張靖平神經精神科內科診所").snippet("電話:06-2515998").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9141076,120.1314675)).title("第一聯合診所").snippet("電話:06-2350116").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.0070663,119.7617528)).title("安立診所").snippet("電話:06-3564816").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.0682488,120.2146362)).title("吳吉得診所").snippet("電話:06-3563342").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.0394846,120.1858101)).title("安大身心精神科診所").snippet("電話:06-2568886").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.013967,120.2701793)).title("仁享診所").snippet("電話:06-2052922").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.005678,120.2329343)).title("心寬診所").snippet("電話:06-3131212").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.0119211,120.224661)).title("林俞仲身心精神科診所").snippet("電話:06-3036006").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.0363321,120.2569337)).title("明澤欣心診所").snippet("電話:06-2012866").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.0304648,120.2575792)).title("民麗骨科診所").snippet("電話:06-2011032").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9986644,120.2370674)).title("心永康身心精神科診所").snippet("電話:06-3112000").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.0208399,120.2344164)).title("東橋身心診所").snippet("電話:06-3027772").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.0957027,119.7917025)).title("晴光診所").snippet("電話:06-5856629").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.1821209,120.2494573)).title("安芯診所").snippet("電話:06-5727629").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9664218,120.2857337)).title("心田診所").snippet("電話:06-3306768").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.3073616,120.314461)).title("陳俊升精神科診所").snippet("電話:06-6335025").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.3058838,120.3175512)).title("林晏弘診所").snippet("電話:06-6336500").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.1631962,120.1756676)).title("明如身心診所").snippet("電話:06-7225656").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.3465381,120.4111933)).title("白河林眼科診所").snippet("電話:06-6831116").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.035761,120.3007544)).title("陳相國聯合診所").snippet("電話:06-5908878").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6297149,120.2993054)).title("高安診所").snippet("電話:07-2727662").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6320026,120.3124405)).title("謝前亮診所").snippet("電話:07-2259763").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.8319228,119.7960936)).title("信恩診所").snippet("電話:07-2384119").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6306645,120.3134367)).title("青欣診所").snippet("電話:07-2254800").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6252069,120.3045923)).title("榮欣身心診所").snippet("電話:07-2150123").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6339624,120.3116441)).title("柯偉恭診所").snippet("電話:07-2232273").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6244393,120.3220856)).title("楊寬弘診所").snippet("電話:07-7240088").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6174318,120.3047303)).title("家慈診所").snippet("電話:07-3333375").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6319409,120.3270493)).title("大和診所").snippet("電話:07-2295666").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6205718,120.3297543)).title("童春診所").snippet("電話:07-7210983").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6240187,120.3224093)).title("展穎診所暨彼得潘自然醫學中心").snippet("電話:07-7130822").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6366385,120.3188955)).title("楊明仁診所").snippet("電話:07-2232223").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.4856996,119.354364)).title("琉璃光診所").snippet("電話:07-7160887").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6214799,120.3296253)).title("徐獨立診所").snippet("電話:07-7133284").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6346869,120.3240083)).title("靜安診所").snippet("電話:07-2239025").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6201849,120.3146803)).title("雲上太陽心寧診所").snippet("電話:07-3389160").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6149412,120.3003513)).title("陳三能診所").snippet("電話:07-3359133").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6289946,120.3159786)).title("文化身心診所").snippet("電話:07-2255123").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6228653,120.3304206)).title("佳璋診所").snippet("電話:07-7212090").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6229801,120.3181972)).title("為人診所").snippet("電話:07-7272989").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.8203143,120.1034899)).title("元和雅診所").snippet("電話:07-5550056").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9142396,119.9913573)).title("樂群診所").snippet("電話:07-5555596").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6116687,120.3152513)).title("大福診所").snippet("電話:07-3317601").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6030962,120.3249877)).title("喜洋洋心靈診所").snippet("電話:07-7272888").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6413169,120.3282113)).title("正中診所").snippet("電話:07-3809900").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6389591,120.3426398)).title("覺民診所").snippet("電話:07-3980107").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6396607,120.3392473)).title("拉法診所").snippet("電話:07-3854591").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6414748,120.3064463)).title("茂德診所").snippet("電話:07-3122238").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.0656415,120.0899308)).title("開心診所").snippet("電話:07-3925036").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6445717,120.3043361)).title("舒心身心診所").snippet("電話:07-3116116").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6602479,120.3266163)).title("文心診所").snippet("電話:07-3108004").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6408539,120.3036073)).title("希望心靈診所").snippet("電話:07-3110303").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6368858,120.3124693)).title("劉精神科診所").snippet("電話:07-2229450").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6380881,120.3309972)).title("心樂是診所").snippet("電話:07-3847373").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6451585,120.3442209)).title("澄清文鳳診所").snippet("電話:07-3986611").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.8154553,120.1175344)).title("心悅診所").snippet("電話:07-3805562").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6484449,120.317213)).title("建工心喜診所").snippet("電話:07-3801585").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6536159,120.3170543)).title("大順景福診所").snippet("電話:07-3833799").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.7288339,120.3239492)).title("祈福診所").snippet("電話:07-3531117").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.7110079,120.2903969)).title("郭玉柱診所").snippet("電話:07-3661166").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.7203256,120.2892995)).title("楠梓心寬診所").snippet("電話:07-3629688").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.7218835,120.2892396)).title("心欣診所").snippet("電話:07-3640490").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.7280659,120.3239083)).title("元景耳鼻喉科神經科").snippet("電話:07-3524100").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9142396,119.9913573)).title("誼安診所").snippet("電話:07-8069353").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6645215,120.3085793)).title("河堤診所").snippet("電話:07-5575658").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6783509,120.3115323)).title("李全忠診所").snippet("電話:07-3450696").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0171722,121.3360999)).title("心晴診所").snippet("電話:07-3463099").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9141076,120.1314675)).title("陽光診所").snippet("電話:07-3412598").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9142396,119.9913573)).title("哲民診所").snippet("電話:07-5570500").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6800923,120.3044587)).title("耕心療癒診所").snippet("電話:07-3592011").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6783159,120.3004913)).title("心方診所").snippet("電話:07-3450697").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6736169,120.3016508)).title("養全診所").snippet("電話:07-3451258").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.8157249,120.1416518)).title("心樂診所").snippet("電話:07-3421968").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6570113,120.3051629)).title("唐子俊診所").snippet("電話:07-5564028").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6611369,120.3076983)).title("寬福診所").snippet("電話:07-5580809").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.793725,120.2937619)).title("維心診所").snippet("電話:07-6231829").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.8627195,120.2594391)).title("國良診所").snippet("電話:07-6076698").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6361009,120.3576343)).title("信元聯合診所").snippet("電話:07-7765955").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6435714,120.3475876)).title("文鳳診所").snippet("電話:07-7807333").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6030962,120.3249877)).title("快樂心靈診所").snippet("電話:07-7686789").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6291482,120.3565574)).title("季宏診所").snippet("電話:07-7458315").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6105829,120.3467433)).title("張簡精神科診所").snippet("電話:07-7634948").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.5994187,120.3352312)).title("五甲心靈診所").snippet("電話:07-8412588").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.8852599,120.480772)).title("欣明精神科診所").snippet("電話:07-6627802").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.7009319,120.3516727)).title("仁華診所").snippet("電話:07-3725066").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6826792,120.4898732)).title("興安診所").snippet("電話:08-7346666").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9141076,120.1314675)).title("泰祥診所").snippet("電話:08-7881717").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.674421,120.520294)).title("屏安醫院附設門診").snippet("電話:08-7378888").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.8451869,120.2069941)).title("寬心診所").snippet("電話:08-7333555").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.6698549,120.5659083)).title("屏安診所").snippet("電話:08-7211777").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.6809969,121.7746683)).title("平和身心診所").snippet("電話:03-9559960").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.7552219,121.7543213)).title("光中身心診所").snippet("電話:03-9312300").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.7550618,121.745844)).title("雅信診所").snippet("電話:03-9326498").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.9850569,121.5923403)).title("福田耳鼻喉科診所").snippet("電話:03-8578290").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9139955,120.2015139)).title("同心診所").snippet("電話:03-8567803").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.9863669,121.5997213)).title("中山身心診所").snippet("電話:03-8315589").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.9921266,121.5744919)).title("悅思身心診所").snippet("電話:03-8460436").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(25.0832308,121.5077933)).title("慈田耳鼻喉科診所").snippet("電話:03-8316818").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9139279,120.236535)).title("周耳鼻喉科").snippet("電話:03-8568801").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(23.9745997,121.5928877)).title("悅增身心診所").snippet("電話:03-8321805").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.7828643,121.1256376)).title("楊國明身心科診所").snippet("電話:089-228865").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.7518585,121.1467066)).title("陳柄辰身心科診所").snippet("電話:089-348998").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(22.9139955,120.2015139)).title("陽明診所").snippet("電話:06-9266633").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
        map.addMarker(new MarkerOptions().position(new LatLng(24.4396137,118.3170789)).title("愛人放心診所").snippet("電話:08-2312313").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));


//        map.addMarker(new MarkerOptions().position(new LatLng(22.642867, 120.350811)).title("文心診所").snippet("電話:07-3108004").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
//        map.addMarker(new MarkerOptions().position(new LatLng(22.658567, 120.313500)).title("開心診所").snippet("電話:07-3925036").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
//        map.addMarker(new MarkerOptions().position(new LatLng(22.625222, 120.306760)).title("榮欣身心診所").snippet("電話:07-2150123").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
//        map.addMarker(new MarkerOptions().position(new LatLng(22.681580, 120.321456)).title("心樂診所").snippet("電話:07-3421968").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
//        map.addMarker(new MarkerOptions().position(new LatLng(22.644596, 120.306536)).title("舒心身心診所").snippet("電話:07-3116116").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
//        map.addMarker(new MarkerOptions().position(new LatLng(22.656987, 120.307341)).title("唐子俊診所").snippet("電話:07-5564028").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.like)));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        rLocationGranted=false;
        switch (requestCode){
            case 1001:{
                if(grantResults.length>0){
                    for(int i=0;i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            rLocationGranted=false;
                            return;
                        }
                    }
                    rLocationGranted=true;
                }
            }

        }

    }

    private boolean checkService(){
        Context c=getContext();
        int avai= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(c);
        if(avai == ConnectionResult.SUCCESS){
            Log.i("Map test","version is Fine");
            return true;
        }
        else {
            Toast.makeText(c,"版本不符合，無法執行MAP",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;

//        LatLng Mapp=new LatLng(25.032986,121.565878);
//        map.addMarker(new MarkerOptions().position(Mapp).title("台北市"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(Mapp));
        getDeviceLocation();
    }
}
