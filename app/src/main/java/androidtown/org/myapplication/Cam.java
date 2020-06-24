package androidtown.org.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.CursorJoiner;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.StaticLayout;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.support.design.widget.FloatingActionButton;

import org.w3c.dom.Text;


public class Cam extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;
    TextView view;
    private int one=0;

    private static final String CLOUD_VISION_API_KEY = "AIzaSyBSTSxaGRUXkBVGbJbDaA1yuDXrQQFCfLM";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1200;

    private static final String TAG = Cam.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    public ImageView mMainImage;
    private static String total="";
    private static Bitmap bitmap;
    private static Button add;
    private static TextView info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        add=(Button)findViewById(R.id.add);
        info=(TextView)findViewById(R.id.info);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage("선택")
                    .setPositiveButton("갤러리 선택", (dialog, which) -> startGalleryChooser())
                    .setNegativeButton("카메라 선택", (dialog, which) -> startCamera());
            builder.create().show();
        });
        mMainImage = findViewById(R.id.main_image);
    }
    //카메라 사용 체크

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);

                callCloudVision(bitmap);
                mMainImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }
        one=1;
        Toast.makeText(this, "추가하기 버튼 활성화....", Toast.LENGTH_SHORT).show();

    }

    private Vision.Images.Annotate prepareAnnotationRequest(final Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {

                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature labelDetection = new Feature();
                labelDetection.setType("LABEL_DETECTION");
                labelDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(labelDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d(TAG, "created Cloud Vision request object, sending request");

        return annotateRequest;
    }

    private static class LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<Cam> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;

        LableDetectionTask(Cam application, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(application);
            mRequest = annotate;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d(TAG, "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(response);

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        protected void onPostExecute(String result) {
            Float check;
            String temp;

            Cam activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                String target="\n";
                int index= result.indexOf(target);
                String str =result.substring(index+2);
                result=result.substring(index+2);
                total="";
                while(result.length()>0){
                    index = result.indexOf(target);
                    str = result.substring(0, index);
                    temp=str.substring(2,4);
                    str=str.substring(7,index);
                    check=Float.parseFloat(temp);
                    if(check>70) {
                        total+=str+" ";
                    }
                    result = result.substring(index + 1);
                }
            }
            add.setVisibility(View.VISIBLE);
            info.setText("Press the button");
        }
    }
    public int weather(String[] check,String total,int weather){
        int count=0;
        for(int i=0;i<check.length;i++){
            if(total.contains(check[i])){
                count++;
            }
        }
        if (count>0){
            return weather;
        }
        else{
            return 0;
        }
    }
    public String korean(String total,String[] cloth,String[] color){
        String ch="";
        String[] kor={"자켓","아우터","오버코드","코트","블레이저","스웨터","셔츠",
                "활동화","런닝화","스니커즈","야외화","슬렉스","청바지","자켓","아우터","셔츠","티셔츠",
                "언더셔츠",
                "긴팔","반바지","버무다 바지","짧은 바지","카라셔츠","속옷","운동복",
                "양말, 신발","상의"};
        String[] kor_c={"흰색","검정","빨강","파랑","초록","노랑","퍼플"};
        for(int i=0;i<cloth.length;i++){
            if(total.contains(cloth[i])){
                ch=kor[i];
            }
        }
        for(int i=0;i<kor_c.length;i++){
            if(total.contains(color[i])){
                ch+=" "+kor_c[i];
            }
        }
        total=ch;
        return total;
    }
    public void click(View v){
        Context context = getApplicationContext();
        Bitmap attachedbitmap= ((BitmapDrawable)mMainImage.getDrawable()).getBitmap();
        String result="";
        String[] cloth={"Outwearjacket","Outwear","Overcoat","Coat","Blazer","Sweater","Shirt",
                "Walking shoe","Running shoe","Sneakers","Outdoor shoe",
                "Trousers",
                "Jeans",
                "Jacket" ,
                "Outerwear",
                "Shirt" ,
                "T-shirt", "UnderShirt","Long-sleeved",
                "Trunks", "Bermuda shorts","Shorts",
                "Collar","Undershirt","Sportswear",
                "Footwear","Top"};
        String[] all={"Shirt",
                "Walking shoe","Running shoe","Sneakers","Outdoor shoe","Trousers","Undershirt",
                "Footwear","Top"};
        String[] spring={"Walking shoe","Running shoe","Sneakers","Outdoor shoe","Jeans","Long-sleeved"};
        String[] summer={"Shirt" ,"T-shirt", "UnderShirt","Trunks", "Bermuda shorts","Shorts",
                "Collar","Sportswear"};
        String[] fall={"Sweater","Blazer","Jeans","Long-sleeved"
                };
        String[] winter={"Coat","Outwearjacket","Outwear","Overcoat",
                "Walking shoe","Running shoe","Sneakers","Outdoor shoe","Jeans","Jacket" , "Outerwear"};
        String[] color={"White","Black","Red","Blue","Green","Yellow","Purple"};
        for(int i=0; i<cloth.length;i++){
            if(total.contains(cloth[i])) {
                result += cloth[i] + " ";
            }
        }
        for(int j=0;j<color.length;j++){
            if(total.contains(color[j])) {
                result += color[j];
            }
        }


        total=result;
        if(weather(spring, total, 1)!=0){
            total=korean(total,cloth,color);
            total+=" 1";
        }
        else if(weather(summer, total, 2)!=0){
            total=korean(total,cloth,color);
            total+=" 2";
        }
        else if(weather(fall, total, 3)!=0){
            total=korean(total,cloth,color);
            total+=" 3";
        }
        else if(weather(winter, total, 4)!=0){
            total=korean(total,cloth,color);
            total+=" 4";
        }

        if(one==1) {
            FileOutputStream fos;
            PrintWriter out;
            if (total.contains("1")) {
                try {
                    fos = openFileOutput
                            ("Spring.txt",
                                    Context.MODE_APPEND);
                    out = new PrintWriter(fos);
                    total=total.substring(0,total.length()-1);
                    saveBitmaptoJpeg(context,attachedbitmap,"Spring", "Spring" + System.currentTimeMillis());
                    out.println(total);
                    out.close();
                    Toast.makeText(this, "봄 옷장에 추가하셨습니다.", Toast.LENGTH_SHORT).show();
                    one=2;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if(total.contains("2")) {
                try {
                    fos = openFileOutput
                            ("Summer.txt", // 파일명 지정
                                    Context.MODE_APPEND);// 저장모드
                    out = new PrintWriter(fos);
                    total=total.substring(0,total.length()-1);
                    saveBitmaptoJpeg(context,attachedbitmap,"Summer", "Summer" + System.currentTimeMillis());
                    out.println(total);
                    out.close();
                    Toast.makeText(this, "여름 옷장에 추가하셨습니다.", Toast.LENGTH_SHORT).show();
                    one=2;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(total.contains("3")) {
                try {
                    fos = openFileOutput
                            ("Fall.txt", // 파일명 지정
                                    Context.MODE_APPEND);// 저장모드
                    out = new PrintWriter(fos);
                    total=total.substring(0,total.length()-1);
                    saveBitmaptoJpeg(context,attachedbitmap,"Fall", "Fall" + System.currentTimeMillis());
                    out.println(total);
                    out.close();
                    Toast.makeText(this, "가을 옷장에 추가하셨습니다.", Toast.LENGTH_SHORT).show();
                    one=2;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(total.contains("4")) {
                try {
                    fos = openFileOutput
                            ("Winter.txt", // 파일명 지정
                                    Context.MODE_APPEND);// 저장모드
                    out = new PrintWriter(fos);
                    total=total.substring(0,total.length()-1);
                    saveBitmaptoJpeg(context,attachedbitmap,"Winter", "Winter" + System.currentTimeMillis());
                    out.println(total);
                    out.close();
                    Toast.makeText(this, "겨울 옷장에 추가하셨습니다.", Toast.LENGTH_SHORT).show();
                    one=2;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(this, "실패...", Toast.LENGTH_SHORT).show();
                    one=3;
            }
            if(one!=3){
            try {
                fos = openFileOutput
                        ("All.txt", // 파일명 지정
                                Context.MODE_APPEND);// 저장모드
                out = new PrintWriter(fos);
                total=total.substring(0,total.length()-1);
                saveBitmaptoJpeg(context,attachedbitmap,"All", "All" + System.currentTimeMillis());
                out.println(total);
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }}
        else if(one==2){
            Toast.makeText(this, "이미 추가하셨습니다.", Toast.LENGTH_SHORT).show();
        }
        else if(one==3){
            Toast.makeText(this, "정보를 얻는데 실패하였습니다", Toast.LENGTH_SHORT).show();

        }
    }



    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth =
                maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private static String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("I found these things:\n\n");

        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message.append(String.format(Locale.US, "%.3f: %s", label.getScore(), label.getDescription()));
                message.append("\n");
            }
        } else {
            message.append("nothing");
        }

        return message.toString();
    }

    public static void saveBitmaptoJpeg(Context context, Bitmap bitmap,String folder, String name)
    {
        String ex_storage =context.getFilesDir().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String foler_name = "/"+folder+"/";
        String file_name = name+".jpg";
        String string_path = ex_storage+foler_name;
        File file_path;
        try {
            file_path = new File(string_path);
            if(!file_path.isDirectory())
            {
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path+file_name);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); out.close();
        }
        catch(FileNotFoundException exception)
        { Log.e("FileNotFoundException", exception.getMessage());
        }
        catch(IOException exception){ Log.e("IOException", exception.getMessage()); } }
};