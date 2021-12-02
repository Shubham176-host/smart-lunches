package com.example.smartlunches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartlunches.Model.AdminOrders;
import com.example.smartlunches.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission_group.CAMERA;

public class Qr_code_scanner extends AppCompatActivity {

    private ScannerLiveView camera;
    private TextView scannedTV;
    DatabaseReference rootref1 , rootref2;
    String orderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);

        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
        scannedTV = findViewById(R.id.idTVscanned);
        camera = findViewById(R.id.camview);

        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                Toast.makeText(Qr_code_scanner.this, "Scanner Started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
            }

            @Override
            public void onScannerError(Throwable err) {
                Toast.makeText(Qr_code_scanner.this, "Scanner Error: " + err.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeScanned(String data) {
                orderid = data;
                scannedTV.setText(data);
                OrderSuccessfull();
            }
        });
    }
    private void OrderSuccessfull() {
        rootref1 = FirebaseDatabase.getInstance().getReference().child("Orders").child(orderid);

        rootref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> ordersMap = new HashMap<>();
                ordersMap.put("orderstatus", true);
                AdminOrders orders = snapshot.getValue(AdminOrders.class);
                rootref2 = FirebaseDatabase.getInstance().getReference().child("User").child(orders.getUsn());
                if(orders.getOrderstatus()){
                    Toast.makeText(Qr_code_scanner.this, "Order has been already recieved", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    rootref1.updateChildren(ordersMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            rootref2.child("Order").updateChildren(ordersMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(Qr_code_scanner.this, "Order Recieved Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Qr_code_scanner.this, AdminActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Qr_code_scanner.this, "Retry: Error 04", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(Qr_code_scanner.this, "Retry: Error 05", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Qr_code_scanner.this, error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZXDecoder decoder = new ZXDecoder();
        decoder.setScanAreaPercent(0.8);
        camera.setDecoder(decoder);
        camera.startScanner();
    }

    @Override
    protected void onPause() {
        camera.stopScanner();
        super.onPause();
    }

    private boolean checkPermission() {
        int camera_permission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int vibrate_permission = ContextCompat.checkSelfPermission(getApplicationContext(), VIBRATE);
        return camera_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(this, new String[]{CAMERA, VIBRATE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean cameraaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean vibrateaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (cameraaccepted && vibrateaccepted) {
                Toast.makeText(this, "Permission granted..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denined \n You cannot use app without providing permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
