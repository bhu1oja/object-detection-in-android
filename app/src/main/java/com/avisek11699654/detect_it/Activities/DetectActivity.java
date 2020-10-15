package com.avisek11699654.detect_it.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avisek11699654.detect_it.Adapters.RelatedFruit;
import com.avisek11699654.detect_it.MainActivity;
import com.avisek11699654.detect_it.R;
import com.avisek11699654.detect_it.common.FruitModel;
import com.avisek11699654.detect_it.common.MySQLITE;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DetectActivity extends AppCompatActivity {

    private FirebaseAutoMLLocalModel localModel;
    FirebaseVisionImageLabeler labeler; //For running the image labeler
    FirebaseVisionImage image;


    // Recycler View object
    RecyclerView recyclerViewRelatedContent;

    // Layout Manager
    RecyclerView.LayoutManager RecyclerViewLayoutManager;

    LinearLayoutManager HorizontalLayout;

    // adapter class object
    RecyclerView.Adapter adapter;

ImageView ivDetectedImage;
TextView tvDetectedTitle, tvDetectedConf;

    TextToSpeech t1;


    Button btnShare;
    //database
    private MySQLITE mySQLITE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.OFF)
                .start(DetectActivity.this);


        ivDetectedImage = findViewById(R.id.iv_detected_image);
        tvDetectedTitle = findViewById(R.id.tv_detected_title);
        tvDetectedConf = findViewById(R.id.tv_detected_conf);

        btnShare = findViewById(R.id.btn_share);


        recyclerViewRelatedContent = findViewById(R.id.recyclerviewRelated);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRelatedContent.setLayoutManager(RecyclerViewLayoutManager);
        recyclerViewRelatedContent.setHasFixedSize(true);

        HorizontalLayout
                = new LinearLayoutManager(
                DetectActivity.this,
                LinearLayoutManager.VERTICAL,
                false);

        mySQLITE = new MySQLITE(DetectActivity.this);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });
    }

    private void setLabelerFromLocalModel(Uri uri) {
        localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("manifest.json")
                .build();
        try {
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions options =
                    new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
                            .setConfidenceThreshold(0.0f)
                            .build();
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
            image = FirebaseVisionImage.fromFilePath(DetectActivity.this, uri);
            processImageLabeler(labeler, image);
        } catch (FirebaseMLException | IOException e) {
            // ...
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                ivDetectedImage.setImageURI(resultUri);
                setLabelerFromLocalModel(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void processImageLabeler(FirebaseVisionImageLabeler labeler, FirebaseVisionImage image) {
        labeler.processImage(image).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onComplete(@NonNull Task<List<FirebaseVisionImageLabel>> task) {
                final ArrayList<String> resultName = new ArrayList<>();
                final ArrayList<String> resultConfidecne = new ArrayList<>();
                for (FirebaseVisionImageLabel label : task.getResult()) {
                    String eachlabel = label.getText().toUpperCase();
                    float confidence = label.getConfidence();
                    resultName.add(eachlabel);
                    resultConfidecne.add(Float.toString(confidence * 100));
                }
                //_showAlert(resultName, resultConfidecne);

           // addNotification(resultName.get(0) + " Detected!!!", resultName.get(0) + " Detected With Confidence  "+ resultConfidecne.get(0)+ " %");
                tvDetectedTitle.setText(resultName.get(0));
                tvDetectedConf.setText(resultConfidecne.get(0));
                adapter = new RelatedFruit(resultName,resultConfidecne);
                recyclerViewRelatedContent.setAdapter(adapter);
                t1.speak(resultName.get(0) + "Detected with confidence " + resultConfidecne.get(0) + "%", TextToSpeech.QUEUE_FLUSH, null);

                addNotification(resultName.get(0) + "Detected!!!", resultName.get(0) + "detected with confidence" + resultConfidecne.get(0) + "%");
                FruitModel newFruit = new FruitModel(resultName.get(0), resultConfidecne.get(0).substring(0,4));
                mySQLITE.addFruit(newFruit);
                Toast.makeText(DetectActivity.this, resultName.get(0) + "  is added in SQLite", Toast.LENGTH_SHORT).show();
                btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareText(resultName.get(0) + "Detected with confidence " , resultName.get(0) + "Detected with confidence " + resultConfidecne.get(0) + "%");
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("OnFail", "" + e);
                Toast.makeText(DetectActivity.this, "Something went wrong! " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.home:
                startActivity(new Intent(DetectActivity.this, MainActivity.class));
                break;


        }
        return super.onOptionsItemSelected(item);
    }


    private void addNotification(String title, String body) {
        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder
                (getApplicationContext()).setContentTitle(title).setContentText(body).setSmallIcon(R.drawable.ic_baseline_notifications_none_24).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
    }


    public void shareText(String subject, String body) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent .setType("text/plain");
        txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(txtIntent ,"Share"));
    }
}